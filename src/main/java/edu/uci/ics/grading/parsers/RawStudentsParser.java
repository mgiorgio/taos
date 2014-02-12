package edu.uci.ics.grading.parsers;

import java.io.Reader;
import java.util.List;

import edu.uci.ics.grading.model.Student;

public interface RawStudentsParser {

	public List<Student> parseStudents(Reader reader) throws ParsingException;
}
