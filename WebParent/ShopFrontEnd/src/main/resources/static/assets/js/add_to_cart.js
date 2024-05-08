$(document).ready(function() {
    $("#buttonAdd2Cart").on("click", function(evt) {
        evt.preventDefault();
        addToCart();

    });
});

function addToCart() {
    quantity = $("#quantity" + productId).val();
    url = contextPath + "cart/add/" + productId + "/" + quantity;

    $.ajax({
        type: "POST",
        url: url,
        beforeSend: function(xhr) {
            xhr.setRequestHeader(csrfHeaderName, csrfValue);
        }
    }).done(function(response) {
        showModalAddNotification("Giỏ hàng", response)

    }).fail(function() {

        showErrorModal("Lỗi khi thêm sản phẩm vào giỏ hàng")
    });
}
