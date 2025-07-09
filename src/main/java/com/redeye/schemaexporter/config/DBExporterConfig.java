package com.redeye.schemaexporter.config;

import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import com.jutools.DBDriverType;

/**
 * 스키마 저장 데이터베이스 접속 설정 클래스
 * 
 * @author jmsohn
 */
@Configuration
@ConditionalOnProperty(name="app.exporter.type", havingValue="DB")
@MapperScan(
	basePackages = "com.redeye.schemaexporter.exporter.db",
	sqlSessionFactoryRef = "exporterSqlSessionFactory"
)
public class DBExporterConfig {
	
	/** db type */
	@Value("${app.exporter.db.datasource.type}")
	private DBDriverType type;
	
	/** host */
	@Value("${app.exporter.db.datasource.host}")
	private String host;
	
	/** port */
	@Value("${app.exporter.db.datasource.port}")
	private int port;

	/** database */
	@Value("${app.exporter.db.datasource.database}")
	private String database;
	
	/** 접속 UserName */
	@Value("${app.exporter.db.datasource.username}")
	private String username;
	
	/** 접속 Password */
	@Value("${app.exporter.db.datasource.password}")
	private String password;

	
    @Bean(name = "exporterDataSource")
    DataSource dataSource() throws Exception {
    	
        return DataSourceBuilder.create()
	        .driverClassName(this.type.getDriver())
	        .url(this.type.getUrl(this.host, this.port, this.database))
	        .username(this.username)
	        .password(this.password)
			.build();
    }

    @Bean(name = "exporterSqlSessionFactory")
    SqlSessionFactory sqlSessionFactory(@Qualifier("exporterDataSource") DataSource dataSource) throws Exception {

    	// Mapper 자원 로딩
        Resource[] resources = new PathMatchingResourcePatternResolver()
                .getResources("classpath:mapper/exporter/*.xml");
    	
        // Session Factory 생성 후 반환
        SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
        sessionFactory.setDataSource(dataSource);
        sessionFactory.setMapperLocations(resources);
        
        return sessionFactory.getObject();
    }

    @Bean(name = "exporterSqlSessionTemplate")
    SqlSessionTemplate sqlSessionTemplate(@Qualifier("exporterSqlSessionFactory") SqlSessionFactory sqlSessionFactory) {
        return new SqlSessionTemplate(sqlSessionFactory);
    }
}
