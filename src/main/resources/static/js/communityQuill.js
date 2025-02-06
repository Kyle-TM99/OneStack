// Quill 에디터 초기화
var quill = new Quill('#editor', {
    theme: 'snow',
    modules: {
        toolbar: [
            [{ 'header': [1, 2, false] }],
            ['bold', 'italic', 'underline'],
            ['link', 'image', 'code-block'],
            [{ 'list': 'ordered' }, { 'list': 'bullet' }],
            ['clean'] // remove formatting button
        ]
    }
});
// #editor가 초기화되었는지 확인
if (quill.root) {
    // 기존 본문 내용을 Quill 에디터에 삽입
    var existingContent = $('#communityBoardContent').val();
    quill.root.innerHTML = existingContent;
    // 폼 제출 시 Quill 에디터의 내용을 숨겨진 필드에 설정
    $('#writeForm').on('submit', function() {
        console.log(quill.getText());
        $('#communityBoardContent').val(quill.root.innerHTML); // Quill 에디터의 내용
    });
    $("#updateForm").on('submit', function() {
        if ($("#communityBoardTitle").val().length <= 0) {
            alert("제목을 입력해주세요");
            $("#communityBoardTitle").focus();
            return false;
        }
        if (quill.getText().trim().length <= 0) {
            alert("내용을 입력해주세요");
            quill.focus(); // Quill 에디터에 포커스를 맞춤
            return false;
        }
        $('#communityBoardContent').val(quill.root.innerHTML); // Quill 에디터의 내용 설정
    });
} else {
    console.error("Quill 에디터 초기화 실패: #editor 요소를 찾을 수 없습니다.");
}
$("#detailDelete").on("click", function() {
    $("#checkForm").attr("action", "delete");
    $("#checkForm").attr("method", "post");
    $("#checkForm").submit();
});
$("#detailUpdate").on("click", function() {
    $("#checkForm").attr("action", "updateForm");
    $("#checkForm").attr("method", "post");
    $("#checkForm").submit();
});
$("#writeForm").on("submit", function() {
    if ($("#boardTitle").val().length <= 0) {
        alert("제목을 입력해주세요");
        $("#title").focus();
        return false;
    }
    if (quill.getText().trim().length <= 0) {
        alert("내용을 입력해주세요");
        quill.focus(); // Quill 에디터에 포커스를 맞춤
        return false;
    }
});