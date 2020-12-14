package com.mr.web;

import com.mr.repository.SpecGroup;
import com.mr.repository.SpecParam;
import com.mr.service.SpecGroupService;
import com.mr.service.SpecParamService;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("spec")
public class SpecController {
    @Autowired
    private SpecGroupService specGroupService;

    @Autowired
    private SpecParamService specParamService;
    /**
     * groups 查询
     * @param cid
     * @return
     */
    @GetMapping("groups/{cid}")
    public ResponseEntity<List<SpecGroup>> querySpecGroup(@PathVariable("cid") Long cid){
        List<SpecGroup> list=specGroupService.querySpecGroup(cid);
        return ResponseEntity.ok().body(list);
    }

    @PostMapping("group")
    public ResponseEntity<Void> saveSpecGroup(@RequestBody SpecGroup specGroup){
        specGroupService.saveSpecGroup(specGroup);
        return ResponseEntity.ok(null);
    }

    @PutMapping("group")
    public ResponseEntity<Void> editSpecGroup(@RequestBody SpecGroup specGroup){
        specGroupService.saveSpecGroup(specGroup);
        return ResponseEntity.ok(null);
    }

    @DeleteMapping("group/{id}")
    public ResponseEntity<Void> deleteSpecGroup(@PathVariable("id") Integer id){
        specGroupService.deleteSpecGroup(id);
        return ResponseEntity.ok(null);
    }


    /**
     * 查
     * @param gid
     * @param cid
     * @return
     */
    @GetMapping("params")
    public ResponseEntity<List<SpecParam>> querySpecParam(
            @RequestParam(value = "gid",required = false) Long gid,
            @RequestParam(value = "cid",required = false) Long cid,
            @RequestParam(value = "searching",required = false) Boolean searching,
            @RequestParam(value = "generic",required = false) Boolean generic
            ){
        List<SpecParam> list=specParamService.querySpecParam(gid,cid,searching,generic);
        return ResponseEntity.ok().body(list);
    }

    @PostMapping("param")
    public ResponseEntity<Void> saveSpecParam(@RequestBody SpecParam param){
        specParamService.saveSpecParam(param);
        return ResponseEntity.ok().body(null);
    }

    @PutMapping("param")
    public ResponseEntity<Void> editSpecParam(@RequestBody SpecParam param){
        specParamService.saveSpecParam(param);
        return ResponseEntity.ok().body(null);
    }

    @DeleteMapping("param/{id}")
    public ResponseEntity<Void> deleteSpecParam(@Param("id") Integer id){
        specParamService.deleteSpecParam(id);
        return ResponseEntity.ok().body(null);
    }

}
