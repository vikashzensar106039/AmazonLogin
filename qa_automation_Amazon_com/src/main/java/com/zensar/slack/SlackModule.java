//package com.zensar.slack;
//
//import java.net.InetAddress;
//import java.net.UnknownHostException;
//import java.util.Base64;
//
//import org.json.JSONArray;
//import org.json.JSONObject;
//
//import com.mongodb.util.JSON;
//import com.sun.jersey.api.client.Client;
//import com.sun.jersey.api.client.WebResource;
//import com.zensar.excel.ExcelInput;
//import com.zensar.utilities.APIBean;
//import com.zensar.utilities.ApiClient;
//import  com.zensar.utilities.StringLiterals;
//
//public class SlackModule implements StringLiterals {
//	private static final String OAUTH_KEY = "xoxb-405580933268-461962978516-r5YB0KYwJtGBRvrtA7v7nyX8";
//	private static String statusUpdateFormat = "[{'fallback':'Environment Info','pretext':'Environment Information','color':'#03a9f4','fields':[]},{'pretext':'Christies Dot Com Execution','footer':'Windows - Chrome','fallback':'Framework Execution','color':'#03a9f4','author_name':'Manveer Singh','fields':[{'title':'Status','value':'Executing','short':true},{'title':'Feature','value':'SLP','short':true},{'title':'Current Scenario','value':'Verify user can do something','short':false}]},{'fallback':'Scenarios Completed','pretext':'Completed Scenarios','color':'#36a64f','fields':[]}]";
//	private static JSONArray attachmentsArray;
//	private static String browser;
//	private static String os;
//	
//	public static final String CHANNEL_NAME = "GDB94QX71";
//	public static String postTimeStamp = "1540564508.000200";
//	static JSONObject messageObject = new JSONObject();
//	private static boolean slackEnabled = false;
//	public static boolean isSlackEnabled(){
//		try{
//			slackEnabled = getExcecutionProperty("Slack Update").trim().equalsIgnoreCase("yes");
//			return slackEnabled;
//		}catch(Exception e){
//			slackEnabled = getExcecutionProperty("Slack Update").trim().equalsIgnoreCase("yes");
//			return slackEnabled;
//		}
//	}
//
//	private static String getExcecutionProperty(String string) {
//		return ExcelInput.fetchProperty("ExecutionProperties", "Slack Update");
//	}
//
//	public static boolean startTest() {
//		isSlackEnabled();
//		if(!slackEnabled) return true;
//		try{
//			browser = getExcecutionProperty("Browser");
//			os = getExcecutionProperty("OS");
//		}catch(Exception e){
//			try{
//				init("", "", false);
//				browser = getExcecutionProperty("Browser");
//				os = getExcecutionProperty("OS");
//			}catch(Exception ex){
//				System.out.println("Reading property for slack failed");
//				ex.printStackTrace();
//			}
//		}
//		
//		APIBean messageBean = new APIBean();
//		messageObject.put("channel", CHANNEL_NAME);
//		attachmentsArray = new JSONArray(statusUpdateFormat);
//		attachmentsArray.getJSONObject(0).put("footer", os + " - " + browser );
//		messageObject.put("attachments", attachmentsArray);
//		messageObject.put("text", DESCRIPTION);
//		messageBean.setRequestPayload(messageObject.toString());
//		messageBean.setUrl("https://slack.com/api/chat.postMessage");
//		messageBean.setMethod("POST");
//		messageBean.setContentType("application/json");
//		ApiClient apiTester = new ApiClient();
//		apiTester.testApi(messageBean);
//		
//		JSONObject response = new JSONObject(messageBean.getActualResponse());
//		if(response.getBoolean("ok")){
//			postTimeStamp = response.getString("ts");
//			System.out.println("=====================" + postTimeStamp + "============================");
//		}
//		System.out.println(messageBean);
//		return true;
//	}
//	
//	
//	public static boolean updateStatus() {
//		if(!slackEnabled) return true;
//		APIBean messageBean = new APIBean();
//		messageObject.put("channel", CHANNEL_NAME);
//		messageObject.put("text", DESCRIPTION);
//		messageObject.put("ts", postTimeStamp);
//		messageObject.put("attachments", attachmentsArray);		
//		messageBean.setRequestPayload(messageObject.toString());
//		messageBean.setUrl("https://slack.com/api/chat.update");
//		messageBean.setMethod("POST");
//		messageBean.setContentType("application/json");
//		ApiClient apiTester = new ApiClient();
//		apiTester.testApi(messageBean);
//		return true;
//	}
//	
//	
//	public static void setRunStatus(String status){
//		if(!slackEnabled) return;
//		JSONObject jsonObject = attachmentsArray.getJSONObject(1).getJSONArray("fields").getJSONObject(0);
//		jsonObject.put("value", status);
//	}
//	
//	
//	public static void addCompletedScenario(String scenario, String status){
//		if(!slackEnabled) return;
////		JSONArray jsonArray = attachmentsArray.getJSONObject(2).getJSONArray("fields");
////		JSONObject jsonObject = new JSONObject();
////		jsonObject.put("title", scenario);
////		jsonObject.put("value", status);
////		jsonObject.put("short", false);
////		jsonArray.put(jsonObject);
//		String colorFail = "#f44336";
//		String colorPassed = "#4caf50";
//		
//		String color = "#03a9f4";
//		if(status.equalsIgnoreCase("passed")) color = colorPassed;
//		else color = colorFail;
//		JSONObject scenarioAttachment = new JSONObject("{'fallback':'Scenario Status','pretext':'','color':'"+color+"','fields':[{'title':'"+scenario+"','value':'"+status+"'}]}");
//		attachmentsArray.put(scenarioAttachment);
//		//		fileAttachment.getJSONArray("actions").getJSONObject(0).put("url", reportdownloadURL);
//		
//		
//	}
//	
//	
//	public static void setCurrentRunningScenario(String scenario){
//		if(!slackEnabled) return;
//		JSONObject jsonObject = attachmentsArray.getJSONObject(1).getJSONArray("fields").getJSONObject(2);
//		jsonObject.put("value", scenario);
//	}
//	
//	
//	public static void setCurrentFeature(String feature){
//		if(!slackEnabled) return;
//		JSONObject jsonObject = attachmentsArray.getJSONObject(1).getJSONArray("fields").getJSONObject(1);
//		jsonObject.put("value", feature);
//	}
//	
//	public static void setExecutionStartTime(){
//		
//	}
//	
//	public static void setExecutionEndTime(){
//		
//	}
//	
//	
//	public static void completeStatus(String reportPath){
//		if(!slackEnabled) return;
//		APIBean messageBean = new APIBean();
//		messageObject.put("channel", CHANNEL_NAME);
//		messageObject.put("text", DESCRIPTION);
//		messageObject.put("ts", postTimeStamp);
////		JSONObject jsonObject = attachmentsArray.getJSONObject(1);
////		jsonObject.put("color", "#ff6363");
////		JSONObject jsonObject2 = attachmentsArray.getJSONObject(2);
////		jsonObject2.put("color", "#ff6363");
//		messageObject.put("attachments", attachmentsArray);
//		attachmentsArray.getJSONObject(1).getJSONArray("fields").remove(1);
//		attachmentsArray.getJSONObject(1).getJSONArray("fields").remove(2);
//		if(reportPath != null) setRunStatus("Uploading Report");
//		else setRunStatus("Complete - Report not uploaded");
//		messageBean.setRequestPayload(messageObject.toString());
//		messageBean.setUrl("https://slack.com/api/chat.update");
//		messageBean.setMethod("POST");
//		messageBean.setContentType("application/json");
//		ApiClient apiTester = new ApiClient();
//		apiTester.testApi(messageBean);
//		if(reportPath != null){
//			postReports(reportPath);
//		}else{
//			System.out.println("Extent report was not uploaded as file not found.");
//		}
//	}
//	
//	
//	public static void main(String args[]) {
////		startTest();
////		attachmentsArray = new JSONArray(statusUpdateFormat);
////		setRunStatus("Executing 10");
////		setCurrentRunningScenario("Scenario 20");
////		setCurrentFeature("Feature 1");
////		addCompletedScenario("Scenario 1", "Passed");
////		addCompletedScenario("Scenario 2", "Failed");
////		addCompletedScenario("Scenario 3", "Passed");
////		updateStatus();
////		completeStatus();
//		
//		
//		
//		
////		APIBean messageBean = new APIBean();
////		messageObject.put("channel", CHANNEL_NAME);
////		messageObject.put("text", "Testing File upload");
////		messageBean.setRequestPayload(messageObject.toString());
////		messageBean.setUrl("https://slack.com/api/chat.postMessage");
////		messageBean.setMethod("POST");
////		messageBean.setContentType("application/json");
////		ApiTester apiTester = new ApiTester();
////		apiTester.testApi(messageBean);
////		System.out.println(messageBean);
//		
//		ApiClient apiTesterc = new  ApiClient();
//		apiTesterc.uploadFile("C:\\Users\\mr51732\\Projects\\Automation_Framework\\Reports\\Automation Framework_2018-10-22_06-48-56\\extent.html", "Test Report.html", "automation_team");
//    	
//		
//		
//		try {
//			System.out.println(InetAddress.getLocalHost().getHostName());
//			System.out.println(InetAddress.getLocalHost().getHostAddress());
//		} catch (UnknownHostException e) {
//			e.printStackTrace();
//		}
//	}
//	
//	public static void postReports(String path){
//		ApiClient apiTesterc = new  ApiClient();
//		String response = apiTesterc.uploadFile(path, OUTPUT_EXCEL_FILE, CHANNEL_NAME);
//		JSONObject responseObject = new JSONObject(response);
//		if(responseObject.getBoolean("ok")){
//			
//			String reportdownloadURL = responseObject.getJSONObject("file").getString("url_private_download");
//			JSONObject fileAttachment = new JSONObject("{'fallback':'Reports','pretext':'','color':'#29b6f6','actions':[{'text':'Get Report','type' : 'button','url':''}]}");
//			fileAttachment.getJSONArray("actions").getJSONObject(0).put("url", reportdownloadURL);
//			attachmentsArray.put(fileAttachment);
//			System.out.println("Report uploaded");
//			setRunStatus("Completed - Report Attached");
//			updateStatus();
//			
//			
//		}else{
//			System.out.println("Report upload failed");
//			setRunStatus("Completed - Report not uploaded");
//			updateStatus();
//			System.out.println(response);
//		}
//		
//    	
//	}
//	
//
//}
//
//
