package com.redeye.dbspec;

import java.io.File;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.jutools.DateUtil;
import com.jutools.PublishUtil;
import com.redeye.dbspec.common.dto.ColumnD;
import com.redeye.dbspec.common.dto.TableD;
import com.redeye.dbspec.exporter.SchemaExporter;
import com.redeye.dbspec.target.DicS;
import com.redeye.dbspec.target.DicUtil;

/**
 * 메인 클래스
 * 
 * @author jmsohn
 */
@SpringBootApplication
public class GenDBSpecApplication implements CommandLineRunner {
	
	@Autowired
	@Qualifier("exporter")
	private SchemaExporter exporter;
	
	/**
	 * 
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		SpringApplication.run(GenDBSpecApplication.class, args);
	}
	
	@Override
    public void run(String... args) throws Exception {
		
		// 스키마명
		String schemaName = "ridertax";

		//
		this.exporter.export(schemaName);
    }
}
