package com.redeye.dbspec;

import java.io.File;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.jutools.DateUtil;
import com.jutools.PublishUtil;
import com.redeye.dbspec.common.dto.ColumnD;
import com.redeye.dbspec.common.dto.TableD;
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
	private DicS dicSvc;
	
	public static void main(String[] args) {
		SpringApplication.run(GenDBSpecApplication.class, args);
	}
	
	@Override
    public void run(String... args) throws Exception {
		
		// 스키마명
		String schemaName = "ridertax";
		// 출력파일명
		String outFileName = "C:/data/dbspec.txt";
		
		// 출력파일 확장자에 따라 스키마 정보 출력
		if(outFileName.endsWith(".xlsx") == true) {
			this.load2Xls(schemaName, outFileName);
		} else {
			this.load2Txt(schemaName, outFileName);
		}
		
		//this.load2Txt("ridertax", "C:/data/dbspec.txt");
        //this.load2Xls("ridertax", "C:/data/dbspec.xlsx");
		//this.miscSvc.load2DB();
    }
	
	/**
	 * 스키마 정보를 엑셀 파일로 출력  
	 * 
	 * @param schemaName 스키마 명
	 * @param outFileName 출력할 파일명
	 */
	public void load2Xls(
		String schemaName,
		String outFileName
	) throws Exception {
		
		// DB 스키마 정보 획득하여 value 컨텐이너에 추가
		Map<String, Object> values = this.getSchemaInfo(schemaName);
		
	    // 출력 format input stream
		InputStream formatInputStream = getClass().getClassLoader().getResourceAsStream("markdown_format.xml");
		
	    // 출력할 텍스트 파일
	    File outFile = new File(outFileName);
	    
	    // 엑셀 파일 출력 실행
	    PublishUtil.toExcel(formatInputStream, outFile, values);
	}
	
	/**
	 * 스키마 정보를 텍스트 파일로 출력
	 * mermaid mark down 양식으로 출력함
	 * 
	 * @param schemaName 스키마 명
	 * @param outFileName 출력할 파일명
	 */
	public void load2Txt(
		String schemaName,
		String outFileName
	) throws Exception {
		
		// DB 스키마 정보 획득하여 value 컨텐이너에 추가
		Map<String, Object> values = this.getSchemaInfo(schemaName);
		
	    // 출력 format input stream
		InputStream formatInputStream = getClass().getClassLoader().getResourceAsStream("markdown_format.xml");
		
	    // 출력할 텍스트 파일
	    File outFile = new File(outFileName);
	    
	    // 엑셀 파일 출력 실행
	    PublishUtil.toTxt(formatInputStream, outFile, values);
	}
	
	/**
	 * DB 스키마 정보 반환
	 * 
	 * @param schemaName 스키마 명
	 * @return DB 스키마 정보
	 */
	private Map<String, Object> getSchemaInfo(String schemaName) throws Exception {
		
		// 스키마 정보
		Map<String, Object> values = new HashMap<>();
		
		// 오늘 날짜 설정
		String today = DateUtil.getDateStr(System.currentTimeMillis(), ".");
		values.put("today", today);
		
		// 테이블 정보 목록 획득 및 설정
		List<TableD> tableList = this.dicSvc.getTableList(schemaName);
		values.put("tableList", tableList);
		
		// 테이블별 컬럼 정보 목록 획득 및 설정
		Map<String, List<ColumnD>> tableColumnMap = this.dicSvc.getColumnMap(schemaName, tableList);
		values.put("tableColumnMap", tableColumnMap);
		
		// DB 스키마에서 관계 정보 추출 및 value 컨테이너에 추가
		values.put("relationList", DicUtil.getRelationList(tableColumnMap));
		
		// 시퀀스 목록 조회
		values.put("sequenceList", this.dicSvc.getSequenceList(schemaName));
		
		// 뷰 목록 조회
		values.put("viewList", this.dicSvc.getViewList(schemaName));
		
		return values;
	}
}
