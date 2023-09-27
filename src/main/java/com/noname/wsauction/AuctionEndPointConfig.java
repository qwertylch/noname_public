package com.noname.wsauction;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import javax.websocket.HandshakeResponse;
import javax.websocket.server.HandshakeRequest;
import javax.websocket.server.ServerEndpointConfig;
import javax.websocket.server.ServerEndpointConfig.Configurator;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import com.noname.security.CustomUser;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
public class AuctionEndPointConfig extends Configurator  implements ApplicationContextAware {
	public static final String Session = "Session";
	
    private static volatile BeanFactory context;

    @Override
    public <T> T getEndpointInstance(Class<T> clazz) throws InstantiationException {
        return context.getBean(clazz);
    }
    
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
    	AuctionEndPointConfig.context = applicationContext;	
	}
	  
	@Override
	public void modifyHandshake(ServerEndpointConfig config, HandshakeRequest request, HandshakeResponse response) {
	
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication != null && authentication.getPrincipal() instanceof CustomUser) {
		    CustomUser customUser = (CustomUser) authentication.getPrincipal();
		    config.getUserProperties().put("memberId", customUser.getMember().getMemberId());
		} 

	}



}
