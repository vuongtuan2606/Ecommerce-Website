$(document).ready(function (){


    $(".link-delete").on("click", function (e){

        e.preventDefault();

        var link = $(this);

        var entityId = link.attr("entityId");

        var entityName = link.attr("entityName");

        $("#confirmSuccess").attr("href", link.attr("href"));

        $("#confirmText").text("Bạn có muốn xóa "+entityName+" : " + entityId + "  ? ");

    });

    $("#fileImage").change(function() {
        if (!checkFileSize(this)) {
            return;
        }
        showImageThumbnail(this);
    });

});

function showImageThumbnail(fileInput) {
    var file = fileInput.files[0];
    var reader = new FileReader();
    reader.onload = function(e) {
        $("#thumbnail").attr("src", e.target.result);
    };

    reader.readAsDataURL(file);
}


function checkFileSize(fileInput) {
    fileSize = fileInput.files[0].size;

    if (fileSize > MAX_FILE_SIZE) {
        fileInput.setCustomValidity("You must choose an image less than " + MAX_FILE_SIZE + " bytes!");
        fileInput.reportValidity();

        return false;
    } else {
        fileInput.setCustomValidity("");

        return true;
    }
}

document.addEventListener("DOMContentLoaded", function() {

    const form = document.querySelector('.search-form');

    form.addEventListener('submit', function(event) {

        const keywordInput = document.querySelector('input[name="keyword"]');

        if (keywordInput.value.trim() === '') {

            // Nếu trường keyword rỗng, chuyển hướng đến trang moduleURL
            window.location.href = moduleURL;

            event.preventDefault();
        }
    });
});
