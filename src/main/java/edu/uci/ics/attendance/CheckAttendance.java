package edu.uci.ics.attendance;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.io.FileUtils;

import au.com.bytecode.opencsv.CSVReader;
import au.com.bytecode.opencsv.CSVWriter;

public class CheckAttendance {

	Map<String, Integer> studentIDs;
	private List<String> notecards;

	public CheckAttendance() {
	}

	public void check() throws IOException {
		loadStudentIDList();

		loadNotecards();

		updateStudentsAttendance();

		exportAttendance();
	}

	private void exportAttendance() throws IOException {
		CSVWriter writer = new CSVWriter(new FileWriter("attendance.csv"));

		for (Entry<String, Integer> student : studentIDs.entrySet()) {
			writer.writeNext(new String[] { student.getKey(), String.valueOf(student.getValue()) });
		}

		writer.close();
	}

	private void updateStudentsAttendance() {
		for (final String notecard : notecards) {
			Collection matchingStudents = CollectionUtils.select(studentIDs.keySet(), new Predicate() {

				@Override
				public boolean evaluate(Object object) {
					String student = (String) object;
					return student.contains(notecard);
				}
			});

			if (matchingStudents.isEmpty()) {
				System.out.println("EMPTY: " + notecard);
			} else if (matchingStudents.size() > 1) {
				System.out.println("AMBIGUOUS: " + notecard);
			} else {
				String studentID = matchingStudents.iterator().next().toString();
				studentIDs.put(studentID, studentIDs.get(studentID) + 1);
			}
		}
	}

	private void loadNotecards() throws IOException {
		notecards = FileUtils.readLines(new File("notecards.txt"));
	}

	private void loadStudentIDList() throws FileNotFoundException, IOException {
		studentIDs = new HashMap<>();
		CSVReader reader = new CSVReader(new FileReader("students.csv"));

		List<String[]> all = reader.readAll();

		for (String[] student : all) {
			studentIDs.put(student[0], 0);
		}
		reader.close();
	}
}
