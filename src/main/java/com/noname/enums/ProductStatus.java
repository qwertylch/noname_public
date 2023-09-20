package com.noname.enums;


/**
 * 상품 심사 상태
 */
public enum ProductStatus {
		/**
		 * 상품 심사 대기 <br>
		 * code  - P01 <br>
		 * title - 대기
		 */
		PENDING("P01", "대기"),
		
    	/**
		 * 심사 완료 <br>
		 * code  - P02 <br>
		 * title - 완료
		 */
	    APPROVED("P02","완료"),
	    
    	/**
		 * 경매 대기 <br>
		 * code  - P03 <br>
		 * title - 보류
		 */
	    HOLD("P03", "보류");
	
	    private final String title;
	    private final String code;
	    
	    private ProductStatus(String code, String title) {
	        this.code = code;
	        this.title = title;
	    }

	    public String getCode() {
	        return code;
	    }

	    public String getTitle() {
	        return title;
	    }
}
