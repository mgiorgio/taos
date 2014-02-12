package edu.uci.ics.grading.gradersassignment;

import java.util.List;

import edu.uci.ics.grading.model.Grader;
import edu.uci.ics.grading.model.Student;

public interface GradersAssigner {

	public void assignGraders(List<Student> students, List<Grader> graders);

	public String name();
}
