package com.jee.api.wxqyh.controller;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.jee.api.wxqyh.annoation.QyhSession;
import com.jee.api.wxqyh.bean.QyhUser;
import com.jee.api.wxqyh.bean.TextMsg;
import com.jee.api.wxqyh.exception.AesException;
import com.jee.api.wxqyh.service.SessionService;
import com.jee.api.wxqyh.service.WxqyhService;
import com.jee.api.wxqyh.util.ConstantUtil;
import com.jee.api.wxqyh.util.WXBizMsgCrypt;
import com.jee.api.wxqyh.util.XmlUtil;
import com.jee.rest.base.exception.BusinessException;
import com.jee.rest.base.response.biz.BizResult;
import com.jee.rest.base.response.code.ResponseCode;

@RestController
@RequestMapping("/qyh")
public class WxqyhController {
	
	private static final Logger logger = LogManager.getLogger(WxqyhController.class);
	
	private static WXBizMsgCrypt crypt = null ;
	
	@Resource
	private WxqyhService wxqyhService ;
	
	@Resource
	private SessionService sessionService ;
	
	@Resource
	private RedisTemplate<String , String> redisTemplate ;
	
	//https://open.weixin.qq.com/connect/oauth2/authorize?appid=CORPID&redirect_uri=http://58.61.28.200/api-wxqyh/qyh/login&response_type=code&scope=SCOPE&state=download_app#wechat_redirect
	static{
		try {
			crypt = new WXBizMsgCrypt(ConstantUtil.Token , ConstantUtil.EncodingAESKey , ConstantUtil.CorpID ) ;
		} catch (AesException e) {
			logger.error(e.getMessage() , e);
		}
	}
			 
	
	
	
	/**
	 * 1.解析出Get请求的参数，包括消息体签名(msg_signature)，时间戳(timestamp)，随机数字串(nonce 
     *   )以及公众平台推送过来的随机加密字符串(echostr), 这一步注意作URL解码。
     * 2.验证消息体签名的正确性 
     * 3.解密出echostr原文，将原文当作Get请求的response，
     *    返回给公众平台 第2，3步可以用公众平台提供的库函数VerifyURL来实现。 
     *
	 * @param msgSignature
	 * @param timestamp
	 * @param nonce
	 * @param echoStr
	 * @param request
	 * @return
	 * @throws AesException
	 */
	@RequestMapping(value = "/door" , method = RequestMethod.GET)
	public String weixinAccessGet(
			@RequestParam(name = "msg_signature") String msgSignature ,
			@RequestParam(name = "timestamp") String timestamp ,
			@RequestParam(name = "nonce" ) String nonce ,
			@RequestParam(name = "echostr") String echoStr ,
			HttpServletRequest request
			) throws AesException{
		
	  
	    return crypt.VerifyURL(msgSignature, timestamp, nonce, echoStr) ;
		
		//return echoStr ;
	}
	
	
	/**
	 * * 企业收到post请求之后应该 
     * 1.解析出url上的参数，包括消息体签名(msg_signature)，时间戳(timestamp)以及随机数字串(nonce) 
     * 2.验证消息体签名的正确性。 
     * 3.将post请求的数据进行xml解析，并将<Encrypt>标签的内容进行解密，解密出来的明文即是用户回复消息的明文，明文格式请参考官方文档 
     * 第2，3步可以用公众平台提供的库函数DecryptMsg来实现。 
	 * @param msgSignature
	 * @param timestamp
	 * @param nonce
	 * @param echoStr
	 * @param request
	 * @return
	 * @throws AesException
	 */
	@RequestMapping(value = "/door" , method = RequestMethod.POST)
	public String weixinAccessPost(
			@RequestParam(name = "msg_signature") String msgSignature ,
			@RequestParam(name = "timestamp") String timestamp ,
			@RequestParam(name = "nonce" ) String nonce ,
			HttpServletRequest request ,
			@RequestBody() String sReqData 
			) throws AesException{
		
	    
	    String xml = crypt.DecryptMsg(msgSignature, timestamp, nonce, sReqData) ;
	    logger.info(xml);
		
	    try {
			Map<String , String> map =  XmlUtil.parseXml(xml) ;
			
			//redisTemplate.convertAndSend("QYH", JSON.toJSONString(map));
			
			String msgType = map.get("MsgType") ;
			String userId = map.get("FromUserName") ;
			String eventType = map.get("Event") ;
			String eventKey = map.get("EventKey") ;
			String agentId = map.get("AgentID") ;
			
			if("event".equals(msgType)){
				if("click".equals(eventType)){
					//按钮key
					if("Sports".equals(eventKey)){
						
					    String accessToken = wxqyhService.getAccessToken(ConstantUtil.CorpID, ConstantUtil.Secret) ;
						if(StringUtils.isBlank(accessToken)){
							throw new BusinessException(ResponseCode.FAILUER_1 , "获取accessToke失败") ;
						}
						TextMsg msg = new TextMsg() ;
						msg.setAgentid(Integer.valueOf(agentId));
						msg.setTouser(userId);
						msg.setContent("欢迎光临兄弟情深企业号http://www.jee.com.cn 点击<a href=\"http://www.jee.com.cn\">点击</a>");
						wxqyhService.sendMsg(accessToken, msg) ;
						
						
						JSONObject js = wxqyhService.getDetailUserInfo(accessToken, userId) ;
						
						if(StringUtils.isNotBlank(js.getString("errcode"))){
							String avatarUrl = js.getString("avatar") ;
							if(StringUtils.isNotBlank(avatarUrl)){
								logger.info(avatarUrl);
								msg.setContent(avatarUrl);
								
								wxqyhService.sendMsg(accessToken, msg) ;
								
							}
						}
						
					}
					
					
				}
			}
			
			
		} catch (Exception e) {
			
		}
	    
	    
		return "success" ;
	}
	
	
	@RequestMapping("/login")
	public BizResult<JSONObject> loginAuth(String code , String state){
		
		String accessToken = wxqyhService.getAccessToken(ConstantUtil.CorpID, ConstantUtil.Secret) ;
		if(StringUtils.isBlank(accessToken)){
			throw new BusinessException(ResponseCode.UNAUTHORIZED_401 , "获取access_token失败") ;
		}
		JSONObject js = wxqyhService.getUserInfo(accessToken, code) ;
		logger.info("return user info:{}" , js.toJSONString()) ;
		if(js != null && StringUtils.isNotBlank(js.getString("UserId"))){
			String userId = js.getString("UserId") ;
			JSONObject ds = wxqyhService.getDetailUserInfo(accessToken, userId) ;
			logger.info("detail user info:{}" , ds);
			if(ds != null && ds.getIntValue("errcode") == 0){
				QyhUser user = new QyhUser() ; //拼装QyhUser对象 存到redis中
				
				
				
				sessionService.createSession(user) ;
				return new BizResult<JSONObject>()
						   .success("登录成功")
						   .data(ds)
						   .sessionid(user.getSessionid())
						   .hash(user.getHash()) ;
			}else{
				return new BizResult<JSONObject>().failure("登录失败") ;
			}
			
 		}
		
		throw new BusinessException(ResponseCode.FAILUER_10 , "获取不到userId") ;
		
	}
	
	
	@RequestMapping("/detail")
	public BizResult<QyhUser> getDetailUser(@QyhSession(detail = true) QyhUser user ){
		return new BizResult<QyhUser>().success("查询成功").data(user) ;
	}
	
	
	
	
	@RequestMapping("/detail")
	public BizResult<QyhUser> updateUserInfo(@QyhSession(hash = true) QyhUser user ){
		String nickName = user.getAttribute("nickName") ;  //session操作  类似HttpSession
		int money = user.getInt("money" , 0) ;
		money += 15 ;
		user.setAttribute("money", money);
		List<String> days = user.getList("days", String.class) ;
				
		
		
		
		return new BizResult<QyhUser>().success().data(user) ;
	}
	

}
