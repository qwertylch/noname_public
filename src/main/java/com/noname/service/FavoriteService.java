package com.noname.service;

import java.util.List;

import com.noname.dto.ResponseFavoriteDTO;

public interface FavoriteService {
	
	public List<ResponseFavoriteDTO> getFavorite(Long memberId);
	
	 public boolean isFavorite(Long productId, Long memberId);
	 
	 public boolean addFavorite(Long productId, Long memberId);
	
	 public boolean removeFavorite(Long productId, Long memberId);

}
