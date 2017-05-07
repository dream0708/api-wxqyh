package com.jee.api.wxqyh.bean;

import com.alibaba.fastjson.JSON;

public class MsgTemplate {
	
	private String touser = "@all" ;
	private String toparty = "@all" ;
	private String totage = "@all" ;
	
	private String msgtype ;
	
	private int agentid ;
	
	private int safe = 0 ;

	public String getTouser() {
		return touser;
	}

	public void setTouser(String touser) {
		this.touser = touser;
	}

	public String getToparty() {
		return toparty;
	}

	public void setToparty(String toparty) {
		this.toparty = toparty;
	}

	public String getTotage() {
		return totage;
	}

	public void setTotage(String totage) {
		this.totage = totage;
	}

	public String getMsgtype() {
		return msgtype;
	}

	public void setMsgtype(String msgtype) {
		this.msgtype = msgtype;
	}

	public int getAgentid() {
		return agentid;
	}

	public void setAgentid(int agentid) {
		this.agentid = agentid;
	}

	public int getSafe() {
		return safe;
	}

	public void setSafe(int safe) {
		this.safe = safe;
	}
	
	public String toString(){
		return JSON.toJSONString(this) ;
	}
	
	

}
