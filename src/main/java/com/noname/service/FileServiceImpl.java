package com.noname.service;

import com.noname.dto.ImageDTO;
import com.noname.entity.MemberImage;
import com.noname.entity.ProductImage;
import com.noname.enums.ImageType;

import lombok.extern.slf4j.Slf4j;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
public class FileServiceImpl implements FileService{

//    @Value("${product.image.dir}")
//    private String PRODUCT_UPlOAD_PATH;
//
//    @Value("${user.image.dir}")
//    private String USER_UPlOAD_PATH;


    private final ImageType[] thumbnails =  {ImageType.PRODUCT};

    @Override
    public ImageDTO uploadImage(MultipartFile uploadFile, ImageType imageType) {
        
        if(uploadFile.isEmpty() || !isImageFile(uploadFile)) {
        	return null;
        }
    	
    	String datePath = getPathByDate();
        String uploadPath = getPathByType(imageType);
        createDirIfNotExists(uploadPath);
        boolean isThumbnail = Arrays.stream(thumbnails)
                .anyMatch(thumbnail -> thumbnail == imageType);
    
        ImageDTO image = new ImageDTO();
        String orgFileName = uploadFile.getOriginalFilename();
        String identifier =  UUID.randomUUID().toString();
        String uploadFileName = identifier + orgFileName;
        File saveFile = new File(uploadPath, uploadFileName);
        
        try {
        	uploadFile.transferTo(saveFile);

            image.setPath(datePath);
            image.setIdentifier(identifier);
            image.setName(orgFileName);

            if(isThumbnail){
                createThumnail(saveFile, uploadPath, uploadFileName);
            }


        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return image; 
    }
    

    @Override
    public List<ImageDTO> uploadImages(List<MultipartFile> uploadFile, ImageType imageType) {

        List<ImageDTO> images = new ArrayList();

        for(MultipartFile file : uploadFile) {
            if(!file.isEmpty()) {
                ImageDTO image = uploadImage(file, imageType);
                images.add(image);
            }
        }
        return images;
     
    }


    private void createThumnail(File orginalFile, String uploadPath, String uploadFileName)  {
        try {

            Thumbnails.of(orginalFile)
                    .size(500, 500)
                    .toFile(new File(uploadPath, "th_" + uploadFileName) );
        }catch (IOException e) {
            throw new RuntimeException(e);
        }

      
    }

    private String getPathByType(ImageType imageType) {
        String uploadPath = "";
        if (imageType == ImageType.USER) {
           uploadPath = imageType.getUploadPath() + getPathByDate();
        } else if (imageType == ImageType.PRODUCT) {
           uploadPath = imageType.getUploadPath() + getPathByDate();

        }
        return uploadPath;
    }


    private String getPathByDate() {
        SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
        String datePath = fmt.format(new Date()).replace('-', File.separatorChar);

        return datePath;
    }

    private void createDirIfNotExists(String uploadPath) {
        File uploadDirectory = new File(uploadPath);

        if (!uploadDirectory.exists() && !uploadDirectory.isDirectory()) {
            uploadDirectory.mkdirs();
        }
    }

    private boolean isImageFile(MultipartFile file) {
        String fileName = file.getOriginalFilename();
        if (fileName != null) {
            String extension = fileName.substring(fileName.lastIndexOf(".") + 1);
            String[] imageExtensions = {"jpg", "jpeg", "png", "gif"};

            for (String validExtension : imageExtensions) {
                if (extension.equalsIgnoreCase(validExtension)) {
                    return true;
                }
            }
        }
        return false;
    }



    public byte[] getImage(String file, ImageType imageType) {
    	log.info("File Service getImage :  {} {}", imageType.getUploadPath(), file);
        File savedFile = new File(imageType.getUploadPath(), file);
        byte[] bytes = null;
        try {
            bytes = FileCopyUtils.copyToByteArray(savedFile);
        }catch (IOException e) {
            throw new RuntimeException(e);
        }

        return bytes;
    }


    
    public String encodeImageURL(
            @Nullable ProductImage productImage,
            @Nullable MemberImage memberImage,
            @Nullable boolean isThumbnail) {
        try {
            String imageUrl = "";

            if (productImage != null) {
            	if(isThumbnail == true) {
                	imageUrl = productImage.getPath() + File.separator + "th_" + productImage.getIdentifier() + productImage.getName();

            	}else {
                	imageUrl = productImage.getPath() + File.separator + productImage.getIdentifier() + productImage.getName();
            	}
            	
            } else if (memberImage != null) {
            	
            	imageUrl = memberImage.getPath() + File.separator + memberImage.getIdentifier() + memberImage.getName();
            }

            return URLEncoder.encode(imageUrl, StandardCharsets.UTF_8.toString());
        } catch (Exception e) {
            throw new RuntimeException("Error encoding image URL", e);
        }
    }
    
   
    public void deleteImageFile(ImageDTO imageDTO, ImageType imageType) {
        
    	try {
	    	File orgFile = new File(imageType.getUploadPath() + imageDTO.getPath(), imageDTO.getIdentifier()+ imageDTO.getName()) ;
	    	orgFile.delete();
	    	boolean isThumbnail = Arrays.stream(thumbnails)
	                 .anyMatch(thumbnail -> thumbnail == imageType);
	    	if(isThumbnail) {
	        	File thFile = new File(imageType.getUploadPath() + imageDTO.getPath(), "th_" + imageDTO.getIdentifier()+ imageDTO.getName()) ;
	        	thFile.delete();
	    	}  	

        } catch(Exception e) {
        	throw new RuntimeException("Error delete image ", e);
        	 
        }

    }








}
