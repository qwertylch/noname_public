package com.noname.service;

import com.noname.dto.ImageDTO;
import com.noname.entity.MemberImage;
import com.noname.entity.ProductImage;
import com.noname.enums.ImageType;

import org.springframework.lang.Nullable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface FileService {

    public byte[] getImage(String file, ImageType imageType);
    
    public ImageDTO uploadImage(MultipartFile uploadFile, ImageType imageType);
    
    public List<ImageDTO> uploadImages(List<MultipartFile> uploadFile, ImageType imageType);
    
    public String encodeImageURL(@Nullable ProductImage productImage, @Nullable MemberImage memberImage, @Nullable boolean isThumbnail);
    
    public void deleteImageFile(ImageDTO imageDTO, ImageType imageType);

}
