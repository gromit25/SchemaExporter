package com.redeye.dbspec.common.dto;

import lombok.Data;

/**
 * 키 컬럼 정보 DTO
 * 
 * @author jmsohn
 */
@Data
public class KeyColumnD {
	
	/** 테이블 명 */
	private String tableName;
	
	/** 컬럼 명 */
	private String columnName;
	
	/** 키 타입 */
	private String keyType;
	
	/** 외부 키일 경우 테이블 명 */
	private String refTableName;
	
	/** 외부 키일 경우 컬럼 명 */
	private String refColumnName;
	
	/**
	 * 테이블명.컬럼영 반환
	 * 
	 * @return 테이블명.컬럼명
	 */
	public String getKey() {
		return this.tableName + "." + this.columnName;
	}
	
	/**
	 * 외부키 테이블명.컬럼영 반환
	 * 
	 * @return 외부키 테이블명.컬럼명
	 */
	public String getRef() {
		
		if(this.refTableName == null || this.refColumnName == null) {
			return "";
		}
		
		return this.refTableName + "." + this.refColumnName;
	}
}
