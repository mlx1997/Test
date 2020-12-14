package com.mr.service;

import com.github.tobato.fastdfs.domain.StorePath;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Service
public class UploadService {

    @Autowired
    FastFileStorageClient storageClient;

    public String upload(MultipartFile file) throws IOException {
        List<String> suffixes = Arrays.asList("image/png", "image/jpg", "image/jpeg");
        // 1.图片信息调用 jpg png jpeg
//        String type = file.getContentType();
//        if(suffixes.contains(type)){
//            logger.info();
//        }
        // 2.校验图片内容 是否真的是 图片

        // 3.保存图片  本机上传
        // 创建图片路径
//        File dir = new File("D://mr//upload//img");
//        // 没有路径 创建路径
//        if(!dir.exists()){
//            dir.mkdirs();
//        }
//        // 保存图片
//        file.transferTo(new File(dir, file.getOriginalFilename()));
        // 拼接图片地址,返回
//        String url="http://image.b2c.com/"+file.getOriginalFilename();


        //上传图片 FastDFS
        String extension = StringUtils.substringAfterLast(file.getOriginalFilename(), ".");
        // 2.2、上传
        StorePath storePath = this.storageClient.uploadFile(
                file.getInputStream(), file.getSize(), extension, null);
        // 2.3、返回完整路径
        return "http://image.b2c.com/" + storePath.getFullPath();
//        String s = Objects.requireNonNull(file.getOriginalFilename()).substring(file.getOriginalFilename().lastIndexOf(".")+1);
//
//        StorePath storePath = storageClient.uploadImageAndCrtThumbImage(file.getInputStream(), file.getSize(), s, null);
//        System.out.println(storePath.getFullPath());
//        System.out.println(storePath.getGroup());
//        System.out.println(storePath.getPath());
//        String url="http://image.b2c.com/"+storePath.getFullPath();
//        return url;
    }

}
