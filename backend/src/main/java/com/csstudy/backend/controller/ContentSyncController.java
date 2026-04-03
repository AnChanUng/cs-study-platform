package com.csstudy.backend.controller;

import com.csstudy.backend.entity.Category;
import com.csstudy.backend.entity.Difficulty;
import com.csstudy.backend.entity.Question;
import com.csstudy.backend.repository.QuestionRepository;
import com.csstudy.backend.service.CategoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.nio.file.*;
import java.util.*;
import java.util.stream.Stream;

@Slf4j
@RestController
@RequestMapping("/api/v1/sync")
@RequiredArgsConstructor
public class ContentSyncController {

    private final CategoryService categoryService;
    private final QuestionRepository questionRepository;

    @PostMapping
    public ResponseEntity<Map<String, Object>> sync() {
        // Try multiple possible content directory locations
        Path contentDir = findContentDir();
        if (contentDir == null) {
            return ResponseEntity.badRequest().body(Map.of(
                    "error", "Content directory not found",
                    "searched", List.of("../content", "content", "../../content")
            ));
        }

        log.info("Syncing content from: {}", contentDir.toAbsolutePath());
        int synced = 0;
        int errors = 0;

        try (Stream<Path> paths = Files.walk(contentDir)) {
            List<Path> mdFiles = paths
                    .filter(p -> p.toString().endsWith(".md"))
                    .toList();

            log.info("Found {} markdown files", mdFiles.size());

            for (Path mdFile : mdFiles) {
                try {
                    String fileContent = Files.readString(mdFile);
                    Map<String, String> frontmatter = parseFrontmatter(fileContent);
                    String body = extractBody(fileContent);

                    if (frontmatter.isEmpty() || !frontmatter.containsKey("title")) {
                        log.warn("Skipping file without valid frontmatter: {}", mdFile);
                        errors++;
                        continue;
                    }

                    String title = frontmatter.get("title");
                    String categorySlug = frontmatter.getOrDefault("category", "database");
                    String difficultyStr = frontmatter.getOrDefault("difficulty", "BASIC").toUpperCase();
                    String tags = frontmatter.getOrDefault("tags", "");

                    Category category;
                    try {
                        category = categoryService.findBySlug(categorySlug);
                    } catch (RuntimeException e) {
                        log.warn("Category not found for slug: {}, skipping file: {}", categorySlug, mdFile);
                        errors++;
                        continue;
                    }

                    Difficulty difficulty;
                    try {
                        difficulty = Difficulty.valueOf(difficultyStr);
                    } catch (IllegalArgumentException e) {
                        difficulty = Difficulty.BASIC;
                    }

                    // Parse question and answer from body sections
                    String questionContent = extractSection(body, "질문");
                    String answerContent = extractSection(body, "답변");

                    // If no sections found, use full body as content
                    if (questionContent.isEmpty()) {
                        questionContent = body;
                    }
                    if (answerContent.isEmpty()) {
                        answerContent = body;
                    }

                    List<Question> existing = questionRepository.searchByTitleOrContent(title);
                    Question question;
                    Optional<Question> match = existing.stream()
                            .filter(q -> q.getTitle().equals(title))
                            .findFirst();

                    if (match.isPresent()) {
                        question = match.get();
                        question.setContent(questionContent);
                        question.setAnswer(answerContent);
                        question.setCategory(category);
                        question.setDifficulty(difficulty);
                        question.setTags(tags);
                    } else {
                        question = Question.builder()
                                .title(title)
                                .content(questionContent)
                                .answer(answerContent)
                                .category(category)
                                .difficulty(difficulty)
                                .tags(tags)
                                .build();
                    }

                    questionRepository.save(question);
                    synced++;
                } catch (Exception e) {
                    log.error("Error processing file: {}", mdFile, e);
                    errors++;
                }
            }
        } catch (IOException e) {
            log.error("Error walking content directory", e);
            return ResponseEntity.internalServerError().body(Map.of("error", e.getMessage()));
        }

        return ResponseEntity.ok(Map.of(
                "synced", synced,
                "errors", errors,
                "contentDir", contentDir.toAbsolutePath().toString()
        ));
    }

    private Path findContentDir() {
        String[] candidates = {"../content", "content", "../../content", "../../../content"};
        for (String candidate : candidates) {
            Path path = Paths.get(candidate);
            if (Files.exists(path) && Files.isDirectory(path)) {
                return path;
            }
        }
        return null;
    }

    private String extractSection(String body, String sectionName) {
        String marker = "## " + sectionName;
        int start = body.indexOf(marker);
        if (start == -1) return "";

        start = body.indexOf('\n', start);
        if (start == -1) return "";
        start++;

        // Find next ## header
        int end = body.indexOf("\n## ", start);
        if (end == -1) {
            return body.substring(start).trim();
        }
        return body.substring(start, end).trim();
    }

    private Map<String, String> parseFrontmatter(String content) {
        Map<String, String> frontmatter = new HashMap<>();
        if (!content.startsWith("---")) {
            return frontmatter;
        }

        int endIndex = content.indexOf("---", 3);
        if (endIndex == -1) {
            return frontmatter;
        }

        String fmBlock = content.substring(3, endIndex).trim();
        for (String line : fmBlock.split("\n")) {
            int colonIdx = line.indexOf(':');
            if (colonIdx > 0) {
                String key = line.substring(0, colonIdx).trim();
                String value = line.substring(colonIdx + 1).trim();
                if (value.startsWith("\"") && value.endsWith("\"")) {
                    value = value.substring(1, value.length() - 1);
                }
                frontmatter.put(key, value);
            }
        }

        return frontmatter;
    }

    private String extractBody(String content) {
        if (!content.startsWith("---")) {
            return content;
        }
        int endIndex = content.indexOf("---", 3);
        if (endIndex == -1) {
            return content;
        }
        return content.substring(endIndex + 3).trim();
    }
}
