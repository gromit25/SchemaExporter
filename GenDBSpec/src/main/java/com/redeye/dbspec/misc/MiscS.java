package com.redeye.dbspec.misc;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jutools.StringUtil;
import com.redeye.dbspec.common.dto.ColumnD;
import com.redeye.dbspec.common.dto.SequenceD;
import com.redeye.dbspec.common.dto.TableD;
import com.redeye.dbspec.common.dto.ViewD;
import com.redeye.dbspec.target.DicS;

import lombok.extern.slf4j.Slf4j;

/**
 * 데이터베이스 스키마 정보 저장 서비스
 * 
 * @author jmsohn
 */
@Slf4j
@Service
public class MiscS {
	
	/** 스키마 정보 추출 서비스 */
	@Autowired
	private DicS dicSvc; 
	
	/** 스키마 정보 저장 Mapper */
	@Autowired
	private MiscM mapper;
	
	/**
	 * 데이터베이스에 스키마 정보 저장
	 * 
	 * @param schemaName 스키마 명
	 */
	public void load2DB(String schemaName) throws Exception {

		// 스키마명
		if(StringUtil.isBlank(schemaName) == true) {
			throw new IllegalArgumentException("schema name is blank.");
		}
		
		// ---- 데이터 획득 ----
		
		// 테이블 정보 획득
		List<TableD> tableList = this.dicSvc.getTableList(schemaName);
		
		// 컬럼 정보 획득
		Map<String, List<ColumnD>> tableColumnMap = this.dicSvc.getColumnMap(schemaName, tableList);
		
		// 시퀀스 정보 획득
		List<SequenceD> sequenceList = this.dicSvc.getSequenceList(schemaName);
		
		// 뷰 정보 획득
		List<ViewD> viewList = this.dicSvc.getViewList(schemaName);
		
		// ---- 데이터 저장 ----
		
		// 테이블 정보 저장
		this.mapper.upsertTableList(schemaName, tableList);
		log.info("table list saved sucessfully.");
		
		// 컬럼 정보 저장
		tableColumnMap.forEach(
			(tableName, columnList) -> {
				this.mapper.upsertColumnList(schemaName, columnList);
			}
		);
		log.info("column list saved sucessfully.");
		
		// 시퀀스 정보 저장
		this.mapper.upsertSequenceList(schemaName, sequenceList);
		log.info("sequence list saved sucessfully.");
		
		// 뷰 정보 저장
		this.mapper.upsertViewList(schemaName, viewList);
		log.info("view list saved sucessfully.");
		
		// 최종 종료 메시지 출력
		log.info("all schema info saved sucessfully.");
	}
}
