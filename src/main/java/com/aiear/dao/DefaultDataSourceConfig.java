package com.aiear.dao;

import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;


/**
 * MyBatis-Spring 연동 설정
 * @author zz
 *
 */
@Configuration
@Lazy
@EnableTransactionManagement
@MapperScan(basePackages={"com.aiear.dao","com.aiear.custom"})
public class DefaultDataSourceConfig {

	@Bean(destroyMethod="close")
	@ConfigurationProperties(prefix = "spring.default.datasource")
	public DataSource defaultDatasource() {
		return DataSourceBuilder.create().build();
	}
	
	@Value("${dbType}")
	String dbType;
	
	@Bean
	public SqlSessionFactory defaultSqlSessionFactory(DataSource defaultDataSource, ApplicationContext appContext) throws Exception {
		SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
		sqlSessionFactoryBean.setDataSource(defaultDataSource);
		sqlSessionFactoryBean.setMapperLocations(appContext.getResources("classpath:defaultmapper/" + dbType + "/*-mapper.xml"));
		sqlSessionFactoryBean.setConfigLocation(appContext.getResource("classpath:default-mybatis-config.xml"));
		return sqlSessionFactoryBean.getObject();
	}
	
	@Bean
	public SqlSession defaultSqlSessionTemplate(SqlSessionFactory defaultSqlSessionFactory) throws Exception {
		return new SqlSessionTemplate(defaultSqlSessionFactory);
	}

//	@Bean(name="txManager")
//	public PlatformTransactionManager  transactionManager() {
//        DataSourceTransactionManager transactionManager = new DataSourceTransactionManager(defaultDatasource());
//        transactionManager.setGlobalRollbackOnParticipationFailure(false);
//        return transactionManager;
//    }
    
}
