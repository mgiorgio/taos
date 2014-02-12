package edu.uci.ics.grading.studentdatacollection;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.uci.ics.grading.StudentDatabase;
import edu.uci.ics.grading.model.Student;
import edu.uci.ics.grading.parsers.HTMLDropboxParser;
import edu.uci.ics.grading.parsers.HTMLGradebookCollaborator;

public class StudentsDataCollector {

	private static final Logger log = LoggerFactory.getLogger(StudentsDataCollector.class);
	private static final Logger console = LoggerFactory.getLogger("console");

	private static final String GRADEBOOK_HTML = "gradebook.html";
	private static final String DROPBOX_HTML = "dropbox.html";
	private StudentDatabase database;

	public StudentsDataCollector() {
	}

	public void initialize() {
		this.database = new StudentDatabase();
	}

	public void collect() throws StudentDataException {
		try {
			processRawContentFromEEE();
		} catch (IOException e) {
			throw new StudentDataException(e);
		}
	}

	public List<Student> getStudents() {
		return database.getStudents();
	}

	private void processRawContentFromEEE() throws StudentDataException, IOException {
		StudentDataCollaborator dropboxCollaborator = new HTMLDropboxParser();
		StudentDataCollaborator gradebookCollaborator = new HTMLGradebookCollaborator();

		dropboxCollaborator.collaborate(database, new StringReader(getFileContent(DROPBOX_HTML)));
		log.info("Students database contains {} students.", database.size());
		gradebookCollaborator.collaborate(database, new StringReader(getFileContent(GRADEBOOK_HTML)));
		log.info("Students database contains {} students.", database.size());
		
		console.info("{} students have been found and will be processed.", database.size());
	}

	private static String getFileContent(String filename) throws IOException {
		log.debug("Getting file contents from {}", filename);
		File file = new File(filename);
		FileReader reader = new FileReader(filename);
		char[] fileContents = new char[(int) file.length()];
		reader.read(fileContents);

		reader.close();

		return new String(fileContents);
	}
}
