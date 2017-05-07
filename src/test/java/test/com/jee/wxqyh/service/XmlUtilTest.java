package test.com.jee.wxqyh.service;

import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.jee.api.wxqyh.util.XmlUtil;

public class XmlUtilTest {
	
	
	public static void main(String args[]) throws Exception{
		String xml = 
		"<xml><ToUserName><![CDATA[wxc151742b376e507b]]></ToUserName>" +
		"<FromUserName><![CDATA[lemker]]></FromUserName>" +
		"<CreateTime>1487753775</CreateTime>" +
		"<MsgType><![CDATA[event]]></MsgType>" +
		"<AgentID>8</AgentID>" +
		"<Event><![CDATA[click]]></Event>" +
		"<EventKey><![CDATA[Sports]]></EventKey>" +
		"</xml>" ;
		
		Map<String , String> map = XmlUtil.parseXml(xml) ;
		
		System.out.println(JSON.toJSON(map));
		
	}

}
