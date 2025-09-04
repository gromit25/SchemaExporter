package com.redeye.schemaexporter.exporter.api;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.redeye.schemaexporter.exporter.Exporter;

/**
 * 
 * 
 * @author jmsohn
 */
@Configuration
@ConditionalOnProperty
(
	value = "app.exporter.type",
	havingValue = "API"
)
public class ApiExporterConfig {

	@Bean("exporter")
	Exporter exporter() {
		return new ApiExporter();
	}
}
