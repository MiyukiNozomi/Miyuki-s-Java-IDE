package com.miyukideveloper.ide.blue;

import java.nio.file.Files;
import java.nio.file.Path;

public class BlueLanguage {
	
	private String[] source;
	
	public BlueLanguage(Path path) {
		try {
			String file = new String(Files.readAllBytes(path));
			source = file.split(",");
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public String getKey(String key) {
		if(source == null) {
			System.err.println("Something went wrong in Constructor.");
			System.err.println("Contact Miyuki#6838 in discord.");
			return "nullptr";	
		}	
		for(String s : source) {
			if(s.startsWith("//"))
				continue;
			String thisKey = s.substring(0,s.indexOf("=["));
			System.out.println("Current key: " + thisKey + " Key: " + key);
			if(thisKey.equals(key)) {
				String result = s.substring(s.indexOf("[") + 1,s.lastIndexOf("]"));
				return result;
			}
		}
		
		throw new IllegalArgumentException("Unknown Key: " + key);
	}
	
	public boolean containsKey(String key) {
		if(source == null) {
			System.err.println("Something went wrong in Constructor.");
			System.err.println("Contact Miyuki#6838 in discord.");
			return false;
		}
		for(String s : source) {
			if(s.startsWith("//"))
				continue;
			String thisKey = s.substring(0,s.indexOf("=["));
			System.out.println("Current key: \"" + thisKey + "\" Key: \"" + key + "\"");
			if(key.contentEquals(thisKey)) {
				System.out.println("have key.");
				return true;
			}
		}
		return false;
	}
	
}
