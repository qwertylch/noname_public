var stompClient = null; // 전역변수로 설정 -> 구독 및 메시지 전송에서 사용

// 이벤트 작성
jQuery(document).ready(function() {
    console.log("Index page is ready");
    // 클릭 이벤트 작성
    // 팝업창 일때 : id="btn-popup-ent" 클릭하면 sendMessage() 호출
    jQuery("#btn-popup-ent").click(function() {
        var msg = jQuery("#msg").val();
        sendMessage(msg, productId); // sendMessage 호출
    });

    // 전체 화면일 때 메시지 전송
    jQuery("#btn-panel-ent").click(function() {
        var msg = jQuery("#msg_panel").val();
        sendMessage(msg, productId);
    });

    // 웹소켓 연결 설정
    connect(productId);
});
    // connect() 메서드 호출
    // -> 변수로 제품 아이디를 넣어 주면서 해당 ID로 웹소켓 연결 설정
    // 구독 및 메시지 표시 , 전송 기능을 구현하도록 설정
    function connect(productId){ // 프로덕트아이디는 detail 페이지 컨트롤러에서 모델로 전달받아옴
        // chatConfig에서 설정했던 endpoint로 연결(채팅 활성화?)
        // 1. SockJS로 Websocket 연결을 생성
        var sockJS = new SockJS("/chat");
        // 2. Stomp.js 클라이언트 생성하고 SockJS 연결을 사용
        stompClient = Stomp.over(sockJS);
        console.log("sockJS 연결!!!")

        // 3. Stomp.js 클라이언트 설정 -> connect이 이루어지면 실행되는 function
        stompClient.connect({},function(frame){
            console.log("STOMP Connected" + frame); // 연결 확인 메시지

            //4. subscribe(path, callback)으로 메세지를 받을 수 있음
            // 클라이언트에서 메시지 수신
            stompClient.subscribe('/sub/chat/' + productId, function(message){
                var receivedMessage = JSON.parse(message.body).message;
                showMessage(receivedMessage,memberName);
            });
        }); // (end)stompClient.connect
    }
    // 메시지 보내기 : key:value값으로 해서 브로커에 전달
    function sendMessage(msg,productId) {
        console.log("sending message");
        if(stompClient){
            stompClient.send("/pub/chat/" + productId, {}, JSON.stringify({message: msg}));
            jQuery("#msg , #msg_panel").val('');
            //jQuery("#msg_panel").val('');
        }else{
            console.log("stompClient 초기화 아직 안돼어있음")
        }
    }

    // 팝업창 및 전체 화면에 동일하게 내용이 뿌려져야 된다.
    function showMessage(receivedMessage, memberName) {
        var messageElement = "<p class='chat-messages'>" + memberName + " : " + receivedMessage + "</p>"
        jQuery("#msgArea_pop , #msgArea_panel").append(messageElement);
        //jQuery("#msgArea_panel").append(messageElement);
    }

    // 마지막 메시지 위치로 스크롤 이동
    function scrollToBottom(element) {
      element.scrollTop = element.scrollHeight;
    }

// Enter 키 눌렀을 때 메시지 전송하는 함수
document.addEventListener("DOMContentLoaded", function() {
    var msgInputs = document.querySelectorAll(".message-box-popup , .message-box-panel");
    msgInputs.forEach(function(msgInput) {
        msgInput.addEventListener("keyup", function(event) {
            if (event.key === "Enter" && !event.shiftKey) {
                event.preventDefault(); // 기본동작을 막아줌
                var msg = msgInput.value.trim(); // 입력된 메시지 가져오기
                if(msg !== ''){
                    sendMessage(msg, productId); // sendMessage 호출 메시지와 프로덕트 아이디 변수로 담아서 전달
                    msgInput.value = ''; // 입력창 초기화
                }
            }
        });
    });
});