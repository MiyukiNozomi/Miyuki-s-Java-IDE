package com.miyukideveloper.ide.project;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import com.miyukideveloper.ide.MiyukiIDE;
import com.miyukideveloper.ide.compatibilites.Language;
import com.miyukideveloper.ide.explorer.Explorer;
import com.miyukideveloper.ide.systems.Warn;

public class ProjectManager {

	private MiyukiIDE miyukiIDE;
	private List<Project> projects = new ArrayList<Project>();
	private final String workspace;

	public ProjectManager(MiyukiIDE ide, String workspace) {
		this.workspace = workspace;
		this.miyukiIDE = ide;
	}

	public Explorer createProject(String name) {
		Project project = new Project(name, workspace);
		project.createProjectFolders();
		project.writeConfigFile();
		projects.add(project);

		return miyukiIDE.getExplorer().refresh();
	}

	public Explorer createFolder(String path, String name) {
		String tmpPath = path.replace("\\", "/");
		String[] ANP = tmpPath.split("/");

		String newPath = "";
		for (int i = 2; i < ANP.length; i++) {
			newPath += "\\" + ANP[i];
		}

		File file = new File(workspace + newPath + "\\" + name + "\\");
		System.out.println("FILE: " + file.getPath());
		file.mkdirs();

		return miyukiIDE.getExplorer().refresh();
	}

	public Explorer createFile(String model, String path, String name, String ext) {
		String tmpPath = path.replace("\\", "/");
		String[] ANP = tmpPath.split("/");

		String newPath = "";
		for (int i = 2; i < ANP.length; i++) {
			newPath += "\\" + ANP[i];
		}
		try {
			File file = new File(workspace + newPath + "\\" + name + ext + "\\");
			Path apath = Paths.get(file.getPath());
			System.out.println(apath.toString());
			byte[] m = model.getBytes();
			Files.write(apath, m);
			return miyukiIDE.getExplorer().refresh();
		} catch (Exception e) {
			Warn.launchError(Language.getLangKey("cannot_make_file"),"CANNOT_MAKE_FOLDER");
			return miyukiIDE.getExplorer().refresh();
		}
	}

	public Explorer createPackage(String path, String packge) {
		String apth = packge.replace(".", "\\");
		String tmpPath = path.replace("\\", "/");
		String[] ANP = tmpPath.split("/");

		String newPath = "";
		for (int i = 2; i < ANP.length; i++) {
			newPath += "\\" + ANP[i];
		}

		File file = new File(workspace + newPath + "\\" + apth + "\\");
		System.out.println("FILE: " + file.getPath());
		file.mkdirs();

		return miyukiIDE.getExplorer().refresh();
	}

	public void deleteProject(String name) {
		Warn.launchWarn(Language.getLangKey("warn_delete_project"), "Can't delete the project.");
	}

	public String getWorkspace() {
		return workspace;
	}
}
