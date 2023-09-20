package com.noname.service;

import com.noname.dto.SampleDTO;

import java.util.List;

public interface SampleService {



    public List<SampleDTO>getProductList();

    public byte[] getImage(String file);

//    public void saveProductImage(MultipartFile[] uploadFile, Long productId);
//
//    public List<ImageResponseDTO> getImageByProductId(Long productId);
//





}
