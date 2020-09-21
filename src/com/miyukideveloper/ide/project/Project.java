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

	public void writeBuildAnt(String mainClassPackageName) {
		try {
			Path path = Paths.get(workspace + projectName + "/build.xml");
			byte[] buildbytes = getBuildXML(mainClassPackageName).getBytes();
			Files.write(path, buildbytes);
		}catch(Exception e) {
			Warn.launchError(Language.getLangKey("invalid_project_config"),e.getMessage());
		}
	}
	
	private String getBuildXML(String maincpkg) {
		String result = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n" + 
				"\r\n" + 
				"<project name=\"" + projectName + "\" default=\"run\" basedir=\".\">\r\n" + 
				"\r\n" + 
				"   <description>Project Made with MiyukiDeveloper's Java IDE</description>\r\n" + 
				"\r\n" + 
				"   <target name=\"compile\"\r\n" + 
				"\r\n" + 
				"           description=\"Compile the Java code.\">\r\n" + 
				"\r\n" + 
				"      <javac srcdir=\"src\"\r\n" + 
				"\r\n" + 
				"             destdir=\"classes\"\r\n" + 
				"\r\n" + 
				"             debug=\"true\"\r\n" + 
				"\r\n" + 
				"      includeantruntime=\"false\" />\r\n" + 
				"\r\n" + 
				"   </target>\r\n" + 
				"\r\n" + 
				"   <target name=\"run\" depends=\"compile\"\r\n" + 
				"\r\n" + 
				"           description=\"Run the Java application.\">\r\n" + 
				"\r\n" + 
				"      <java classname=\"" + maincpkg + "\" fork=\"true\">\r\n" + 
				"\r\n" + 
				"         <classpath>\r\n" + 
				"\r\n" + 
				"           <pathelement path=\"classes\"/>\r\n" + 
				"\r\n" + 
				"         </classpath>\r\n" + 
				"\r\n" + 
				"      </java>\r\n" + 
				"\r\n" + 
				"   </target>\r\n" + 
				"\r\n" + 
				"</project>";
		
		return result;
	}
}
