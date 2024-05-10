var dropDownProvince;
var dataListDistrict;
var fieldDistrict;

$(document).ready(function() {
	dropDownProvince = $("#province");
	dataListDistrict = $("#listDistrict");
	fieldDistrict = $("#district");

	dropDownProvince.on("change", function() {
		loadDistrictForProvince();
		fieldDistrict.val("").focus();
	});


	$(".linkDelete").on("click", function (evt) {
		evt.preventDefault();
		showDeleteConfirmModal($(this), 'địa chỉ');
	});
});
function showDeleteConfirmModal(link, entityName) {
	entityId = link.attr("entityId");

	$("#confirmDelete").attr("href", link.attr("href"));
	$("#confirmText").text("Bạn có muốn xóa "
		+ entityName + " : " + entityId + "?");
	$("#confirmModal").modal();
}
function loadDistrictForProvince() {
	selectedProvince = $("#province option:selected");
	provinceId = selectedProvince.val();
	url = contextPath + "list_district_by_province/" + provinceId;

	$.get(url, function(responseJSON) {
		dataListDistrict.empty();

		$.each(responseJSON, function(index, district) {
			$("<option>").val(district.name).text(district.name).appendTo(dataListDistrict);
		});

	}).fail(function() {
		alert('Lỗi không kết nối được máy chủ!');
	});
}

function checkPasswordMatch(confirmPassword) {
	if (confirmPassword.value != $("#password").val()) {
		confirmPassword.setCustomValidity("Mật khẩu không trùng khớp!");
	} else {
		confirmPassword.setCustomValidity("");
	}
}

