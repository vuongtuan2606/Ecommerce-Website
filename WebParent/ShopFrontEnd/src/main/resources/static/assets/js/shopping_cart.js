$(document).ready(function() {
    $(".minus").on("click", function() {
        var productId = $(this).attr("pid");
        var sizeId = $(this).data("size-id");
        decreaseQuantity($(this), productId, sizeId);
    });

    $(".plus").on("click", function() {
        var productId = $(this).attr("pid");
        var sizeId = $(this).data("size-id");
        increaseQuantity($(this), productId, sizeId);
    });


    $('.linkRemove').on('click', function(evt){
        evt.preventDefault();
        removeProduct($(this));
    })

});
function decreaseQuantity(link, productId, sizeId) {
    var quantityInput = $("#quantity" + productId + "_" + sizeId);
    var newQuantity = parseInt(quantityInput.val()) - 1;
    if (newQuantity > 0) {
        quantityInput.val(newQuantity);
        updateQuantity(productId, newQuantity, sizeId);
    }
}

function increaseQuantity(link, productId, sizeId) {
    var quantityInput = $("#quantity" + productId + "_" + sizeId);
    var newQuantity = parseInt(quantityInput.val()) + 1;

    var checkStockUrl = contextPath + "cart/checkStock/" + productId + "/" + newQuantity;
    var data = {
        sizeId: sizeId
    };

    $.ajax({
        type: "GET",
        url: checkStockUrl,
        data: data,
        success: function(response) {
            if (newQuantity <= 5) {
                quantityInput.val(newQuantity);
                updateQuantity(productId, newQuantity, sizeId);
            } else {
                showWarningModal("Số lượng tối đa là 5");
            }
        },
        error: function(response) {
            showErrorModal(response.responseText);
        }
    });
}

/* Cập nhật số lượng của một sản phẩm trong giỏ hàng */
function updateQuantity(productId, quantity, sizeId) {
    var url = contextPath + "cart/update/" + productId + "/" + quantity;
    var data = {
        sizeId: sizeId
    };
    $.ajax({
        type: "POST",
        url: url,
        data: data,
        beforeSend: function(xhr) {
            // Thiết lập tiêu đề mã CSRF trước khi gửi yêu cầu
            xhr.setRequestHeader(csrfHeaderName, csrfValue);
        }
    }).done(function(updatedSubtotal) {
        // Cập nhật tổng số tiền phụ cho sản phẩm
        updateSubtotal(updatedSubtotal, productId, sizeId);

        // Cập nhật tổng số tiền của giỏ hàng
        updateTotal();
    }).fail(function() {
        showErrorModal("Lỗi khi cập nhật số lượng sản phẩm.");
    });
}

/*Xóa một sản phẩm khỏi giỏ hàng*/
/* Xóa một sản phẩm khỏi giỏ hàng */
function removeProduct(link) {
    var url = link.attr("href");

    $.ajax({
        type: "DELETE",
        url: url,
        beforeSend: function(xhr) {
            // Thiết lập tiêu đề mã CSRF trước khi gửi yêu cầu
            xhr.setRequestHeader(csrfHeaderName, csrfValue);
        }
    }).done(function(response) {
        // Xóa phần tử cha của phần tử link (cart_item)
        link.closest('.cart_item').fadeOut(300, function() {
            // Xóa phần tử khỏi DOM
            $(this).remove();

            // Cập nhật tổng số tiền của giỏ hàng
            updateTotal();
        });

        // Hiển thị modal
        showModalDialog("Giỏ hàng", response);

    }).fail(function() {
        // Hiển thị modal
        showErrorModal("Lỗi khi xóa sản phẩm.");
    });
}

/*Cập nhật tổng số tiền phụ cho một sản phẩm trong giỏ hàng*/
function updateSubtotal(updatedSubtotal, productId,sizeId) {
    $("#subtotal" + productId + "_" + sizeId).text(formatCurrency(updatedSubtotal));
}


/*Cập nhật tổng số tiền của giỏ hàng*/
function updateTotal() {
    var total = 0.0;
    var productCount = 0;

    // Duyệt qua mỗi phần tử có class "subtotal"
    $(".subtotal").each(function(index, element) {
        // Tăng số lượng sản phẩm lên mỗi lần lặp
        productCount++;

        // Cộng số tiền phụ của sản phẩm vào tổng số tiền
        total += parseFloat(clearCurrencyFormat($(element).text()));
    });

    if (productCount < 1) {
        $(".cart-bottom-wrap").hide();
    } else {
        // Cập nhật tổng số tiền được định dạng lại
        $("#total").text(formatCurrency(total));
        $("#total2").text(formatCurrency(total));
        $(".cart-bottom-wrap").show();
    }
}

decimalSeparator = decimalPointType == 'COMMA' ? ',' : '.';
thousandsSeparator = thousandsPointType == 'COMMA' ? ',' : '.';

/* Định dạng số tiền thành định dạng tiền tệ */
function formatCurrency(amount) {
    return $.number(amount, decimalDigits, decimalSeparator, thousandsSeparator);
}

/* Loại bỏ định dạng tiền tệ từ một chuỗi số*/
function clearCurrencyFormat(numberString) {
    result = numberString.replaceAll(thousandsSeparator, "");
    return result.replaceAll(decimalSeparator, ".");
}



