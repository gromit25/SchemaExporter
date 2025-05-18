package com.redeye.dbspec.common.dto;

import java.math.BigInteger;

import lombok.Data;

/**
 * 시퀀스 정보 DTO
 * 
 * @author jmsohn
 */
@Data
public class SequenceD {
	
	/** 스키마 아이디 */
	private int schemaId;
	
	/** 시퀀스 명 */
	private String sequenceName;
	
	/** 시작 값 */
	private BigInteger startValue;
	
	/** 최대 값 */
	private BigInteger maxValue;
	
	/** 증가량 */
	private BigInteger increment;
	
	/** cycle 여부 */
	private String isCycle;
}
