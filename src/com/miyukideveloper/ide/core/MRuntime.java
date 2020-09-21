package com.miyukideveloper.ide.core;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JTextArea;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;

import com.miyukideveloper.ide.explorer.ExplorerObjects;
import com.miyukideveloper.ide.explorer.ExplorerObjects.FolderType;

public class MRuntime {

	public static String JAVAC_HOME;

	public static void compile(JTextArea compilerOutput, String workspace, JTree tree, String mainClass)
			throws Exception {
		if (((DefaultMutableTreeNode) tree.getSelectionPath().getLastPathComponent())
				.getUserObject() instanceof ExplorerObjects.Folder) {
			if (((ExplorerObjects.Folder) ((DefaultMutableTreeNode) tree.getSelectionPath().getLastPathComponent())
					.getUserObject()).type == FolderType.PROJECT) {
				compilerOutput.setText("Ok, Starting Java Compiler.."
						+ "\n This can take Several Minutes. (the log will be displayed here.)");

				final ExplorerObjects.Folder s = ((ExplorerObjects.Folder) ((DefaultMutableTreeNode) tree
						.getSelectionPath().getLastPathComponent()).getUserObject());

				File file = new File(s.path + "\\src\\");

				List<File> files = new ArrayList<File>();
				getList(files, file);

				String javacs = "javac -d " + s.path + "\\bin\\";

				for (File f : files) {
					javacs += " " + f.getPath();
				}

				String javaCommand = "java " + mainClass + "\r\n";

				String copyNonSources = "";

				List<File> nonSourceFiles = new ArrayList<File>();
				getNonSourceFiles(nonSourceFiles, file);

				for (File fl : nonSourceFiles) {
					copyNonSources += "\r\n" + "copy " + fl.getPath() + " "
							+ fl.getPath().substring(0, fl.getPath().lastIndexOf("\\")).replace("src", "bin");
				}

				String batchFile = "@echo off\r\n" + "cd " + JAVAC_HOME + "\r\n" + "echo Starting...\r\n" + javacs
						+ "\r\n cd " + s.path + "\\bin\\" + copyNonSources + "\r\n cls\r\n" + javaCommand;

				File fil3 = new File(workspace + "temp\\");
				fil3.mkdir();
				Path path = Paths.get(workspace + "temp\\run.cmd");
				Files.write(path, batchFile.getBytes());

				Runtime runtime = Runtime.getRuntime();

				Process process = runtime.exec("cmd /c start " + workspace + "temp\\run.cmd");
				process.waitFor();
				compilerOutput
						.setText("The class: " + mainClass + " Has exited with the exit code: " + process.exitValue());

			} else {
				compilerOutput.setText("Invalid Selection, Selection must be a  Project.");
			}
		} else {
			compilerOutput.setText("Invalid Selection: "
					+ ((DefaultMutableTreeNode) tree.getSelectionPath().getLastPathComponent()).getUserObject());
		}
	}

	public static void getList(List<File> files, File f) {
		if (!f.isDirectory()) {
			if (f.getName().endsWith(".java")) {
				files.add(f);
			}
		} else {
			File fList[] = f.listFiles();
			for (int i = 0; i < fList.length; i++)
				getList(files, fList[i]);
		}
	}

	private static void getNonSourceFiles(List<File> list, File f) {
		if (f.exists()) {
			if (f.isDirectory()) {
				for (File g : f.listFiles()) {
					getNonSourceFiles(list, g);
				}
			} else if (!f.getName().endsWith(".java")) {
				list.add(f);
			}
		}
	}
}
