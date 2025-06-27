package com.redeye.dbspec.exporter.db;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.redeye.dbspec.domain.ColumnDto;
import com.redeye.dbspec.domain.SequenceDto;
import com.redeye.dbspec.domain.TableDto;
import com.redeye.dbspec.domain.ViewDto;

/**
 * Schema 를 데이터베이스에 저장하는 Mapper
 * 
 * @author jmsohn
 */
@Mapper
public interface ExportMapper {
	
	/**
	 * 테이블 목록 저장(upsert)
	 * 
	 * @param schemaName 스키마 명
	 * @param tableList 저장할 테이블 목록
	 */
	public void upsertTableList(
		@Param("schemaName") String schemaName,
		@Param("tableList") List<TableDto> tableList
	);
	
	/**
	 * 컬럼 정보 저장(upsert)
	 * 
	 * @param schemaName 스키마 명
	 * @param columnList 저장할 컬럼 목록
	 */
	public void upsertColumnList(
		@Param("schemaName") String schemaName,
		@Param("columnList") List<ColumnDto> columnList
	);
	
	/**
	 * 시퀀스 정보 저장(upsert)
	 * 
	 * @param schemaName 스키마 명
	 * @param sequenceList 저장할 스키마 목록
	 */
	public void upsertSequenceList(
		@Param("schemaName") String schemaName,
		@Param("sequenceList") List<SequenceDto> sequenceList
	);
	
	/**
	 * 뷰 정보 저장(upsert)
	 * 
	 * @param schemaName 스키마 명
	 * @param viewList 저장할 뷰 목록
	 */
	public void upsertViewList(
		@Param("schemaName") String schemaName,
		@Param("viewList") List<ViewDto> viewList
	);
}
