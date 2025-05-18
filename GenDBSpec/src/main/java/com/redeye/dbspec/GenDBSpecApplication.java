package com.redeye.dbspec;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.jutools.DateUtil;
import com.jutools.PublishUtil;
import com.redeye.dbspec.common.dto.TableD;
import com.redeye.dbspec.misc.MiscS;
import com.redeye.dbspec.target.DicS;

/**
 * 메인 클래스
 * 
 * @author jmsohn
 */
@SpringBootApplication
public class GenDBSpecApplication implements CommandLineRunner {
	
	@Autowired
	private MiscS miscSvc;
	
	@Autowired
	private DicS dicSvc;
	
	public static void main(String[] args) {
		SpringApplication.run(GenDBSpecApplication.class, args);
	}
	
	@Override
    public void run(String... args) throws Exception {
        this.loadAndSave("ridertax", "C:/data/dbspec.xlsx");
		//this.miscSvc.load2DB();
    }
	
	/**
	 *  
	 * 
	 * @param schemaName 스키마 명
	 * @param outFileName 출력할 파일명
	 */
	public void loadAndSave(
		String schemaName,
		String outFileName
	) throws Exception {
		
		//
		Map<String, Object> values = new HashMap<>();
		
		// 오늘 날짜 설정
		String today = DateUtil.getDateStr(System.currentTimeMillis(), ".");
		values.put("today", today);
		
		// 테이블 정보 목록 획득 및 설정
		List<TableD> tableList = this.dicSvc.getTableList(schemaName);
		values.put("tableList", tableList);
		
		// 테이블별 컬럼 정보 목록 획득 및 설정
		values.put("tableColumnMap", this.dicSvc.getColumnMap(schemaName, tableList));
		
		// 시퀀스 목록 조회
		values.put("sequenceList", this.dicSvc.getSequenceList(schemaName));
		
		// 뷰 목록 조회
		values.put("viewList", this.dicSvc.getViewList(schemaName));
		
		// DB 테이블 명세 엑셀 파일 생성
		
	    // 출력 format file
	    File formatFile = new File("C:/workspace/GenDBSpec/src/main/resources/format.xml");
	    // 출력할 엑셀 파일
	    File outFile = new File(outFileName);
	    
	    // 엑셀 파일 출력 실행
	    PublishUtil.toExcel(formatFile, outFile, values);
	}
}
