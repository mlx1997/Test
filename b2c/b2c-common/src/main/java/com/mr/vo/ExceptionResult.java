package com.mr.vo;

import com.mr.enums.ExceptionEnums;
import lombok.Data;

import java.util.Date;

@Data
public class ExceptionResult {
    private Date timestamp;
    private int status;
    private String message;

    public ExceptionResult(ExceptionEnums en) {
        this.timestamp = new Date();
        this.status = en.getCode();
        this.message = en.getMessage();
        //this.timestamp = System.currentTimeMillis();
    }

    //    private String error;
//    private String path;

}
