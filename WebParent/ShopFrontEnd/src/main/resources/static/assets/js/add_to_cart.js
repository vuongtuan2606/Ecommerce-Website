$(document).ready(function() {
    var selectedSizeId; // Biến để lưu trữ id của kích thước được chọn

    // Xử lý khi nhấp vào kích thước
    $(".product-size").on("click", function(evt) {
        evt.preventDefault();
        selectedSizeId = $(this).data("size-id");
        // Đánh dấu kích thước đã được chọn
        $(".product-size").removeClass("active");
        $(this).addClass("active");
    });

    $("#buttonAdd2Cart").on("click", function(evt) {
        evt.preventDefault();
        addToCart(selectedSizeId);

    });
});

function addToCart(selectedSizeId) {
    quantity = $("#quantity" + productId).val();

    url = contextPath + "cart/add/" + productId + "/" + quantity;
    var data = {
        sizeId: selectedSizeId
    };

    $.ajax({
        type: "POST",
        url: url,
        data:data,
        beforeSend: function(xhr) {
            xhr.setRequestHeader(csrfHeaderName, csrfValue);
        }
    }).done(function(response) {
        showModalAddNotification("Giỏ hàng", response)

    }).fail(function() {

        showErrorModal("Lỗi khi thêm sản phẩm vào giỏ hàng")
    });
}
