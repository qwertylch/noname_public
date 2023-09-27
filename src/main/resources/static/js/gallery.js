// 이미지 클릭 시 모달 열기
function showModal(element) {
  var productImage = element.getAttribute("data-product-image");
  var productName = element.getAttribute("data-product-name");
  var productId = element.getAttribute("data-product-id");

  var pid = document.getElementById("pid");
  pid.value = productId;

  var modal = document.getElementById("gallery-modal");
  var modalImage = document.getElementById("modalImage");
  var modalTitle = document.getElementById("modalTitle");

  console.log("이미지 경로:", productImage);
  console.log("이미지 요소:", modalImage);

  modalImage.src = "/product/image?filename=" + productImage;
  modalImage.alt = "상세 이미지";
  modalTitle.textContent = productName;

  modal.style.display = "block";
  getReplyList()
}


jQuery(document).ready(function(){
    // 댓글 삭제 이벤트 등록
    jQuery("#replyListDiv").on("click", "button#deleteBtn", function(e){
      console.log("삭제 버튼 클릭!!!");
      let rid = jQuery(this).data('rid');
      jQuery.ajax({
          type: 'delete',
          url: '/replies/' + rid,
          success: function(result, status) {
              console.log("삭제 성공!! : " + result);
              getReplyList(); // 댓글 목록 새로 조회 + 화면에 출력
          },
          error: function(e){
              console.log("삭제 요청 실패...." + e);
          }
      });
    });

    // 엔터 키 눌렀을 때 이벤트 처리
    // keyup : 키가 눌렸다가 올라올때 작동
    // keydown : 키가 눌렸을때 작동
    // keypress : 위 두개는 한글로 입력했을시 두번 입력되는데 press로 하면 해결됨
    jQuery(".form-control").on("keypress", function(event){
        if(event.key === "Enter"){
            console.log("엔터키입력!!!")
            event.preventDefault();
            event.stopPropagation();
            replySubmit();
        }
    });
    // 클릭했을때 저장처리
    jQuery("#newReplySubmit").on("click", function(e){
        console.log("마우스 클릭!!!");
        replySubmit();
    });
});

// 댓글 저장처리
function replySubmit(){
        let replyVal = jQuery("#newReply").val();
        let replyerVal = jQuery("#newReplyer").val();
        let pid = jQuery("#pid").val();
        let data = { reply: replyVal, replyer: replyerVal, productId: pid };
        console.log(data);
        jQuery.ajax({
            type: 'post',
            url: '/replies/add',
            data: data,
            success: function(result){
                console.log("add result : " + result);
                jQuery("#newReply").val('');
                jQuery("#newReplyer").val('');
                // 댓글 목록 재 요청
                getReplyList();
            },
            error: function(e){
                console.log("add reply error.....");
                console.log(e);
            }
        });
}

// 이미지 눌러서 모달 떴을때 리뷰 목록 불러오기 위함
// 댓글 목록 조회
function getReplyList() {
  console.log("reply list!");
  let productId = jQuery("#pid").val();
  console.log("댓글 목록 조회하는데 필요한 productId : " + productId);
  jQuery.ajax({
      type: 'get',
      url: '/replies/list/' + productId,
      success: function(result){
          console.log("reply list request success");
          console.log("result : " + result);
          printReplyList(result);
      },
      error: function(e){
          console.log("reply list request error...");
      }
  });
}

// 댓글 목록 화면에 뿌리기
function printReplyList(result) {
  let html = '';
  result.forEach(function(rep, i){
      html += '<div class="row no-gutters align-items-center"><div class="col mr-2">';
      html += '<div class="h5 mb-0 font-weight-bold text-dark-800">' + rep.reply + '</div>';
      html += '<div class="text-xs font-weight-bold text-gray text-uppercase mb-1" style="margin-left:3px; font-size:5px"> ' + rep.replyer;
      html += ' <span class="ml-2 text-gray-500">' + rep.createDate + '</span></div></div>';
      html += '<div class="col-auto btn-group-sm">';
      html += '<button id="deleteBtn" type="button" data-rid="'+ rep.replyId +'" class="btn btn-outline-dark">삭제</button>';
      html += '</div></div><hr />';
  });
  jQuery("#replyListDiv").html(html);
}


// 모달 닫기
function closeModal() {
  var modal = document.getElementById("gallery-modal");
  modal.style.display = "none";
}
var newBtn = document.getElementById("new");
var oldBtn = document.getElementById("old");

// 데이터 불러오는 것은 되는데 masonry형식으로 정렬이 안돼고있음-> 수정 필요
function getProducts(keyword) {
  // AJAX 요청을 생성하여 선택한 키워드를 Controller로 전달
  jQuery.ajax({
      type: 'GET',
      url: '/gallery', // Controller의 URL
      data: { keyword: keyword }, // 선택한 키워드를 'keyword' 매개변수로 controller에 전달
      success: function(data) { // 키워드 넘겨서 컨트롤러로 부터 받은 결과 값
          console.log('받은 데이터:', data); // 데이터 확인
          // 이 부분은 HTML 문자열을 그대로 추가하도록 수정
          var galleryContainer = document.querySelector('.row[data-masonry]');
          galleryContainer.innerHTML = data;

          if(keyword == 'new'){
              newBtn.classList.add('active');
              oldBtn.classList.remove('active');
          }else {
              oldBtn.classList.add('active');
              newBtn.classList.remove('active');
          }

          // Masonry 적용
          var masonry = new Masonry(galleryContainer, {
              itemSelector: '.col-sm-6',
              percentPosition: true
          });
          masonry.layout();
      }
  });
}