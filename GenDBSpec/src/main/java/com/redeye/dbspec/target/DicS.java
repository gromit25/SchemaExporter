package com.redeye.dbspec.target;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.redeye.dbspec.common.dto.ColumnD;
import com.redeye.dbspec.common.dto.KeyColumnD;
import com.redeye.dbspec.common.dto.SequenceD;
import com.redeye.dbspec.common.dto.TableD;
import com.redeye.dbspec.common.dto.ViewD;

/**
 * 스키마 정보 추출 서비스
 * 
 * @author jmsohn
 */
@Service
public class DicS {
	
	/** 스키마 정보 추출 Mapper */
	@Autowired
	private DicM mapper;

	/**
	 * 테이블 목록 반환
	 * 
	 * @param schemaName 스키마명
	 * @return 테이블 목록
	 */
	public List<TableD> getTableList(String schemaName) throws Exception {
		return this.mapper.selectTables(schemaName);
	}

	/**
	 * 테이블별 컬럼 목록 반환 <br>
	 * key : 테이블명, value : 컬럼 목록
	 * 
	 * @param schemaName 스키마 명
	 * @param tableList 테이블 명 목록
	 * @return 테이블별 컬럼 목록 반환
	 */
	public Map<String, List<ColumnD>> getColumnMap(String schemaName, List<TableD> tableList) throws Exception {
		
		// 입력값 검증
		if(tableList == null) {
			throw new IllegalArgumentException("table list is null.");
		}
		
		// 테이블별 컬럼 정보 목록 획득 및 설정
		// Key - 테이블 명, Value - 컬럼 정보
		Map<String, List<ColumnD>> tableColumnMap = new HashMap<>();
		
		// 컬럼별 Key 정보(PK, FK 등)을 업데이트 하기 위한 용도
		// Key - 테이블 명.컬럼 명, Value - 컬럼 정보
		Map<String, ColumnD> columnMap = new HashMap<>();
		
		tableList.forEach(table -> {
			
			// 테이블명에 따라 컬럼 정보 획득
			String tableName = table.getTableName();
			List<ColumnD> columnList = this.mapper.selectColumns(schemaName, tableName);
			
			// 테이블에 컬럼 목록 저장
			tableColumnMap.put(tableName, columnList);
			
			// 컬럼 정보 맵에 저장
			columnList.forEach(column -> {
				columnMap.put(column.getKey(), column);
			});
		});
		
		// 테이블 컬럼 별 키 정보 추가
		tableList.forEach(table -> {
			
			// 테이블의 키 정보 획득
			String tableName = table.getTableName();
			List<KeyColumnD> keyColumns = this.mapper.selectKeyColumns(schemaName, tableName);
			
			// 컬럼별 키 정보 추가
			keyColumns.forEach(keyColumn -> {
				
				//
				String key = keyColumn.getKey();
				if(columnMap.containsKey(key) == false) {
					return;
				}
				
				//
				ColumnD column = columnMap.get(key);
				
				// 키 타입에 따른 처리
				switch(keyColumn.getKeyType()) {
				
				// Primary Key
				case "p":	
					column.setIsPrimaryKey("T");
					break;
					
				// Unique
				case "u":
					column.setIsUnique("T");
					break;
				
				// Foreign Key
				case "f":
					column.setIsForeignKey("T");
					column.setRefTableName(keyColumn.getRefTableName());
					column.setRefColumnName(keyColumn.getRefColumnName());
					break;
				}
			});
		});
		
		return tableColumnMap;
	}
	
	/**
	 * 시퀀스 목록 반환
	 * 
	 * @param schemaName 스키마 명
	 * @return 시퀀스 목록
	 */
	public List<SequenceD> getSequenceList(String schemaName) throws Exception {
		return this.mapper.selectSeq(schemaName);
	}
	
	/**
	 * 뷰 목록 반호나
	 * 
	 * @param schemaName 스키마 명
	 * @return 뷰 목록
	 */
	public List<ViewD> getViewList(String schemaName) throws Exception {
		return this.mapper.selectView(schemaName);
	}
}
