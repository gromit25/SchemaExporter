package com.redeye.dbspec.exporter.db;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.redeye.dbspec.common.dto.ColumnD;
import com.redeye.dbspec.common.dto.SequenceD;
import com.redeye.dbspec.common.dto.TableD;
import com.redeye.dbspec.common.dto.ViewD;

/**
 * 
 * 
 * @author jmsohn
 */
@Mapper
public interface ExportMapper {
	
	/**
	 * 
	 * 
	 * @param schemaName 
	 * @param table
	 */
	public void upsertTableList(
		@Param("schemaName") String schemaName,
		@Param("tableList") List<TableD> tableList
	);
	
	/**
	 * 
	 * 
	 * @param schemaName
	 * @param columnList
	 */
	public void upsertColumnList(
		@Param("schemaName") String schemaName,
		@Param("columnList") List<ColumnD> columnList
	);
	
	/**
	 * 
	 * 
	 * @param schemaName
	 * @param sequenceList
	 */
	public void upsertSequenceList(
		@Param("schemaName") String schemaName,
		@Param("sequenceList") List<SequenceD> sequenceList
	);
	
	/**
	 * 
	 * 
	 * @param schemaName
	 * @param viewList
	 */
	public void upsertViewList(
		@Param("schemaName") String schemaName,
		@Param("viewList") List<ViewD> viewList
	);
}
