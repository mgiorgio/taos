package edu.uci.ics.grading.parsers;

import java.io.IOException;
import java.io.Reader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.uci.ics.grading.StudentDatabase;
import edu.uci.ics.grading.model.Student;
import edu.uci.ics.grading.studentdatacollection.StudentDataCollaborator;
import edu.uci.ics.grading.studentdatacollection.StudentDataException;

public abstract class PatternBasedCollaborator implements StudentDataCollaborator {

	private static final Logger log = LoggerFactory.getLogger(PatternBasedCollaborator.class);

	public PatternBasedCollaborator() {
	}

	protected void parseStudents(StudentDatabase database, Reader reader) throws ParsingException {
		try {
			String rawContent = IOUtils.toString(reader);

			Pattern recordPattern = createPattern();

			Matcher matcher = createMatcher(rawContent, recordPattern);

			while (matcher.find()) {
				Student student = processMatcherFind(matcher);
				log.trace("Providing data for student {}", student);
				database.update(student);
			}
		} catch (IOException e) {
			throw new ParsingException(e);
		}
	}

	protected abstract Student processMatcherFind(Matcher matcher);

	protected abstract Pattern createPattern();

	protected Matcher createMatcher(String rawContent, Pattern recordPattern) {
		return recordPattern.matcher(rawContent);
	}

	public void collaborate(StudentDatabase database, Reader reader) throws StudentDataException {
		try {
			log.info("Invoking {} collaborator...", this.getClass().getName());
			this.parseStudents(database, reader);
		} catch (ParsingException e) {
			throw new StudentDataException(e);
		}
	}

}
