package com.noname.service;

import com.noname.entity.ProductAuction;
import com.noname.entity.ProductImage;
import com.noname.enums.AuctionStatus;
import com.noname.repository.BiddingRepository;
import com.noname.repository.ImageRepository;
import com.noname.repository.ProductAuctionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class QuartzServiceImpl implements QuartzService {

    private final ProductAuctionRepository productAuctionRepository;
    private final BiddingRepository biddingRepository;
    private final ImageRepository productImageRepository;

    int i = 0;
    // pending 대기 -> inbid 진행
    // inbid 진행 -> confirm 낙찰
    // inbid -> outbid 유찰  hold???
    public void checkOutPendingDate(ProductAuction productAuction) {

        if (productAuction.getStatus().equals(AuctionStatus.PENDING) && productAuction.getStartTime().isBefore(LocalDateTime.now())) {
            log.info("====== true or false pending : {}", productAuction.getStartTime().isBefore(LocalDateTime.now()));
            log.info("====== PENDING -> INBID ! : {}", productAuction);
            productAuction.setStatus(AuctionStatus.INBID);
            productAuctionRepository.save(productAuction);
            i++;
        }
    }

    public void checkOutInbidDate(ProductAuction productAuction) {
        Integer maxPrice = biddingRepository.findMaxPriceByProductId(productAuction.getProduct().getProductId());

        if(productAuction.getStatus().equals(AuctionStatus.INBID) && productAuction.getEndTime().isBefore(LocalDateTime.now())) {
            if (maxPrice != null && !maxPrice.equals(productAuction.getStartPrice())) {
                log.info("===== end with different price !");
                log.info("===== current price : {}", maxPrice);
                log.info("====== INBID -> CONFIRM ! : {}", productAuction);
//                productAuction.setStatus(AuctionStatus.CONFIRM);
//                productAuctionRepository.save(productAuction);
                i++;
            } else if (maxPrice == null) {
                log.info("===== No bid price !");
                log.info("====== INBID -> OUTBID ! : {}", productAuction);
//                productAuction.setStatus(AuctionStatus.OUTBID);
//                productAuctionRepository.save(productAuction)
                i++;
            }
        }
        System.out.println(i + "개가 변경이 필요합니다");
    }

    public List<ProductImage> changeImages() {
        List<ProductImage> imageList = productImageRepository.findAll();
        String IMAGE_FOLDER_PATH = "/Users/ch/Documents/workspace/upload/product";
        // 이미지 목록을 순회하면서 실제 파일이 있는지 확인하고 삭제
        for (ProductImage image : imageList) {
            String imagePath = IMAGE_FOLDER_PATH + image.getPath();

            File imageFile = new File(imagePath);

            if (!imageFile.exists()) {
                // 파일이 존재하지 않으면 DB에서 삭제
//                productImageRepository.delete(image);
                log.info("=====imagePath : {}",imagePath);

            }
        }
        return imageList;
    }

        @Override
        public List<ProductAuction> changePending() {

            List<ProductAuction> auctionList = productAuctionRepository.findAll();

            for (ProductAuction productAuction : auctionList) {
                checkOutPendingDate(productAuction);
            }
            return auctionList;
        }

        public List<ProductAuction> ChangeInbid () {

            List<ProductAuction> auctionList = productAuctionRepository.findAll();

            for (ProductAuction productAuction : auctionList) {
                checkOutInbidDate(productAuction);
            }
            return auctionList;
        }

        @Override
        public List<ProductAuction> findAllAuctions () {
            return productAuctionRepository.findAll();
        }


    }

