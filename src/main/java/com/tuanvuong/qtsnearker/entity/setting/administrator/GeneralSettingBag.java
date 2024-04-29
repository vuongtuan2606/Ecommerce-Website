package com.tuanvuong.qtsnearker.entity.setting.administrator;

 import com.tuanvuong.qtsnearker.entity.setting.Setting;
import com.tuanvuong.qtsnearker.entity.setting.SettingBag;

import java.util.List;

public class GeneralSettingBag extends SettingBag {
	public GeneralSettingBag(List<Setting> listSettings) {
		super(listSettings);
	}
	public void updateCurrencySymbol(String value) {
		super.update("CURRENCY_SYMBOL", value);
	}

	public void updateSiteLogo(String value) {
		super.update("SITE_LOGO", value);
	}
}
