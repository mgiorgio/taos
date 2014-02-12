/**
 * 
 */
package edu.uci.ics.grading.gradersassignment.strategies;

import java.util.List;

import edu.uci.ics.grading.gradersassignment.GradersAssigner;
import edu.uci.ics.grading.model.Grader;
import edu.uci.ics.grading.model.Student;

/**
 * @author mgiorgio
 * 
 */
public class SequentialAssigner implements GradersAssigner {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.uci.informatics.inf43.assigningstrategies.GradersAssigner#assignGraders
	 * (java.util.List, java.util.List)
	 */
	@Override
	public void assignGraders(List<Student> students, List<Grader> graders) {
		int studentsPerGrader = (int) Math.floor(students.size() / graders.size());

		// Distribute students.
		for (int j = 0; j < graders.size(); j++) {
			for (int i = 0; i < studentsPerGrader; i++) {
				Student student = students.get(studentsPerGrader * j + i);
				graders.get(j).addStudent(student);
			}
		}

		// Distribute the rest.
		for (int i = 0; i < students.size() - graders.size() * studentsPerGrader; i++) {
			Student student = students.get(students.size() - i - 1);
			graders.get(i).addStudent(student);
		}
	}

	@Override
	public String name() {
		return "Sequential";
	}

}
