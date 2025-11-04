package com.sakura.demo.mqtt;

import com.sakura.demo.mqtt.domain.MqttProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(value = MqttProperties.class)
public class DemoMqttApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoMqttApplication.class, args);
	}

}
