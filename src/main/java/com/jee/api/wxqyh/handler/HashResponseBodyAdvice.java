package com.jee.api.wxqyh.handler;


import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import com.alibaba.fastjson.JSON;
import com.jee.rest.base.response.biz.BizResult;
import com.jee.rest.base.util.Constants;

@ControllerAdvice
public class HashResponseBodyAdvice implements ResponseBodyAdvice<BizResult<?>> {

	private static final Logger logger = LogManager.getLogger("response") ;
	
	public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
		return returnType.getParameterType().isAssignableFrom(BizResult.class) ;
		
	}
     
	public BizResult<?> beforeBodyWrite(BizResult<?> result, MethodParameter returnType, MediaType selectedContentType,
			Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request,
			ServerHttpResponse response) {
		HttpServletRequest httpRequest =  ((ServletServerHttpRequest) request).getServletRequest() ;
		String hash = (String) httpRequest.getAttribute(Constants.REQUEST_HASH) ;
		if(result != null && StringUtils.isNotBlank(hash)){ //统一hash处理
			result.hash(hash) ;
		}
		logger.info("URL:{}, RESPONSE:{}", request.getURI() , JSON.toJSONString(result));
		return result ;
	}

}
