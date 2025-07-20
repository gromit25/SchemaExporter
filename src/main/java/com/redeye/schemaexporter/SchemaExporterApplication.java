package com.redeye.schemaexporter;

import java.io.File;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.jutools.CronJob;
import com.jutools.FileUtil;
import com.redeye.schemaexporter.exporter.Exporter;

import lombok.extern.slf4j.Slf4j;

/**
 * 스키마를 읽어 출력하는 메인 클래스
 * 
 * @author jmsohn
 */
@Slf4j
@SpringBootApplication
public class SchemaExporterApplication implements CommandLineRunner {

	/** 실행 모드 */
	@Value("${app.run-mode}")
	private RunMode runMode;

	/** 크론 실행 모드일 경우의 실행 주기 */
	@Value("${app.cron.schedule}")
	private String cronSchedule;
	
	/** */
	@Value("${app.stop.file}")
	private File stopFile;
	
	/**
	 * 출력 객체
	 * SchemaExporterConfig.getExport 메소드에 의해 빈 컴포넌트 등록됨
	 */
	@Autowired
	@Qualifier("exporter")
	private Exporter exporter;
	
	/**
	 * 메인 메소드
	 * 
	 * @param args 명령행 인자
	 */
	public static void main(String[] args) {
		SpringApplication.run(SchemaExporterApplication.class, args);
	}
	
	/**
	 * 스키마를 읽어 출력
	 * 
	 * @param args 명령행 인자
	 */
	@Override
	public void run(String... args) throws Exception {
		
		log.info("start schema exporter.");
		log.info("run-mode : " + this.runMode);
		
		if(this.runMode == RunMode.CRON) {

			// 크론잡 생성 및 실행
			CronJob job = CronJob.builder()
				.cronExp(this.cronSchedule)
				.job(new Runnable() {
					@Override
					public void run() {
						try {
							exporter.export();
						} catch(Exception ex) {
							log.error("exception occured.", ex);
						}
					}
				})
				.build();
			
			job.run();
			
			// stop 파일이 touch 될때까지 대기
			FileUtil.waitForFileTouched(this.stopFile);
			
			// 크론잡 중단
			job.stop();
			
		} else {
			
			// 한번만 실행
			this.exporter.export();
		}
	}
}
