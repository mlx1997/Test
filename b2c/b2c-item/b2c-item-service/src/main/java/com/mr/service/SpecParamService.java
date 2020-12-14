package com.mr.service;

import com.mr.mapper.SpecParamMapper;
import com.mr.repository.SpecParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SpecParamService {
    @Autowired
    private SpecParamMapper specParamMapper;

    public List<SpecParam> querySpecParam(Long gid,Long cid,Boolean searching, Boolean generic) {
        SpecParam param = new SpecParam();
        if(gid != null){
            param.setGroupId(gid);
        }
        if(cid != null){
            param.setCid(cid);
        }
        param.setSearching(searching);
        param.setGeneric(generic);
        return specParamMapper.select(param);
    }

    public void saveSpecParam(SpecParam param) {
        if(param.getId()==0){
            specParamMapper.insertSelective(param);
        }else{
            specParamMapper.updateByPrimaryKeySelective(param);
        }
    }

    public void deleteSpecParam(Integer id) {
        specParamMapper.deleteByPrimaryKey(id);
    }
}
