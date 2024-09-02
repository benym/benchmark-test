package com.benym.benchmark.test.BeanBenchMark.model.simple;

import java.io.Serializable;
import java.util.Date;

/**
 * @author: benym
 */
public class DataBaseDO implements Serializable {

    private static final long serialVersionUID = 7503645886166703053L;
    private String name;
    private Integer age;
    private Date time;
    private Date otherTime;
    private Long year;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public Date getOtherTime() {
        return otherTime;
    }

    public void setOtherTime(Date otherTime) {
        this.otherTime = otherTime;
    }

    public Long getYear() {
        return year;
    }

    public void setYear(Long year) {
        this.year = year;
    }
}
