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



function ShowModal(i) {
			var modal = document.getElementById("myModal"+i);
			modal.style.display = "block";
		}
		
function CloseModal(i) {
	var modal = document.getElementById("myModal"+i);
	modal.style.display = "none";
}

