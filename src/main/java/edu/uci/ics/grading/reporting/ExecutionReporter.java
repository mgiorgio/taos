package edu.uci.ics.grading.reporting;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import au.com.bytecode.opencsv.CSVWriter;
import edu.uci.ics.grading.model.Grader;
import edu.uci.ics.grading.model.Student;
import edu.uci.ics.grading.parsers.HTMLDropboxParser;
import edu.uci.ics.grading.sortstrategies.StudentsSorter;

public class ExecutionReporter {

	private static final Logger console = LoggerFactory.getLogger(ExecutionReporter.class);

	public ExecutionReporter() {
	}

	public void reportCollectedStudents(List<Student> students) {
	}

	public void reportGradersAssignment(List<Grader> graders) throws ReportException {
		try {
			this.createGradersFiles(graders);
		} catch (IOException e) {
			throw new ReportException(e);
		}
	}

	private void createGradersFiles(List<Grader> graders) throws IOException {
		for (Grader grader : graders) {
			createGraderFile(grader);
		}
		console.info("Output files were successfully created.");
	}

	private void createGraderFile(Grader grader) throws IOException {
		String filename = grader.getName() + ".csv";
		CSVWriter writer = new CSVWriter(new FileWriter(filename), ',');

		console.info("Creating " + filename + " file...");

		writer.writeNext(new String[] { "Student ID #", "Name", "UCINetID", "Score", "Comment", "Submitted Late?", "File names" });

		List<Student> studentsPerGrader = grader.getStudents();

		StudentsSorter.sortAlphabeticallyByUCINetID(studentsPerGrader);

		for (Student student : studentsPerGrader) {
			writer.writeNext(this.generateArrayForStudentEntry(student));
		}
		writer.close();
	}

	private String[] generateArrayForStudentEntry(Student student) {
		Map<String, String> studentInfo = student.getInfo();
		String[] elements = new String[7];

		boolean submittedLate = studentInfo.containsKey(HTMLDropboxParser.SUBMITTED_LATE);

		elements[0] = student.getStudentID();
		elements[1] = student.getName();
		elements[2] = student.getUCINetID();
		elements[3] = "";
		elements[4] = "";
		if (submittedLate) {
			elements[5] = "Late";
		} else {
			elements[5] = "";
		}
		elements[6] = studentInfo.get(HTMLDropboxParser.ASSIGNMENT_FILENAME);

		return elements;
	}
}
