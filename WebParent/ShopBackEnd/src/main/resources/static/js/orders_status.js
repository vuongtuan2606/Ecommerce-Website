var iconNames = {
	'PROCESSING': 'fa-retweet',
	'PACKAGED': 'fa-box-open',
	'SHIPPING': 'fa-shipping-fast',
	'DELIVERED': 'fa-file-invoice-dollar'
};

var confirmText;
var confirmModalDialog;
var confirmSuccessButton;
var cancelButton;

$(document).ready(function() {
	confirmText = $("#confirmText");
	confirmModalDialog = $("#confirmModal");
	confirmSuccessButton = $("#confirmSuccess");
	cancelButton = $(".btn-close, .btn-danger");

	$(".linkUpdateStatus").on("click", function(e) {
		e.preventDefault();
		var link = $(this);
		showUpdateConfirmModal(link);
	});

	addEventHandlersForModalButtons();
});

function addEventHandlersForModalButtons() {
	confirmSuccessButton.click(function(e) {
		e.preventDefault();
		sendRequestToUpdateOrderStatus($(this));
	});

	cancelButton.click(function(e) {
		e.preventDefault();
		confirmModalDialog.modal('hide');
	});
}

function sendRequestToUpdateOrderStatus(button) {
	var requestURL = button.attr("href");

	$.ajax({
		type: 'POST',
		url: requestURL,
		beforeSend: function(xhr) {
			xhr.setRequestHeader(csrfHeaderName, csrfValue);
		}
	}).done(function(response) {
		showMessageModal("Trạng thái đơn hàng đã được cập nhật");
		updateStatusIconColor(response.orderId, response.status);

		console.log(response);
	}).fail(function(err) {
		showMessageModal("Có lỗi khi cập nhật trạng thái đơn hàng");
	});
}

function updateStatusIconColor(orderId, status) {
	var link = $("#link" + status + orderId);
	link.replaceWith("<i class='fas " + iconNames[status] + " fa-2x icon-green'></i>");
}

function showUpdateConfirmModal(link) {
	var orderId = link.attr("orderId");
	var status = link.attr("status");
	var requestURL = link.attr("href");

	confirmText.text("Xác nhận cập nhật trạng thái đơn hàng có ID #" + orderId +"?");

	confirmSuccessButton.attr("href", requestURL);
	confirmModalDialog.modal('show');
}

function showMessageModal(message) {
	confirmText.text(message);
	confirmSuccessButton.hide();
	cancelButton.text("Close");
	confirmModalDialog.modal('show');
}
