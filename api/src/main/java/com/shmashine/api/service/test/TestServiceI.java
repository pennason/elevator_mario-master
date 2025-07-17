package com.shmashine.api.service.test;

import java.util.List;

/**
 * 采番
 */
public interface TestServiceI {

    List<String> list(String key);

    boolean add(String key);

    boolean delete(String key);

}
