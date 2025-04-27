package com.chat;

import io.github.cdimascio.dotenv.Dotenv;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

@Component
public class EnvLoader {

    @PostConstruct
    public void init() {
        // .env 파일을 읽기
        Dotenv dotenv = Dotenv.load();

        // .env의 각 항목을 시스템 속성에 등록
        dotenv.entries().forEach(entry ->
            System.setProperty(entry.getKey(), entry.getValue())
        );
    }
}

