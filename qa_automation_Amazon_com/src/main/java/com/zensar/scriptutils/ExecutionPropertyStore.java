package com.zensar.scriptutils;

import java.util.HashMap;
import java.util.List;


import com.zensar.datatypes.ExecutionProperty;
import com.zensar.excel.ExcelExecutionProperty;
import com.zensar.utilities.StringLiterals;

public class ExecutionPropertyStore implements StringLiterals {

	private ExecutionPropertyStore() {

	}

	enum CONFS {
		CAP_ID, Execution_Type, Platform, Device, OS, OS_Version, Browser, Browser_Version, BSLocal, Logging_Type, App_URL,Media_URL
	}

	private static HashMap<String, ExecutionProperty> hashMap = null;

	private static HashMap<String, ExecutionProperty> scenariosForExecution = new HashMap<>();

	private static HashMap<String, Integer> scenariosCount = new HashMap<>();

	public static synchronized HashMap<String, ExecutionProperty> get() {
		if (hashMap == null) {
			hashMap = new HashMap<>();
			String slackChannel = ExcelExecutionProperty.getGlobalExecutionProperty("Slack Channel");
			boolean slackUpdateOn = ExcelExecutionProperty.getGlobalExecutionProperty("Slack Update").trim()
					.equalsIgnoreCase("yes");
			String resolution = ExcelExecutionProperty.getGlobalExecutionProperty("Resolution");
			List<HashMap<String, String>> runners = ExcelExecutionProperty.getRunnersList();
			for (HashMap<String, String> runner : runners) {
				String runnerName = runner.get("RunnerName");
				String hostname = runner.get("Hostname");
				String port = runner.get("Port");
				String executionEnvironment = runner.get("Environment");
				boolean isRunnerActive = runner.get("Is Active").trim().equalsIgnoreCase("yes");
				if (!isRunnerActive)
					continue;
				scenariosCount.put(runnerName, 0);
				List<HashMap<String, String>> scenarios = ExcelExecutionProperty.getScenarioList(runnerName);
				for (HashMap<String, String> scenario : scenarios) {
					if (!scenario.get("Switch").trim().equalsIgnoreCase("yes"))
						continue;
					ExecutionProperty executionProperty = new ExecutionProperty();
					executionProperty.setAddress(hostname);
					executionProperty.setPort(port);
					executionProperty.setRunnerActive(isRunnerActive);
					executionProperty.setDataSetName(scenario.get("DataSetName"));
					executionProperty.setFeatureName(scenario.get("FeatureName"));
					executionProperty.setEnvironment(executionEnvironment);
					executionProperty.setScenarioName(scenario.get("ScenarioName"));
					executionProperty.setOrder(Integer.parseInt(scenario.get("Order")));
					executionProperty.setConfiguration(scenario.get("Configuration"));
					executionProperty.setConfiguration2(scenario.get("Configuration2"));
					executionProperty.setResolution(resolution);
					executionProperty.setRunner(runnerName);
					
					String configurationId = executionProperty.getConfiguration();
					executionProperty.setOs(getConfiguration(CONFS.OS , configurationId));
					executionProperty.setOsVersion(getConfiguration(CONFS.OS_Version , configurationId));
					executionProperty.setBrowser(getConfiguration(CONFS.Browser , configurationId));
					executionProperty.setBrowserVersion(getConfiguration(CONFS.Browser_Version ,configurationId));
					executionProperty.setBrowserStackLocal(getConfiguration(CONFS.BSLocal , configurationId));
					executionProperty.setDevice(getConfiguration(CONFS.Device , configurationId));
					executionProperty.setAppURL(getConfiguration(CONFS.App_URL , configurationId));
					executionProperty.setMediaURL(getConfiguration(CONFS.Media_URL , configurationId));
					executionProperty.setPlatform(getConfiguration(CONFS.Platform , configurationId));
					executionProperty.setLoggingType(getConfiguration(CONFS.Logging_Type , configurationId));
					executionProperty.setExecutionType(getConfiguration(CONFS.Execution_Type, configurationId));
					
					int retryCount = 0;
					try {
						retryCount = Integer.parseInt(scenario.get("Retry Count"));
					} catch (Exception e) {
						System.out.println("Could not fetch retry count, setting it to 0");
					}
					executionProperty.setRetryCount(retryCount);
					executionProperty.setToggle(scenario.get("Switch").trim().equalsIgnoreCase("yes"));
					executionProperty.setSlackUpdate(slackUpdateOn);
					executionProperty.setSlackChannel(slackChannel);
					hashMap.put(executionProperty.getIdentifier(), executionProperty);
					scenariosCount.put(runnerName, ((Integer) scenariosCount.get(runnerName)) + 1);
					
				}
			}
		}
		return hashMap;
	}

	public static synchronized int getScenarioCount(String runnerName) {
		return scenariosCount.containsKey(runnerName) ? scenariosCount.get(runnerName) : 0;
	}
	
	
	private static String getConfiguration(CONFS conf , String configurationId) {
		return ExcelExecutionProperty.fetchData("Configuration_List",  configurationId, conf.name());
	}
	



	public static synchronized HashMap<String, ExecutionProperty> getScenariosInExecution() {
		return scenariosForExecution;
	}

}
