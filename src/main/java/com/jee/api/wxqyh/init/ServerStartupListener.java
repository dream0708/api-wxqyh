package com.jee.api.wxqyh.init;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Service;



@Service
public class ServerStartupListener implements ApplicationListener<ContextRefreshedEvent>  {
	
	private Logger logger = LogManager.getLogger(ServerStartupListener.class); 
	
	private static boolean started = false ;
	
    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
    	logger.info(".....Spring 容器启动完成.....");
    	if(!started){
    		
    		
    		
    		started = true ; //防止重复初始化
    	}
    	
    	logger.info(".....加入一些数据初始化工作.....");
    }

    

}
