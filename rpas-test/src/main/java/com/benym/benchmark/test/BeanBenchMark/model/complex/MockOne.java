package com.benym.benchmark.test.BeanBenchMark.model.complex;

import java.io.Serializable;
import java.util.List;

/**
 * @author: benym
 */
public class MockOne implements Serializable {

    private static final long serialVersionUID = -8650205635404915114L;

    private String id;
    private String parentId;
    private String name;
    private String no;
    private String address;
    private Integer level;
    private Integer orderNo;
    private List<MockOne> children;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNo() {
        return no;
    }

    public void setNo(String no) {
        this.no = no;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public Integer getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(Integer orderNo) {
        this.orderNo = orderNo;
    }

    public List<MockOne> getChildren() {
        return children;
    }

    public void setChildren(List<MockOne> children) {
        this.children = children;
    }
}
