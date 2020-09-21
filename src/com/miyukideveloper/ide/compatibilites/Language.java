package com.miyukideveloper.ide.compatibilites;

import java.util.ResourceBundle;

import com.miyukideveloper.ide.systems.Warn;

public class Language {
	
	private static ResourceBundle LANG_FILE;
	
	public static void init() {
		LANG_FILE = ResourceBundle.getBundle("com.miyukideveloper.ide.language");
	}
	
	public static String getLangKey(String key) {
		if(LANG_FILE.containsKey(key)) {
			return LANG_FILE.getString(key);
		}else {
			Warn.launchError("Can't load Language File", "ERROR: INVALID LANGUAGE FILE");
			return "Error: can't find key: " + key; 
		}
	}
	
}
