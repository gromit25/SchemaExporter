package com.redeye.schemaexporter.exporter.restapi;

import static com.redeye.schemaexporter.Constants.DOMAIN_CODE;
import static com.redeye.schemaexporter.Constants.ORGAN_CODE;
import static com.redeye.schemaexporter.Constants.SCHEMA_NAME;

import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import com.jutools.FileUtil;
import com.jutools.publish.Publisher;
import com.jutools.publish.PublisherFactory;
import com.jutools.publish.PublisherType;
import com.redeye.schemaexporter.exporter.Exporter;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;

/**
 * 
 * 
 * @author jmsohn
 */
@Slf4j
@Component("exporter")
@ConditionalOnProperty
(
	value = "app.exporter.type",
	havingValue = "RESTAPI"
)
public class RestAPIExporter extends Exporter {
	
	/** 포맷 파일 명 */
	private static final String FORMAT_FILE = "format/api/json_format.xml";

	/** API 클라이언트 */
	@Autowired
	private WebClient webClient;
	
	/** API 전송 데이터를 생성하는 publisher 객체 */
	private Publisher apiPublisher;
	
	/**
	 * 초기화
	 */
	@PostConstruct
	public void init() throws Exception {
		
	    // 출력 format input stream
		InputStream formatInputStream = FileUtil.getInputStream(FORMAT_FILE);
		
		// 출력 publisher 객체 생성
		this.apiPublisher = PublisherFactory.create(PublisherType.TEXT_FILE, formatInputStream);
	}

	@Override
	protected void write(Map<String, Object> values) throws Exception {

		// 입력 데이터 확인
		if(values.containsKey(ORGAN_CODE) == false) {
			throw new IllegalArgumentException(ORGAN_CODE + " is not found.");
		}

		if(values.containsKey(DOMAIN_CODE) == false) {
			throw new IllegalArgumentException(DOMAIN_CODE + " is not found.");
		}

		if(values.containsKey(SCHEMA_NAME) == false) {
			throw new IllegalArgumentException(SCHEMA_NAME + " is not found.");
		}

		// 기관 코드/도메인 코드/스키마 명 설정
		String organCode = values.get(ORGAN_CODE).toString();
		String domainCode = values.get(DOMAIN_CODE).toString();
		String schemaName = values.get(SCHEMA_NAME).toString();
		
		// JSON 출력 실행
		String schemaJSON = this.apiPublisher.publish(Charset.forName("UTF-8"), values);
		
		log.info("SCHEMA JSON: \n" + schemaJSON);
		
		// API 호출
		String result = this.webClient.post()
			.uri(String.format("/api/schema/%s/%s/%s", organCode, domainCode, schemaName))
			.header("Content-Type", "application/json")
			.bodyValue(schemaJSON)
			.retrieve()
			.bodyToMono(String.class)
			.block();
		
		log.info("RESULT: \n" + result);
	}
}
