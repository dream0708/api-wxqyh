package com.jee.api.wxqyh.bean;

import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.jee.api.wxqyh.util.ConstantUtil;

public class FileMsg extends MsgTemplate{
	
    private Map<String , String> file = new HashMap<String, String>() ; 
	
	
	
	public FileMsg(){
		super.setMsgtype(ConstantUtil.MSG_TYPE_FILE);
	}	
	
	public void setMediaId(String medidaId){
		file.put("media_id", medidaId) ;
	}
	/*public String getMediaId(){
		return file.get("media_id") ;
	}*/
	

	public Map<String, String> getFile() {
		return file;
	}

	public void setFile(Map<String, String> file) {
		this.file = file;
	}
	public String toString(){
		return JSON.toJSONString(this) ;
	}
	

}
