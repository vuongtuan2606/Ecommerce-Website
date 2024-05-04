package com.qtsneaker.common.entity.setting;

import java.util.List;

public class SettingBag {
	private List<Setting> listSettings;

	// constructor
	public SettingBag(List<Setting> listSettings) {
		this.listSettings = listSettings;
	}


	// Lấy đối tượng Setting từ List listSettings dựa trên khóa key
	public Setting get(String key) {
		// Tìm kiếm chỉ mục của đối tượng Setting trong danh sách có khóa key tương ứng
		int index = listSettings.indexOf(new Setting(key));
		// Nếu chỉ mục được tìm thấy
		if (index >= 0) {
			// Trả về đối tượng Setting tại vị trí chỉ mục tìm được
			return listSettings.get(index);
		}
		// Nếu không tìm thấy đối tượng Setting nào có khóa key tương ứng, trả về null
		return null;
	}


	// Lấy giá trị của một cài đặt từ danh sách listSettings dựa trên khóa key
	public String getValue(String key) {
		// Lấy đối tượng Setting từ danh sách listSettings bằng phương thức get
		Setting setting = get(key);
		// Kiểm tra xem đối tượng setting có tồn tại không
		if (setting != null) {
			// Nếu có, trả về giá trị của setting
			return setting.getValue();
		}
		// Nếu không, trả về null
		return null;
	}

	// Cập nhật giá trị của Setting trong danh sách listSettings dựa trên khóa key
	public void update(String key, String value) {
		// Lấy đối tượng Setting từ danh sách listSettings bằng phương thức get
		Setting setting = get(key);
		// Kiểm tra xem đối tượng setting có tồn tại và giá trị mới value không là null
		if (setting != null && value != null) {
			// Nếu có, cập nhật giá trị của setting thành giá trị mới
			setting.setValue(value);
		}
	}


	public List<Setting> list() {
		return listSettings;
	}
	
}
