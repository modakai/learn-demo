package com.sakura.mcp;

import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.ai.tool.method.MethodToolCallbackProvider;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class DemoImagesSearchMcpApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoImagesSearchMcpApplication.class, args);
    }

    @Bean
    public ToolCallbackProvider pexelsImageSearchTool(PexelsImageSearchTool pexelsImageSearchTool) {
        return MethodToolCallbackProvider.builder()
                .toolObjects(pexelsImageSearchTool)
                .build();
    }

}
