package test.com.jee.wxqyh.service;
import com.jee.api.wxqyh.exception.AesException;
import com.jee.api.wxqyh.util.ConstantUtil;
import com.jee.api.wxqyh.util.WXBizMsgCrypt;

public class CryptTest {
	
	public static void main(String args[]) throws AesException{
		String msg_signature = "8519fa42b38964b83cfb801031971f5b6d18b5cb" ;
		
		String nonce = "2137062149" ;
		
		String echostr = "GE0mGrkbqrMQanKJUtTBfZNT0KVVtNlitJ0ocnlbi+1rLowzkZKQTyaS84ukUJe1Qr7J2gaWTFJEZQpvXNmhFg==" ;
		String timestamp = "1487749764" ;
		
		
		String token = "oTInGk7" ;
		String encodingAESKey = "PXyGuLQVtm7tPsX1lbRVS0vgUih5DuF7lhvcUhGD9ES" ;
		
		
		WXBizMsgCrypt crypt =
				  new WXBizMsgCrypt(token , encodingAESKey , ConstantUtil.CorpID ) ;
		
		String echoStrV = crypt.VerifyURL(msg_signature, timestamp, nonce, echostr) ;
		
		System.out.println(echoStrV);
		
		
	}
	
	/**
	 * 我去安装东西
刘营辉 20:15:07
liuyinghui0719@aliyun.com liuyinghui523
漸行漸遠 20:15:40
我只要ip地址 
漸行漸遠 20:15:42
SSH
刘营辉 20:16:01
120.26.75.28
刘营辉 20:17:19
root  root
刘营辉 20:17:37
你试一下密码对不对
漸行漸遠 20:17:44
密码不对
刘营辉 20:17:45
我这都保存起来了 
刘营辉 20:17:53
liuyinghui*0719
刘营辉 20:17:58
你再试试s
	 */

}
