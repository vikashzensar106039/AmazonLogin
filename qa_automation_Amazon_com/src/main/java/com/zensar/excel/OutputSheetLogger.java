package com.zensar.excel;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.glassfish.jersey.client.authentication.HttpAuthenticationFeature;
import org.json.JSONObject;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.sun.jersey.api.client.filter.HTTPBasicAuthFilter;
import com.zensar.datatypes.APIBean;
import com.zensar.datatypes.ExecutionProperty;
import com.zensar.scriptutils.ScenarioEventListener;
import com.zensar.scriptutils.ScenarioState;
import com.zensar.utilities.ApiClient;
import com.zensar.utilities.StringLiterals;

public class OutputSheetLogger implements ScenarioEventListener, StringLiterals {

	long startTime;
	long endTime;

	public OutputSheetLogger() {
//		ExcelOutput.init();
	}

	@Override
	public void beforeAlertAccept(WebDriver driver) {
		// TODO Auto-generated method stub

	}

	@Override
	public void afterAlertAccept(WebDriver driver) {
		// TODO Auto-generated method stub

	}

	@Override
	public void afterAlertDismiss(WebDriver driver) {
		// TODO Auto-generated method stub

	}

	@Override
	public void beforeAlertDismiss(WebDriver driver) {
		// TODO Auto-generated method stub

	}

	@Override
	public void beforeNavigateTo(String url, WebDriver driver) {
		// TODO Auto-generated method stub

	}

	@Override
	public void afterNavigateTo(String url, WebDriver driver) {
		// TODO Auto-generated method stub

	}

	@Override
	public void beforeNavigateBack(WebDriver driver) {
		// TODO Auto-generated method stub

	}

	@Override
	public void afterNavigateBack(WebDriver driver) {
		// TODO Auto-generated method stub

	}

	@Override
	public void beforeNavigateForward(WebDriver driver) {
		// TODO Auto-generated method stub

	}

	@Override
	public void afterNavigateForward(WebDriver driver) {
		// TODO Auto-generated method stub

	}

	@Override
	public void beforeNavigateRefresh(WebDriver driver) {
		// TODO Auto-generated method stub

	}

	@Override
	public void afterNavigateRefresh(WebDriver driver) {
		// TODO Auto-generated method stub

	}

	@Override
	public void beforeFindBy(By by, WebElement element, WebDriver driver) {
		// TODO Auto-generated method stub

	}

	@Override
	public void afterFindBy(By by, WebElement element, WebDriver driver) {
		// TODO Auto-generated method stub

	}

	@Override
	public void beforeClickOn(WebElement element, WebDriver driver) {
		// TODO Auto-generated method stub

	}

	@Override
	public void afterClickOn(WebElement element, WebDriver driver) {
		// TODO Auto-generated method stub

	}

	@Override
	public void beforeChangeValueOf(WebElement element, WebDriver driver, CharSequence[] keysToSend) {
		// TODO Auto-generated method stub

	}

	@Override
	public void afterChangeValueOf(WebElement element, WebDriver driver, CharSequence[] keysToSend) {
		// TODO Auto-generated method stub

	}

	@Override
	public void beforeScript(String script, WebDriver driver) {
		// TODO Auto-generated method stub

	}

	@Override
	public void afterScript(String script, WebDriver driver) {
		// TODO Auto-generated method stub

	}


	@Override
	public void onException(Throwable throwable, WebDriver driver) {

	}

	@Override
	public void onScenarioStart(ScenarioState scenarioState) {
		startTime = System.currentTimeMillis();
		int id = scenarioState.getId();
		String strQuery = "INSERT INTO " + DEFAULT_OUTPUT_SHEET_NAME + "(ID,Feature,Scenario) " + "VALUES('" + id
				+ "','" + scenarioState.getFeatureName() + "','" + scenarioState.getScenarioName() + "')";
		ExcelOutput.executeInsertStatement(strQuery);
		ExcelOutput.setOutputDataValue(DEFAULT_OUTPUT_SHEET_NAME, "DataSetName", scenarioState.getDataSetName(), id);
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss");
		LocalDateTime now = LocalDateTime.now();
		String startTime = dtf.format(now);
		ExcelOutput.setOutputDataValue(DEFAULT_OUTPUT_SHEET_NAME, "Start Time", startTime, id);
		ExcelOutput.setOutputDataValue(DEFAULT_OUTPUT_SHEET_NAME, "Runner Name", scenarioState.getRunnerName(), id);
		System.out.println("BROWSER STACK SESSION : ");
		System.out.println(scenarioState.getDriverHandler().getDriver().getSessionId().toString());
	}

