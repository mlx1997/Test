package com.mr.mapper;

import com.mr.exception.MrException;
import com.mr.vo.ExceptionResult;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice  //标明为控制层通知类
public class CommonExceptionHandler {
    //捕捉异常
    @ExceptionHandler(MrException.class)
    public ResponseEntity<ExceptionResult> handleException(MrException e){
        System.out.println("有异常了了了");
        //e.getExceptionEnums().getMessage()
        return ResponseEntity.status(e.getExceptionEnums().getCode()).body(new ExceptionResult(e.getExceptionEnums()));
    }
}
