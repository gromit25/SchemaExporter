package com.redeye.schemaexporter.exporter.txt;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.redeye.schemaexporter.exporter.Exporter;

@Configuration
@ConditionalOnProperty
(
	value = "app.exporter.type",
	havingValue = "MARKDOWN"
)
public class MarkdownTxtExporterConfig {

	@Bean("exporter")
	Exporter exporter() {
		return new TxtExporter();
	}
}
