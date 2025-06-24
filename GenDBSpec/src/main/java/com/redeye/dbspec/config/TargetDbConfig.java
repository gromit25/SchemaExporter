package com.redeye.dbspec.config;

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
	
    /** db type */
    @Value("${target.datasource.type}")
    private String typeStr;
	
    /** host */
    @Value("${target.datasource.host}")
    private String host;
	
    /** port */
    @Value("${target.datasource.port}")
    private int port;

    /** database */
    @Value("${target.datasource.database}")
    private String database;
	
    /** 접속 UserName */
    @Value("${target.datasource.username}")
    private String username;
	
    /** 접속 Password */
    @Value("${target.datasource.password}")
    private String password;
	
    @Bean(name = "targetDataSource")
    DataSource dataSource() {

        DBDriver driverType = DBDriver.find(this.typeStr);

        return DataSourceBuilder.create()
	        .driverClassName(driverType.getDriver())
	        .url(driverType.getUrl(this.host, this.port, this.database))
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
                .getResources(this.getMapperPath());
    	
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
     * 대상 DB 에 따라 Mapper 위치 반환
     * 
     * @return  대상 DB의 Mapper 위치
     */
    private String getMapperPath() throws Exception {
	
        if(StringUtil.isBlank(this.typeStr) == true) {
            throw new Exception("type is null or blank.");
	}
    	
    	return switch(this.typeStr) {
	    case DBDriver.ORACLE.getName() -> "classpath:mapper/target/oracle/*.xml";
	    case DBDriver.POSTGRESQL.getName() -> "classpath:mapper/target/postgresql/*.xml";
	    case DBDriver.MYSQL.getName() -> "classpath:mapper/target/mysql/*.xml";
	    default -> throw new Exception("unexpected target db type:" + this.typeStr);
	};
    }
}
