package edu.uci.ics.grading.model;

import java.util.HashMap;
import java.util.Map;

/**
 * A UCI Student.
 * 
 * Two UCI Students are equal if they have the same student ID, name or
 * UCINetID.
 * 
 * @author mgiorgio
 * 
 */
public class Student {

	public static final String DEFAULT_INFO_SEPARATOR = ",";
	private String studentID;
	private String UCINetID;
	private String name;
	private Map<String, String> info;

	public Student() {
		this(null, null, null);
	}

	public Student(String studentID, String UCINetID, String name) {
		this.studentID = studentID;
		this.UCINetID = UCINetID;
		this.name = name;
		this.info = new HashMap<String, String>();
	}

	public String getUCINetID() {
		return UCINetID;
	}

	public void setUCINetID(String uCINetID) {
		UCINetID = uCINetID;
	}

	public Map<String, String> getInfo() {
		return new HashMap<String, String>(this.info);
	}

	public void putInfo(Map<String, String> newInfo) {
		this.info.putAll(newInfo);
	}

	public void putInfo(String key, String value) {
		this.info.put(key, value);
	}

	public String getInfo(String key) {
		return this.info.get(key);
	}

	public void appendInfo(String key, String value) {
		this.appendInfo(key, value, DEFAULT_INFO_SEPARATOR);
	}

	public void appendInfo(String key, String value, String separator) {
		String existingInfoForThisKey = this.info.get(key);

		if (existingInfoForThisKey != null) {
			StringBuilder builder = new StringBuilder(existingInfoForThisKey.length() + value.length() + separator.length());
			builder.append(existingInfoForThisKey).append(separator).append(value);
			value = builder.toString();
		}
		this.putInfo(key, value);
	}

	@Override
	public String toString() {
		return this.name + "(" + this.UCINetID + ")" + " #" + this.studentID + " " + this.info.values();
	}

	public String getStudentID() {
		return studentID;
	}

	public void setStudentID(String studentID) {
		this.studentID = studentID;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public int hashCode() {
		/*
		 * Although this hashcode implementation has the worst possible
		 * performance for hashing algorithms, we cannot dinamically calculate
		 * it because of the nature of the equality.
		 */
		return 1;
	}

	@Override
	public boolean equals(Object obj) {
		Student sndStudent = (Student) obj;
		if (obj == null) {
			return false;
		}

		if (this.getName() != null && sndStudent.getName() != null && this.getName().equalsIgnoreCase(sndStudent.getName())) {
			return true;
		}
		if (this.getUCINetID() != null && sndStudent.getUCINetID() != null && this.getUCINetID().equalsIgnoreCase(sndStudent.getUCINetID())) {
			return true;
		}
		if (this.getStudentID() != null && sndStudent.getStudentID() != null && this.getStudentID().equals(sndStudent.getStudentID())) {
			return true;
		}

		return false;
	}
}
