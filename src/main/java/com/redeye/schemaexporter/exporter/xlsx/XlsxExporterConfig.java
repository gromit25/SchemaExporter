package com.redeye.schemaexporter.exporter.xlsx;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.redeye.schemaexporter.exporter.Exporter;

@Configuration
@ConditionalOnProperty
(
	value = "app.exporter.type",
	havingValue = "XLSX"
)
public class XlsxExporterConfig {

	@Bean("exporter")
	Exporter exporter() {
		return new XlsxExporter();
	}
}
