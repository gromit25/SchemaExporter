package com.redeye.schemaexporter.exporter.api;

import java.io.InputStream;
import java.util.Map;

import com.jutools.PublishUtil;
import com.redeye.schemaexporter.exporter.Exporter;

/**
 * 
 * 
 * @author jmsohn
 */
public class ApiExporter extends Exporter {
	
	/** 포맷 파일 명 */
	private static final String FORMAT_FILE = "format/api/json_format.xml";


	@Override
	protected void write(Map<String, Object> values) throws Exception {
		
	    // 출력 format input stream
		InputStream formatInputStream = getClass().getClassLoader().getResourceAsStream(FORMAT_FILE);
		
	    // 엑셀 파일 출력 실행
	    PublishUtil.toTxt(null, null, null, values);;
	}
}
