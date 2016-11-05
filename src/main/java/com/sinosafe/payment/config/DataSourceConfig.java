package com.sinosafe.payment.config;

import com.alibaba.druid.pool.DruidDataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.util.StringUtils;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Created by Angus on Nov 11,2015.
 */
@Configuration
@MapperScan(basePackages = "com.sinosafe.payment.dao.mapper")
@EnableTransactionManagement
public class DataSourceConfig {

    @Autowired
    private Environment env;



    @Bean
    public DataSource dataSource() throws SQLException {

        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setDriverClassName(env.getProperty("db_driverClassName"));
        dataSource.setUrl(env.getProperty("db_url"));
        dataSource.setUsername(env.getProperty("db_username"));
        dataSource.setPassword(env.getProperty("db_password"));
        dataSource.setInitialSize(Integer.parseInt(env.getProperty("db_initialSize")));
        dataSource.setMinIdle(Integer.parseInt(env.getProperty("db_minIdle")));
        dataSource.setMaxActive(Integer.parseInt(env.getProperty("db_maxActive")));
        return dataSource;
    }

    @Bean
    public SqlSessionFactory sqlSessionFactory(DataSource dataSource) throws Exception {

        SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();

        // 获取properties中的对应配置信息
        String mapperPackage = env.getProperty("spring.mybatis.mapperLocations");
        String dialect = env.getProperty("spring.mybatis.dialect");

        Properties properties = new Properties();
        properties.setProperty("dialect", dialect);


        sessionFactory.setDataSource(dataSource);
        sessionFactory.setConfigurationProperties(properties);
        // 设置MapperLocations路径
        if(!StringUtils.isEmpty(mapperPackage)){
            ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();
            sessionFactory.setMapperLocations(resourcePatternResolver.getResources(mapperPackage));

        }
//        // 设置插件
//        sessionFactory.setPlugins(new Interceptor[]{
//                new PaginationStatementInterceptor(),
//                new PaginationResultSetInterceptor()
//        });

        return sessionFactory.getObject();
    }

    @Bean
    public SqlSessionTemplate sqlSessionTemplate(SqlSessionFactory sqlSessionFactory){
        return new SqlSessionTemplate(sqlSessionFactory);
    }

   /* @Bean //name
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(EntityManagerFactoryBuilder builder, DataSource dataSource) {

        LocalContainerEntityManagerFactoryBean entityManagerFactory = builder.dataSource(dataSource).build();

        Properties properties = new Properties();
        properties.setProperty("hibernate.dialect", env.getProperty("spring.jpa.properties.hibernate.dialect"));
        properties.setProperty("hibernate.hbm2ddl.auto", env.getProperty("spring.jpa.hibernate.ddl-auto"));
        entityManagerFactory.setPackagesToScan("com.sinosafe.payment.entity");
        entityManagerFactory.setJpaProperties(properties);

        return entityManagerFactory;
    }*/

    @Bean
    public DataSourceTransactionManager transactionManager(DataSource dataSource){
        return new DataSourceTransactionManager(dataSource);
    }

}
