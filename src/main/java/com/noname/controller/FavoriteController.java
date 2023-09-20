package com.noname.controller;

import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttribute;

import com.noname.dto.ResponseFavoriteDTO;
import com.noname.security.CustomUser;
import com.noname.service.FavoriteService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/favorite")
public class FavoriteController {
	
	private FavoriteService favoriteService;
	
	@Autowired
	public FavoriteController(FavoriteService favoriteService) {
		this.favoriteService = favoriteService;
	}
	
	
	
	@GetMapping
	private String getFavorite(Model model, @AuthenticationPrincipal CustomUser user) {
		
		if(user == null) {
			return "redirect:/login";
		}
		
		
		List<ResponseFavoriteDTO>favorites = favoriteService.getFavorite(user.getMember().getMemberId());
		model.addAttribute("favorites", favorites);
		log.info("favorites size : {} ", favorites.size());

		
		return "mypage/favorite";
	}
	
	@ResponseBody
	@PostMapping(value = "/{productId}", produces = MediaType.TEXT_PLAIN_VALUE)
	private ResponseEntity<String> addFavorite(@PathVariable Long productId, @AuthenticationPrincipal CustomUser user) {
		
		if(user == null) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("잘못된 요청.");
		}
		log.info(user.getMember().getName());
		log.info("memberId : {}" + user.getMember().getMemberId());
		
		boolean result = favoriteService.addFavorite(productId, user.getMember().getMemberId());
		
		return result ? ResponseEntity.status(HttpStatus.OK).body("관심 품목에 등록되었습니다.")
				: ResponseEntity.status(HttpStatus.BAD_REQUEST).body("잘못된 요청.");
	
	}
	
	@ResponseBody                                                                                                       
	@DeleteMapping(value = "/{productId}", produces = MediaType.TEXT_PLAIN_VALUE)
	private ResponseEntity<String> removeFavorite(@PathVariable Long productId, @AuthenticationPrincipal CustomUser user) {
		
		
		
		boolean result = favoriteService.removeFavorite(productId, user.getMember().getMemberId());
		
		return result ? ResponseEntity.status(HttpStatus.OK).body("관심 품목에서 삭제되었습니다.")
				: ResponseEntity.status(HttpStatus.BAD_REQUEST).body("잘못된 요청.");
	
	}
	
	

	
	
	
	
}
