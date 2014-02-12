/**
 * 
 */
package edu.uci.ics.grading.sortstrategies;

import java.util.Comparator;

import edu.uci.ics.grading.model.Student;

/**
 * @author mgiorgio
 * 
 */
public class StudentNameComparator implements Comparator<Student> {

	public StudentNameComparator() {
	}

	@Override
	public int compare(Student o1, Student o2) {
		return o1.getName().compareToIgnoreCase(o2.getName());
	}
}
