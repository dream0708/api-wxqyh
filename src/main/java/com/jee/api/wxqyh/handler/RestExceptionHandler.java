package com.jee.api.wxqyh.handler;



import java.util.concurrent.CompletionException;
import java.util.concurrent.ExecutionException;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.RedisConnectionFailureException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.NoHandlerFoundException;

import com.alibaba.fastjson.JSONException;
import com.jee.api.wxqyh.exception.AesException;
import com.jee.rest.base.exception.BusinessException;
import com.jee.rest.base.response.biz.BizResult;
import com.jee.rest.base.response.code.ResponseCode;

import redis.clients.jedis.exceptions.JedisException;
/**
 * 全局错误异常处理 只要是Servlet容器线程中出现异常 都会在这里得到直接返回
 * 异步业务线程出现的异常 要程序封装显式返回
 * @author yaomengke
 *
 */

@ControllerAdvice
public class RestExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger(RestExceptionHandler.class);

    
    public BizResult<Object> handleException(Throwable ex , HttpServletRequest request){
    	if(ex instanceof BusinessException){
    		 BusinessException be = (BusinessException)ex ;
    		 logger.error("BusinessException , Url : {} , ErrorCode : {} , Message : {} " , request.getRequestURL() , be.getErrorCode().getCode() , be.getErrorCode().getMsg() );
    		 return new BizResult<Object>().failure(be).data(be.getReserved()) ;
    	}else if(ex instanceof AesException){
    		 AesException aes = (AesException) ex ;
    		 logger.error("AesException , Url : {} , ErrorCode : {} , Message : {} " , request.getRequestURL() , Math.abs(aes.getCode() ), aes.getMessage() );
    		 return new BizResult<Object>().failure(Math.abs(aes.getCode() ), aes.getMessage() ) ;
    	}else if(ex instanceof JSONException){
   		     logger.error("JSONException , Url : {} , ErrorCode : {} , Message : {} " , request.getRequestURL() , ResponseCode.JSON_FORMAT_ERROR_671.getCode() , "数据格式转换异常" );
   		     return new BizResult<Object>().failure(ResponseCode.JSON_FORMAT_ERROR_671 , "数据格式转换异常") ;
    	}else if(ex instanceof RedisConnectionFailureException){
    		 logger.error(ex.getMessage() ,  ex );
    		 logger.error("RedisConnectionFailureException , Url : {} , ErrorCode : {} , Message : {} " , request.getRequestURL() , ResponseCode.REQUEST_TIMEOUT_408.getCode() , "会话服务器连接失败" );
  		     return new BizResult<Object>().failure(ResponseCode.REQUEST_TIMEOUT_408 , "会话服务器连接失败") ;
    	}else if(ex instanceof JedisException){
    		 logger.error(ex.getMessage() ,  ex );
    		 logger.error("JedisException , Url : {} , ErrorCode : {} , Message : {} " , request.getRequestURL() , ResponseCode.REQUEST_TIMEOUT_408.getCode() , "会话服务器开小差" );
  		     return new BizResult<Object>().failure(ResponseCode.REQUEST_TIMEOUT_408 , "会话服务器开小差") ;
    	}else if(ex instanceof HttpMessageNotReadableException){
    		 logger.error("HttpMessageNotReadableException , Url : {} , ErrorCode : {} , Message : {} " , request.getRequestURL() , ResponseCode.REQUEST_TIMEOUT_408.getCode() , "请求入参格式有误,请按照接口文档组织参数" );
	         return new BizResult<Object>().failure(ResponseCode.JSON_FORMAT_ERROR_671.getCode() , "请求入参格式有误,请按照接口文档组织参数") ;
	    }else if(ex instanceof HttpRequestMethodNotSupportedException){
	    	 logger.error("HttpRequestMethodNotSupportedException , Url : {} , ErrorCode : {} , Message : {} " , request.getRequestURL() , ResponseCode.SERVER_REJECT_512.getCode() , "请求方式[GET,POST,PUT,DELETE]非法,请参考接口文档" );
	         return new BizResult<Object>().failure(ResponseCode.SERVER_REJECT_512.getCode() , "请求方式[GET,POST,PUT,DELETE]非法,请参考接口文档") ;
	    }else{
	    	 logger.error("{} , Url : {} , ErrorCode : {} , Message : {} " ,ex.getClass().getSimpleName() ,  request.getRequestURL() , ResponseCode.SYSTEM_ERROR_504.getCode() , ex.getMessage() );
	    	 logger.error(ex.getMessage() , ex);
	    	 return new BizResult<Object>().failure(ResponseCode.SYSTEM_ERROR_504 , "服务器异常") ;
	    }
    	
    }
    
    @ExceptionHandler(NoHandlerFoundException.class)
    @ResponseBody
    public BizResult<String> requestHandlingNoHandlerFound(HttpServletRequest request, NoHandlerFoundException ex) {
    	 logger.error("NoHandlerFoundException , Url : {} , ErrorCode : {} , Message : {} " , request.getRequestURL() , ResponseCode.NOT_FOUND_404.getCode() , ResponseCode.NOT_FOUND_404.getMsg() );
         return new BizResult<String>().failure(ResponseCode.NOT_FOUND_404) ;
    }
    
   //  @ExceptionHandler(ClientAbortException.class)
    public  void handleClientAbortException(HttpServletRequest request, Exception ex){
    	logger.error("管道破裂 异常 (Broken Pipe)  org.apache.catalina.connector.ClientAbortException: java.io.IOException ");
        logger.error("ClientAbortException:{}, URL:{}, Message:{}", ex.getClass().getSimpleName(),  request.getRequestURI(), ex.getMessage());
    }
    
    @ExceptionHandler({ExecutionException.class})
    @ResponseBody
    public BizResult<Object> hanlderExecutionException(ExecutionException ex , HttpServletRequest request ) {
    	logger.error(ex.getMessage() , ex);
    	return handleException(ex.getCause() , request) ;
    }
    
    @ExceptionHandler({CompletionException.class})
    @ResponseBody
    public BizResult<Object> hanlderCompletionException(CompletionException ex , HttpServletRequest request ) {
    	logger.error(ex.getMessage() , ex);
    	return handleException(ex.getCause() , request) ;
    }
    
    @ExceptionHandler({Throwable.class , RuntimeException.class , Exception.class })
    @ResponseBody
    public  BizResult<Object> handleException(HttpServletRequest request, Exception ex){
    	return handleException(ex , request) ;
    }
    
    
    
    
}