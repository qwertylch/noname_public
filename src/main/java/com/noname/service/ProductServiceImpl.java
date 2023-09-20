package com.noname.service;

import com.noname.dto.AdminProductDTO;
import com.noname.dto.Auction;
import com.noname.dto.ImageDTO;
import com.noname.dto.MemberProductDTO;
import com.noname.dto.ProductDTO;
import com.noname.dto.ProductFormDTO;
import com.noname.dto.ResponseFavoriteDTO;
import com.noname.dto.ResponseProductDTO;
import com.noname.dto.SearchProductDTO;
import com.noname.entity.Bidding;
import com.noname.entity.Member;
import com.noname.entity.MemberImage;
import com.noname.entity.Product;
import com.noname.entity.ProductAuction;
import com.noname.entity.ProductImage;
import com.noname.enums.ImageType;
import com.noname.enums.ProductStatus;
import com.noname.repository.BiddingRepository;
import com.noname.repository.MemberRepository;
import com.noname.repository.ProductAuctionRepository;
import com.noname.repository.ProductImageRepository;
import com.noname.repository.ProductRepository;
import com.noname.repository.ProductSpecification;
import com.noname.util.DateConverter;
import com.noname.util.NumberConverter;

import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Service
@Transactional(readOnly = true)
public class ProductServiceImpl implements ProductService{

    private final ProductRepository productRepository;
    
    private final FavoriteService favoriteService;
    
    private final FileService fileService;
    
    private final ProductImageRepository productImageRepository;
    
    private final ProductAuctionRepository productAuctionRepository;
    
    private final MemberRepository memberRepository;
    
    private final BiddingRepository biddingRepository;


    @Autowired
    public ProductServiceImpl(ProductRepository productRepository, FavoriteService favoriteService,
			FileService fileService, ProductImageRepository productImageRepository,
			ProductAuctionRepository productAuctionRepository, MemberRepository memberRepository,
			BiddingRepository biddingRepository) {
		this.productRepository = productRepository;
		this.favoriteService = favoriteService;
		this.fileService = fileService;
		this.productImageRepository = productImageRepository;
		this.productAuctionRepository = productAuctionRepository;
		this.memberRepository = memberRepository;
		this.biddingRepository = biddingRepository;
	}

    
    public List<Auction> searchProductList(SearchProductDTO search, Pageable pageable, Long memberId){
        Specification<Product> spec = (root, query, criteriaBuilder) -> null;
        
        Optional<ProductAuction> minBid = productAuctionRepository.findTopByOrderByStartPriceAsc();
        
        Optional<Bidding> maxBid= biddingRepository.findTopByOrderByBidPriceDesc();        
    
        if(search.getName() != null) {
            spec = spec.and(ProductSpecification.containsName(search.getName()));
        
	    } 
        if(search.getType() != null) {
	            spec = spec.and(ProductSpecification.equalType(search.getType()));
	    } 
        if(search.getShape() != null) {
	            spec = spec.and(ProductSpecification.equalShape(search.getShape()));
	    }
        if(search.getBulk() != null) {
	            spec = spec.and(ProductSpecification.equalBulk(search.getBulk()));
        }
        
        if(search.getColor() != null) {
            spec = spec.and(ProductSpecification.equalBulk(search.getBulk()));
        } 
        
        
        if(search.getSortBy() == null || search.getSortBy().equals("createDate")) {
        	spec = spec.and(ProductSpecification.sortByCreateDateDesc());
        } else if(search.getSortBy().equals("startTime")) {
        	spec = spec.and(ProductSpecification.sortByAuctionStartAsc());
        } else if(search.getSortBy().equals("endTime")) {
        	spec = spec.and(ProductSpecification.sortByAuctionEndAsc());

        } else if(search.getMinPrice() != null || search.getMaxPrice() != null) {
	    	if(search.getMinPrice() != null && search.getMaxPrice() != null) {
	    		spec = spec.and(ProductSpecification.betweenPrice(search.getMinPrice(), search.getMaxPrice()));
	    	}else if(search.getMinPrice() == null &&  search.getMaxPrice() != null) {
	    		spec = spec.and(ProductSpecification.betweenPrice(minBid.get().getStartPrice().intValue(), search.getMaxPrice()));
	    	}else if(search.getMaxPrice() == null &&  search.getMinPrice() != null) {
	    		spec = spec.and(ProductSpecification.betweenPrice(search.getMinPrice(), maxBid.get().getBidPrice()));
	    	}
	    } else if (search.getMinPrice() == null && search.getMaxPrice() == null ) {
	    	spec = spec.and(ProductSpecification.betweenPrice(minBid.get().getStartPrice().intValue(), maxBid.get().getBidPrice()));
	    } 
 
        List<Product> searched = productRepository.findAll(spec);
        if(searched != null) {
            List<Auction> collect = searched
                    .stream()
                    .map(p -> new Auction(
                            p.getProductId()
                            ,p.getName()
                            ,p.getMember()
                            ,biddingRepository.findMaxPriceByProduct(p).orElse(0)
                            ,p.getProductImages().get(0)
                            ,p.getProductAuction()
                            ,DateConverter.timeDifferenceToMillis(p.getProductAuction().getStartTime())
                            ,memberId == null ? false :favoriteService.isFavorite(p.getProductId(), memberId)
                    		))
                    .collect(Collectors.toList());
            
            return collect;
        }
        return null;
    }

// hz


    
    // 상품목록 불러오는거***********************************
    @Override
    public List<Product> getProducts() {
        List<Product> productsList = productRepository.findAll();
        return productsList;
    }

