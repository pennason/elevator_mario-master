package com.shmashine.sender.utils;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import com.mongodb.client.result.DeleteResult;

/**
 * 默认说明
 *
 * @author jiangheng
 * @since 2022/8/8 15:30
 */
@Component
public class MongoTemplateUtil {

    @Resource
    private MongoTemplate mongoTemplate;

    /**
     * 添加
     */
    public <T> T insert(T t) {
        T insert = mongoTemplate.insert(t);
        return insert;
    }

    /**
     * 根据id全文档更新，没有的话则新建
     */
    public <T> void updateById(T doc) {

        mongoTemplate.save(doc);

    }

    /**
     * 查询所有
     */
    public <T> List<T> findAll(Class<T> t) {
        return mongoTemplate.findAll(t);
    }

    /**
     * 查询所有，根据字段排序
     */
    public <T> List<T> findAllByASC(Class<T> clazz, String properties) {
        Query query = new Query().with(Sort.by(Sort.Direction.ASC, properties));
        return mongoTemplate.find(query, clazz);
    }

    /**
     * 根据id查询
     */
    public <T> T findById(Long id, Class<T> t) {
        return mongoTemplate.findById(id, t);
    }

    public <T> T findById(String id, Class<T> t) {
        return mongoTemplate.findById(id, t);
    }

    /**
     * 根据id删除
     */
    public <T> Long removeById(String id, Class<T> clazz) {

        Query query = new Query(Criteria.where("_id").is(id));

        DeleteResult remove = mongoTemplate.remove(query, clazz);

        return remove.getDeletedCount();
    }


    public <T> List<T> queryByElevatorCodeDesc(String elevatorCode, Class<T> clazz, String properties) {
        Query query = new Query(Criteria.where("elevatorCode").is(elevatorCode))
                .with(Sort.by(Sort.Direction.ASC, properties));
        return mongoTemplate.find(query, clazz);
    }

    public <T> T queryByFaultId(String faultId, Class<T> clazz) {
        Query query = new Query(Criteria.where("faultId").is(faultId));
        return mongoTemplate.findOne(query, clazz);
    }
}
