package com.noname.service;

import java.io.IOException;
import java.util.List;

import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties.Pageable;
import org.springframework.lang.Nullable;

import com.noname.dto.AdminProductDTO;
import com.noname.dto.Auction;
import com.noname.dto.MemberProductDTO;
import com.noname.dto.ProductDTO;
import com.noname.dto.ProductFormDTO;
import com.noname.dto.ResponseProductDTO;
import com.noname.dto.SearchProductDTO;
import com.noname.entity.Member;
import com.noname.entity.Product;

public interface ProductService {
	
//	public List<Auction> getProductList();
	
	public List<Auction> searchProductList(SearchProductDTO search, Pageable pageable, @Nullable Long memberId);
	
//	hz
    // 랜덤값으로 뿌려주기 위해서 전체 상품객체 불러옴
    public List<Product> getProducts();
    //  최신순
    public List<Product> getProductsByMnfctDateDesc();
    // 오래된순
    public List<Product> getProductsByMnfctDateAsc();

    // 상품 저장 처리
    public Long saveProduct(ProductFormDTO productFormDTO,  Long memberId) throws IOException;
	
// dk
    public ResponseProductDTO getProductDetail(Long productId, Long memberId);

	public List<AdminProductDTO> getAllAdminProducts();

	public List<MemberProductDTO> getProductByMember(Member member);

	public Long modProduct(ProductFormDTO productFormDTO) throws IOException;

	public ProductDTO getProduct(Long productId);
	

}
