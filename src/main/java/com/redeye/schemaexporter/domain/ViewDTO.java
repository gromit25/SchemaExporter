package com.redeye.schemaexporter.domain;

import lombok.Data;

/**
 * 뷰 DTO
 * 
 * @author jmsohn
 */
@Data
public class ViewDTO {

	/** 스키마 아이디 */
	private int schemaId;
	
	/** 뷰 명 */
	private String viewName;
	
	/** 뷰 쿼리 문자열 */
	private String query;
}
