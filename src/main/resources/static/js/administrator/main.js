(function () {
	"use strict";

	var treeviewMenu = $('.app-menu');

	// Toggle Sidebar
	$('[data-toggle="sidebar"]').click(function(event) {
		event.preventDefault();
		$('.app').toggleClass('sidenav-toggled');
	});

	// Activate sidebar treeview toggle
	$("[data-toggle='treeview']").click(function(event) {
		event.preventDefault();
		if(!$(this).parent().hasClass('is-expanded')) {
			treeviewMenu.find("[data-toggle='treeview']").parent().removeClass('is-expanded');
		}
		$(this).parent().toggleClass('is-expanded');
	});

	// Set initial active toggle
	$("[data-toggle='treeview.'].is-expanded").parent().toggleClass('is-expanded');

	//Activate bootstrip tooltips
	$("[data-toggle='tooltip']").tooltip();

})();

function updateDateTime() {
	var currentDate = new Date();

	// Hiển thị ngày tháng năm
	var dateOptions = { weekday: 'long', year: 'numeric', month: 'long', day: 'numeric' };
	var formattedDate = currentDate.toLocaleDateString('en-US', dateOptions);
	document.getElementById('current-date').textContent = formattedDate;

	// Hiển thị thời gian
	var timeOptions = { hour: 'numeric', minute: 'numeric', second: 'numeric', hour12: true };
	var formattedTime = currentDate.toLocaleTimeString('en-US', timeOptions);
	document.getElementById('current-time').textContent = formattedTime;
}

// Cập nhật ngày giờ mỗi giây
setInterval(updateDateTime, 1000);

// Gọi hàm để hiển thị ngày giờ khi trang được tải
updateDateTime();