	@Override
	public void onScenarioEnd(ScenarioState scenarioState) {
		int id = scenarioState.getId();
		LocalDateTime now = LocalDateTime.now();
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss");
		String time = dtf.format(now);
		ExcelOutput.setOutputDataValue(DEFAULT_OUTPUT_SHEET_NAME, "End Time", time, id);
		ExcelOutput.setOutputDataValue(DEFAULT_OUTPUT_SHEET_NAME, "Status", scenarioState.getScenario().getStatus().name(),
				id);
		endTime = System.currentTimeMillis();
		long elapsedTime = endTime - startTime;
		Duration d = Duration.ofMillis(elapsedTime);
		long minutes = d.toMinutes();
		ExcelOutput.setOutputDataValue(DEFAULT_OUTPUT_SHEET_NAME, "ExecutionTime", minutes + "", id);
		if (scenarioState.getDriverHandler().isBrowserStackExecution) {
			try {
				APIBean apiBean = new APIBean();
				String sesssionId = scenarioState.getDriverHandler().getDriver().getSessionId().toString();
				
				switch(scenarioState.getExecutionProperty().getPlatform()){
				case "App":apiBean.setUrl("https://api.browserstack.com/app-automate/sessions/" + sesssionId + ".json");
					break;
				default :apiBean.setUrl("https://api.browserstack.com/automate/sessions/" + sesssionId + ".json");
				}
				
				Map<String, String> authHeader = new HashMap<String, String>();
				authHeader.put("Authorization", "Basic YnJvd3NlcnN0YWNrMTQzMzpCVGp4aVVuWXdRZ2J6dlhxeU5xVA==");
				apiBean.setHeaders(authHeader);
				apiBean.setMethod("GET");

				ApiClient apiClient = new ApiClient();
				apiClient.request(apiBean);
				
				String jsonResponse = apiBean.getActualResponse();
				System.out.println("JSON RESPONSE BROWSER STACK: "+jsonResponse);
				JSONObject jsonObject = new JSONObject(jsonResponse);
				String executionLink = jsonObject.getJSONObject("automation_session").getString("public_url");
				String executedOsVer = jsonObject.getJSONObject("automation_session").getString("os_version");
				System.out.println(executionLink);
				scenarioState.getScenario().write("Scenario session url:\n"+executionLink);
				ExcelOutput.setOutputDataValue(DEFAULT_OUTPUT_SHEET_NAME, "Browser_Stack_Link", executionLink, id);
				ExecutionProperty ep=scenarioState.getExecutionProperty();
				String deviceOS=ep.getDevice()+", "+ep.getOs()+" "+executedOsVer;
				ExcelOutput.setOutputDataValue(DEFAULT_OUTPUT_SHEET_NAME, "Device & OS", deviceOS, id);

			} catch (Exception e) {
				System.out.println("Error while getting Browser stack execution link.");
			}
		}

	}

	@Override
	public void onScenarioPass(ScenarioState scenarioState2) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onScenarioFailed(ScenarioState scenarioState) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onPageObjectChange(ScenarioState scenarioState, String previous, String current) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onGlobalFunctionCalled(ScenarioState scenarioState, String pageObjectName, String functionInfo) {
		// TODO Auto-generated method stub

	}

	@Override
	public void beforeSwitchToWindow(String windowName, WebDriver driver) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void afterSwitchToWindow(String windowName, WebDriver driver) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public <X> void beforeGetScreenshotAs(OutputType<X> target) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public <X> void afterGetScreenshotAs(OutputType<X> target, X screenshot) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void beforeGetText(WebElement element, WebDriver driver) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void afterGetText(WebElement element, WebDriver driver, String text) {
		// TODO Auto-generated method stub
		
	}

}
