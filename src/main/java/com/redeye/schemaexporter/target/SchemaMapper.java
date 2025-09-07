package com.redeye.schemaexporter.target;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.redeye.schemaexporter.domain.ColumnDTO;
import com.redeye.schemaexporter.domain.KeyColumnDTO;
import com.redeye.schemaexporter.domain.SequenceDTO;
import com.redeye.schemaexporter.domain.TableDTO;
import com.redeye.schemaexporter.domain.ViewDTO;


/**
 * DB Dictionary 테이블 Mapper
 * 
 * @author jmsohn
 */
@Mapper
public interface SchemaMapper {
	
	/**
	 * 테이블 목록 반환
	 * 
	 * @param schemaName 스키마 명
	 * @return 테이블 목록
	 */
	public List<TableDTO> selectTables(@Param("schemaName") String schemaName);
	
	/**
	 * 특정 테이블의 컬럼 목록 반환
	 * 
	 * @param schemaName 스키마 명
	 * @param tableName 조회할 테이블 명
	 * @return 컬럼 목록
	 */
	public List<ColumnDTO> selectColumns(
		@Param("schemaName") String schemaName,
		@Param("tableName") String tableName
	);
	
	/**
	 * 특정 테이블의 키 컬럼 목록 반환
	 * 
	 * @param schemaName 스키마 명
	 * @param tableName 조회할 테이블 명
	 * @return 키 컬럼 목록
	 */
	public List<KeyColumnDTO> selectKeyColumns(
		@Param("schemaName") String schemaName,
		@Param("tableName") String tableName
	);
	
	/**
	 * 시퀀스 목록 반환
	 * 
	 * @param schemaName 스키마 명
	 * @return 시퀀스 목록
	 */
	public List<SequenceDTO> selectSeq(@Param("schemaName") String schemaName);
	
	/**
	 * 뷰 목록 반환
	 * 
	 * @param schemaName 스키마 명
	 * @return 뷰 목록
	 */
	public List<ViewDTO> selectView(@Param("schemaName") String schemaName);
}
