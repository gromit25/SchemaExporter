package com.redeye.schemaexporter.exporter.xlsx;

import java.io.File;
import java.io.InputStream;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;

import com.jutools.PublishUtil;
import com.jutools.StringUtil;
import com.redeye.schemaexporter.exporter.Exporter;

import jakarta.annotation.PostConstruct;

/**
 * 엑셀 파일로 스키마를 툴력함
 * 
 * @author jmsohn
 */
public class XlsxExporter extends Exporter {

	/** 포맷 파일 명 */
	private static final String FORMAT_FILE = "excel_format.xml";
	
	/** 출력 엑셀 파일 명 */
	@Value("${app.exporter.xlsx.out}")
	private String outFileName;
	
	/** 출력할 엑셀 파일 */
	private File outFile;

	
	@PostConstruct
	public void init() throws Exception {
		
		// 출력 파일명을 가져옴
		if(StringUtil.isBlank(this.outFileName) == true) {
			throw new Exception("out file name is null or blank.");
		}
		
		// 출력 파일 객체 생성
		this.outFile = new File(outFileName);
	}

	@Override
	protected void write(Map<String, Object> values) throws Exception {
		
	    // 출력 format input stream
		InputStream formatInputStream = getClass().getClassLoader().getResourceAsStream(FORMAT_FILE);
		
	    // 엑셀 파일 출력 실행
	    PublishUtil.toExcel(formatInputStream, this.outFile, values);
	}
}
