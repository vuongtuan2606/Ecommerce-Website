var dropdownBrands = $("#brand");
var dropdownCategories = $("#category");
$(document).ready(function() {
    // thêm richText Editor
    $("#shortDescription").richText();
    $("#fullDescription").richText();

    // Thiết lập sự kiện change cho dropdownBrands
    dropdownBrands.change(function() {
        // Xóa tất cả các options hiện có trong dropdownCategories
        dropdownCategories.empty();

        // Gọi hàm getCategories() để lấy các danh mục mới dựa trên thương hiệu được chọn
        getCategories();
    });

    // Gọi hàm getCategoriesForNewForm() khi trang được tải
    getCategoriesForNewForm();

});

function getCategoriesForNewForm() {
    catIdField = $("#categoryId");
    editMode = false;

    if (catIdField.length) {
        editMode = true;
    }

    if (!editMode) getCategories();
}


function getCategories() {
    var brandId = dropdownBrands.val();
    var url = brandModuleURL + "/" + brandId + "/categories";

    // Gửi yêu cầu GET đến url
    fetch(url)
        .then(response => response.json())
        .then(data => {
            // Xóa các options hiện có trong dropdownCategories
            dropdownCategories.empty();

            // Thêm các option mới từ responseJson vào dropdownCategories
            data.forEach(category => {
                var option = document.createElement("option");
                option.value = category.id;
                option.text = category.name;
                dropdownCategories.append(option);
            });
        })
        .catch(error => console.error('Error:', error));
}

function checkUnique(form) {
    var productId = $("#id").val();
    var productName = $("#name").val();

    var csrfValue = $("input[name='_csrf']").val();
    var params = {id: productId, name: productName, _csrf: csrfValue};

    $.post(checkUniqueUrl, params, function (response) {

        if (response == "OK") {
            form.submit();
        } else if (response == "Duplicate") {

            $("#errorBody").text("Có một sản phẩm khác có cùng tên " + productName);
            $('#errorModal').modal('show');

        } else {
            $("#errorBody").text("Phản hồi không xác định từ máy chủ");
        }

    }).fail(function () {
        $("#errorBody").text("Không thể kết nối đến máy chủ");
    });

    return false;
}