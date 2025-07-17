package com.shmashine.socket.mongo.utils;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.group;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.match;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.project;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import com.mongodb.client.result.DeleteResult;

import lombok.Data;

/**
 * MongoTemplateUtil
 *
 * @author jiangheng
 * @version V1.0.0 - 2022/8/8 15:30
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
     * 查询所有
     */
    public <T> List<T> findAll(Class<T> t) {
        return mongoTemplate.findAll(t);
    }

    /**
     * 根据id查询
     */
    public <T> T findById(Long id, Class<T> t) {
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

    /**
     * IN 查询
     */
    public <T> List<T> queryListByAgeIn(Class<T> clazz, List<String> ids) {

        Query query = new Query(Criteria.where("id").in(ids));

        return mongoTemplate.find(query, clazz);
    }

    /**
     * 获取范围内楼层统计
     *
     * @param elevatorCode 电梯编号
     * @param startTime    统计时间-查询开始时间段
     * @param endTime      统计时间-查询开始时间段
     * @return 楼层统计列表
     */
    public List<AggregationResult> getFloorStopCountInfo(String elevatorCode, String startTime, String endTime) {

        Aggregation aggregation = Aggregation.newAggregation(
                match(Criteria.where("elevatorCode").is(elevatorCode)),
                project()
                        .andExpression("dateToString('%Y-%m-%d', '$countTime')").as("statisticalDate")
                        .andExpression("dateToString('%H:%M:%S', '$countTime')").as("statisticalTime")
                        .and("floor").as("floor")
                        .and("floorCount").as("floorCount"),
                match(Criteria.where("statisticalTime").gte(startTime).lt(endTime)),
                group("statisticalDate", "floor")
                        .sum("floorCount").as("floorCount"),
                project()
                        .and("statisticalDate").as("statisticalDate")
                        .and("floor").as("floor")
                        .and("floorCount").as("floorCount")
        );

        AggregationResults<AggregationResult> results =
                mongoTemplate.aggregate(aggregation, "floorStopCountInfo", AggregationResult.class);

        return results.getMappedResults();
    }

    /**
     * 统计结果实体类
     */
    @Data
    public static class AggregationResult {
        private String statisticalDate;
        private Integer floor;
        private Integer floorCount;
    }

}
