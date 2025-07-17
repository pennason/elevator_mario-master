// Copyright (C) 2022 Shmashine Holding Ltd. All Rights Reserved.

package com.shmashine.common.convert;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.util.CollectionUtils;

/**
 * 实体与DTO互转.
 *
 * @param <Entity> entity实体
 * @param <DTO>    DTOE对象
 * @author chenxue(chenxue4076 @ 163.com)
 * @version v1.0.0  -  2023/1/7 18:46
 * @since v1.0.0
 */

public abstract class BaseEntityDtoConvertor<Entity, DTO> {

    /**
     * DTO 转 Entity
     *
     * @param source dto
     * @return entity
     */
    public abstract Entity dto2Entity(DTO source);

    /**
     * Entity 转 DTO
     *
     * @param source entity
     * @return dto
     */
    public abstract DTO entity2Dto(Entity source);

    public List<DTO> entity2DtoList(List<Entity> sources) {
        if (CollectionUtils.isEmpty(sources)) {
            return Collections.emptyList();
        }
        return sources.stream().map(this::entity2Dto).filter(Objects::nonNull).collect(Collectors.toList());
    }

    public List<Entity> dto2EntityList(List<DTO> sources) {
        if (CollectionUtils.isEmpty(sources)) {
            return Collections.emptyList();
        }
        return sources.stream().map(this::dto2Entity).filter(Objects::nonNull).collect(Collectors.toList());
    }


}