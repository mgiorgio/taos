package edu.uci.ics.grading.sortstrategies;

import java.util.Collections;
import java.util.List;

import edu.uci.ics.grading.model.Student;

public class StudentsSorter {

	public static void sortAlphabeticallyByName(List<Student> students) {
		Collections.sort(students, new StudentNameComparator());
	}

	public static void sortAlphabeticallyByUCINetID(List<Student> students) {
		Collections.sort(students, new UCINetIDComparator());
	}
}
