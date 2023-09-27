package com.noname.wsauction;

import javax.websocket.DecodeException;
import javax.websocket.Decoder;
import javax.websocket.EndpointConfig;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class AuctionDecoder implements Decoder.Text<Auction>{
	
	ObjectMapper objectMapper = new ObjectMapper();
	
	@Override
	public Auction decode(String s) throws DecodeException {
		Auction auction = null;
		try {
			auction = objectMapper.readValue(s, Auction.class);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		
		return auction;
	}

	@Override
	public boolean willDecode(String s) {
		
		 try {
			 Auction auction = objectMapper.readValue(s, Auction.class);
	            return true;
	        } catch (Exception e) {
	            return false;
	        }
	}
	
	@Override
	public void init(EndpointConfig config) {
		
		
	}

	@Override
	public void destroy() {
		
	}


	


}
