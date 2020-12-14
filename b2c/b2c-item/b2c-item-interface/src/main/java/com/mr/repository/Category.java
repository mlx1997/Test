package com.mr.repository;

import lombok.Data;
import tk.mybatis.mapper.annotation.KeySql;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Table(name="tb_category")

public class Category {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @KeySql(useGeneratedKeys = true)//增加后回显Id问题
    private Long id;
    private String name;
    private Long parentId;
    private Boolean isParent;
    private Integer sort;
}