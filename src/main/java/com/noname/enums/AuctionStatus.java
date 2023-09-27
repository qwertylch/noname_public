package com.noname.enums;

/**
 * 경매 진행 상태 
 */
public enum AuctionStatus {
		
    	/**
		 * 경매 대기 <br>
		 * code  - A03 <br>
		 * title - 대기
		 */
		PENDING("A01","대기"),
		
	    /**
		 * 경매 진행 <br>
		 * code  - A03 <br>
		 * title - 진행
		 */
        INBID("A02", "진행"),
        
        /**
		 * 경매 낙찰 상태 <br>
		 * code  - A03 <br>
		 * title - 낙찰
		 */
        CONFIRM("A03", "낙찰"),
        
        /**
  		 * 경매 낙찰 상태 <br>
  		 * code  - A03 <br>
  		 * title - 유찰
  		 */
        OUTBID("A04", "유찰");
		
		private final String code;
        private final String title;

        private AuctionStatus(String code, String title) {
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
