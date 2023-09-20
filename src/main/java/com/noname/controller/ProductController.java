package com.noname.controller;

import com.noname.dto.Auction;
import com.noname.dto.GalleryDTO;
import com.noname.dto.MemberProductDTO;
import com.noname.dto.ProductDTO;
import com.noname.dto.ProductFormDTO;
import com.noname.dto.ResponseProductDTO;
import com.noname.dto.SearchProductDTO;
import com.noname.entity.Product;
import com.noname.enums.ImageType;
import com.noname.security.CustomUser;
import com.noname.service.FileService;
import com.noname.service.ProductService;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.hibernate.annotations.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties.Pageable;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttribute;

@Slf4j
@Controller
public class ProductController {

    private final ProductService productService;
    private final FileService fileService;

    @Autowired
    public ProductController(ProductService productService, FileService fileService) {
        this.productService = productService;
        this.fileService = fileService;
    }
    

    @GetMapping("/product/artworks")
    public String searchProduct(@ModelAttribute("search") SearchProductDTO search
            	,@PageableDefault(page = 0, size = 10)Pageable pageable, Model model
            	,@AuthenticationPrincipal CustomUser user) {
    	Long sid = null;
    	if(user != null) {
    		sid = user.getMember().getMemberId();
    	}
    	
    	List<Auction> products= productService.searchProductList(search, pageable,  sid);
    	model.addAttribute("products", products);
    	model.addAttribute("search", search);  
        log.info("search -> : {}", search.toString());
        return "product/artworks";

    }
    

    
    
//    @GetMapping("/product/artworks")
//    public String getProductList(Model model) {
//        List<Auction> productList = productService.getProductList();
//        model.addAttribute("products", productList);
//        return "product/artworks";
//    }
//    
    
//  hz
    
    @GetMapping("/product/upload")
    public String uploadForm() {
        return "product/productForm";
    }

    @PostMapping("/product/upload")
    public String uploadPro(@AuthenticationPrincipal CustomUser user, @ModelAttribute ProductFormDTO productFormDTO) throws IOException {
    	
    	if(user == null) {
    		return "redirect:/login";	
    	}
    	
        log.info("productFormDTO : {}" , productFormDTO.toString());
        log.info("size :{} ", productFormDTO.getImageFiles().size());
        Long productId = productService.saveProduct(productFormDTO, user.getMember().getMemberId());
        return "redirect:/product/member"; // 갤러리 페이지 호출 (임시)
    }
    
	
	
	@GetMapping(value = "/product/member")
	public String getProductByMemberId(@AuthenticationPrincipal CustomUser user, Model model) {
		if(user == null) {
    		return "redirect:/login";	
    	}
		
		List<MemberProductDTO> products= productService.getProductByMember(user.getMember());
		model.addAttribute("products", products);
		log.info("product ===={}",products.size());
		
		return  "mypage/product";
	}
	
	@GetMapping("/product/modify/{productId}")
	public String getProduct(@PathVariable Long productId, Model model) {
		
		ProductDTO product= productService.getProduct(productId);
		model.addAttribute("product", product);
	
		
		return  "product/productMod";
	}
	
	@PostMapping("/product/modify")
	public String productModify(@ModelAttribute ProductFormDTO productFormDTO, Model model) throws IOException {
		
		Long id = productService.modProduct(productFormDTO);

		
		return "redirect:/product/member";
	}

