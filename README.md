 
<br>

# 프로젝트 - NONAME (무명)
<br>
<br>

************
<br>

### 목차 
1.  프로젝트 선정 이유  
2.  구현 목표
3.  개발 환경
4.  ER 다이어 그램
5. 	시스템 주요코드
<br>
<br>

************
<br>


### 프로젝트 선정 이유
<br>

* 무명 아티스트 홍보  
미술품 경매 사이트를 개발하는 가장 주된 이유중 하나는 미처 유명해지지 못한아티스트와 예술가들에게  
홍보의 기회를 제공함으로써 NONAME은 이러한 무명 아티스트들을 그들의 작품을 홍보할수 있는 플랫폼 역할
<br>
<br>

* 작품 판매 지원  
NONAME은 무명 작품을 판매하는데 필요한 경로를 제공. 아티스트들을 작품을 소개하고판매함으로써 예술활동을 지속적으로 수행가능
<br>
<br>

* 예술 시장 혁신  
NONAME은 예술 시장을 혁신하고 다양한 아티스트들이 참여할 수 있는 플랫폼을  
제공함으로써 예술 시장의 다양성을 증진시키고 이를 통해 작품 구매자와 판매자간의 연결을 촉진
<br>
<br>

************
<br>

### 구현 목표
<br>

* 경매  
아티스트는 관리자의 승인후 자신의 작품을 경매 등록
<br>
<br>

* 검색  
구매자의 원하는 크기 가격 형태 색상을 검색 및 찾기도구를 제공
<br>
<br>

* 차별성  
판매만이 목적이 아닌 갤러리를 통해 다양한 예술 스타일과 형식을 제공<br>
<br>

* 거래  
안전한 결제 시스템 서비스를 제공하여 판매자와 구매자의 거래를 보호
<br>
<br>

* 장소  
앱, 웹 개발로 시간과 장소를 제한 없이 제공
<br>
<br>

************
<br>

### 개발 환경
<br>


|구분               | 항목|  
|:--:| :--:|  
|OS               |Window 11|  
|Server           |Tomcat 9.0|  
|DBMS             |Oracle 19c|  
|Framework & Platform|Spring Boot 2.7.13, Bootstrap, Thymeleaf|  
|Language               |JAVA, HTML, CSS, JavaScript|  
|Tools            |INTELLIJ IDEA, Android Studio|  



<br>
<br>

************
<br>

### ER 다이어그램
<br>


![Imgur](https://i.imgur.com/papbWZ0.png)



<br>
<br>

************

<br>

### 시스템 주요코드
<br>

************
<br>


### * WebSocket & STOMP
#### - WebSocket
<img src="https://i.imgur.com/pkkpFvj.png" width="600px" />

#### - STOMP
<img src="https://i.imgur.com/YtQVOpU.png" width="600px" />

<br>

### 채팅

### 경매
<br>
<br>

#### 경매 화면
<br>

<img src="https://i.imgur.com/QAhRFJv.png" width="600px" />
<br>
<br>
<br>
<br>



WebSocket 연결이 열리면 onopen이 실행된다  
먼저, 해당 productId에 대한 세션을 auctions 맵에 저장 후  
사용자의 ID를 가져와서 인증 상태를 확인하고 메시지를 해당 세션으로 전송한다
<br>
<br>
<br>

<img src="https://imgur.com/udtJdlw.png" width="500px" />
<br>
<br>
<br>
<br>

클라이언트로부터 입력값과 상품 아이디를 전달받은 후  
값을 비교하여 메시지를 전달하거나 입찰가를 저장한다
<br>
<br>

 <img src="https://imgur.com/iEyP5TH.png" width="500px" />
<br>
<br>
<br>

<img src="https://imgur.com/iTu1ow7.png" width="350px" />
<br>

 <img src="https://imgur.com/VBAvkLC.png" width="350px" />
<br>
<br>
<br>

************
<br>

### 안드로이드
<br>

안드로이드 스튜디오 설정
<br>

<img src="https://i.imgur.com/Wh6VuMA.png" width="500px" />


<br>
<br>

************






