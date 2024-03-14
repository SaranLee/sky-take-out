package com.sky.handler;

import com.sky.constant.DBConstant;
import com.sky.exception.BaseException;
import com.sky.exception.DuplicatedUsernameException;
import com.sky.exception.DuplicatedUsernameException;
import com.sky.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.SQLIntegrityConstraintViolationException;

/**
 * 全局异常处理器，处理项目中抛出的业务异常
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    /**
     * 捕获业务异常
     * @param ex
     * @return
     */
    @ExceptionHandler
    public Result exceptionHandler(BaseException ex){
        log.error("异常信息：{}", ex.getMessage());
        return Result.error(ex.getMessage());
    }

    @ExceptionHandler
    public Result exceptionHandler(SQLIntegrityConstraintViolationException ex) {
        String msg = ex.getMessage();
        if (msg.contains("Duplicate entry") && msg.contains(DBConstant.DB_EMP_KEY_USERNAME)) {
            String rtn_msg = "用户名" + msg.split(" ")[2] + "已经存在，请修改用户名！";
            log.error(rtn_msg);
            return Result.error(rtn_msg);
        }
        return Result.error("Unknown Error!");
    }

}
