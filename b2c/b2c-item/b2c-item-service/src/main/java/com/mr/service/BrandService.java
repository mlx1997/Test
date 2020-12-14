package com.mr.service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.mr.mapper.BrandMapper;
import com.mr.repository.Brand;
import com.mr.utils.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.ArrayList;
import java.util.List;

@Service
public class BrandService {
    @Autowired
    private BrandMapper brandMapper;

    /**
     *  条件查询  分页  排序
     * @param page
     * @param rows
     * @param key
     * @param sortBy
     * @param desc
     * @return
     */
    public PageResult<Brand> queryBrand(Integer page, Integer rows, String key, String sortBy, Boolean desc) {
        //  page 页数  rows 条数
        PageHelper.startPage(page, rows);
        //  过滤条件
        Example example = new Example(Brand.class);
        if(key!=null && !"".equals(key)){
            // and 交集      设置 'name' 字段   模糊查询
            example.createCriteria().andLike("name","%"+key.trim()+"%");
        }

        if(sortBy != null && !"".equals(sortBy)){
            // 增加一个 排序条件
            example.setOrderByClause(sortBy+ (desc?" asc":" desc"));
        }

        Page<Brand> brands = (Page<Brand>) brandMapper.selectByExample(example);

        return new PageResult<Brand>(brands.getTotal(),brands.getResult());
    }

    @Transactional  // 多表之间控制事务
    public void addBrand(Brand brand ,List<Long> cids){
        brandMapper.insertSelective(brand);

        for (Long cid:cids) {
            brandMapper.insertCategoryBrand(brand.getId(),cid);
        }
    }

    @Transactional  // 多表之间控制事务
    public void updateBrand(Brand brand, List<Long> cids) {
        brandMapper.updateByPrimaryKeySelective(brand);

        //  删除原来的 ,,,
        brandMapper.deleteCategoryBrand(brand.getId());

        for (Long cid:cids) {
            brandMapper.insertCategoryBrand(brand.getId(),cid);
        }
    }

    public void deleteBrand(Long bid) {
        brandMapper.deleteByPrimaryKey(bid);
    }

    public List<Brand> queryBrandByCid(Long cid) {
        List<Long> list=brandMapper.queryCateBrandByCid(cid);
        List<Brand> bra=new ArrayList<>();
        for (Long bid : list) {
            Brand brand1 = brandMapper.selectByPrimaryKey(bid);
            bra.add(brand1);
        }

        return bra;
    }

    public Brand queryBrandBybId(Long bid) {
        return brandMapper.selectByPrimaryKey(bid);
    }
}
