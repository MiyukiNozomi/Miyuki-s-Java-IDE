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
import com.miyukideveloper.ide.helpers.MUtils;
import com.miyukideveloper.ide.systems.Warn;

public class ProjectManager {

	private MiyukiIDE miyukiIDE;
	private List<Project> projects = new ArrayList<Project>();
	private final String workspace;

	public ProjectManager(MiyukiIDE ide, String workspace) {
		this.workspace = workspace;
		this.miyukiIDE = ide;
	}

	public Explorer createProject(String name,String mainClassPackageName) {
		Project project = new Project(name, workspace);
		project.createProjectFolders();
		project.writeConfigFile();
		project.writeBuildAnt(mainClassPackageName);
		projects.add(project);

		return new Explorer(miyukiIDE);
	}

	public Explorer createFolder(String path, String name) {
		File file = new File(path + MUtils.FSLASH +  name  + MUtils.FSLASH);
		System.out.println("FILE: " + file.getPath());
		file.mkdirs();

		return new Explorer(miyukiIDE);
	}

	public Explorer createFile(String model, String path, String name, String ext) {
		try {
			File file = new File(path + MUtils.FSLASH + name + ext + MUtils.FSLASH );
			Path apath = Paths.get(file.getPath());
			System.out.println(apath.toString());
			byte[] m = model.getBytes();
			Files.write(apath, m);
			return new Explorer(miyukiIDE);
		} catch (Exception e) {
			Warn.launchError(Language.getLangKey("cannot_make_file"),"CANNOT_MAKE_FOLDER");
			return new Explorer(miyukiIDE);
		}
	}

	public Explorer createPackage(String path, String packge) {
		String apth = packge.replace(".", "\\");

		File file = new File(path + MUtils.FSLASH + apth + MUtils.FSLASH);
		System.out.println("FILE: " + file.getPath());
		file.mkdirs();

		return new Explorer(miyukiIDE);
	}

	public void deleteProject(String name) {
		Warn.launchWarn(Language.getLangKey("warn_delete_project"), "Can't delete the project.");
	}

	public String getWorkspace() {
		return workspace;
	}
}
