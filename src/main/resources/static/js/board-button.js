function edit(){
    $("input[name='title']").attr("readonly", false);
    $("textarea[name='content']").attr("readonly", false);
    $("#attachFiles").removeAttr("style");

    // 저장&삭제 버튼 활성화
    if($("#saveButton").css("display") === "none" && $("#deleteButton").css("display") === "none"){
        $('#saveButton').show();
        $('#deleteButton').show();
        // $('#cancelButton').show();
        $('#editButton').hide();
        $(".delete-link").show();
    }
}

function cancel(){
    location.href="/";
}
function deleteBoard(boardNo){
    console.log( "boardNo" + boardNo);
    const formObj = $("form");
    formObj.attr("action", "/Board/"+boardNo +"/delete");
    formObj.attr("method", "post");
    formObj.submit();
}