package com.redeye.schemaexporter.exporter.api;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.Map;

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
public class ApiExporter extends Exporter {
	
	/** 포맷 파일 명 */
	private static final String FORMAT_FILE = "format/api/json_format.xml";


	@Override
	protected void write(Map<String, Object> values) throws Exception {
		
	    // 출력 format input stream
		InputStream formatInputStream = getClass().getClassLoader().getResourceAsStream(FORMAT_FILE);
		
		// JSON 출력 실행
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		
		Publisher publisher = PublisherFactory.create(PublisherType.TEXT_FILE, formatInputStream);
		publisher.publish(out, Charset.forName("UTF-8"), values);
		
		String schemaJSON = new String(out.toByteArray(), "UTF-8");
		log.info("SCHEMA JSON: " + schemaJSON);
		
		// API 호출
	}
}
