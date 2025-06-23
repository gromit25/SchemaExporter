package com.redeye.dbspec.target;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.redeye.dbspec.domain.entity.ColumnDto;
import com.redeye.dbspec.domain.entity.KeyColumnDto;
import com.redeye.dbspec.domain.entity.SequenceDto;
import com.redeye.dbspec.domain.entity.TableDto;
import com.redeye.dbspec.domain.entity.ViewDto;


/**
 * DB Dictionary 테이블 Mapper
 * 
 * @author jmsohn
 */
@Mapper
public interface DicM {
	
	/**
	 * 테이블 목록 반환
	 * 
	 * @param schemaName 스키마 명
	 * @return 테이블 목록
	 */
	public List<TableDto> selectTables(@Param("schemaName") String schemaName);
	
	/**
	 * 특정 테이블의 컬럼 목록 반환
	 * 
	 * @param schemaName 스키마 명
	 * @param tableName 조회할 테이블 명
	 * @return 컬럼 목록
	 */
	public List<ColumnDto> selectColumns(
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
	public List<KeyColumnDto> selectKeyColumns(
		@Param("schemaName") String schemaName,
		@Param("tableName") String tableName
	);
	
	/**
	 * 시퀀스 목록 반환
	 * 
	 * @param schemaName 스키마 명
	 * @return 시퀀스 목록
	 */
	public List<SequenceDto> selectSeq(@Param("schemaName") String schemaName);
	
	/**
	 * 뷰 목록 반환
	 * 
	 * @param schemaName 스키마 명
	 * @return 뷰 목록
	 */
	public List<ViewDto> selectView(@Param("schemaName") String schemaName);
}
