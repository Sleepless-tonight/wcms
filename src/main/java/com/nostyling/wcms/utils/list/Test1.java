package com.example.testall.Test;

/**
 * @author shiliang
 * @Classname Test
 * @Date 2021/1/21 20:18
 * @Description 模拟被排序的对象。
 */
public class Test1 {
    /**
     * 颜色
     */
    private String colour;
    /**
     * 空置率
     */
    private String nullValue;
    /**
     * 字段名
     */
    private String fieldName;
    /**
     * 其它字段
     */
    private String otherFieldName;

    @Override
    public String toString() {
        return "Test1{" +
                "colour='" + colour + '\'' +
                ", nullValue='" + nullValue + '\'' +
                ", fieldName='" + fieldName + '\'' +
                ", otherFieldName='" + otherFieldName + '\'' +
                '}';
    }

    public Test1(String colour, String nullValue, String fieldName, String otherFieldName) {
        this.colour = colour;
        this.nullValue = nullValue;
        this.fieldName = fieldName;
        this.otherFieldName = otherFieldName;
    }

    public String getColour() {
        return colour;
    }

    public void setColour(String colour) {
        this.colour = colour;
    }

    public String getNullValue() {
        return nullValue;
    }

    public void setNullValue(String nullValue) {
        this.nullValue = nullValue;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getOtherFieldName() {
        return otherFieldName;
    }

    public void setOtherFieldName(String otherFieldName) {
        this.otherFieldName = otherFieldName;
    }
}
