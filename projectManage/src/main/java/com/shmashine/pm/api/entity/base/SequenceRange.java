package com.shmashine.pm.api.entity.base;

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
