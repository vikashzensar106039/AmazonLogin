package com.zensar.excel;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;

import com.codoid.products.exception.FilloException;
import com.codoid.products.fillo.Connection;
import com.codoid.products.fillo.Fillo;
import com.codoid.products.fillo.Recordset;
import com.zensar.scriptutils.ExecutionState;
import com.zensar.utilities.ExecutionEnvironment;
import com.zensar.utilities.StringLiterals;

public class ExcelConnectionManager implements StringLiterals {
	
	protected ExcelConnectionManager() {
		
	}
	
	private static ExcelConnectionManager connectionManager;
	private static Connection inputSheetConnection;
	private static Connection outputSheetConnection;
	private static Connection multiDataInputConnection;
	private static Connection executionPropertyConnection;
	
	protected static ExcelConnectionManager getInstance() {
		if(connectionManager == null) connectionManager = new ExcelConnectionManager();
		return connectionManager;
	}
	
	
	protected static Connection getInputSheetConnection() throws FilloException {
		if(inputSheetConnection == null) {
			Fillo fillo = new Fillo();
			inputSheetConnection = fillo.getConnection(StringLiterals.getInputFilePath());
		}
		return inputSheetConnection;
	}
	
	
	protected static Connection getOutputSheetConnection() throws FilloException {
		if(outputSheetConnection == null) {
			File file = new File(OUTPUT_EXCEL_TEMPLATE_FILE);
			String outputFilePath = ExecutionState.getCurrentReportsPath() + "/" + OUTPUT_EXCEL_FILE;
			File outputFile = new File(outputFilePath);
			try {
				FileUtils.copyFile(file, outputFile);
			} catch (IOException e) {
				System.out.println("Problem copying output file.");
				e.printStackTrace();
			}
			Fillo fillo = new Fillo();
			outputSheetConnection = fillo.getConnection(outputFilePath);
		}
		return outputSheetConnection;
	}
	
	protected static Connection getMultiDataInputConnection() {
		if(multiDataInputConnection == null) {
			Fillo fillo = new Fillo();
			try {
				multiDataInputConnection = fillo.getConnection(MULTI_DATA_TEMPLATE);
			} catch (FilloException e) {
				System.out.println("Problem getting Multi Data Input Connection.(Verify if file exists.)");
				e.printStackTrace();
			}
		}
		return multiDataInputConnection;
	}
	
	protected static Connection getExecutionPropertiesConnection(){
		if(executionPropertyConnection == null){
			Fillo fillo = new Fillo();
			try{
				
					if (ExecutionEnvironment.isJenkinsBuild()) {
						System.out.println("JENKINS JOB");
						String jobName = ExecutionEnvironment.getJobName();
						System.out.println("JOB NAME : "+jobName);
						executionPropertyConnection = fillo.getConnection(EXECUTION_PROPERTIES_JENKINS_EXCEL_PATH.replace(VAR, jobName));
					}
					else
					executionPropertyConnection = fillo.getConnection(EXECUTION_PROPERTIES_EXCEL_PATH);
			
			}catch (FilloException e) {
				System.out.println("Problem getting Execution Properties Connection.(Verify if file exists.)");
				e.printStackTrace();
			}
		}
		
		return executionPropertyConnection;
	}
	
	protected static synchronized boolean executeInsertStatement(String strQuery, Connection connection ) {
		try {
			System.out.println("[QUERY] " + strQuery + " -- " + connection.getClass().getSimpleName());
			connection.executeUpdate(strQuery);
			return true;
		} catch (FilloException e) {
			System.out.println("[ERROR] Unable to execute insert statement.");
			e.printStackTrace();
			return false;
		}

	}

	protected static synchronized Recordset executeSelectStatement(String query, Connection connection ) {
		
		Recordset recordset;
		try {
			System.out.println("[QUERY] " + query + " -- ");
			recordset = connection.executeQuery(query);
			return recordset;
		} catch (FilloException e) {
			System.out.println("[ERROR] Unable to execute select statement.");
			e.printStackTrace();
			return null;
		}
	}
	
	protected static synchronized Recordset executeSelectStatement(String query, Connection connection ,boolean suppress) {
		
		Recordset recordset;
		try {
			recordset = connection.executeQuery(query);
			return recordset;
		} catch (FilloException e) {
			return null;
		}
	}

	protected static synchronized int countRows(String sheetName, Connection connection) {
		Recordset recordset;
		String query = "select * from " + sheetName;
		try {
			recordset = connection.executeQuery(query);
			return recordset.getCount();
		} catch (FilloException e) {
			return -1;
		}
	}
	
	
	
	
	
	
	
	
	
}
