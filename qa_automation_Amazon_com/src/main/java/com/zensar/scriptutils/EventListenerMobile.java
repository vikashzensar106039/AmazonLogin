package com.zensar.scriptutils;


import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.zensar.datatypes.APIBean;
import com.zensar.datatypes.ExecutionProperty;
import com.zensar.utilities.ApiClient;
import com.zensar.utilities.StringLiterals;

import cucumber.api.Scenario;


public class EventListenerMobile implements ScenarioEventListener,StringLiterals {

	private static final String APP_SESSION_URL="https://api.browserstack.com/app-automate/sessions/";
	private static final String AUTH="";
	

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
		
	}

	@Override
	public void afterFindBy(By by, WebElement element, WebDriver driver) {
	}

	@Override
	public void beforeClickOn(WebElement element, WebDriver driver) {
	}

	@Override
	public void afterClickOn(WebElement element, WebDriver driver) {
	}

	@Override
	public void beforeChangeValueOf(WebElement element, WebDriver driver, CharSequence[] keysToSend) {
	}

	@Override
	public void afterChangeValueOf(WebElement element, WebDriver driver, CharSequence[] keysToSend) {
	}

	@Override
	public void beforeScript(String script, WebDriver driver) {
	}

	@Override
	public void afterScript(String script, WebDriver driver) {
	}



	@Override
	public void onException(Throwable throwable, WebDriver driver) {
		System.out.println("Exception occured");
		
	}



	@Override
	public void onScenarioStart(ScenarioState scenarioState) {
		System.out.println("Scenario Start");
		
		//Add scenario url in report
		try {
			APIBean apiBean = new APIBean();
			String sesssionId = scenarioState.getDriverHandler().getDriver().getSessionId().toString();
			
			switch(scenarioState.getExecutionProperty().getPlatform()){
			case "App":apiBean.setUrl(APP_SESSION_URL + sesssionId + ".json");
				break;
			default :apiBean.setUrl("https://api.browserstack.com/automate/sessions/" + sesssionId + ".json");
			}
			
			Map<String, String> authHeader = new HashMap<String, String>();
			authHeader.put("Authorization", AUTH);
			apiBean.setHeaders(authHeader);
			apiBean.setMethod("GET");

			ApiClient apiClient = new ApiClient();
			apiClient.request(apiBean);
			
			String jsonResponse = apiBean.getActualResponse();
			System.out.println("JSON RESPONSE BROWSER STACK: "+jsonResponse);
			JSONObject jsonObject = new JSONObject(jsonResponse);
			String executionLink = jsonObject.getJSONObject("automation_session").getString("public_url");
			String executedOsVer = jsonObject.getJSONObject("automation_session").getString("os_version");
			String appVer = jsonObject.getJSONObject("automation_session").getJSONObject("app_details").getString("app_version");
			
			scenarioState.getScenario().write("Scenario session url:\n"+executionLink);
			ExecutionProperty ep=scenarioState.getExecutionProperty();
			String deviceOS=ep.getDevice()+", "+ep.getOs()+" "+executedOsVer;


			String msg="Scenario session url:\n"+executionLink;
			scenarioState.getScenario().embed(msg.getBytes(), "text/plain");
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Error while getting Browser stack execution link.");
		}
	}

	@Override
	public void onScenarioEnd(ScenarioState scenarioState) {
		System.out.println("Scenario end");
	}

	@Override
	public void onScenarioPass(ScenarioState scenarioState2) {
		System.out.println("Scenario pass");
		
	}

	@Override
	public void onScenarioFailed(ScenarioState scenarioState) {
		System.out.println("Scenario failed");
		
		if (scenarioState.getMobileDriver() != null) {
			try {
				Scenario scenario = scenarioState.getScenario();
				JavascriptExecutor jse = (JavascriptExecutor) scenarioState.getMobileDriver();
				jse.executeScript(
						"browserstack_executor: {\"action\": \"setSessionStatus\", \"arguments\": {\"status\":\"failed\", \"reason\": \"\"}}");
			} catch (Exception e) {
				e.printStackTrace();
			}

		}

	}

	@Override
	public void onPageObjectChange(ScenarioState scenarioState, String previous, String current) {
		System.out.println(previous + " > " + current);
		
	}

	@Override
	public void onGlobalFunctionCalled(ScenarioState scenarioState, String pageObjectName, String functionInfo) {
		System.out.println(functionInfo);
		
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
