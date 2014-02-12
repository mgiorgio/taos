/**
 * 
 */
package edu.uci.ics.grading;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

import net.lingala.zip4j.exception.ZipException;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.uci.ics.grading.filesmanagement.StudentsFileManager;
import edu.uci.ics.grading.gradersassignment.GradersAssigner;
import edu.uci.ics.grading.gradersassignment.strategies.AssigningStrategies;
import edu.uci.ics.grading.model.Grader;
import edu.uci.ics.grading.model.Student;
import edu.uci.ics.grading.reporting.ExecutionReporter;
import edu.uci.ics.grading.reporting.ReportException;
import edu.uci.ics.grading.studentdatacollection.StudentsDataCollector;

/**
 * @author Matías
 * 
 */
public class GradingToolLauncher {

	private static final Logger log = LoggerFactory.getLogger(GradingToolLauncher.class);
	private static final Logger console = LoggerFactory.getLogger("console");

	private static final String GRADERS_NAMES_KEY = "graders.names";
	private static final String CONFIG_PROPERTIES_DEFAULT_FILENAME = "cfg/config.properties";
	private static final String GRADERS_STRATEGY_KEY = "graders.strategy";
	private static Properties properties = new Properties();

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			loadProperties(args);

			StudentsDataCollector collector = new StudentsDataCollector();

			collector.initialize();

			collector.collect();

			List<Student> students = collector.getStudents();

			List<Grader> graders = extractGradersFromInputArguments();
			manageGradersAssignment(students, graders);

			managePhysicalFiles(graders);

			manageReporting(students, graders);
		} catch (Exception e) {
			System.err.println("Students list could not be obtained or graders could not be assigned: " + e.getMessage());
		}

	}

	private static void managePhysicalFiles(List<Grader> graders) throws ZipException, IOException {
		StudentsFileManager fileManager = new StudentsFileManager(properties);
		fileManager.separateFiles(graders);
	}

	private static void loadProperties(String[] args) throws FileNotFoundException, IOException {
		String filename = CONFIG_PROPERTIES_DEFAULT_FILENAME;
		if (args.length > 0) {
			filename = args[1];
		}
		log.info("Loading properties from {}", filename);
		properties.load(new FileReader(filename));
		log.info("Properties successfully loaded.");
	}

	private static void manageReporting(List<Student> students, List<Grader> graders) throws ReportException {
		ExecutionReporter reporter = new ExecutionReporter();

		reporter.reportCollectedStudents(students);
		reporter.reportGradersAssignment(graders);
	}

	private static void manageGradersAssignment(List<Student> students, List<Grader> graders) {
		console.info("The following graders will grade students: {}", StringUtils.join(graders, ", "));
		GradersAssigner gradersAssigner = selectAssigningStrategy(getConfig(GRADERS_STRATEGY_KEY, AssigningStrategies.random.name()));

		console.info("Assigning graders using " + gradersAssigner.name() + " strategy.");

		gradersAssigner.assignGraders(students, graders);

		printGradingAssignmentResult(graders);
	}

	private static void printGradingAssignmentResult(List<Grader> graders) {
		for (Grader grader : graders) {
			console.info("{} will grade {} Students.", grader.getName(), grader.getStudents().size());
		}
	}

	private static GradersAssigner selectAssigningStrategy(String assigningStrategy) {
		return AssigningStrategies.valueOf(assigningStrategy).createAssigner();
	}

	private static List<Grader> createGradersFromNames(List<String> graderNames) {
		List<Grader> graders = new LinkedList<Grader>();
		for (String graderName : graderNames) {
			Grader grader = new Grader();
			grader.setName(graderName);
			graders.add(grader);
		}
		return graders;
	}

	private static List<Grader> extractGradersFromInputArguments() {
		List<String> graderNames = Arrays.asList(StringUtils.split(getConfig(GRADERS_NAMES_KEY), ","));
		return createGradersFromNames(graderNames);
	}

	private static String getConfig(String key) {
		return getConfig(key, null);
	}

	private static String getConfig(String key, String defaultIfNull) {
		return properties.getProperty(key, defaultIfNull);
	}
}
