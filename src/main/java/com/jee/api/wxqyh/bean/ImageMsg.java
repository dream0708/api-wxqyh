package com.jee.api.wxqyh.bean;

import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
import com.jee.api.wxqyh.util.ConstantUtil;

public class ImageMsg extends MsgTemplate{
	
	private Map<String , String> image = new HashMap<String, String>() ; 
	
	
	
	public ImageMsg(){
		super.setMsgtype(ConstantUtil.MSG_TYPE_IMAGE);
	}
	
	public void setMediaId(String mediaId){
		image.put("media_id", mediaId) ;
	}
	
	@JSONField(serialize = false)
	public String getMeidaId(){
		return image.get("media_id") ;
	}

	public Map<String, String> getImage() {
		return image;
	}

	public void setImage(Map<String, String> image) {
		this.image = image;
	}
	
	public String toString(){
		return JSON.toJSONString(this) ;
	}

}
