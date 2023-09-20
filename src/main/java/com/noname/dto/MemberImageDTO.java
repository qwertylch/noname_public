package com.noname.dto;


import com.noname.entity.MemberImage;

import lombok.*;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class MemberImageDTO {
	
	private Long imageId;

    private String identifier;

    private String name;
    

}
