package com.redeye.dbspec;

import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import lombok.Getter;

/**
 * DB 스키마를 수집할 대상 데이터베이스 접속 설정 클래스
 * 
 * @author jmsohn
 */
@Configuration
@MapperScan(
	basePackages = "com.redeye.dbspec.target",
	sqlSessionFactoryRef = "targetSqlSessionFactory"
)
public class TargetDbConfig {
	
	/**
	 * 대상 DB의 타입
	 * 
	 * @author jmsohn
	 */
	private enum DBType {
		
		/** Oracle DB */
		ORACLE("oracle.jdbc.OracleDriver", "classpath:mapper/target/oracle/*.xml"),
		
		/** Postgresql DB */
		POSTGRESQL("org.postgresql.Driver", "classpath:mapper/target/postgresql/*.xml"),
		
		/** MySQL DB */
		MYSQL("com.mysql.cj.jdbc.Driver", "classpath:mapper/target/mysql/*.xml");
		
		// ---------------------
		
		/** DB 접속 드라이버 클래스 명 */
		@Getter
		private String driverClassName;
		
		/** mapper 위치 */
		@Getter
		private String mapperPath;
		
		/**
		 * 생성자
		 * 
		 * @param driverClassName JDBC 드라이버 클래스 명
		 * @param mapperPath Mapper 위치
		 */
		DBType(String driverClassName, String mapperPath) {
			this.driverClassName = driverClassName;
			this.mapperPath = mapperPath;
		}
	}

	/** 대상 DB의 타입 */
	private DBType type;
	
	/** 대상 DB의 타입 설정 문자열 */
	@Value("${target.datasource.type}")
	private String typeStr;
	
	/** 접속 URL */
	@Value("${target.datasource.url}")
	private String url;
	
	/** 접속 UserName */
	@Value("${target.datasource.username}")
	private String username;
	
	/** 접속 Password */
	@Value("${target.datasource.password}")
	private String password;
	
    @Bean(name = "targetDataSource")
    DataSource dataSource() {
    	
        return DataSourceBuilder.create()
	        .driverClassName(this.getType().getDriverClassName())
	        .url(this.url)
	        .username(this.username)
	        .password(this.password)
			.build();
    }

    @Bean(name = "targetSqlSessionFactory")
    SqlSessionFactory sqlSessionFactory(
    	@Qualifier("targetDataSource") DataSource dataSource
    ) throws Exception {

    	// Mapper 자원 로딩
        Resource[] resources = new PathMatchingResourcePatternResolver()
                .getResources(this.getType().getMapperPath());
    	
        // Session Factory 생성 후 반환
        SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
        sessionFactory.setDataSource(dataSource);
        sessionFactory.setMapperLocations(resources);
        
        return sessionFactory.getObject();
    }

    @Bean(name = "targetSqlSessionTemplate")
    SqlSessionTemplate sqlSessionTemplate(
    	@Qualifier("targetSqlSessionFactory") SqlSessionFactory sqlSessionFactory
    ) {
        return new SqlSessionTemplate(sqlSessionFactory);
    }
    
    /**
     * 대상 DB 타입 반환
     * 
     * @return 대상 DB 타입
     */
    private DBType getType() {
    	
    	if(this.type == null) {
    		
    		for(DBType candidateType: DBType.values()) {
    			
    			if(candidateType.name().equals(this.typeStr) == true) {
    				this.type = candidateType;
    				break;
    			}
    		}
    	}
    	
    	return this.type;
    }
}
