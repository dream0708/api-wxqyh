package com.jee.api.wxqyh.handler;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.ThreadContext;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import com.alibaba.fastjson.JSONObject;
import com.jee.api.wxqyh.annoation.QyhSession;
import com.jee.api.wxqyh.bean.QyhUser;
import com.jee.api.wxqyh.service.SessionService;
import com.jee.api.wxqyh.util.ConstantUtil;
import com.jee.rest.base.exception.BusinessException;
import com.jee.rest.base.response.code.ResponseCode;

public class QyhSessionAccess  implements HandlerMethodArgumentResolver{
	
	@Resource
	private SessionService sessionService ;

	@Override
	public boolean supportsParameter(MethodParameter parameter) {
		 if (parameter.getParameterType().isAssignableFrom(QyhUser.class) &&
	                parameter.hasParameterAnnotation(QyhSession.class)) {
	            return true;
	     }
	     return false;
	}

	@Override
	public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
			NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
		String sessionId = webRequest.getParameter(ConstantUtil.SESSION_NAME) ;
		if(StringUtils.isBlank(sessionId)){
			throw new BusinessException(ResponseCode.UNAUTHORIZED_401 , "sessionid必须传入") ;
		}
		QyhUser user = sessionService.getUserDetail(sessionId) ;
		if(null == user || StringUtils.isBlank(user.getUserid())){
			throw new BusinessException(ResponseCode.NO_LOGIN_600 , "登录超时") ;
		}
		ThreadContext.put("userid", user.getUserid());
		user.setSessionid(sessionId);
		user.setPrefixSession(ConstantUtil.SESSION_PREFIX);
		user.setSession(sessionService.getSessionTemplate());
		
		QyhSession session = parameter.getParameterAnnotation(QyhSession.class) ;
		if(session != null && session.hash()){
			String requestHash = webRequest.getParameter("hash") ;
			if(StringUtils.isBlank(requestHash)){
				throw new BusinessException(ResponseCode.HASH_UNAUTHORIZED_632 , "hash需要传入") ;
			}
			String sessionHash = sessionService.getHash(sessionId) ;
			String newHash = sessionService.updateHash(sessionId) ;
			webRequest.setAttribute("hash", newHash, NativeWebRequest.SCOPE_REQUEST);
			if(!sessionHash.equals(requestHash)){
				throw new BusinessException(ResponseCode.HASH_UNAUTHORIZED_632 , "请勿重复提交") ;
			}
		}
		sessionService.expire(user);
		
		return user ;
	}

}
