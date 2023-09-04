$(document).ready(function () {

    //등록
    $('.register_box').click(function () {
        const clickedLink = $(this);
        register(clickedLink);
    });

    // 삭제
    $(".comment_delete_btn").click(function (event) {
        event.preventDefault(); // 기본 클릭 동작 방지

        const commentNo = parseInt($(this).parents("ul li").attr('comment_no'));
        const boardNo = parseInt($(this).parents("ul li").attr('board_no'));
        const clickedLink = $(this);
        console.log("commentNo = " , commentNo , "clickedLink = ", clickedLink)
        commentDelete(commentNo,boardNo, clickedLink);
    });

    //답글쓰기
    $(document).on('click', '.comment_write_btn', function () {
    // $(".comment_write_btn").click(function (event) {
    //     event.preventDefault(); // 기본 클릭 동작 방지

        const regId =$(this).attr("reg_id");
        const targetLink = $(this).parents("li");

        //console.log("targetLink =", targetLink, ", regId =", regId);

        // 답글쓰기 버튼을 누르면 새로운 댓글 입력창을 생성하여 HTML 코드를 추가
        createRegisterForm(targetLink, regId);

    });

});



// 답글쓰기 버튼 클릭시 새로운 댓글 입력창 생성
function createRegisterForm(targetLink, regId){

    const commentNo = parseInt($(targetLink).attr('comment_no'));
    const level = parseInt($(targetLink).attr('comment_level'));
    const boardNo = parseInt($(targetLink).attr('board_no'));

    const newCommentHTML = "<li>" +
        "<div class='CommentWriter'> " +
        "<div class='comment_inbox'>" +
        "<input class='comment_info' type='hidden' board_no=" + boardNo + " comment_level="+ level + " comment_no="+ commentNo + " reg_id=" + regId +" />" +
        "<p class='comment_inbox_name'>댓글작성자</p>" +
        "<textarea class='comment_inbox_text' rows='1' style='overflow:hidden; overflow-wrap: break-word; height:18px;' placeholder='댓글을 남겨보세요'></textarea>" +
        "</div>" +
        "<div class= 'register_box'>" +
        "<a role='button' class=comment_register'>등록</a>" +
        "</div>" +
        "</div>" +
        "</li>";
    // console.log("newCommentHTML = ", newCommentHTML);

    // 다른데 생성된 댓글입력창이 있으면 초기화
    $('.CommentWriter').parent("li").remove();

    // 생성한 HTML 코드를 댓글 목록에 추가
    $('#comment_li_'+ commentNo.toString()).append(newCommentHTML);  // #comment_li_1211

    $('.register_box').click(function () {
        const clickedLink = $(this);
        register(clickedLink);
    });

}

// 댓글등록
function register(clickedLink){

    const commentBox = $(clickedLink).closest(".CommentWriter");
    // const commentBox = $(clickedLink).closest("li").find(".CommentWriter");
    console.log("commentBox =", commentBox);

    const content = commentBox.find(".comment_inbox_text").val(); // 댓글 내용
    const level = commentBox.find(".comment_info").attr("comment_level"); // 부모
    const commentNo = commentBox.find(".comment_info").attr("comment_no"); // 부모
    const boardNo = commentBox.find(".comment_info").attr("board_no"); // 게시판 번호
    const regId = commentBox.find(".comment_info").attr("reg_id"); // 글쓴이

    // content, regId, parentNo, level
    var formData = new FormData();
    formData.append("content", content);
    formData.append("regId", regId);
    if(commentNo !== undefined ) {
        formData.append("parentNo", commentNo);
        formData.append("level", level)
    }
    $.ajax({
        processData: false,
        contentType : false,
        cache: false,
        type: "POST",
        url: "/comment/" + boardNo + "/new",
        data: formData,
        success: function (data) {
            console.log("data = ", data);
            alert("댓글을 등록하였습니다.");
            window.location.href = "/Board/"+boardNo + "/view";
            $(clickedLink).parent("li").remove();
        },
        error: function (xhr, status, error) {
            alert(status + "=> 에러났어");
            console.log(xhr, error);
        }
    });
}


function commentDelete(commentNo,boardNo, clickedLink) {
    $.ajax({
        type: "POST",
        url: "/comment/" + commentNo + "/delete",
        success: function (response) {
            if (response === "success") {
                // 삭제 성공시 태그 삭제
                //$(clickedLink).parents("li").remove();  // comment_li_xx 삭제
                alert("댓글을 삭제하였습니다.");
                window.location.href = "/Board/" + boardNo + "/view";

            } else {
                alert("error- 댓글을 삭제 중 오류가 발생했습니다.");
            }
        },
        error: function (xhr, status, error) {
            alert("댓글을 삭제 중 오류가 발생했습니다.\n" + status + ": " + error);
        }
    });
}
