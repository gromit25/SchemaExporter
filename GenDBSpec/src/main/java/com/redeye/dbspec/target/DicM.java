package com.redeye.dbspec.target;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.redeye.dbspec.common.dto.ColumnD;
import com.redeye.dbspec.common.dto.KeyColumnD;
import com.redeye.dbspec.common.dto.SequenceD;
import com.redeye.dbspec.common.dto.TableD;
import com.redeye.dbspec.common.dto.ViewD;


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
	public List<TableD> selectTables(@Param("schemaName") String schemaName);
	
	/**
	 * 특정 테이블의 컬럼 목록 반환
	 * 
	 * @param schemaName 스키마 명
	 * @param tableName 조회할 테이블 명
	 * @return 컬럼 목록
	 */
	public List<ColumnD> selectColumns(
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
	public List<KeyColumnD> selectKeyColumns(
		@Param("schemaName") String schemaName,
		@Param("tableName") String tableName
	);
	
	/**
	 * 시퀀스 목록 반환
	 * 
	 * @param schemaName 스키마 명
	 * @return 시퀀스 목록
	 */
	public List<SequenceD> selectSeq(@Param("schemaName") String schemaName);
	
	/**
	 * 뷰 목록 반환
	 * 
	 * @param schemaName 스키마 명
	 * @return 뷰 목록
	 */
	public List<ViewD> selectView(@Param("schemaName") String schemaName);
}
