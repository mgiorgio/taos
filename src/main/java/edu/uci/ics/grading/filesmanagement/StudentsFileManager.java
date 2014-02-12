package edu.uci.ics.grading.filesmanagement;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Collection;
import java.util.List;
import java.util.Properties;

import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.PrefixFileFilter;
import org.apache.commons.io.filefilter.TrueFileFilter;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.uci.ics.grading.model.Grader;
import edu.uci.ics.grading.model.Student;

public class StudentsFileManager {

	private static final Logger log = LoggerFactory.getLogger(StudentsFileManager.class);
	private static final Logger console = LoggerFactory.getLogger("console");

	private static final String FOLDER_NAME_IN_ZIP = "Files";
	private static final String GRADERS_ASSIGNMENTS_FOLDER = "assignments";
	private static final String ASSIGNMENTS_ZIP_FILENAME_KEY = "assignments.zip.filename";

	private Properties properties;

	public StudentsFileManager(Properties properties) {
		this.properties = properties;
	}

	public void separateFiles(List<Grader> graders) throws ZipException, IOException {
		log.info("Separating physical files...");
		String tmpFolder = uncompressFilesInTmpFolder();
		createFoldersForGraders(graders);
		copyAssignmentsToEachGraderFolder(graders, tmpFolder);
		manageEndOfTmpFolder(tmpFolder);
		log.info("Phyisical files were successfully separated.");
	}

	/**
	 * If there are remaining files in the TMP folder, a message is printed in
	 * the console and the folder is not deleted. Otherwise it is deleted.
	 * 
	 * @throws IOException
	 *             If the temporary folder cannot be deleted.
	 */
	private void manageEndOfTmpFolder(String tmpFolderName) throws IOException {
		File tmpFolder = new File(tmpFolderName);
		if (FileUtils.sizeOfDirectory(tmpFolder) > 0) {
			console.info("There are remaining files in " + tmpFolderName + ". Checking them is advised.");
		} else {
			FileUtils.deleteDirectory(tmpFolder);
			log.info("{} folder has been deleted..", tmpFolder);
		}
	}

	private void copyAssignmentsToEachGraderFolder(List<Grader> graders, String tmpFolder) throws IOException {
		for (Grader grader : graders) {
			int numberOfFilesMovedForGrader = 0;
			log.debug("Moving assignments for {}", grader.getName());
			File graderDirectory = new File(graderDirectory(grader));
			for (Student student : grader.getStudents()) {
				numberOfFilesMovedForGrader += moveStudentFiles(tmpFolder, graderDirectory, student);
			}
			log.info("{} files were moved to {}'s folder.", numberOfFilesMovedForGrader, grader.getName());
		}
	}

	/**
	 * Moves the assignments for a particular student from a folder to other
	 * one.
	 * 
	 * @param srcFolder
	 *            Original files location.
	 * @param graderDirectory
	 *            Where the student assignment files will be moved to.
	 * @param student
	 *            The student associated to the assignments to move.
	 * @return The number of files moved.
	 * @throws IOException
	 */
	private int moveStudentFiles(String srcFolder, File graderDirectory, Student student) throws IOException {
		int studentFiles = 0;
		/*
		 * Take all the documents starting with the student's UCINetID.
		 */
		if (!StringUtils.isEmpty(student.getUCINetID())) {
			Collection<File> filesPerStudent = FileUtils.listFiles(new File(srcFolder), new PrefixFileFilter(student.getUCINetID()), TrueFileFilter.INSTANCE);
			for (File file : filesPerStudent) {
				moveFile(file, graderDirectory);
				studentFiles++;
			}
		}
		return studentFiles;
	}

	private void moveFile(File srcFile, File graderDirectory) throws IOException {
		File destinationFile = new File(graderDirectory + "/" + srcFile.getName());
		if (srcFile.exists()) {
			log.trace("Moving file {}...", srcFile.getName());
			Files.move(srcFile.toPath(), destinationFile.toPath(), StandardCopyOption.ATOMIC_MOVE, StandardCopyOption.REPLACE_EXISTING);
		}
	}

	private String graderDirectory(Grader grader) {
		return GRADERS_ASSIGNMENTS_FOLDER + "/" + grader.getName();
	}

	private void createFoldersForGraders(List<Grader> graders) {
		for (Grader grader : graders) {
			File file = new File(graderDirectory(grader));
			file.mkdirs();
			log.debug("{} directory created.", file.getName());
		}
	}

	private String uncompressFilesInTmpFolder() throws ZipException {
		String tmpFolder = "tmpzipfolder";
		ZipFile zipFile = new ZipFile((String) properties.get(ASSIGNMENTS_ZIP_FILENAME_KEY));

		zipFile.extractAll(tmpFolder);

		return tmpFolder + "/" + FOLDER_NAME_IN_ZIP;
	}
}
