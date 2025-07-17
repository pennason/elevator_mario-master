package com.shmashine.api.service.system.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.shmashine.api.dao.BizSequenceDao;
import com.shmashine.api.entity.base.SequenceRange;
import com.shmashine.api.service.system.BizSequenceService;

import lombok.extern.slf4j.Slf4j;

/**
 * 系统采番
 * Liulifu
 */
@SuppressWarnings("ALL")
@Service
@Slf4j
public class BizSequenceServiceImpl implements BizSequenceService {

    @Autowired
    private BizSequenceDao bizSequenceDao;

    /**
     * 获取最新编号
     *
     * @param keyName
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public Integer getNewSequenceID(String keyName) {
        // 检查参数
        if (!StringUtils.hasText(keyName)) {
            // 采番失败
            return null;
        }
        // 统一转 大写
        String key = keyName.toUpperCase();
        Integer newId = 1;

        Map map = bizSequenceDao.selectKey(key);
        if (map == null) {
            Integer integer = bizSequenceDao.insertKey(key);
            if (integer == 0) {
                // 采番失败
                return newId;
            }
            Map sequenceMap = bizSequenceDao.selectKey(key);
            if (sequenceMap != null) {
                return Integer.parseInt(sequenceMap.get("seqNo").toString());
            }
        } else {
            Integer integer = bizSequenceDao.updateKey(key);
            if (integer == 0) {
                // 采番失败
                return newId;
            }
            Map sequenceMap = bizSequenceDao.selectKey(key);
            if (sequenceMap != null) {
                return Integer.parseInt(sequenceMap.get("seqNo").toString());
            }
        }
        // 采番失败
        return newId;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public SequenceRange GetNewSequenceIDRange(String keyName, int count) {
        SequenceRange sequenceRange = new SequenceRange();
        int newID = 1;
        // 统一转 大写
        keyName = keyName.toUpperCase();
        Map map = bizSequenceDao.selectKey(keyName);
        if (map == null) {
            newID = count;
            Integer integer = bizSequenceDao.insertKeyIDRange(keyName, newID + "");
            if (integer == 0) {
                // 采番失败
                return sequenceRange;
            }
        } else {
            Integer integer = bizSequenceDao.updateKeyIDRange(keyName, count);
            if (integer == 0) {
                // 采番失败
                return sequenceRange;
            }
        }
        List<SequenceRange> sequenceRanges = (List<SequenceRange>) bizSequenceDao.selectKeyIDRange(keyName, --count);
        if (sequenceRanges.size() != 0) {
            return sequenceRanges.get(0);
        }
        return sequenceRange;
    }

    /**
     * 获取唯一键 (一个)
     *
     * @param key,digit 前缀,位数
     * @return
     */
    @Override
    public String getSequenceId(String key, int digit) {
        Integer newSequenceID = getNewSequenceID(key);
        return key + String.format("%0" + (digit - key.length()) + "d", newSequenceID);
    }

    /**
     * 获取唯一键 (多个)
     *
     * @param key,digit,count 前缀,位数,采集数量
     * @return
     */
    @Override
    public List<String> getSequenceIdList(String key, int digit, int count) {
        ArrayList<String> result = new ArrayList<>();
        SequenceRange sequenceRange = GetNewSequenceIDRange(key, count);
        int fromId = sequenceRange.getFromId();
        int toId = sequenceRange.getToId();
        if (fromId != 0 && toId != 0) {
            for (int i = fromId; i <= toId; i++) {
                result.add(key + String.format("%0" + (digit - key.length()) + "d", i));
            }
        }
        return result;
    }
}