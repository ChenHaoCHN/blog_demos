package com.bolingcavalry.curd.entity;

import java.sql.Date;

/**
 * @Description: 实体类
 * @author: willzhao E-mail: zq2599@gmail.com
 * @date: 2020/8/4 8:24
 */
public class Log {
    private Integer id;
    private Integer userId;
    private String action;
    private Date createTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        return "Log{" +
                "id=" + id +
                ", userId=" + userId +
                ", action='" + action + '\'' +
                ", createTime=" + createTime +
                '}';
    }
}
