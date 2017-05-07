package com.jee.api.wxqyh.bean;

import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
import com.jee.api.wxqyh.util.ConstantUtil;

public class TextMsg extends MsgTemplate{
	
	private Map<String , String> text = new HashMap<String, String>() ;
	

	
	public TextMsg(){
		super.setMsgtype(ConstantUtil.MSG_TYPE_TEXT);
	}

	public Map<String, String> getText() {
		return text;
	}

	public void setText(Map<String, String> text) {
		this.text = text;
	}
	
	public void setContent(String content){
		text.put("content", content) ;
	}
	
	@JSONField(serialize = false)
	public String getContent(){
		return text.get("content") ;
	}
	
	
	public String toString(){
		return JSON.toJSONString(this) ;
	}
	
	
	public static void main(String args[]){
		TextMsg msg = new TextMsg() ;
		msg.setAgentid(1);
		msg.setContent("谎言滴啊分");
		System.out.println(msg);
	}
	
	

}
