package com.shmashine.api.config.mongodb;

import javax.annotation.Resource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.core.convert.DbRefResolver;
import org.springframework.data.mongodb.core.convert.DefaultDbRefResolver;
import org.springframework.data.mongodb.core.convert.DefaultMongoTypeMapper;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;
import org.springframework.data.mongodb.core.mapping.MongoMappingContext;

/**
 * @Author jiangheng
 * @create 2022/8/11 17:49
 */
@Configuration
public class MongoDBConfig {

    @Resource
    private MongoDatabaseFactory mongoDbFactory;

    @Resource
    private MongoMappingContext mongoMappingContext;

    /**
     * 转换类配置
     *
     * @return 转换类
     */
    @Bean
    public MappingMongoConverter mappingMongoConverter() {
        DbRefResolver dbRefResolver = new DefaultDbRefResolver(mongoDbFactory);
        MappingMongoConverter converter = new MappingMongoConverter(dbRefResolver, mongoMappingContext);
        //不保存 _class 属性到mongo
        converter.setTypeMapper(new DefaultMongoTypeMapper(null));
        return converter;
    }

}
