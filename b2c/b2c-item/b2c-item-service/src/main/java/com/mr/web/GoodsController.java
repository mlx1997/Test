package com.mr.web;

import com.mr.enums.ExceptionEnums;
import com.mr.exception.MrException;
import com.mr.repository.*;
import com.mr.service.GoodsService;
import com.mr.utils.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("goods")
public class GoodsController {
    @Autowired
    private GoodsService goodsService;

    @GetMapping("page")
    public ResponseEntity<PageResult<SpuBo>> querySpu(
            @RequestParam(value = "page",defaultValue = "1") Integer page,
            @RequestParam(value = "rows",defaultValue = "5") Integer rows,
            @RequestParam(value = "saleable",required = false) String saleable,
            @RequestParam(value = "key",required = false) String key){

        PageResult<SpuBo> spuBoPageResult = goodsService.querySpu(page, rows, saleable, key);

        if(spuBoPageResult == null){
            throw new MrException(ExceptionEnums.CHABUCHULAI_IS_NULL);
        }

        return ResponseEntity.ok(spuBoPageResult);
    }

    @PostMapping
    public ResponseEntity<Void> saveGoods(@RequestBody SpuBo spuBo){
        goodsService.saveGoods(spuBo);
        return ResponseEntity.ok(null);
    }

    @PutMapping
    public ResponseEntity<Void> updateGoods(@RequestBody SpuBo spuBo){
        goodsService.updateGoods(spuBo);
        return ResponseEntity.ok(null);
    }

    @GetMapping("spu/detail/{id}")
    public ResponseEntity<SpuDetail> querySpuDetailById(@PathVariable("id") Long id){
        return ResponseEntity.ok(goodsService.querySpuDetailById(id));
    }

    @GetMapping("list/{spuId}")
    public ResponseEntity<List<Sku>> list(@PathVariable("spuId") Long spuId){
        return ResponseEntity.ok(goodsService.querySkuBySpuId(spuId));
    }

    @PutMapping("updateSaletable")
    public ResponseEntity<Void> updateSaletable(@RequestBody Spu spu){
        goodsService.updateSaletable(spu);
        return ResponseEntity.ok(null);
    }

    @GetMapping("querySpuById/{spuId}")
    public ResponseEntity<Spu> querySpuById(@PathVariable("spuId") Long spuId){
        return ResponseEntity.ok(goodsService.querySpuById(spuId));
    }

    @GetMapping("sku/{skuId}")
    public ResponseEntity<Sku> querySkuBySkuId(@PathVariable("skuId") Long skuId){
        return ResponseEntity.ok(goodsService.querySkuBySkuId(skuId));
    }

    @GetMapping("stock/{skuId}")
    public ResponseEntity<Stock> queryStockBySkuId(@PathVariable("skuId") Long skuId){
        return ResponseEntity.ok(goodsService.queryStockBySkuId(skuId));
    }
}
