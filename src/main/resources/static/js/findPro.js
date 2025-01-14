$(function() {

    $("#resetCategory").on("click", function() {
        $('#categorySelect').val("");
        let radios = document.querySelectorAll('input[type="radio"]');
        radios.forEach(function(radio) {
            radio.checked = false;
        });
    });











});