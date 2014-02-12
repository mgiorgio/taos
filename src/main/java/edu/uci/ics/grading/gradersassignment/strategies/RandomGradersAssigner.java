package edu.uci.ics.grading.gradersassignment.strategies;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import edu.uci.ics.grading.gradersassignment.GradersAssigner;
import edu.uci.ics.grading.model.Grader;
import edu.uci.ics.grading.model.Student;

public class RandomGradersAssigner implements GradersAssigner {

	@Override
	public void assignGraders(List<Student> students, List<Grader> graders) {
		Random random = new Random();
		List<Student> pickingUpStudents = new LinkedList<Student>(students);
		int studentsPerGrader = (int) Math.floor(students.size() / graders.size());

		// Distribute students randomly.
		for (int j = 0; j < graders.size(); j++) {
			for (int i = 0; i < studentsPerGrader; i++) {
				Student student = pickingUpStudents.remove(random.nextInt(pickingUpStudents.size()));
				graders.get(j).addStudent(student);
			}
		}

		// Distribute the rest.
		for (int i = 0; i < students.size() - graders.size() * studentsPerGrader; i++) {
			Student student = pickingUpStudents.remove(0);
			graders.get(i).addStudent(student);
		}
	}

	@Override
	public String name() {
		return "Random";
	}

}
