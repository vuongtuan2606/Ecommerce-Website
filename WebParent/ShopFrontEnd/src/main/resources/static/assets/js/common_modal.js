function showModalDialog(title, message) {
	$("#modalTitle").text(title);
	$("#modalBody").text(message);
	$('#errorModal').modal('show');
}
function showErrorModal(message) {
	showModalDialog("Thông báo lỗi", message);
}

function showWarningModal(message) {
	showModalDialog("Cảnh báo", message);
}

function showModalAddNotification(title, message){
	$("#textTitle").text(title);
	$("#textBody").text(message);
	$('.modal-add-notifacition').fadeTo(300, 1);
	$('.modal-add-notifacition').css({'opacity': "1", "visibility":"visible"}).delay(2000).fadeTo(300, 0);
}