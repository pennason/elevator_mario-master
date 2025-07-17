package com.shmashine.socket.config;

import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

/**
 * nezha库数据源配置
 *
 * @author little.li
 */
@Configuration
@MapperScan(basePackages = {"com.shmashine.socket.nezha.dao"}, sqlSessionTemplateRef = "nezhaSqlSessionTemplate")
public class NezhaDataSourceConfig {


    @Autowired
    @Qualifier("nezha")
    private DataSource nezhaDataSource;


    @Bean(name = "nezhaSqlSessionFactory")
    public SqlSessionFactory nezhaSqlSessionFactory() throws Exception {
        SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
        bean.setDataSource(nezhaDataSource);
        bean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources("classpath:mapper/nezha/*.xml"));
        return bean.getObject();
    }


    @Bean(name = "nezhaSqlSessionTemplate")
    public SqlSessionTemplate nezhaSqlSessionTemplate(
            @Qualifier("nezhaSqlSessionFactory") SqlSessionFactory sqlSessionFactory) {

        return new SqlSessionTemplate(sqlSessionFactory);
    }


}