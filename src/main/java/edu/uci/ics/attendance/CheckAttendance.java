package edu.uci.ics.attendance;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
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

	public static void main(String[] args) throws IOException {
		CheckAttendance attendance = new CheckAttendance();

		attendance.check();
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
		List<String> ambiguous = checkPartialNumbers(notecards);
		System.out.println("First pass:");
		printAmbiguous(ambiguous);
		ambiguous = checkPartialNumbers(ambiguous);
		System.out.println("Second pass:");
		printAmbiguous(ambiguous);
	}

	private void printAmbiguous(List<String> ambiguous) {
		for (String each : ambiguous) {
			StringBuilder builder = new StringBuilder();
			builder.append(each).append(": ");
			for (Iterator iterator = CollectionUtils.select(this.studentIDs.entrySet(), new PartialNumberPredicate(each)).iterator(); iterator.hasNext();) {
				Entry<String, Integer> student = (Entry<String, Integer>) iterator.next();

				builder.append(student.getKey()).append(",");
			}
			System.out.println(builder.toString());
		}
	}

	private class PartialNumberPredicate implements Predicate {

		private String partialNumber;

		public PartialNumberPredicate(String partialNumber) {
			this.partialNumber = partialNumber;
		}

		@Override
		public boolean evaluate(Object object) {
			Entry<String, Integer> student = (Entry<String, Integer>) object;
			return student.getKey().contains(this.partialNumber) && student.getValue() == 0;
		}
	}

	private List<String> checkPartialNumbers(List<String> partialNumbers) {
		List<String> ambiguous = new LinkedList<>();
		for (final String notecard : partialNumbers) {
			Collection matchingStudents = CollectionUtils.select(studentIDs.entrySet(), new PartialNumberPredicate(notecard));

			if (matchingStudents.isEmpty()) {
				System.out.println("EMPTY: " + notecard);
			} else if (matchingStudents.size() > 1) {
				ambiguous.add(notecard);
			} else {
				String studentID = ((Entry<String, Integer>) matchingStudents.iterator().next()).getKey();
				studentIDs.put(studentID, studentIDs.get(studentID) + 1);
			}
		}
		return ambiguous;
	}

	private void loadNotecards() throws IOException {
		notecards = FileUtils.readLines(new File("notecards.txt"));
	}

	private void loadStudentIDList() throws FileNotFoundException, IOException {
		studentIDs = new LinkedHashMap<>();
		CSVReader reader = new CSVReader(new FileReader("students.csv"));

		List<String[]> all = reader.readAll();

		for (String[] student : all) {
			studentIDs.put(student[0], 0);
		}
		reader.close();
	}
}
