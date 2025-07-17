package com.shmashine.api.entity.base;

/**
 * @PackageName com.sharingtrolley.model.COMMON
 * @ClassName SequenceRange
 * @Date 2019/11/27 13:35
 * @Author Liulifu
 * Version v1.0
 * @description 采番编号范围采番
 */
public class SequenceRange {
    private int fromId;
    private int ToId;

    public int getFromId() {
        return fromId;
    }

    public void setFromId(int fromId) {
        this.fromId = fromId;
    }

    public int getToId() {
        return ToId;
    }

    public void setToId(int toId) {
        ToId = toId;
    }

    @Override
    public String toString() {
        return "SequenceRange{" +
                "fromId=" + fromId +
                ", ToId=" + ToId +
                '}';
    }
}
