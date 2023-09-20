package com.noname.dto;


import com.noname.entity.MemberImage;

import lombok.*;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class ImageDTO {
	
	private Long imageId;
	
    private String path;

    private String identifier;

    private String name;
    

}
