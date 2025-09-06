package com.redeye.schemaexporter.exporter.db;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import com.redeye.schemaexporter.domain.ColumnDto;
import com.redeye.schemaexporter.domain.SequenceDto;
import com.redeye.schemaexporter.domain.TableDto;
import com.redeye.schemaexporter.domain.ViewDto;
import com.redeye.schemaexporter.exporter.Exporter;

import lombok.extern.slf4j.Slf4j;

/**
 * Database로 Schema를 출력하는 클래스
 * 
 * @author jmsohn
 */
@Slf4j
@Component("exporter")
@ConditionalOnProperty
(
	value = "app.exporter.type",
	havingValue = "DB"
)
public class DBExporter extends Exporter {
	
	/** 스키마 정보 저장 Mapper */
	@Autowired
	private ExportMapper mapper;

	@Override
	@SuppressWarnings("unchecked")
	protected void write(Map<String, Object> values) throws Exception {
		
		// 테이블 스키마 명
		String schemaName = (String)values.get("schemaName");
		
		// 테이블 정보 저장
		List<TableDto> tableList = (List<TableDto>)values.get("tableList");
		this.mapper.upsertTableList(schemaName, tableList);
		log.info("table list saved sucessfully.");
		
		// 컬럼 정보 저장
		Map<String, List<ColumnDto>> tableColumnMap = (Map<String, List<ColumnDto>>)values.get("tableList");
		tableColumnMap.forEach(
			(tableName, columnList) -> {
				this.mapper.upsertColumnList(schemaName, columnList);
			}
		);
		log.info("column list saved sucessfully.");
		
		// 시퀀스 정보 저장
		List<SequenceDto> sequenceList = (List<SequenceDto>)values.get("sequenceList");
		this.mapper.upsertSequenceList(schemaName, sequenceList);
		log.info("sequence list saved sucessfully.");
		
		// 뷰 정보 저장
		List<ViewDto> viewList = (List<ViewDto>)values.get("viewList");
		this.mapper.upsertViewList(schemaName, viewList);
		log.info("view list saved sucessfully.");
		
		// 최종 종료 메시지 출력
		log.info("all schema info saved sucessfully.");
	}
}