	@Override
    public List<Product> getProductsByMnfctDateDesc() {
        // 최신순으로 정렬된 상품 목록을 가져오는 로직
        return productRepository.getByMnfctDateDesc();
    }
    @Override
    public List<Product> getProductsByMnfctDateAsc() {
        // 오래된순으로 정렬된 상품 목록을 가져오는 로직
        return productRepository.getByMnfctDateAsc();
    }
    //***********************************************
    // 상품 등록****************************************
    @Override
    @Transactional // 실행단위
    public Long saveProduct(ProductFormDTO productFormDTO, Long memberId) throws IOException {
        // 이미지 파일 저장

        Optional<Member> member = memberRepository.findById(memberId);



        List<ImageDTO> imagefiles = fileService.uploadImages(productFormDTO.getImageFiles(), ImageType.PRODUCT);
        log.info("imagefiles !!!!!!!:{}",imagefiles);
       List<ProductImage> images = imagefiles.stream().map(m-> new ProductImage(m.getPath(),m.getIdentifier(), m.getName())).collect(Collectors.toList());
        log.info("imagefiles !!!!!!!:{}",imagefiles);

        // DB에 파일 저장
        Product product = new Product();
        product.setType(productFormDTO.getType());
        product.setShape(productFormDTO.getShape());
        product.setColor(productFormDTO.getColor());
        product.setBulk(productFormDTO.getWidth()+"*"+productFormDTO.getDepth() +"*"+productFormDTO.getHeight());
        product.setMnfctDate(convertToLocalDT(productFormDTO.getMnfctDate()));
        product.setExplanation(productFormDTO.getExplanation());
        product.setName(productFormDTO.getProductName());
//        product.setStatus();
        product.setMember(member.get());
        Product result = productRepository.save(product);

        ProductAuction productAuction = new ProductAuction();
        productAuction.setStartTime(convertToLocalDT(productFormDTO.getStartTime()));
        productAuction.setEndTime(convertToLocalDT(productFormDTO.getStartTime()).plusDays(7));
        productAuction.setStartPrice(stringToInteger(productFormDTO.getPrice()));
//        productAuction.setStatus("pending");
        productAuction.setProduct(product);
        productAuctionRepository.save(productAuction);

        for (ProductImage m : images) {
            m.setProduct(product);
            productImageRepository.save(m);
        }
        return result.getProductId();
    }
    
    // 상품 등록****************************************
    
    
    
    @Override
    public ProductDTO getProduct(Long productId) {
    	
    	Optional<Product> found = productRepository.findById(productId);
    	Product product = found.get();
    	String[] dimension = product.getBulk().split("\\*");
    	int size = dimension.length;
    	ProductDTO dto = ProductDTO.builder()
    			.productId(product.getProductId())
    			.productName(product.getName())
    			.color(product.getColor())
    			.type(product.getType())
    			.shape(product.getShape())
    			.explanation(product.getExplanation())
    			.width(size != 0 ? dimension[0] : "0")
    			.depth(size != 0 ? dimension[1] : "0")
    			.height(size == 3 ? dimension[2] : "0")
    			.mnfctDate(DateConverter.formatToYYYYMMDD(product.getMnfctDate())
)    			.startPrice(NumberConverter.formatNumberWithCommas(product.getProductAuction().getStartPrice()))
    			.endTime(DateConverter.formatToYYYYMMDD(product.getProductAuction().getEndTime()))
    			.startTime(DateConverter.formatToYYYYMMDD(product.getProductAuction().getStartTime()))
    			.images(convertImages(product.getProductImages()))
    			.build();
    			
    			
//    			 private String path;
//    		    private String identifier;
//    		    private String name;
//    		   
//    		    // Product
//    		    private Long productId;
//    		    private String type;
//    		    private String shape;
//    		    private String color;
//    		    // bulk 로 넘길값
//    		    private String width;
//    		    private String depth;
//    		    private String height;
//
//    		    private String mnfctDate;
//    		    private String explanation;
//
//    		    // ProductImage
//    		    private List<MultipartFile> imageFiles;
//    		    private String productName;
//
//    		    // ProductAuction
//    		    private String startPrice;
//
//    		    // ProductAuction
//    		    private String startTime;
//    		    private String endTime;


    	
    	return dto;
    }
    
    
    
