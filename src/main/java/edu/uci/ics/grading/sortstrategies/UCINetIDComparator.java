package edu.uci.ics.grading.sortstrategies;

import java.util.Comparator;

import edu.uci.ics.grading.model.Student;

public class UCINetIDComparator implements Comparator<Student> {

	public UCINetIDComparator() {
	}

	@Override
	public int compare(Student o1, Student o2) {
		if (o1.getUCINetID() == null) {
			return -1;
		}
		if (o2.getUCINetID() == null) {
			return 1;
		}
		return o1.getUCINetID().compareToIgnoreCase(o2.getUCINetID());
	}
}
