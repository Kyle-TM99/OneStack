$(function() {
    $("#detailDelete").on("click", function() {
        $("#checkForm").attr("action", "delete");
        $("#checkForm").attr("method", "post");
        $("#checkForm").submit();
        });
    $("#detailUpdate").on("click", function() {
        $("#updateForm").attr("action", "updateForm");
        $("#updateForm").attr("method", "post");
        $("#updateForm").submit();
    });
});