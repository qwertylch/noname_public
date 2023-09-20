package com.noname.wsauction;

import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.http.HttpSession;
import javax.websocket.Endpoint;
import javax.websocket.EndpointConfig;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.noname.dto.ResponseBidDTO;
import com.noname.service.BiddingService;
import com.noname.wsauction.*;

import ch.qos.logback.core.rolling.helper.IntegerTokenConverter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@ServerEndpoint(value = "/auction/{productId}", 
	encoders = { AuctionEncoder.class }, 
	configurator = AuctionEndPointConfig.class)
public class AuctionWebSocket {

	private static Map<Long, Set<Session>> auctions = new ConcurrentHashMap<>();
	private static Map<Session, Long> members = new ConcurrentHashMap<>();
	
	private final BiddingService biddingService;

	@Autowired
	public AuctionWebSocket(BiddingService biddingService) {
		this.biddingService = biddingService;
	}

	@OnOpen
	public void onOpen(Session session, @PathParam("productId") Long productId, EndpointConfig config) {

		auctions.computeIfAbsent(productId, k -> 
			Collections.synchronizedSet(Collections.newSetFromMap(new ConcurrentHashMap<>()))).add(session);
		
		Long memberId = (Long) config.getUserProperties().get("memberId");

		if (memberId == null) {
			sendMessage(session, "Unauthorized User");
		} else {
			members.put(session, memberId);
			sendMessage(session, "Authorized User");
		}
	}

	@OnMessage
	public void onMessage(String message, Session session, @PathParam("productId") Long productId) {
	    log.info("onMessage session : {} ", session.getId());
	    boolean auth = members.containsKey(session);
	    if (auth) {
	        handleBidMessage(session, productId, message);
	    } else {
	        sendMessage(session, "Unauthorized User: Cannot send messages.");
	    }
	}

	private void handleBidMessage(Session session, Long productId, String message) {
		log.info("message : {} ", message);
		String messageTrim = message.trim();
		
	    Long sid = members.get(session);
	    Long pid = productId;
	    try {
	        Integer bidPrice = Integer.parseInt(messageTrim);
	        String isNotMax = "0";
	        String isMaxMember = "1";

	        if (biddingService.isMaxBidPrice(bidPrice, pid)) {
	            if (biddingService.isMaxBidMember(sid, pid)) {
	                sendMessage(session, isMaxMember);
	            } else {
	                ResponseBidDTO bidDTO = biddingService.saveBidding(pid, sid, bidPrice);
	                broadcastObject(productId, bidDTO);
	            }
	        } else {
	            sendMessage(session, isNotMax);
	        }
	    } catch (NumberFormatException e) {
	        log.error("입찰 가격 파싱 오류: " + e.getMessage());
	        sendMessage(session, "Invalid bid price format.");
	    }
	}
	

	@OnClose
	public void onClose(Session session, @PathParam("productId") Long productId) {
		log.info("onClose session : {} ", session.getId());
//		sendMessage(session, "User disconnected");
		members.remove(session);
		auctions.getOrDefault(productId, Collections.emptySet()).remove(session);
		
	}
	
	@OnError
	public void onError(Session session, @PathParam("productId") Long productId, Throwable throwable) {
		members.remove(session);
		auctions.getOrDefault(productId, Collections.emptySet()).remove(session);
	   log.info("WebSocket error occurred: {}", throwable.getMessage());
	}
	

	private void broadcastMessage(Long pid, String message) {
		Set<Session> channelSessions = auctions.getOrDefault(pid, Collections.emptySet());
		for (Session s : channelSessions) {
			sendMessage(s, message);
		}
	}

	private void broadcastObject(Long pid, Object object) {
		Set<Session> channelSessions = auctions.getOrDefault(pid, Collections.emptySet());
		for (Session s : channelSessions) {
			sendObject(s, object);
		}
	}

	private void sendMessage(Session session, String message) {

		session.getAsyncRemote().sendText(message);

	}

	private void sendObject(Session session, Object object) {
		
		session.getAsyncRemote().sendObject(object);

	}

}