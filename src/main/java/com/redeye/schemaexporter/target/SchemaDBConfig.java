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
@MapperScan
(
	basePackages = "com.redeye.schemaexporter.target",
	sqlSessionFactoryRef = "targetSqlSessionFactory"
)
public class SchemaDBConfig {
	
	/** db type */
	@Value("${app.target.db.type}")
	private DBDriverType type;


	@Bean(name = "targetDataSource")
	DataSource dataSource(
		@Value("${app.target.db.host}") String host,
		@Value("${app.target.db.port}") int port,
		@Value("${app.target.db.database}") String database,
		@Value("${app.target.db.username}") String username,
		@Value("${app.target.db.password}") String password
    ) throws Exception {

        return DataSourceBuilder.create()
	        .driverClassName(this.type.getDriver())
	        .url(this.type.getUrl(host, port, database))
	        .username(username)
	        .password(password)
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

    	switch(this.type) {
            case ORACLE:
            	return "classpath:mapper/target/oracle/*.xml";
            case POSTGRESQL:
            	return "classpath:mapper/target/postgresql/*.xml";
            case MYSQL:
            	return "classpath:mapper/target/mysql/*.xml";
            default:
            	throw new Exception("unexpected target db type:" + this.type);
        }
    }
}
