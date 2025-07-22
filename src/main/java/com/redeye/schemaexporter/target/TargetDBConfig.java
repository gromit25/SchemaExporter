package com.redeye.schemaexporter.target;

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

import com.jutools.DBDriverType;

/**
 * DB 스키마를 수집할 대상 데이터베이스 접속 설정 클래스
 * 
 * @author jmsohn
 */
@Configuration
@MapperScan(
	basePackages = "com.redeye.schemaexporter.target",
	sqlSessionFactoryRef = "targetSqlSessionFactory"
)
public class TargetDBConfig {
	
    /** db type */
    @Value("${app.target.datasource.type}")
    private DBDriverType type;
	
    /** host */
    @Value("${app.target.datasource.host}")
    private String host;
	
    /** port */
    @Value("${app.target.datasource.port}")
    private int port;

    /** database */
    @Value("${app.target.datasource.database}")
    private String database;
	
    /** 접속 UserName */
    @Value("${app.target.datasource.username}")
    private String username;
	
    /** 접속 Password */
    @Value("${app.target.datasource.password}")
    private String password;
	
    @Bean(name = "targetDataSource")
    DataSource dataSource() throws Exception {

        return DataSourceBuilder.create()
	        .driverClassName(this.type.getDriver())
	        .url(this.type.getUrl(this.host, this.port, this.database))
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

    	return switch(this.type) {
            case ORACLE -> "classpath:mapper/target/oracle/*.xml";
            case POSTGRESQL -> "classpath:mapper/target/postgresql/*.xml";
            case MYSQL -> "classpath:mapper/target/mysql/*.xml";
            default -> throw new Exception("unexpected target db type:" + this.type);
        };
    }
}
