const deletionQueue  = [];
$(document).ready(function () {
    $("#attachFiles").attr("change-files", false);

    $(".delete-link").click(function (event) {
        event.preventDefault(); // 기본 클릭 동작 방지
        const fileId = parseInt($(this).attr('id'));
        const clickedLink = $(this);

        deletionQueue.push(fileId);
        console.log("added fileId to deletionQueue", deletionQueue);

        //deleteFile(fileId, clickedLink);
    });

    // fileId 별 1 건 삭제
    function deleteFile(fileId, clickedLink) {
        $.ajax({
            type: "POST",
            url: "/delete/" + fileId,
            success: function (response) {
                if (response === "success") {
                    // alert("파일이 삭제되었습니다.");
                    // 삭제 성공시 태그 삭제
                    $(clickedLink).parent().remove();
                } else {
                    alert("파일 삭제 중 오류가 발생했습니다.");
                }
            },
            error: function () {
                alert("파일 삭제 중 오류가 발생했습니다.");
            }
        });
    }

    $("#attachFiles").on("change", function () {
        $("#attachFiles").attr("change-files", true);
        var fileList = this.files; // 'this'를 통해 선택한 파일들 가져오기
        const selectedFilesList = $("#selectedFiles");

        console.log(fileList , selectedFilesList);

        // 선택한 파일 목록 만들기
        selectedFilesList.empty(); // 기존 목록 비우기
        const ul  = $("<ui>");

        for (let i = 0; i < fileList.length; i++) {
            const listItem = $("<li>");
            const link = $("<a>").text(fileList[i].name);
            const deleteLink = $("<a>")
                .addClass("delete-link")
                .text("x")
                .css("color", "red")
                .css("padding", "3px")
                .attr("data-index", i); // index 값 설정

            listItem.append(link.text(fileList[i].name)); // <a> 태그를 <li> 에 추가
            listItem.append(deleteLink);
            ul.append(listItem); // <ul> 안에 <li> 추가
            selectedFilesList.append(ul); // <div> 안에 추가
        }
    });
    // "x" 클릭시 <li> 삭제 (이벤트 위임) -WHY? 동적태그
    $("#selectedFiles").on("click", ".delete-link", function (event) {
        event.preventDefault(); // 기본 클릭 동작 방지

        const clickedLink = $(this);
        const deleteIndex = clickedLink.attr("data-index"); // 삭제할 index 추출

        $(clickedLink).parent().remove();

        updateFileInput(deleteIndex); // 파일 input 업데이트
        //updateFileList(deleteIndex); // 변경된 input 에 맞춰서 새로 selectedFiles 뿌려주기

    });

    // 파일 input 태그에 업데이트  -> DataTransfer 로 fileList 새로 만들기
    function updateFileInput(deleteIndex) {

        const dataTransfer = new DataTransfer();
        var fileArray = Array.from($("#attachFiles")[0].files);
        fileArray.splice(deleteIndex,1); // 해당하는 index 파일을 배열에서 제거
        console.log("after splice=> ", fileArray);

        fileArray.forEach(file => { dataTransfer.items.add(file);});

        //남은 배열을 dataTransfer로 처리(Array -> FileList)
        $("#attachFiles")[0].files = dataTransfer.files;  // 제거 처리된 FileList를 돌려줌
        console.log("updateFileInput >> attachFiles ", $("#attachFiles")[0].files);
    }

 /*   // 파일 목록 업데이트
    function updateFileList(deleteIndex) {

        const selectedFilesDiv = $("#selectedFiles");
        // 선택한 파일 목록 만들기
        selectedFilesList.empty(); // 기존 목록 비우기
        const ul  = $("<ui>");
        for (let i = 0; i < fileList.length; i++) {
            const listItem = $("<li>");
            const link = $("<a>").text(fileList[i].name);
            const deleteLink = $("<a>")
                .addClass("delete-link")
                .text("x")
                .css("color", "red")
                .css("padding", "3px")
                .attr("data-index", i); // index 값 설정
            listItem.append(link.text(fileList[i].name)); // <a> 태그를 <li> 에 추가
            listItem.append(deleteLink);
            ul.append(listItem); // <ul> 안에 <li> 추가
            selectedFilesList.append(ul); // <div> 안에 추가
        }
    }*/
});