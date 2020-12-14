package com.mr.service;

import com.mr.mapper.SpecGroupMapper;
import com.mr.repository.SpecGroup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

@Service
public class SpecGroupService {
    @Autowired
    private SpecGroupMapper specGroupMapper;

    public List<SpecGroup> querySpecGroup(Long cid) {
        Example example = new Example(SpecGroup.class);
        example.createCriteria().andEqualTo("cid",cid);
        return specGroupMapper.selectByExample(example);
    }


    public void saveSpecGroup(SpecGroup specGroup) {
        if(specGroup.getId() == null){
            specGroupMapper.insertSelective(specGroup);
        }else{
            specGroupMapper.updateByPrimaryKeySelective(specGroup);
        }
    }

    public void deleteSpecGroup(Integer id) {
        specGroupMapper.deleteByPrimaryKey(id);
    }
}
