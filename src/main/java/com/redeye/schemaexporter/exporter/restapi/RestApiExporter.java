package com.redeye.schemaexporter.exporter.restapi;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.Map;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import com.jutools.FileUtil;
import com.jutools.publish.Publisher;
import com.jutools.publish.PublisherFactory;
import com.jutools.publish.PublisherType;
import com.redeye.schemaexporter.exporter.Exporter;

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
public class RestApiExporter extends Exporter {
	
	/** 포맷 파일 명 */
	private static final String FORMAT_FILE = "format/api/json_format.xml";

	/** */
	@Autorwired
	private WebClient webClient;


	@Override
	protected void write(Map<String, Object> values) throws Exception {
		
	    // 출력 format input stream
		InputStream formatInputStream = FileUtil.getInputStream(FORMAT_FILE);
		
		// JSON 출력 실행
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		
		Publisher publisher = PublisherFactory.create(PublisherType.TEXT_FILE, formatInputStream);
		publisher.publish(out, Charset.forName("UTF-8"), values);
		
		String schemaJSON = new String(out.toByteArray(), "UTF-8");
		log.info("SCHEMA JSON: \n" + schemaJSON);
		
		// API 호출
	}
}
