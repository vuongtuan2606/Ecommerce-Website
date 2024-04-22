
	var dropdownBrands = $("#brand");
	var dropdownCategories = $("#category");

	$(document).ready(function() {

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
		// Lấy trường input có id là "categoryId"
		var catIdField = $("#categoryId");

		// Kiểm tra xem form có trong chế độ chỉnh sửa hay không
		var editMode = catIdField.length > 0;

		// Nếu không phải là chế độ chỉnh sửa, gọi hàm getCategories() để lấy danh mục
		if (!editMode) {
			getCategories();
		}
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

