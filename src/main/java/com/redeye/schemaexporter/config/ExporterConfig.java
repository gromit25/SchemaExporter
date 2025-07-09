package com.redeye.schemaexporter.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.jutools.StringUtil;
import com.redeye.schemaexporter.exporter.Exporter;
import com.redeye.schemaexporter.exporter.db.DBExporter;
import com.redeye.schemaexporter.exporter.txt.TxtExporter;
import com.redeye.schemaexporter.exporter.xlsx.XlsxExporter;

/**
 * SchemaExporter 를 설정하기 위한 설정 컴포넌트
 * 
 * @author jmsohn
 */
@Configuration
public class ExporterConfig {
	
	/** 출력 타입 */
	@Value("${app.exporter.type}")
	private String exporterType;

	/**
	 * "exporter" 컴포넌트 설정
	 * 
	 * @return exporter 컴포넌트
	 */
	@Bean(name="exporter")
	Exporter getExporter() throws Exception {
		
		if(StringUtil.isBlank(this.exporterType) == true) {
			throw new Exception("exporter type is null or blank.");
		}

		// exporter type 별로 설정
		Exporter exporter = 
			switch(this.exporterType) {
				case "DB" -> new DBExporter();
				case "TXT" -> new TxtExporter();
				case "XLSX" -> new XlsxExporter();
				default -> throw new Exception("unexpected exporter type:" + exporterType);
			};
			
		return exporter;
	}
}
