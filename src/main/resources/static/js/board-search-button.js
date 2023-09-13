
$(document).ready(function() {
    // 동적 이벤트 핸들러 연결
    $("#additionalFields").on("click", ".delete-link", function (event) {
        event.preventDefault(); // 기본 클릭 동작 방지
        $(this).parent().remove();
    });
});

function createSelectElement() {
    var selectElement = document.createElement('select');
    selectElement.className = 'form-select';
    selectElement.name = 'searchType';
    selectElement.style.width = '15%';
    selectElement.style.marginRight = '5px';
    selectElement.style.marginLeft = '10px';
    selectElement.style.display = 'inline-block';
    return selectElement;
}

function createInputElement() {
    var inputElement = document.createElement('input');
    inputElement.type = 'text';
    inputElement.className = 'form-control';
    inputElement.name = 'searchKeyword';
    inputElement.placeholder = '검색어를 입력해주세요';
    inputElement.style.width = '40%';
    inputElement.style.display = 'inline-block';
    return inputElement;
}

function createCloseElement() {
    var closeElement = document.createElement('button');
    closeElement.type = 'button';
    closeElement.value = '-';
    closeElement.textContent = '-'; // 닫기 아이콘의 텍스트 설정
    // closeElement.href = '#'; // 클릭 가능한 링크로 만들기 위해 href 속성 설정
    closeElement.className = 'delete-link btn btn-group-sm ';
    // closeElement.style.width = '10%';
    closeElement.style.color = 'red';
    closeElement.style.fontSize = 'xx-large';
    return closeElement;
}

// 검색 필드 추가
function addFields() {

    // select 선택된 option 값 체크
    var selectedValues = [];

    // 모든 select box 에서 선택된 값 리스트
    var selectedValues = Array.from(document.querySelectorAll('.form-select')).map(function(select) {
        return select.value;
    });

    // allOptions - 선택할 수 있는 모든 옵션
    var allOptions = [
        { value: 'title', text: '제목' },
        { value: 'content', text: '내용' },
        { value: 'regId', text: '글쓴이' }
    ];

    if(selectedValues.length < allOptions.length){ // 태그 생성 횟수 제한

        var selectElement = createSelectElement();        // 새로운 select 태그 생성
        var inputElement = createInputElement();          // 새로운 input 태그 생성
        var closeElement = createCloseElement();        // 새로운 닫기(X) 태그 생성

        for (var i = 0; i < allOptions.length; i++) {
            var optionData = allOptions[i];
            var optionValue = optionData.value;
            var optionText = optionData.text;

            // 선택된 값 체크 후 옵션 추가
            if (!selectedValues.includes(optionValue)) { // not found
                var option = document.createElement('option');
                option.value = optionValue;
                option.text = optionText;
                selectElement.appendChild(option);
            }
        }

        // 새로운 div 태그 생성
        var divElement = document.createElement('div');
        divElement.className = 'additionalFieldsDiv';
        divElement.appendChild(selectElement);
        divElement.appendChild(inputElement);
        divElement.appendChild(closeElement);

        // 추가할 필드를 컨테이너에 추가
        var additionalFields = document.getElementById('additionalFields');
        additionalFields.appendChild(divElement);

    }
}




