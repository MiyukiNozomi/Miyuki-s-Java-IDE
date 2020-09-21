package com.miyukideveloper.ide.helpers;

import java.io.File;

import javax.swing.ImageIcon;

public class MUtils {
	
	public static final String FSLASH = File.separator;
			
	public static boolean isNumber(String str) {
		if(str == null || str.length() < 0) {
			return false;
		}
		
		for(char d : str.toCharArray()) {
			if(!Character.isDigit(d)) {
				return true;
			}
		}
		return false;
	}

	public static ImageIcon openImg(String string) {
		return new ImageIcon(MUtils.class.getResource("/com/miyukideveloper/ide/images/" + string + ".png"));
	}

	public static ImageIcon openGif(String string) {
		return new ImageIcon(MUtils.class.getResource("/com/miyukideveloper/ide/images/" + string + ".gif"));
	}
}
