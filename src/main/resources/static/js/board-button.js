function edit(){
    $("input[name='title'], textarea[name='content']").removeAttr("readonly");
    $("#attachFiles").removeAttr("style");

    // 저장&삭제 버튼 활성화
    if($("#saveButton").css("display") === "none" && $("#deleteButton").css("display") === "none"){
        $("#saveButton, #deleteButton, .delete-link").show();
        $("#editButton").hide();
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
    const isChanged = filesElement.getAttribute("change-files");

    formData.append("title", title);
    formData.append("content", content);
    if (isChanged==="true") {
        if ($("#attachFiles")[0].files.length > 0) {
            const attachFiles = $("#attachFiles")[0].files;
            for (let i = 0; i < attachFiles.length; i++) {
                formData.append("attachFiles", attachFiles[i]);
                // console.log("attachFiles = ", attachFiles[i]);
            }
        }
    }

/*    const selectedFiles = [];
    $("#selectedFiles li").each(function() {
        const fileId = Number($(this).find("a.delete-link").attr("id"));
        const fileName = $(this).find("a#file_" + fileId).text();
        selectedFiles.push({ fileId, fileName });
    });

    console.log(selectedFiles);
    // 선택된 파일 정보를 FormData에 추가
    for (var i = 0; i < selectedFiles.length; i++) {
        formData.append('files[' + i + '].id', selectedFiles[i].fileId);
        formData.append('files[' + i + '].originalFileName', selectedFiles[i].fileName);
    }*/

    if (isChanged==="false") {
        formData.append('deletionQueue', deletionQueue);
        console.log("deletionQueue = ", deletionQueue);
    }

    //const boardNo = [[${board.boardNo}]];
    $.ajax({
        processData: false,
        contentType : false,
        cache: false,
        type: "POST",
        url: "/Board/" + boardNo + "/view",
        data: formData,
        success: function (data) {
            console.log("data = ", data);
            alert("수정 사항이 저장되었습니다.");
            $("#saveButton, #deleteButton").css("display", "none");
            $("#editButton").removeAttr("style");

            $("input[name='title'], textarea[name='content']").attr("readonly", true);
            $("#attachFiles").css("display", "none");
            $(".delete-link").hide();

        },
        error: function (xhr, status, error) {
            alert(status + "=> 에러났어");
            console.log(xhr, error);
        }
    });

}