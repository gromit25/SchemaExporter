package com.redeye.schemaexporter.exporter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.jutools.DateUtil;
import com.jutools.StringUtil;
import com.redeye.schemaexporter.domain.ColumnDto;
import com.redeye.schemaexporter.domain.TableDto;
import com.redeye.schemaexporter.target.SchemaService;
import com.redeye.schemaexporter.target.SchemaUtil;

import lombok.extern.slf4j.Slf4j;

/**
 * 스키마를 외부 저장소(DB, Excel, Text 등)에 출력하는 클래스
 * 
 * @author jmsohn
 */
@Slf4j
public abstract class Exporter {

	/** 기관 코드 */
	@Value("${app.organ.code}")
	private String organCode;

	/** 도메인 코드 */
	@Value("${app.domain.code}")
	private String domainCode;
	
	/** 추출할 스키마 명 */
	@Value("${app.target.schema}")
	private String schemaName;
	
	/** 스키마 정보 추출 서비스 */
	@Autowired
	private SchemaService schemaSvc;
	
	/**
	 * 스키마를 외부 저장소(DB, File 등)에 출력
	 * 
	 * @param values 저장할 데이터
	 */
	protected abstract void write(Map<String, Object> values) throws Exception;
	
	/**
	 * 스키마 정보를 출력함
	 */
	public void export() throws Exception {
		
		Map<String, Object> values = this.getSchemaInfo();
		this.write(values);
	}
	
	/**
	 * DB의 스키마 정보 추출 및 반환
	 * 
	 * @param schemaName 스키마 명
	 * @return 스키마 정보
	 */
	private Map<String, Object> getSchemaInfo() throws Exception {
		
		if(StringUtil.isBlank(this.schemaName) == true) {
			throw new IllegalArgumentException("schema name is null or blank.");
		}
		
		// 스키마 정보
		Map<String, Object> values = new HashMap<>();
		
		// 기관 코드 설정
		values.put("organCode", this.organCode);
		
		// 도메인 코드 설정
		values.put("domainCode", this.domainCode);
		
		// 현재 스키마명 설정
		values.put("schemaName", this.schemaName);
		
		// 오늘 날짜 설정
		String today = DateUtil.getDateStr(System.currentTimeMillis(), ".");
		values.put("today", today);
		
		// 테이블 정보 목록 획득 및 설정
		List<TableDto> tableList = this.schemaSvc.getTableList(this.schemaName);
		values.put("tableList", tableList);
		log.info("table list loaded successfully.");
		
		// 테이블별 컬럼 정보 목록 획득 및 설정
		Map<String, List<ColumnDto>> tableColumnMap = this.schemaSvc.getColumnMap(this.schemaName, tableList);
		values.put("tableColumnMap", tableColumnMap);
		log.info("table-column map loaded successfully.");
		
		// DB 스키마에서 관계 정보 추출 및 value 컨테이너에 추가
		values.put("relationList", SchemaUtil.getRelationList(tableColumnMap));
		log.info("table-relation list loaded successfully.");
		
		// 시퀀스 목록 조회
		values.put("sequenceList", this.schemaSvc.getSequenceList(this.schemaName));
		log.info("sequence list loaded successfully.");
		
		// 뷰 목록 조회
		values.put("viewList", this.schemaSvc.getViewList(this.schemaName));
		log.info("view list loaded successfully.");
		
		return values;
	}
}
