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

function save(boardNo) {
    // form data 생성
    const formData = new FormData();

    const title = $("#title").val();
    const content = $("#content").val();
    const filesElement =$("#attachFiles")[0];
    const changeFilesValue = filesElement.getAttribute("change-files");

    formData.append("title", title);
    formData.append("content", content);
    if (changeFilesValue) {
        if ($("#attachFiles")[0].files.length > 0) {
            const attachFiles = $("#attachFiles")[0].files;
            for (let i = 0; i < attachFiles.length; i++) {
                formData.append("attachFiles", attachFiles[i]);
                // console.log("attachFiles = ", attachFiles[i]);
            }
        }
    }
    //const boardNo = [[${board.boardNo}]];
    $.ajax({
        enctype: 'multipart/form-data',
        processData: false,
        contentType: false,
        cache: false,
        type:"POST",
        url:"/Board/" + boardNo + "/view",
        data:formData,
        success:function (data){
            console.log("data = ", data);
            alert("수정 사항이 저장되었습니다.");

            $("#saveButton").css("display","none");
            $("#deleteButton").css("display", "none");
            $("#editButton").removeAttr("style");


            $("input[name='title']").attr("readonly", true);
            $("textarea[name='content']").attr("readonly", true);
            $("#attachFiles").css("display", "none");
            $(".delete-link").hide();

        },
        error: function(xhr, status, error) {
            alert(status + "=> 에러났어");
            console.log(error);
        }
    });
}