package com.csstudy.backend.config;

import com.csstudy.backend.entity.Category;
import com.csstudy.backend.entity.Difficulty;
import com.csstudy.backend.entity.Question;
import com.csstudy.backend.repository.CategoryRepository;
import com.csstudy.backend.repository.QuestionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.*;
import java.util.*;
import java.util.stream.Stream;

@Slf4j
@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final CategoryRepository categoryRepository;
    private final QuestionRepository questionRepository;

    @Override
    public void run(String... args) {
        if (categoryRepository.count() == 0) {
            seedCategories();
        } else {
            updateCategoryNames();
        }
        if (questionRepository.count() == 0) {
            syncContent();
        }
    }

    private void updateCategoryNames() {
        Map<String, String> nameMap = Map.of(
                "database", "데이터베이스",
                "spring", "스프링",
                "java", "자바",
                "kubernetes", "쿠버네티스",
                "docker", "도커"
        );
        nameMap.forEach((slug, name) -> categoryRepository.findBySlug(slug).ifPresent(cat -> {
            if (!cat.getName().equals(name)) {
                cat.setName(name);
                categoryRepository.save(cat);
                log.info("카테고리 이름 변경: {} -> {}", slug, name);
            }
        }));
    }

    private void seedCategories() {
        List<Category> categories = List.of(
                Category.builder().name("데이터베이스").slug("database").description("Database concepts and SQL").displayOrder(1).iconEmoji("🗄️").build(),
                Category.builder().name("자료구조").slug("data-structure").description("Data structures").displayOrder(2).iconEmoji("🏗️").build(),
                Category.builder().name("운영체제").slug("operating-system").description("Operating system concepts").displayOrder(3).iconEmoji("🖥️").build(),
                Category.builder().name("네트워크").slug("network").description("Network and protocols").displayOrder(4).iconEmoji("🌐").build(),
                Category.builder().name("알고리즘").slug("algorithm").description("Algorithms and problem solving").displayOrder(5).iconEmoji("⚡").build(),
                Category.builder().name("소프트웨어공학").slug("software-engineering").description("Software engineering principles").displayOrder(6).iconEmoji("📐").build(),
                Category.builder().name("스프링").slug("spring").description("Spring and Spring Boot").displayOrder(7).iconEmoji("🌱").build(),
                Category.builder().name("자바").slug("java").description("Java programming language").displayOrder(8).iconEmoji("☕").build(),
                Category.builder().name("쿠버네티스").slug("kubernetes").description("Kubernetes container orchestration").displayOrder(9).iconEmoji("⎈").build(),
                Category.builder().name("도커").slug("docker").description("Docker containerization").displayOrder(10).iconEmoji("🐳").build(),
                Category.builder().name("전공필기").slug("major-exam").description("코스콤, 금융결제원").displayOrder(11).iconEmoji("📝").build()
        );
        categoryRepository.saveAll(categories);
        log.info("Seeded {} default categories.", categories.size());
    }

    private void syncContent() {
        Path contentDir = findContentDir();
        if (contentDir == null) {
            log.warn("Content directory not found, skipping question sync.");
            return;
        }

        log.info("Syncing content from: {}", contentDir.toAbsolutePath());
        int synced = 0;

        try (Stream<Path> paths = Files.walk(contentDir)) {
            List<Path> mdFiles = paths
                    .filter(p -> p.toString().endsWith(".md"))
                    .sorted()
                    .toList();

            for (Path mdFile : mdFiles) {
                try {
                    String fileContent = Files.readString(mdFile);
                    Map<String, String> frontmatter = parseFrontmatter(fileContent);
                    String body = extractBody(fileContent);

                    if (!frontmatter.containsKey("title")) continue;

                    String title = frontmatter.get("title");
                    String categorySlug = frontmatter.getOrDefault("category", "database");
                    String difficultyStr = frontmatter.getOrDefault("difficulty", "BASIC").toUpperCase();
                    String tags = frontmatter.getOrDefault("tags", "");

                    Optional<Category> catOpt = categoryRepository.findBySlug(categorySlug);
                    if (catOpt.isEmpty()) continue;

                    Difficulty difficulty;
                    try {
                        difficulty = Difficulty.valueOf(difficultyStr);
                    } catch (IllegalArgumentException e) {
                        difficulty = Difficulty.BASIC;
                    }

                    String questionContent = extractSection(body, "질문");
                    String answerContent = extractSection(body, "답변");
                    if (questionContent.isEmpty()) questionContent = body;
                    if (answerContent.isEmpty()) answerContent = body;

                    Question question = Question.builder()
                            .title(title)
                            .content(questionContent)
                            .answer(answerContent)
                            .category(catOpt.get())
                            .difficulty(difficulty)
                            .tags(tags)
                            .build();
                    questionRepository.save(question);
                    synced++;
                } catch (Exception e) {
                    log.error("Error processing: {}", mdFile, e);
                }
            }
        } catch (IOException e) {
            log.error("Error walking content directory", e);
        }

        log.info("Synced {} questions from content.", synced);
    }

    private Path findContentDir() {
        String[] candidates = {"content", "../content", "../../content"};
        for (String c : candidates) {
            Path path = Paths.get(c);
            if (Files.exists(path) && Files.isDirectory(path)) return path;
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
        int end = body.indexOf("\n## ", start);
        return end == -1 ? body.substring(start).trim() : body.substring(start, end).trim();
    }

    private Map<String, String> parseFrontmatter(String content) {
        Map<String, String> fm = new HashMap<>();
        if (!content.startsWith("---")) return fm;
        int end = content.indexOf("---", 3);
        if (end == -1) return fm;
        for (String line : content.substring(3, end).trim().split("\n")) {
            int idx = line.indexOf(':');
            if (idx > 0) {
                String key = line.substring(0, idx).trim();
                String val = line.substring(idx + 1).trim();
                if (val.startsWith("\"") && val.endsWith("\""))
                    val = val.substring(1, val.length() - 1);
                fm.put(key, val);
            }
        }
        return fm;
    }

    private String extractBody(String content) {
        if (!content.startsWith("---")) return content;
        int end = content.indexOf("---", 3);
        return end == -1 ? content : content.substring(end + 3).trim();
    }
}
