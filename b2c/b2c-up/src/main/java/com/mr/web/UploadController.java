package com.mr.web;

import com.mr.enums.ExceptionEnums;
import com.mr.exception.MrException;
import com.mr.service.UploadService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("upload")
public class UploadController {
    @Autowired
    private UploadService uploadService;

    @PostMapping("image")
    public ResponseEntity<String> upload(@RequestParam("file") MultipartFile file) throws IOException {
        String upload = uploadService.upload(file);
        System.out.println("9999999");
        if(StringUtils.isBlank(upload)){
            throw new MrException(ExceptionEnums.IMAGE_CANNOT_IS_NULL);
        }
        return ResponseEntity.ok(upload);
    }
}
