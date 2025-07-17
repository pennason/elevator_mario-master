package com.shmashine.api.mongo.utils;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import com.mongodb.client.result.DeleteResult;
import com.shmashine.api.mongo.entity.DevicePingQuality;
import com.shmashine.api.mongo.entity.FloorStopCountInfo;

/**
 * @Author jiangheng
 * @create 2022/8/8 15:30
 */
@Component
public class MongoTemplateUtil {

    @Resource
    private MongoTemplate mongoTemplate;

    /**
     * 添加
     *
     * @param t
     * @param <T>
     * @return
     */
    public <T> T insert(T t) {
        T insert = mongoTemplate.insert(t);
        return insert;
    }

    /**
     * 根据id全文档更新，没有的话则新建
     *
     * @param doc
     * @return
     */
    public <T> void updateById(T doc) {

        mongoTemplate.save(doc);

    }

    /**
     * 查询所有
     *
     * @param t
     * @param <T>
     * @return
     */
    public <T> List<T> findAll(Class<T> t) {
        return mongoTemplate.findAll(t);
    }

    /**
     * 查询所有，根据字段排序
     *
     * @param clazz
     * @param properties
     * @param <T>
     * @return
     */
    public <T> List<T> findAllByASC(Class<T> clazz, String properties) {
        Query query = new Query().with(Sort.by(Sort.Direction.ASC, properties));
        return mongoTemplate.find(query, clazz);
    }

    /**
     * 根据id查询
     *
     * @param id
     * @param t
     * @param <T>
     * @return
     */
    public <T> T findById(Long id, Class<T> t) {
        return mongoTemplate.findById(id, t);
    }

    public <T> T findById(String id, Class<T> t) {
        return mongoTemplate.findById(id, t);
    }

    /**
     * 根据id删除
     *
     * @param id
     * @param clazz
     * @param <T>
     * @return
     */
    public <T> Long removeById(String id, Class<T> clazz) {

        Query query = new Query(Criteria.where("_id").is(id));

        DeleteResult remove = mongoTemplate.remove(query, clazz);

        return remove.getDeletedCount();
    }

    /**
     * IN 查询
     *
     * @param clazz
     * @param ids
     * @param <T>
     * @return
     */
    public <T> List<T> queryListByAgeIn(Class<T> clazz, List<String> ids) {

        Query query = new Query(Criteria.where("id").in(ids));

        return mongoTemplate.find(query, clazz);
    }

    public <T> List<T> queryByElevatorAndFault(String elevatorCode, String faultType, String ST, Class<T> clazz) {
        Query query = new Query(Criteria.where("elevatorCode").is(elevatorCode).and("faultType").is(faultType).and("ST").is(ST));
        return mongoTemplate.find(query, clazz);
    }

    /**
     * 获取范围内通讯质量
     *
     * @param elevatorCode
     * @param beginTime
     * @param endTime
     * @return
     */
    public List<DevicePingQuality> getPingQualityByElevatorCode(String elevatorCode, Date beginTime, Date endTime) {

        Query query = new Query(Criteria.where("elevatorCode").is(elevatorCode).and("updateTime").gte(beginTime).lte(endTime))
                .with(Sort.by(Sort.Direction.DESC, "updateTime"));
        return mongoTemplate.find(query, DevicePingQuality.class);

    }

    /**
     * 获取今日通讯质量
     *
     * @param elevatorCode
     * @param today
     * @return
     */
    public DevicePingQuality getPingQualityByElevatorCode(String elevatorCode, String today) {

        Query query = new Query(Criteria.where("elevatorCode").is(elevatorCode).and("updateDate").is(today));
        return mongoTemplate.findOne(query, DevicePingQuality.class);

    }

    /**
     * 获取范围内楼层统计
     *
     * @param elevatorCode 电梯编号
     * @param beginTime    统计时间-查询开始时间
     * @return 楼层统计列表
     */
    public List<FloorStopCountInfo> getFloorStopCountInfo(String elevatorCode, Integer floor, Date beginTime) {

        Query query = new Query(Criteria.where("elevatorCode").is(elevatorCode).and("floor").is(floor).and("countTime").gte(beginTime))
                .with(Sort.by(Sort.Direction.DESC, "countTime"));
        return mongoTemplate.find(query, FloorStopCountInfo.class);
    }
}
