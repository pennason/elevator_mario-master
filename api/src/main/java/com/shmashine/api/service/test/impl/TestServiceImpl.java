package com.shmashine.api.service.test.impl;

import java.util.List;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;
import com.shmashine.api.service.test.TestServiceI;

@Service
public class TestServiceImpl implements TestServiceI {

    private List<String> list = Lists.newArrayList();

    @Override
    @Cacheable(value = "test1", key = "#key")
    public List<String> list(String key) {
        System.out.println("TestServiceImpl list() " + key);
        return list;
    }

    @Override
//    @CacheEvict(value = "test1" , allEntries = true)
    public boolean add(String key) {
        return list.add(key);
    }

    @Override
//    @CacheEvict(value = "test1" , allEntries = true)
    public boolean delete(String key) {
        return list.remove(key);
    }
}