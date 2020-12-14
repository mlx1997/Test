package com.mr.repository;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

public interface GoodsReponsitory extends ElasticsearchRepository<Goods, Long> {

    List<Goods> findByAll(String key);

}
