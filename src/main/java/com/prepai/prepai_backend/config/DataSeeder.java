package com.prepai.prepai_backend.config;

import com.prepai.prepai_backend.model.Question;
import com.prepai.prepai_backend.repository.QuestionRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataSeeder {

    @Bean
    CommandLineRunner seedQuestions(QuestionRepository repo) {
        return args -> {
            if (repo.count() > 0) return; // Don't seed if already seeded

            String[][] questions = {
                    // content, type, role, difficulty, hint, tags
                    {"What is the difference between ArrayList and LinkedList?", "technical", "backend", "easy", "Think about memory and access time", "collections,java"},
                    {"Explain REST vs SOAP.", "technical", "backend", "easy", "Think about format and flexibility", "api,web"},
                    {"What is a HashMap and how does it work internally?", "technical", "backend", "medium", "Think about hashing and buckets", "hashmap,collections"},
                    {"Explain database indexing.", "technical", "backend", "medium", "Think about search speed", "database,sql"},
                    {"What is the difference between SQL and NoSQL?", "technical", "backend", "easy", "Think about structure and scalability", "database"},
                    {"How does Spring Boot auto-configuration work?", "technical", "backend", "medium", "Think about @EnableAutoConfiguration", "spring,java"},
                    {"What is JWT and how does it work?", "technical", "backend", "medium", "Think about header.payload.signature", "auth,security"},
                    {"Explain microservices vs monolithic architecture.", "technical", "backend", "hard", "Think about deployment and scalability", "architecture"},
                    {"What is connection pooling?", "technical", "backend", "medium", "Think about reusing DB connections", "database,performance"},
                    {"Write a function to reverse a string.", "technical", "backend", "easy", "Think about two pointers", "string,algorithm"},

                    {"What is the Virtual DOM in React?", "technical", "frontend", "easy", "Think about reconciliation", "react,dom"},
                    {"Explain CSS Flexbox vs Grid.", "technical", "frontend", "easy", "Think about 1D vs 2D layout", "css,layout"},
                    {"What is event delegation in JavaScript?", "technical", "frontend", "medium", "Think about bubbling", "javascript,dom"},
                    {"How does useEffect work in React?", "technical", "frontend", "medium", "Think about side effects and cleanup", "react,hooks"},
                    {"What is the difference between == and === in JS?", "technical", "frontend", "easy", "Think about type coercion", "javascript"},

                    {"Explain the difference between GET and POST.", "technical", "fullstack", "easy", "Think about idempotency", "http,api"},
                    {"What is CORS and why does it exist?", "technical", "fullstack", "medium", "Think about browser security", "web,security"},
                    {"What is Docker and why is it used?", "technical", "devops", "easy", "Think about containerisation", "docker,devops"},
                    {"Explain CI/CD pipeline.", "technical", "devops", "medium", "Think about automation", "devops,deployment"},
                    {"What is Kubernetes?", "technical", "devops", "hard", "Think about container orchestration", "kubernetes,devops"},

                    {"What is ETL?", "technical", "data", "easy", "Think about data pipelines", "data,pipeline"},
                    {"Explain the difference between OLAP and OLTP.", "technical", "data", "medium", "Think about read vs write workloads", "database,data"},
                    {"What is a data warehouse?", "technical", "data", "medium", "Think about historical data", "data,warehouse"},

                    // HR questions (all roles)
                    {"Tell me about yourself.", "hr", "backend", "easy", "Keep it professional and concise", "hr,intro"},
                    {"Why do you want to work here?", "hr", "frontend", "easy", "Research the company first", "hr,motivation"},
                    {"What is your greatest weakness?", "hr", "fullstack", "easy", "Be honest but show self-improvement", "hr,self-awareness"},
                    {"Where do you see yourself in 5 years?", "hr", "backend", "easy", "Align with career growth", "hr,goals"},
                    {"Describe a time you solved a difficult problem.", "hr", "fullstack", "medium", "Use STAR method", "hr,behavioral"},
                    {"How do you handle tight deadlines?", "hr", "devops", "easy", "Talk about prioritisation", "hr,work-style"},
                    {"What motivates you as a developer?", "hr", "data", "easy", "Be genuine and specific", "hr,motivation"}
            };

            for (String[] q : questions) {
                Question question = new Question();
                question.setContent(q[0]);
                question.setType(q[1]);
                question.setRole(q[2]);
                question.setDifficulty(q[3]);
                question.setHint(q[4]);
                question.setTags(q[5]);
                repo.save(question);
            }

            System.out.println("30 questions seeded successfully!");
        };
    }
}
