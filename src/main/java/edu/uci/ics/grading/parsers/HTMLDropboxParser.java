package edu.uci.ics.grading.parsers;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.uci.ics.grading.model.Student;

public class HTMLDropboxParser extends PatternBasedCollaborator {

	private static final Logger log = LoggerFactory.getLogger(HTMLDropboxParser.class);

	public static final String SUBMITTED_LATE = "Submitted Late";
	public static final String ASSIGNMENT_FILENAME = "assignmentFilename";

	public HTMLDropboxParser() {
	}

	@Override
	protected Student processMatcherFind(Matcher matcher) {
		String eachUCINetID = matcher.group(1);
		String eachName = matcher.group(2);

		Student student = new Student(null, eachUCINetID, eachName);

		String filename = matcher.group(3);
		String submittedLateString = matcher.group(4);
		boolean submittedLate = submittedLateString.contains("submitted_late");
		if (filename != null) { // Includes filename.
			student.appendInfo(ASSIGNMENT_FILENAME, filename);

			if (submittedLate) { // Submitted late.
				student.putInfo(SUBMITTED_LATE, Boolean.TRUE.toString());
			}
		}

		return student;
	}

	protected Pattern createPattern() {
		return Pattern
				.compile(
						"<tr class='mydrive_body.'>.*?<td.*?>.*?</td>.*?<td.*?>.*?<span class='alias'><a.*?>(.*?)</a></span>.*?<small class='name'>(.*?)</small>.*?</td>.*?<td.*?>.*?(?:<a.*?>(.*?)</a>.*?)?</td>.*?<td.*?>(.*?)</td>.*?<td.*?>.*?</td>.*?</tr>",
						Pattern.DOTALL);
	}

}
