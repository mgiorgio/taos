package edu.uci.ics.grading;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import edu.uci.ics.grading.model.Student;

public class StudentDatabase {

	private List<Student> students;

	public StudentDatabase() {
		this.students = new LinkedList<Student>();
	}

	public int size() {
		return this.students.size();
	}

	public void update(Student student) {
		Student oldStudent = this.retrieveStudent(student);

		this.mergeStudents(oldStudent, student);
	}

	protected void mergeStudents(Student originalStudent, Student student) {
		if (!StringUtils.isEmpty(student.getName())) {
			originalStudent.setName(student.getName());
		}
		if (!StringUtils.isEmpty(student.getStudentID())) {
			originalStudent.setStudentID(student.getStudentID());
		}
		if (!StringUtils.isEmpty(student.getUCINetID())) {
			originalStudent.setUCINetID(student.getUCINetID());
		}
		originalStudent.putInfo(student.getInfo());
	}

	protected Student retrieveStudent(Student student) {
		for (Student eachStudent : this.students) {
			if (eachStudent.equals(student)) {
				return eachStudent;
			}
		}
		Student newStudent = new Student();
		this.students.add(newStudent);
		return newStudent;
	}

	public List<Student> getStudents() {
		return new ArrayList<>(this.students);
	}
}
