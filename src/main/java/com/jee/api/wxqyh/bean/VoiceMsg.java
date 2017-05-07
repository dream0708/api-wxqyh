package com.jee.api.wxqyh.bean;

import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
import com.jee.api.wxqyh.util.ConstantUtil;

public class VoiceMsg extends MsgTemplate {
	private Map<String , String> voice = new HashMap<String, String>() ;
	
	public VoiceMsg(){
		super.setMsgtype(ConstantUtil.MSG_TYPE_VOICE);
	}

	public Map<String, String> getVoice() {
		return voice;
	}

	public void setVoice(Map<String, String> voice) {
		this.voice = voice;
	}
	
	
	public void setMediaId(String mediaId){
		voice.put("media_id", mediaId) ;
	}
	
	@JSONField(serialize = false)
	public String getMediaId(){
		return voice.get("media_id") ;
	}
	
	public String toString(){
		return JSON.toJSONString(this) ;
	}

}
