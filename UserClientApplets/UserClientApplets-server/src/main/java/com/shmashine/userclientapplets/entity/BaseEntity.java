package com.shmashine.userclientapplets.entity;

import java.util.Date;

import lombok.EqualsAndHashCode;


/**
 * @author jiangheng
 * @version 1.0
 * @date 2022/1/12 15:11
 * 此注解会生成equals(Object other) 和 hashCode()方法
 */

/**
 * 此注解会生成equals(Object other) 和 hashCode()方法
 */

/**onlyExplicitlyIncluded为false时，所有的非静态和非瞬态的字段都会被包含进equals和hashCode方法中；为true时，
 只有在字段上明确使用了EqualsAndHashCode.Include注解才会被包含进equals和hashCode方法中。*/
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class BaseEntity {

    private Date dtCreateTime;
    private Date dtModifyTime;

    public Date getDtCreateTime() {
        return dtCreateTime;
    }

    public void setDtCreateTime(Date dtCreateTime) {
        this.dtCreateTime = dtCreateTime;
    }

    public Date getDtModifyTime() {
        return dtModifyTime;
    }

    public void setDtModifyTime(Date dtModifyTime) {
        this.dtModifyTime = dtModifyTime;
    }
}
