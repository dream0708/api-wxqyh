package com.jee.api.wxqyh.bean;

import java.util.Map;

import com.jee.rest.base.model.user.BaseUser;

public class QyhUser extends BaseUser implements java.io.Serializable{
	
	private String name ;
	private Integer[] department ;
	private String position ;
	private String mobile ;
	private String gender ;
	private String email ;
	private String weixinid ;
	private String avatar_mediaid ;
	private Map<String, Object> extattr ;
	
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Integer[] getDepartment() {
		return department;
	}
	public void setDepartment(Integer[] department) {
		this.department = department;
	}
	public String getPosition() {
		return position;
	}
	public void setPosition(String position) {
		this.position = position;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getWeixinid() {
		return weixinid;
	}
	public void setWeixinid(String weixinid) {
		this.weixinid = weixinid;
	}
	public String getAvatar_mediaid() {
		return avatar_mediaid;
	}
	public void setAvatar_mediaid(String avatar_mediaid) {
		this.avatar_mediaid = avatar_mediaid;
	}
	public Map<String, Object> getExtattr() {
		return extattr;
	}
	public void setExtattr(Map<String, Object> extattr) {
		this.extattr = extattr;
	}
	
	
	

}
