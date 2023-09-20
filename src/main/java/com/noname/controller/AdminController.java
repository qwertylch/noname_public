package com.noname.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.noname.dto.AdminProductDTO;
import com.noname.repository.MemberRepository;
import com.noname.repository.ProductRepository;
import com.noname.service.MemberService;
import com.noname.service.ProductService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/admin")
public class AdminController {
		
	
	private final ProductService productService;
	private final MemberService memberService;
	
	
	@Autowired
	public AdminController(ProductService productService, MemberService memberService) {
		super();
		this.productService = productService;
		this.memberService = memberService;
	}
	
	
	
	
	@GetMapping("/product")
	public String getProducts(Model model) {
		
		List<AdminProductDTO> products = productService.getAllAdminProducts();
		model.addAttribute("products", products);
		return "admin/product";
	}
	
	
	
	
	
	
	
	
	

}
