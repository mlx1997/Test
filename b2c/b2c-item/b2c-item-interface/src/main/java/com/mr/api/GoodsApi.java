package com.mr.api;

import com.mr.repository.*;
import com.mr.utils.PageResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("goods")
public interface GoodsApi {

    @GetMapping("page")
    public PageResult<SpuBo> querySpu(
            @RequestParam(value = "page",defaultValue = "1") Integer page,
            @RequestParam(value = "rows",defaultValue = "5") Integer rows,
            @RequestParam(value = "saleable",required = false) String saleable,
            @RequestParam(value = "key",required = false) String key);

    @PostMapping
    public Void saveGoods(@RequestBody SpuBo spuBo);

    @PutMapping
    public Void updateGoods(@RequestBody SpuBo spuBo);


    @GetMapping("spu/detail/{id}")
    public SpuDetail querySpuDetailById(@PathVariable("id") Long id);

    @GetMapping("list/{spuId}")
    public List<Sku> list(@PathVariable("spuId") Long spuId);

    @GetMapping("querySpuById/{spuId}")
    public Spu querySpuById(@PathVariable("spuId") Long spuId);

    @GetMapping("sku/{skuId}")
    public Sku querySkuBySkuId(@PathVariable("skuId") Long skuId);

    @GetMapping("stock/{skuId}")
    public Stock queryStockBySkuId(@PathVariable("skuId") Long skuId);

}
