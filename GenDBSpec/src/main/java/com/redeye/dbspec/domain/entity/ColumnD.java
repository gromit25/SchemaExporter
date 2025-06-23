package com.redeye.dbspec.domain.entity;

import lombok.Data;

/**
 * 컬럼 정보 DTO
 * 
 * @author jmsohn
 */
@Data
public class ColumnD {
	
	/** 테이블 명 */
    private String tableName = "";
    
    /** 컬럼 명 */
    private String columnName = "";
    
    /** 컬럼 주석 */
    private String description = "";
    
    /** 타입 명 */
    private String type = "";
    
    /** 길이 */
    private int size = 0;
    
    /** Primary Key 여부 - "T", "F" */
    private String isPrimaryKey = "F";
    
    /** Unique 여부 - "T", "F" */
    private String isUnique = "F";
    
    /** Null 여부 - "T", "F" */
    private String isNullable = "F";
    
    /** Foreign Key 여부 - "T", "F" */
    private String isForeignKey = "F";
    
    /** 외부 참조 테이블 명 */
    private String refTableName = "";
    
    /** 외부 참조 컬럼 명 */
    private String refColumnName = "";
    
	/**
	 * 테이블명.컬럼영 반환
	 * 
	 * @return 테이블명.컬럼명
	 */
	public String getKey() {
		return tableName + "." + columnName;
	}
	
	/**
	 * 외부 키(테이블명.컬럼명) 반환
	 * 
	 * @return 외부 키(테이블명.컬럼명)
	 */
	public String getRef() {
		
		if(isForeignKey == null || isForeignKey.equals("F") == true) {
			return "";
		}
		
		return refTableName + "." + refColumnName;
	}
}
