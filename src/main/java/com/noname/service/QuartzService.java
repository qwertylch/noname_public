package com.noname.service;

import com.noname.entity.ProductAuction;
import com.noname.entity.ProductImage;

import java.util.List;

public interface QuartzService {
    List<ProductAuction> changePending();

    List<ProductAuction> findAllAuctions();

    List<ProductAuction> ChangeInbid();

    List<ProductImage> changeImages();
 }
