package com.redeye.dbspec.exporter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.jutools.DateUtil;
import com.jutools.StringUtil;
import com.redeye.dbspec.common.dto.ColumnD;
import com.redeye.dbspec.common.dto.TableD;
import com.redeye.dbspec.target.DicS;
import com.redeye.dbspec.target.DicUtil;

/**
 * 스키마를 외부 저장소(DB, Excel, Text 등)에 출력하는 클래스
 * 
 * @author jmsohn
 */
public abstract class SchemaExporter {
	
	/**
	 * 스키마 정보 추출 서비스
	 */
	@Autowired
	private DicS dicSvc;
	
	/**
	 * 초기화 수행
	 * 출력할 파일등 설정 초기화 수행
	 */
	public abstract void init() throws Exception;
	
	/**
	 * 스키마를 외부 저장소(DB, File 등)에 출력
	 * 
	 * @param values 저장할 데이터
	 */
	protected abstract void write(Map<String, Object> values) throws Exception;
	
	/**
	 * 스키마 정보를 출력함
	 * 
	 * @param schemaName 스키마 명
	 */
	public void export(String schemaName) throws Exception {
		
		if(StringUtil.isBlank(schemaName) == true) {
			throw new IllegalArgumentException("schema name is null or blank.");
		}
		
		Map<String, Object> values = this.getSchemaInfo(schemaName);
		this.write(values);
	}
	
	/**
	 * DB의 스키마 정보 반환
	 * 
	 * @param schemaName 스키마 명
	 * @return 스키마 정보
	 */
	private Map<String, Object> getSchemaInfo(String schemaName) throws Exception {
		
		// 스키마 정보
		Map<String, Object> values = new HashMap<>();
		
		// 현재 스키마명 설정
		values.put("schemaName", schemaName);
		
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
