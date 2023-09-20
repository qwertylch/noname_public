package com.noname.service;

import com.noname.dto.SampleDTO;
import com.noname.entity.Product;
import com.noname.repository.SampleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.FileCopyUtils;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;


@Service
@Transactional(readOnly = true)
public class SampleServiceImpl implements SampleService {


//    private final String ROOT_PATH = "C:\\upload\\";

    @Value("${product.image.dir}")
    private String ROOT_PATH;

    private final SampleRepository sampleRepository;

    @Autowired
    public SampleServiceImpl(SampleRepository sampleRepository) {
        this.sampleRepository = sampleRepository;
    }


    @Override
    public List<SampleDTO> getProductList(){
        List<Product> allProduct = sampleRepository.findAllProduct();
        if(allProduct != null) {
            List<SampleDTO> collect = allProduct
                    .stream()
                    .map(p -> new SampleDTO(
                            p.getProductId()
                            ,p.getName()
                            ,p.getMember()
                            ,p.getProductImages().get(0)
                            ,p.getProductAuction()))
                    .collect(Collectors.toList());
            return collect;
        }
        return null;
    }

    @Override
    public byte[] getImage(String file) {
        File savedFile = new File(ROOT_PATH, file);
        byte[] bytes = null;
        try {
            bytes = FileCopyUtils.copyToByteArray(savedFile);
        }catch (IOException e) {
            throw new RuntimeException(e);
        }

        return bytes;
    }


//
//    @Transactional
//    @Override
//    public void saveProductImage(MultipartFile[] uploadFile, Long productId) {
//
//        for(MultipartFile file : uploadFile) {
//            if(file.isEmpty()) {return;}
//
////            String ext = orgFileName.substring(orgFileName.lastIndexOf("."));
////            String uploadFileName = UUID.randomUUID().toString() + ext;
//            String orgFileName = file.getOriginalFilename();
//            String identifier =  UUID.randomUUID().toString();
//            String uploadFileName = identifier + orgFileName;
//
//            String datePath = getPathByDate();
//            String uploadPath = ROOT_PATH + datePath;
//
//            File saveFile = new File(uploadPath, uploadFileName);
//
//
//            try {
//                file.transferTo(saveFile);
//                ProductImage productImage = converToEntity(productId, identifier ,orgFileName, datePath);
//                sampleRepository.saveProductImage(productImage);
//
//            } catch (IOException e) {
//                throw new RuntimeException(e);
//            }
//
//
//        }
//    }
//
//
//    @Override
//    public List<ImageResponseDTO> getImageByProductId(Long productId) {
//        return sampleRepository.findImageByProductId(productId)
//                .stream().map(i -> new ImageResponseDTO()).collect(Collectors.toList());
//    }
//

//
//    private  ProductImage converToEntity(Long productId, String identifier, String uploadFileName, String datePath) {
//        ProductImage productImage = new ProductImage();
//
//        productImage.setIdentifier(identifier);
//        productImage.setPath(datePath);
//        productImage.setName(uploadFileName);
//        return productImage;
//    }
//
//
//    private String getPathByDate() {
//        SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
//        String datePath = fmt.format(new Date()).replace('-', File.separatorChar);
//
//        File uploadDirectory = new File(ROOT_PATH + datePath);
//
//        if (!uploadDirectory.exists() && !uploadDirectory.isDirectory()) {
//            uploadDirectory.mkdirs();
//        }
//        return datePath;
//    }
//
//
//

}
