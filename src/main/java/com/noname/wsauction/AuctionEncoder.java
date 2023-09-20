package com.noname.wsauction;

import javax.websocket.EncodeException;
import javax.websocket.Encoder;
import javax.websocket.EndpointConfig;

import com.noname.dto.ResponseBidDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class AuctionEncoder implements Encoder.Text<ResponseBidDTO> {

	ObjectMapper mapper = new ObjectMapper();
	
	@Override
	public String encode(ResponseBidDTO bidDTO) throws EncodeException {
		
		String json = null;
		try {
			json = mapper.writeValueAsString(bidDTO);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		
		return json;
	}
	
	@Override
	public void init(EndpointConfig config) {
	
		
	}

	@Override
	public void destroy() {
	
		
	}



}
