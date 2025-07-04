package com.redeye.schemaexporter.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
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
	@Value("${exporter.type}")
	private String exporterType;

	/**
	 * "exporter" 컴포넌트 설정
	 * 
	 * @return exporter 컴포넌트
	 */
	@Bean(name="exporter")
	Exporter getExporter(ApplicationContext context) throws Exception {
		
		if(StringUtil.isBlank(this.exporterType) == true) {
			throw new Exception("exporter type is null or blank.");
		}

		// exporter type 별로 설정
		Exporter exporter = 
			switch(this.exporterType) {
				case "DB" -> context.getBean(DBExporter.class);
				case "TXT" -> context.getBean(TxtExporter.class);
				case "XLSX" -> context.getBean(XlsxExporter.class);
				default -> throw new Exception("unexpected exporter type:" + exporterType);
			};
			
		// 초기화 실행
		// 생성자로 초기화하지 않는 이유는
		// 특정 환경변수가 없을 경우 생성시 오류가 발생하기 때문임
		// 즉, 필요한 컴포넌트만 초기화 함, 사용하지 않는 컴포넌트는 초기화하지 않음
		exporter.init();
		
		return exporter;
	}
}
