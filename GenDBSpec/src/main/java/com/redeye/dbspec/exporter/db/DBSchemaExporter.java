package com.redeye.dbspec.exporter.db;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.redeye.dbspec.common.dto.ColumnD;
import com.redeye.dbspec.common.dto.SequenceD;
import com.redeye.dbspec.common.dto.TableD;
import com.redeye.dbspec.common.dto.ViewD;
import com.redeye.dbspec.exporter.SchemaExporter;

import lombok.extern.slf4j.Slf4j;

/**
 * 
 * 
 * @author jmsohn
 */
@Component
@Slf4j
public class DBSchemaExporter extends SchemaExporter {
	
	/** 스키마 정보 저장 Mapper */
	@Autowired
	private ExportMapper mapper;

	public void init() throws Exception {
		// Do nothing
	}

	@Override
	@SuppressWarnings("unchecked")
	protected void write(Map<String, Object> values) throws Exception {
		
		// 테이블 스키마 명
		String schemaName = (String)values.get("schemaName");
		
		// 테이블 정보 저장
		List<TableD> tableList = (List<TableD>)values.get("tableList");
		this.mapper.upsertTableList(schemaName, tableList);
		log.info("table list saved sucessfully.");
		
		// 컬럼 정보 저장
		Map<String, List<ColumnD>> tableColumnMap = (Map<String, List<ColumnD>>)values.get("tableList");
		tableColumnMap.forEach(
			(tableName, columnList) -> {
				this.mapper.upsertColumnList(schemaName, columnList);
			}
		);
		log.info("column list saved sucessfully.");
		
		// 시퀀스 정보 저장
		List<SequenceD> sequenceList = (List<SequenceD>)values.get("sequenceList");
		this.mapper.upsertSequenceList(schemaName, sequenceList);
		log.info("sequence list saved sucessfully.");
		
		// 뷰 정보 저장
		List<ViewD> viewList = (List<ViewD>)values.get("viewList");
		this.mapper.upsertViewList(schemaName, viewList);
		log.info("view list saved sucessfully.");
		
		// 최종 종료 메시지 출력
		log.info("all schema info saved sucessfully.");
	}
}
