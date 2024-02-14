package com.zensar.utilities;

import java.io.File;

public interface StringLiterals {

	int PAGE_IMPLICIT_WAIT = 60;
	String VAR = "####";
	String VAR2 = "####";
	String PROJECT_NAME = "AmazonLogin";
	String AWS_IP = "172";

	String DESCRIPTION = "AmazonLogin - Executions";

	String UNFOLLOW_LOT = "Unfollow lot";
	String FOLLOW_LOT = "Follow lot";

	String PLACE_BID = "Place Bid";

	String authorName = "Automation Team";

	String BASE_FOLDER_PATH = "support_files";

	String PROPERTIES_FILES_PATH = BASE_FOLDER_PATH + "";
	String EXCEL_FILES_PATH = BASE_FOLDER_PATH + "";

	String UPLOAD_FILES_PATH = BASE_FOLDER_PATH + "/upload_files";

	String SCREENSHOTS_BASE_PATH = BASE_FOLDER_PATH + "/output/screenshots";

	String ENVIRONMENT_PROPERTIES_FODER_NAME = "environment_properties";

	String TEST_DATA_WORKBOOK_PATH = BASE_FOLDER_PATH + "/TestData1.xls";
	String INPUT_PROPERTY_SHEET_NAME = "ExecutionProperties";

	String OUTPUT_EXCEL_TEMPLATE_FILE = BASE_FOLDER_PATH + "/OutputTemplate.xls";
	String OUTPUT_EXCEL_FILE = "OutputData.xls";
	String EXECUTION_PROPERTIES_EXCEL_PATH = BASE_FOLDER_PATH + "/ExecutionProperties.xls";
	String EXECUTION_PROPERTIES_JENKINS_EXCEL_PATH = BASE_FOLDER_PATH + "/Jenkins/" + VAR
			+ "/ExecutionPropertiesAuto.xls";
	String MULTI_DATA_TEMPLATE = BASE_FOLDER_PATH + "/MultiData.xls";
	String MULTI_DATA_OUTPUT = "MultiDataOutput.xls";

	String REPORTS_PATH = "Reports";
	String GSPEC_BASE_PATH = BASE_FOLDER_PATH + "/gspecs";

	String DEFAULT_OUTPUT_SHEET_NAME = "OutputData";
	String ERROR_MSSGE_3Step = "Please enter characters from the Latin alphabet only.";

	String ERROR_MSSGE_3Step_EMAIL = "Please provide a valid Email address";
	String EMAIL_1 = "plk.test.@.test.com";
	String EMAIL_2 = "''@yopmail.com ";
	String EMAIL_3 = "$test-$-test@yopmail.com";
	String EMAIL_4 = "test- -_test123_@yopmail.com";
	String EMAIL_5 = "test%3D___%3D_@yopmail.com";

	String ERROR_MSSGE_3Step_Password = "Must be at least 8 characters long. Must contain at least one number. Must contain letter of both upper and lower case.";
	String ERROR_MSSGE_3Step_CONFIRM_PASSWORD = "The passwords you have entered do not match.";
	String INVALID_PASSWORD = "test1234";

	public static String getInputFilePath() {
		String fileName = System.getProperty("input_file_name");
		if (fileName == null) {
			System.out.println("No input file path found. Using default");
			fileName = TEST_DATA_WORKBOOK_PATH;
		} else {
			fileName = BASE_FOLDER_PATH + "/" + fileName;
		}
		return fileName;
	}

	

	public static String getBasePath() {
		String path = System.getProperty("input_data_path");
		if (path == null) {
			System.out.println("No argument passed for input data directory. Using default.");
			path = "support_files";
		}
		File file = new File(path);
		try {
			if (!file.exists()) {
				System.out.println("Support files directory not found, switching to default");
				path = "support_files";
			}
		} catch (Exception e) {
			System.out.println("Support files directory not found, switching to default");
			path = "support_files";
		}
		System.out.println("\nBase path = \t" + new File(path).getAbsolutePath());
		return path;
	}

	public static String getReportsPath() {
		String path = System.getProperty("output_data_path");
		if (path == null) {
			System.out.println("No argument passed for output data directory. Using default.");
			path = "Reports";
		}
		File file = new File(path);
		try {
			if (!file.exists()) {
				System.out.println("Reports directory not found, trying to create one.");
				file.mkdir();
				System.out.println("Directory created successfully");
			}
		} catch (Exception e) {
			System.out.println("Tried creating reports folder but failed. Using default");
			path = "Reports";
		}
		System.out.println("\nReports path = \t" + new File(path).getAbsolutePath() + "\n");
		return path;
	}

	public static boolean deleteDirectory(File file) {
		if (file.isDirectory()) {
			File[] children = file.listFiles();
			for (int i = 0; i < children.length; i++) {
				boolean success = deleteDirectory(children[i]);
				if (!success) {
					return false;
				}
			}
		}
		System.out.println("removing file or directory : " + file.getName());
		return file.delete();
	}

}