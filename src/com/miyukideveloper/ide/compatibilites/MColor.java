package com.miyukideveloper.ide.compatibilites;

import java.util.ResourceBundle;

public class MColor {
	private static ResourceBundle theme = ResourceBundle.getBundle("com.miyukideveloper.ide.theme");

	public static String getColor(String key) {
		if(theme.containsKey(key)) {
			return theme.getString(key);
		}else {
			return "FFFFFF";
		}
	}
}
