package com.jee.api.wxqyh.init;


import javax.servlet.ServletContext;
import javax.servlet.ServletException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.WebApplicationInitializer;


/**
 * 
 * @author yaomengke
 *
 */
public class WebInitializer implements WebApplicationInitializer  {

	private static final Logger logger = LogManager.getLogger(WebInitializer.class) ;
	
	@Override
	public void onStartup(ServletContext servletContext) throws ServletException {
		logger.info("camping-api  application server starting ...");
		logger.info("部署路径:" + servletContext .getRealPath("/")  +  servletContext.getContextPath()) ;
		logger.info("容器版本:" + servletContext.getServerInfo()) ;
		logger.info("camping-api  application server start  .....");
	}
	
	

}
