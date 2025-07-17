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
 * oreo库数据源配置
 *
 * @author little.li
 */
@Configuration
@MapperScan(basePackages = {"com.shmashine.socket.camera.dao",
        "com.shmashine.socket.device.dao", "com.shmashine.socket.elevator.dao",
        "com.shmashine.socket.fault.dao", "com.shmashine.socket.dal.dao",
        "com.shmashine.socket.file.dao"}, sqlSessionTemplateRef = "oreoSqlSessionTemplate")
public class OreoDataSourceConfig {


    @Autowired
    @Qualifier("oreo")
    private DataSource oreoDataSource;


    @Bean(name = "oreoSqlSessionFactory")
    public SqlSessionFactory oreoSqlSessionFactory() throws Exception {
        SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
        bean.setDataSource(oreoDataSource);
        bean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources("classpath:mapper/oreo/*.xml"));
        return bean.getObject();
    }

    @Bean(name = "oreoSqlSessionTemplate")
    public SqlSessionTemplate oreoSqlSessionTemplate(
            @Qualifier("oreoSqlSessionFactory") SqlSessionFactory sqlSessionFactory) {
        return new SqlSessionTemplate(sqlSessionFactory);
    }
}