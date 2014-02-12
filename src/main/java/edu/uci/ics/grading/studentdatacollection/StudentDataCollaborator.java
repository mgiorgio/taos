package edu.uci.ics.grading.studentdatacollection;

import java.io.Reader;

import edu.uci.ics.grading.StudentDatabase;

public interface StudentDataCollaborator {

	public void collaborate(StudentDatabase database, Reader reader) throws StudentDataException;
}
