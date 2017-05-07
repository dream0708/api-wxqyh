package com.jee.api.wxqyh.bean;

import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.jee.api.wxqyh.util.ConstantUtil;

public class VideoMsg extends MsgTemplate {
	
	private Map<String , String> video = new HashMap<String, String>() ; 
	
	
	public VideoMsg(){
		super.setMsgtype(ConstantUtil.MSG_TYPE_VIDEO);
	}
	
	public void setMediaId(String mediaId){
		video.put("media_id", mediaId) ;
	}
	public void setTitle(String title){
		video.put("title", title) ;
	}
	public void setDescription(String description){
		video.put("description", description) ;
	}

	public Map<String, String> getVideo() {
		return video;
	}

	public void setVideo(Map<String, String> video) {
		this.video = video;
	}
	

	public String toString(){
		return JSON.toJSONString(this) ;
	}
	
}
