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

/**
 * 스키마 저장 데이터베이스 접속 설정 클래스
 * 
 * @author jmsohn
 */
@Configuration
@MapperScan(
	basePackages = "com.redeye.dbspec.misc",
	sqlSessionFactoryRef = "miscSqlSessionFactory"
)
public class MiscDbConfig {
	
	/** 접속 드라이버 클래스 명 */
	@Value("${spring.datasource.misc.driver-class-name}")
	private String driverClassName;
	
	/** 접속 URL */
	@Value("${spring.datasource.misc.url}")
	private String url;
	
	/** 접속 UserName */
	@Value("${spring.datasource.misc.username}")
	private String username;
	
	/** 접속 Password */
	@Value("${spring.datasource.misc.password}")
	private String password;

	
    @Bean(name = "miscDataSource")
    DataSource dataSource() {
    	
        return DataSourceBuilder.create()
	        .driverClassName(driverClassName)
	        .url(url)
	        .username(username)
	        .password(password)
			.build();
    }

    @Bean(name = "miscSqlSessionFactory")
    SqlSessionFactory sqlSessionFactory(@Qualifier("miscDataSource") DataSource dataSource) throws Exception {

    	// Mapper 자원 로딩
        Resource[] resources = new PathMatchingResourcePatternResolver()
                .getResources("classpath:mapper/misc/*.xml");
    	
        // Session Factory 생성 후 반환
        SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
        sessionFactory.setDataSource(dataSource);
        sessionFactory.setMapperLocations(resources);
        
        return sessionFactory.getObject();
    }

    @Bean(name = "miscSqlSessionTemplate")
    SqlSessionTemplate sqlSessionTemplate(@Qualifier("miscSqlSessionFactory") SqlSessionFactory sqlSessionFactory) {
        return new SqlSessionTemplate(sqlSessionFactory);
    }
}