    /*
    갤러리 페이지
    : 각 product당 대표이지 한개만 가져 와서 보여주기
    + 그냥 전체 프로덕트객체만 가져오는 것이 아닌 정렬해서 가져오도록 설정
    1. product를 우선적으로 조회해서 받아와야 한다.-> 전체 product
    2. ProductImage에 product 객체 주고 해당하는 객체의 첫번째 이미지 조회
     */
    @GetMapping("/gallery")
    private String getGallery(@RequestParam(name = "keyword", required = false)String keyword, Model model,
                                @RequestParam(name = "authorName", required = false) String memberName){

    	
        List<Product> foundProducts = null;
        // 넘어오는 키워드의 값이 '최신순' 과 같을 때
        if ("new".equals(keyword)) {
            foundProducts = productService.getProductsByMnfctDateDesc();
        }
        // 넘어오는 키워드의 값이 '오래된순' 과 같을 때
        else if ("old".equals(keyword)) {
            foundProducts = productService.getProductsByMnfctDateAsc();
        }
        /*
        // 넘어오는 키워드의 값이 '관심많은순' 과 같을 때 : favorite 테이블에서 값을 못찾음...  추후 추가예정
        else if ("관심많은순".equals(keyword)){
            foundProducts = productService.getProductsByFavorite();
        }
        // 추후 작가이름 클릭했을 때 해당 작가작품 뿌려주는 용으로 사용 ... 추후 추가 예정
        else if ("작가이름".equals(keyword)) {
            foundProducts = productService.getProductsByAuthor(memberName);
        }
        */
        // 위에 조건들 없이 그냥 리스트 페이지 불러왔을때 랜덤값으로 데이터 뿌려주기
        else {
            // 키워드가 없거나 올바르지 않은 경우 모든 제품 가져오기
            foundProducts = productService.getProducts();
            // 키워드에 따른 정렬을 유지하면서 결과를 랜덤하게 섞기
            Collections.shuffle(foundProducts);
        }

        // List<Product> foundProducts 리스트의 각 요소를 GalleryDTO 객체로 변환하고,
        // 이를 새로운 리스트인 List<GalleryDTO> products에 저장합니다.
        List<GalleryDTO> products = foundProducts.stream().map(p -> new GalleryDTO(p)).collect(Collectors.toList());

        // products에 작가이름, 작품이미지, 작품아이디 를 갖고있다.
        model.addAttribute("products", products);
        log.info("controller products : {}" , products);

        // 해당 모델을 받을 뷰페이지 리턴
        return "product/gallery";
    }
    
    
//  dk

    @GetMapping("/product/{productId}")
    public String getProductDetail(@PathVariable Long productId, Model model, @AuthenticationPrincipal CustomUser user){
    	Long sid = null;
    	if(user != null) {
    		sid = user.getMember().getMemberId();
    	}
    	
        ResponseProductDTO product = productService.getProductDetail(productId, sid);
        
        log.info(product.toString());
        model.addAttribute("product", product);

        return "product/detail";
    }
    
    @GetMapping("/product/{productId}/image")
    public ResponseEntity<Resource> getProductImage(@RequestParam String filename) throws IOException {
        log.info("fileName: {}", filename);

        File file = new File(ImageType.PRODUCT.getUploadPath(), filename);
        if (!file.exists() || !file.isFile()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        log.info("Product getImage :  {}", filename);
        byte[] imageBytes = fileService.getImage(filename, ImageType.PRODUCT);
        ByteArrayResource resource = new ByteArrayResource(imageBytes);

        String contentType = determineContentType(filename);

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .body(resource);
    }
    
    @GetMapping("/product/image")
    public ResponseEntity<Resource> getImage(@RequestParam String filename) throws IOException {
        log.info("fileName: {}", filename);

        File file = new File(ImageType.PRODUCT.getUploadPath(), filename);
        if (!file.exists() || !file.isFile()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        byte[] imageBytes = fileService.getImage(filename, ImageType.PRODUCT);
        ByteArrayResource resource = new ByteArrayResource(imageBytes);

        String contentType = determineContentType(filename);

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .body(resource);
    }



    private String determineContentType(String filename) {
        // 파일 확장자를 기반으로 MIME 타입을 결정하는 로직
        // 예: jpg, jpeg, png, gif 등
        // 필요에 따라 파일 확장자와 MIME 타입의 매핑을 더욱 정교하게 관리할 수 있음
        String extension = getExtension(filename);
        switch (extension.toLowerCase()) {
            case "jpg":
            case "jpeg":
                return "image/jpeg";
            case "png":
                return "image/png";
            case "gif":
                return "image/gif";
            default:
                return "application/octet-stream"; // 기본값으로 바이너리 타입 사용
        }
    }

    private String getExtension(String filename) {
        int lastDotIndex = filename.lastIndexOf('.');
        if (lastDotIndex > 0) {
            return filename.substring(lastDotIndex + 1).toLowerCase();
        }
        return "";
    }








}
