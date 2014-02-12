/**
 * 
 */
package edu.uci.ics.grading.parsers;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import edu.uci.ics.grading.model.Student;

/**
 * @author Matías
 * 
 */
public class HTMLGradebookCollaborator extends PatternBasedCollaborator {

	/**
	 * 
	 */
	public HTMLGradebookCollaborator() {
	}

	@Override
	protected Student processMatcherFind(Matcher matcher) {
		Student student = new Student();
		String studentID = matcher.group(1);
		String name = matcher.group(2);

		student.setStudentID(studentID);
		student.setName(name);

		return student;
	}

	@Override
	protected Pattern createPattern() {
		return Pattern.compile("<tr.*?>.*?<td>(\\d+)</td>.*?<td>(.*?)</td>.*?</tr>", Pattern.DOTALL);
	}

}
