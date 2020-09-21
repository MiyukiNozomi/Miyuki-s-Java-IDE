package com.miyukideveloper.ide.explorer;

import javax.swing.ImageIcon;

public class ExplorerObjects {
	
	/**
	 * @author Shinoa
	 */
	
	public static class Folder {
		public String name;
		public String path;
		public ImageIcon icon;
		public FolderType type;
		
		public Folder(String name,String path,ImageIcon icon,FolderType type) {
			this.name = name;
			this.path = path;
			this.icon = icon;
			this.type = type;
		}
	}
	
	public static class EFile {
		
		public String name;
		public String path;
		
		public EFile(String name,String path) {
			this.name = name;
			this.path = path;
		}
	}
	
	public enum FolderType {
		NORMAL,
		PROJECT,
		SRC_FOLDER,
		BIN_FOLDER,
		PACKAGE
	}
	
}
