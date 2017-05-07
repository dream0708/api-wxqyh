package com.jee.api.wxqyh.util;

public class ConstantUtil {
	
	
	public static final String Token = "pQoCnuY3l2EOAMecg14lRa" ;
	
	public static final String EncodingAESKey = "A6Tt6heXqnO8baqsaaDLKy2jQRf1zP4NHdUnf2QDrYB" ;
	
	public static final String CorpID = "wxc151742b376e507b" ;
	
	public static final String Secret  = "8I2PAyFtYOQvNqCG3CSOytkr_UhXmCsd7e3f-4loyXQ6lKpOyBLCMBnmw3TDuIj1" ;
	
	public static final String USERID_PREFIX = "userid:session:" ;
	public static final String SESSION_PREFIX = "qyh:session:" ;
	public static final String SESSION_NAME = "sessionid" ;
	public static final int EXPIRE_SECONDS = 3600 ;
	
	
	public static String MSG_TYPE_TEXT = "text" ;
	
	public static String MSG_TYPE_IMAGE = "image" ;
	
	public static String MSG_TYPE_VOICE = "voice" ;
	
	public static String MSG_TYPE_VIDEO = "video" ;
	
	public static String MSG_TYPE_FILE = "file" ;
	
	public static String MSG_TYPE_NEWS = "news" ;
	
	public static String MSG_TYPE_MPNEWS = "mpnews" ;
	
	
	/**
	 * 获取Access_token 
	 */
	public static final String FETCH_ACCESS_TOKEN = "https://qyapi.weixin.qq.com/cgi-bin/gettoken?corpid=%s&corpsecret=%s" ;
	
	/**
	 * 回调 根据Code获取userId
	 */
	public static final String USERID_GET_URl = "https://qyapi.weixin.qq.com/cgi-bin/user/getuserinfo?access_token=%s&code=%s" ;
	
	public static final String USERID_TO_OPENID = "https://qyapi.weixin.qq.com/cgi-bin/user/convert_to_openid?access_token=%s" ;
	
	/**
	 * 根据Userid 获取用户详细信息
	 */
	public static final String USER_GET_URL = "https://qyapi.weixin.qq.com/cgi-bin/user/get?access_token=%s&userid=%s" ;
	
	
	/**
	 * 创建用户
	 */
	public static final String USER_CREATE_URL = "https://qyapi.weixin.qq.com/cgi-bin/user/create?access_token=%s" ;
	
	
	public static final String USER_UPDATE_URL = "https://qyapi.weixin.qq.com/cgi-bin/user/update?access_token=%s" ;
	
	public static final String USER_DELETE_URL = "https://qyapi.weixin.qq.com/cgi-bin/user/delete?access_token=%s&userid=%s" ;
	
	public static final String USER_BATCH_DELETE_URL = "https://qyapi.weixin.qq.com/cgi-bin/user/batchdelete?access_token=%s" ;
	
	public static final String SIMPLE_USER_DEPT_LIST_URL = "https://qyapi.weixin.qq.com/cgi-bin/user/simplelist?access_token=%s&department_id=%s&fetch_child=%s&status=%s" ;
	
	public static final String USER_DEPT_LIST_URL = "https://qyapi.weixin.qq.com/cgi-bin/user/list?access_token=%s&department_id=%s&fetch_child=%s&status=%s" ;
	
	
	
	/**
	 * 发送企业号消息
	 */
	public final static String MSG_SEND_URL = "https://qyapi.weixin.qq.com/cgi-bin/message/send?access_token=%s";
	
	/**
	 * 上传临时素材
	 */
	public final static String TEMP_UPLOAD_MEDIA_URL = "https://qyapi.weixin.qq.com/cgi-bin/media/upload?access_token=%s&type=%s" ;
	
	/**
	 * 上传永久素材
	 */
	public final static String PER_UPLOAD_MEDIA_URL = "https://qyapi.weixin.qq.com/cgi-bin/material/add_material?access_token=%s&type=%s" ;   

}
