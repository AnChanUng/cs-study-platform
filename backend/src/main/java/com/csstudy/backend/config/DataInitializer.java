package com.csstudy.backend.config;

import com.csstudy.backend.entity.Category;
import com.csstudy.backend.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final CategoryRepository categoryRepository;

    @Override
    public void run(String... args) {
        if (categoryRepository.count() > 0) {
            log.info("Categories already initialized, skipping seed.");
            return;
        }

        List<Category> categories = List.of(
                Category.builder().name("DB / 데이터베이스").slug("database").description("Database concepts and SQL").displayOrder(1).iconEmoji("🗄️").build(),
                Category.builder().name("자료구조").slug("data-structure").description("Data structures").displayOrder(2).iconEmoji("🏗️").build(),
                Category.builder().name("운영체제").slug("operating-system").description("Operating system concepts").displayOrder(3).iconEmoji("🖥️").build(),
                Category.builder().name("네트워크").slug("network").description("Network and protocols").displayOrder(4).iconEmoji("🌐").build(),
                Category.builder().name("알고리즘").slug("algorithm").description("Algorithms and problem solving").displayOrder(5).iconEmoji("⚡").build(),
                Category.builder().name("소프트웨어공학").slug("software-engineering").description("Software engineering principles").displayOrder(6).iconEmoji("📐").build(),
                Category.builder().name("Spring Framework").slug("spring").description("Spring and Spring Boot").displayOrder(7).iconEmoji("🌱").build(),
                Category.builder().name("Java").slug("java").description("Java programming language").displayOrder(8).iconEmoji("☕").build(),
                Category.builder().name("Kubernetes").slug("kubernetes").description("Kubernetes container orchestration").displayOrder(9).iconEmoji("⎈").build(),
                Category.builder().name("Docker").slug("docker").description("Docker containerization").displayOrder(10).iconEmoji("🐳").build(),
                Category.builder().name("CI/CD").slug("cicd").description("Continuous Integration and Deployment").displayOrder(11).iconEmoji("🔄").build()
        );

        categoryRepository.saveAll(categories);
        log.info("Seeded {} default categories.", categories.size());
    }
}
