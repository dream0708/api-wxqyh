package com.jee.api.wxqyh.service;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.jee.api.wxqyh.bean.QyhUser;
import com.jee.api.wxqyh.util.ConstantUtil;
import com.jee.rest.base.session.template.SessionTemplate;
import com.jee.rest.base.util.RandomUtil;

@Service
public class SessionService {
	
	@Resource
	private SessionTemplate sessionTemplate ;
	
	
	
	public String createSession(QyhUser user){
		String userId = user.getUserid() ;
		
		String sid = sessionTemplate.get(ConstantUtil.USERID_PREFIX + userId) ;
		if(StringUtils.isNotBlank(sid)){
			sessionTemplate.delete(ConstantUtil.SESSION_PREFIX + sid );
		}
		
		//创建会话
		String sessionId = "" ;
		do{
			sessionId = RandomUtil.jstSessionid(ConstantUtil.Secret , userId) ;
		}while(sessionTemplate.exists(ConstantUtil.SESSION_PREFIX + sessionId)) ;
		
		user.setSessionid(sessionId);
		String hash = RandomUtil.random(4) ;
		user.setHash(hash);
		
		sessionTemplate.set(ConstantUtil.USERID_PREFIX + userId, sessionId , ConstantUtil.EXPIRE_SECONDS + 5);
		sessionTemplate.setAttribute(ConstantUtil.SESSION_PREFIX + sessionId,  "user" , user , SerializerFeature.WriteClassName);
		sessionTemplate.setAttribute(ConstantUtil.SESSION_PREFIX + sessionId, "hash" , hash );
		sessionTemplate.expire(ConstantUtil.SESSION_PREFIX + sessionId , ConstantUtil.EXPIRE_SECONDS);
		
		return sessionId ;
		
	}
	

	public String updateHash(String sessionId){
		String hash = RandomUtil.random(4) ;
	    sessionTemplate.setAttribute(ConstantUtil.SESSION_PREFIX + sessionId, "hash"  , hash) ;
	    return hash ;
	}
	
	public String getHash(String sessionId){
		return sessionTemplate.getAttribute(ConstantUtil.SESSION_PREFIX + sessionId, "hash" ) ;
	}
	
	public QyhUser getUserDetail(String sessionId){
		return sessionTemplate.getAttribute(ConstantUtil.SESSION_PREFIX + sessionId, "user" , QyhUser.class) ;
	}
	
	public void expire(QyhUser user){
		sessionTemplate.expire(ConstantUtil.USERID_PREFIX + user.getUserid() , ConstantUtil.EXPIRE_SECONDS + 5);
		sessionTemplate.expire(ConstantUtil.SESSION_PREFIX + user.getSessionid() , ConstantUtil.EXPIRE_SECONDS );
	}


	public SessionTemplate getSessionTemplate() {
		return sessionTemplate;
	}


	public void setSessionTemplate(SessionTemplate sessionTemplate) {
		this.sessionTemplate = sessionTemplate;
	}
	
	
	
	
	
	
	

}
