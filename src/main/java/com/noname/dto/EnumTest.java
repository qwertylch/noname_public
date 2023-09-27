package com.noname.dto;

import com.noname.enums.ProductStatus;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class EnumTest {
	
	
	private String name;
	private ProductStatus status;
	
}
