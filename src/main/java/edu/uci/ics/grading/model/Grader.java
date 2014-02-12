package edu.uci.ics.grading.model;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Grader {

	private String name;

	private List<Student> students;

	public Grader() {
		this.students = new LinkedList<Student>();
	}

	public List<Student> getStudents() {
		return new ArrayList<>(this.students);
	}

	public void addStudent(Student student) {
		this.students.add(student);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return this.getName();
	}
}
