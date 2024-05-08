

$(document).ready(function() {
    $('.product-quantity .minus').on('click', function () {
        decreaseQuantity($(this));
    });

    $('.product-quantity .plus').on('click', function () {
        increaseQuantity($(this));
    });

});

function decreaseQuantity(link) {
    productId = link.attr("pid");
    quantityInput = $("#quantity" + productId);
    newQuantity = parseInt(quantityInput.val()) - 1;

    if (newQuantity > 0) {
        quantityInput.val(newQuantity);
        updateQuantity(productId, newQuantity);
    }
}

function increaseQuantity(link) {
    productId = link.attr("pid");
    quantityInput = $("#quantity" + productId);
    newQuantity = parseInt(quantityInput.val()) + 1;

    if (newQuantity <= 5) {
        quantityInput.val(newQuantity);
        updateQuantity(productId, newQuantity);
    } else {
        showWarningModal("Số lượng tối đa là 5")
    }
}