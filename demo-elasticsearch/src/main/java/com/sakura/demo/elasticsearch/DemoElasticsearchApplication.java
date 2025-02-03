package com.sakura.demo.elasticsearch;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

@EnableElasticsearchRepositories(value = "com.sakura.demo.elasticsearch.repository")
@SpringBootApplication
public class DemoElasticsearchApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoElasticsearchApplication.class, args);
	}

}
