package com.prepai.prepai_backend.config;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EnvConfig {

    private final Dotenv dotenv;

    public EnvConfig(){
        this.dotenv = Dotenv.configure()
                .ignoreIfMissing()
                .load();
    }
    public String getOpenAiKey(){
        return dotenv.get("OPENAI_KEY");
    }
}
