package test.com.jee.wxqyh.service;

import java.io.File;
import java.io.IOException;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.alibaba.fastjson.JSONObject;
import com.jee.api.wxqyh.bean.FileMsg;
import com.jee.api.wxqyh.service.WxqyhService;
import com.jee.api.wxqyh.util.ConstantUtil;

@RunWith(SpringJUnit4ClassRunner.class)  
@ContextConfiguration(locations = { "classpath:spring-content.xml" })
public class WxqyhServiceTest {

	@Resource
	private WxqyhService wxqyhService ;
	
	
	@Test
	public void testGetAccessToken() throws IOException{
		String accessToken = wxqyhService.getAccessToken(ConstantUtil.CorpID, ConstantUtil.Secret) ;
		
		//13:12:36.704
		//13:58:36.704
		System.out.println(accessToken);
		//String accessToken= "_2dpbclRgKGgyW2J0IsnmoCb9Z0a5-fhdt3iByclrU9GmB9R3iHWsj7428ZfPt2P" ;
		//wxqyhService.getUserInfo(accessToken, "lemker") ;
		
		/*JSONObject js = wxqyhService.getUserInfo(accessToken, "test22") ;
		System.out.println(js);
		js = wxqyhService.getDetailUserInfo(accessToken, "lemker") ;
		System.out.println(js);
		TextMsg	 msg = new TextMsg();
	    msg.setAgentid(8);
	    msg.setContent("欢迎光临微信企业号20170227");
		js = wxqyhService.sendMsg(accessToken, msg) ;*/
		File file = new File("G:\\web\\e3-h5-master.zip");
		//InputStream stream = new FileInputStream(file) ;
		String mid = wxqyhService.uploadTempFile(accessToken, "image", file) ;
		//String mid = "1MfRTmyICn6tWUt_vGiBSUDHGuapvOdkKitx9RqIkejWGJSWBSMHCNKixEDjjtUzyIapdRJHWd-EKAE3U3zHkRg" ;
		FileMsg	 msg = new FileMsg();
		msg.setMediaId(mid);
		msg.setAgentid(8);
		msg.setTouser("lemker");
		System.out.println(msg.toString()) ;
		JSONObject js = wxqyhService.sendMsg(accessToken, msg) ;
		System.out.println(js);
	}
	
}
