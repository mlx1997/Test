package com.mr.exception;

import com.mr.enums.ExceptionEnums;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class MrException extends RuntimeException{
    private ExceptionEnums exceptionEnums;
}
