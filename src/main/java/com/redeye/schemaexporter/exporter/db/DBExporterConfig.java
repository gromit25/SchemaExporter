package com.redeye.schemaexporter.exporter.db;

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

@Configuration
@ConditionalOnProperty
(
	value = "app.exporter.type",
	havingValue = "DB"
)
@MapperScan
(
	basePackages = "com.redeye.schemaexporter.exporter.db",
	sqlSessionFactoryRef = "exporterSqlSessionFactory"
)
public class DBExporterConfig {

	/** db type */
	@Value("${app.exporter.db.datasource.type}")
	private DBDriverType type;


	@Bean(name = "exporterDataSource")
	DataSource dataSource(
		@Value("${app.exporter.db.datasource.host}") String host,
		@Value("${app.exporter.db.datasource.port}") int port,
		@Value("${app.exporter.db.datasource.database}") String database,
		@Value("${app.exporter.db.datasource.username}") String username,
		@Value("${app.exporter.db.datasource.password}") String password
	) throws Exception {

		return DataSourceBuilder.create()
			.driverClassName(this.type.getDriver())
			.url(this.type.getUrl(host, port, database))
			.username(username)
			.password(password)
			.build();
	}

    @Bean(name = "exporterSqlSessionFactory")
    SqlSessionFactory sqlSessionFactory(@Qualifier("exporterDataSource") DataSource dataSource) throws Exception {

    	// Mapper 자원 로딩
        Resource[] resources = new PathMatchingResourcePatternResolver()
                .getResources("classpath:mapper/exporter/mysql/*.xml");
    	
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
