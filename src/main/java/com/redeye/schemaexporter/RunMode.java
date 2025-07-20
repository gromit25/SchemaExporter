package com.redeye.schemaexporter;

/**
 * Schema Exporter 실행 모드
 * 
 * @author jmsohn
 */
public enum RunMode {
	
	/** 1 회 실행 후 종료 */
    ONCE,
    
    /** 주기별 실행 */
    CRON;
}
