package com.miyukideveloper.ide.project;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.miyukideveloper.ide.compatibilites.Language;
import com.miyukideveloper.ide.systems.Warn;

public class Project {

	private String projectName;
	private String workspace;
	private static final String configFile = "projectName= %name%\r\n" + "editor_version= 1.0.3\r\n"
			+ "file_system= ";

	public Project(String projectName, String workspace) {
		this.projectName = projectName;
		this.workspace = workspace;
	}

	public String getProjectName() {
		return projectName;
	}
	
	public void createProjectFolders() {
		try {
			File folder = new File(workspace + projectName + "/src/");
			folder.mkdirs();
			folder = new File(workspace + projectName + "/bin/");
			folder.mkdirs();
			folder = new File(workspace + projectName + "/lib/");
			folder.mkdirs();
		}catch(Exception e) {
			Warn.launchError(Language.getLangKey("unable_project_mkdirs"), e.getMessage());
		}
	}

	public void writeConfigFile() {
		try {
			Path path = Paths.get(workspace + projectName + "/config.red");
			String d = configFile.replace("%name%", projectName);
			byte[] configbytes = d.getBytes();
			Files.write(path,configbytes);
		} catch (Exception e) {
			Warn.launchError(Language.getLangKey("invalid_project_config"), e.getMessage());
		}
	}
}