    @Override
    @Transactional // 실행단위
    public Long modProduct(ProductFormDTO productFormDTO) throws IOException {
        // 이미지 파일 저장
    	log.info("productFormDTO !!!!!!!:{}", productFormDTO.toString());

    	log.info("imagefiles !!!!!!!:{}", productFormDTO.getImageFiles().size());
        List<ImageDTO> imagefiles = fileService.uploadImages(productFormDTO.getImageFiles(), ImageType.PRODUCT);
        log.info("imagefiles !!!!!!!:{}",imagefiles);
       List<ProductImage> images = imagefiles.stream().map(m-> new ProductImage(m.getPath(),m.getIdentifier(), m.getName())).collect(Collectors.toList());
        log.info("imagefiles !!!!!!!:{}",imagefiles);

    	Optional<Product> found = productRepository.findById(productFormDTO.getProductId());
    	Product product = found.get();
        product.setType(productFormDTO.getType());
        product.setShape(productFormDTO.getShape());
        product.setColor(productFormDTO.getColor());
        product.setBulk(productFormDTO.getWidth()+"*"+productFormDTO.getDepth() +"*"+productFormDTO.getHeight());
        product.setMnfctDate(convertToLocalDT(productFormDTO.getMnfctDate()));
        product.setExplanation(productFormDTO.getExplanation());
        product.setName(productFormDTO.getProductName());
        Product result = productRepository.save(product);

        Optional<ProductAuction> foundAuction = productAuctionRepository.findById(result.getProductAuction().getAuctionId());
         ProductAuction productAuction = foundAuction.get();
         productAuction.setStartTime(convertToLocalDT(productFormDTO.getStartTime()));
        productAuction.setEndTime(convertToLocalDT(productFormDTO.getStartTime()).plusDays(7));
        productAuction.setStartPrice(stringToInteger(productFormDTO.getPrice()));
        productAuction.setProduct(product);
        productAuctionRepository.save(productAuction);

        for (ProductImage m : images) {
            m.setProduct(product);
            productImageRepository.save(m);
        }
        return result.getProductId();
    }


    private static Long stringToInteger(String numberString) {
        String num = numberString.trim();
        num = num.replace(",", "");
        return Long.parseLong(num);
    }

    private static LocalDateTime convertToLocalDT(String date) {
        LocalDate localDate = LocalDate.parse(date);

        // 시뮬레이션된 시간 (예: 09:00)
        LocalTime localTime = LocalTime.of(12, 0); // 오전 09:00

        // LocalDate 및 LocalTime을 결합하여 LocalDateTime을 생성
        return LocalDateTime.of(localDate, localTime);
    }
    //************************************************

    
    
    
    
    
// dk

    @Override
    public ResponseProductDTO getProductDetail(Long productId, Long memberId) {
    	Optional<Product> product = productRepository.findById(productId);
    	
    	requireExists(product);
    	log.info("MemberId : {}", memberId);

    	ResponseProductDTO productDTO = ResponseProductDTO.convertToDTO(product.get());
    	
    	if(memberId != null) {
    		productDTO.setFavorite(favoriteService.isFavorite(productId, memberId));
    	}else {
    		productDTO.setFavorite(false);
    	}	
        return productDTO;
    }
    
    
    @Override
    public List<AdminProductDTO> getAllAdminProducts() {
    	List<Product> products = productRepository.findAll();
	
        return products.stream().map(p -> AdminProductDTO.convertToDTO(p)).collect(Collectors.toList());
    }
    
    @Override
    public List<MemberProductDTO> getProductByMember(Member member) {
    	
    	List<Product> found= productRepository.findByMember(member);
    	List<MemberProductDTO> conDtos = found.stream()
    		    .map(p -> MemberProductDTO.convertToDTO(p, biddingRepository.findMaxPriceByProduct(p).orElse(0)))
    		    .collect(Collectors.toList());
    	return conDtos;
    	
    }
    
    
    
    
    private void requireExists(Optional<?> optional) {
        if (!optional.isPresent()) {
            String entityName = optional.map(Object::getClass)
                                        .map(Class::getSimpleName)
                                        .orElse("Entity"); 
            throw new EntityNotFoundException(entityName + " not found");
        }

    }
    
    
    
    



    private static List<String> convertImages(List<ProductImage> images) {
    	if(images != null) {
	        return images.stream()
	            .map(image -> encodeImageURL(image.getPath() + File.separator + image.getIdentifier() + image.getName()))
	            .collect(Collectors.toList());
    	}
    	return null;
    }
    
    
    private static String encodeImageURL(String url) {
        try {
            return URLEncoder.encode(url, StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new RuntimeException("Error encoding image URL");
        }
    }

    





}
