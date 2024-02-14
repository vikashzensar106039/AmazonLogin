package com.zensar.runner;


import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;
import org.testng.annotations.AfterClass;
import org.testng.annotations.Test;

import com.zensar.excel.ExcelExecutionProperty;
import com.zensar.scriptutils.ExecutionState;
import com.zensar.slack.SlackClient;
import com.zensar.utilities.ExecutionEnvironment;
import com.zensar.utilities.StringLiterals;

import net.masterthought.cucumber.Configuration;
import net.masterthought.cucumber.ReportBuilder;
import net.masterthought.cucumber.Reportable;



public class ConcludeExecution implements StringLiterals {
	
	@Test
	public void stub() {
		//Placeholder function
	}
	
	
	@AfterClass(alwaysRun = true)
	public void endExecution() {
//		SaleDataHandler saleDataHandler = new SaleDataHandler();
		String reportURL = "Not Available";
		if(ExcelExecutionProperty.getGlobalExecutionProperty("Slack Update").equals("Yes")) {
			String channelName = ExcelExecutionProperty.getGlobalExecutionProperty("Slack Channel");
			SlackClient.endSlack();
			String response = SlackClient.uploadFlie(ExecutionState.getCurrentReportsPath()+"/"+ OUTPUT_EXCEL_FILE, "Execution Output", OUTPUT_EXCEL_FILE.split("\\.")[1],channelName);
			
			File file = new File(ExecutionState.getCurrentReportsPath()+"/HAR"); 
			System.out.println("HAR FILE PATH : "+file.getAbsolutePath());
			
			if(file.exists() && file.isDirectory()){
				File[] harList = file.listFiles();
				for (File har : harList) {
					SlackClient.uploadFlie(har.getAbsolutePath(), har.getName() , "har", channelName);
					try {
						System.out.println("[INFO] Cleaning up har reports.");
						har.delete();
					}catch (Exception e) {
						System.out.println("[CRITICAL] Error while deleting har file.");
						e.printStackTrace();
					}
				}
			}
			
			JSONObject responseObject = new JSONObject(response);
// 			reportURL = responseObject.getJSONObject("file").getString("url_private_download");
		}
//		if(ExecutionState.onlineSale != null){
//			ExecutionState.onlineSale.setRetestCount((Integer.parseInt(ExecutionState.onlineSale.getRetestCount()) + 1) + "" );
//			ExecutionState.onlineSale.setLastest_report_link(reportURL);
////			saleDataHandler.updateSaleInExcel(ExecutionState.onlineSale);
//		}
//		
//		if(ExecutionState.siteSale != null){
//			ExecutionState.siteSale.setRetestCount((Integer.parseInt(ExecutionState.siteSale.getRetestCount()) + 1) + "" );
//			ExecutionState.siteSale.setLastest_report_link(reportURL);
////			saleDataHandler.updateSaleInExcel(ExecutionState.siteSale);
//		}		
//		ExcelMulti.dumpMultiOutputData();
		if (!ExecutionEnvironment.isJenkinsBuild()) generateReport();
		System.out.println("============================Execution Complete==============================");
		
	}
	
private void generateReport() {
		
		File reportOutputDirectory = new File(ExecutionState.getCurrentReportsPath());
		List<String> jsonFiles = new ArrayList<>();		
		File file = new File(ExecutionState.getCurrentReportsPath() + "/json");
		if(file.exists() && file.isDirectory() && file.listFiles().length > 0){
			for (File jFile : file.listFiles()) {
				jsonFiles.add(jFile.getAbsolutePath());
			}
		}else{
			System.out.println("Can't generate report because cucmber json output directory not found");
			return;
		}
		String projectName = PROJECT_NAME;
		boolean runWithJenkins = ExecutionEnvironment.isJenkinsBuild();
		Configuration configuration = new Configuration(reportOutputDirectory, projectName);
		// optional configuration - check javadoc
		configuration.setRunWithJenkins(false);
		if(runWithJenkins)configuration.setBuildNumber(ExecutionEnvironment.getBuildNumber());
		if(runWithJenkins) configuration.addClassifications("Jenkins Jobname", ExecutionEnvironment.getJobName());
		if(runWithJenkins) configuration.addClassifications("Jenkins Buildnumber", ExecutionEnvironment.getBuildNumber());
		configuration.addClassifications("Machine IP", ExecutionEnvironment.getHostIp());
		configuration.addClassifications("Hostname", ExecutionEnvironment.getHostName());
		configuration.setParallelTesting(true);
		ReportBuilder reportBuilder = new ReportBuilder(jsonFiles, configuration);
		Reportable result = reportBuilder.generateReports();
		try{
			System.out.println("Reports Directory Path  \n" + new File(ExecutionState.getCurrentReportsPath()).getAbsolutePath() );
			System.out.println("Reports Path  \n" + new File(ExecutionState.getCurrentReportsPath()).getAbsolutePath()+"/cucumber-html-reports/overview-features.html\n" );
		}catch (Exception e) {
			System.out.println("Reports directory not found.");
		}
		
		
		
	}
	
}
