package com.zensar.datatypes;

public class ExecutionProperty {
	private String environment;
	private String executionType;
	private String browser;
	private String resolution;
	private String address;
	private String port;
	private boolean slackUpdate;
	private boolean browserMob;
	private String slackChannel;
	private String runner;
	private boolean toggle;
	private String scenarioName;
	private String featureName;
	private String dataSetName;
	private boolean isRunnerActive;
	private int retryCount;

	private String os;
	private String osVersion;
	private String browserVersion;
	private String browserStackLocal;
	private String device;
	private String platform;
	private String loggingType;
	
	

	public String getLoggingType() {
		return loggingType;
	}

	public void setLoggingType(String loggingType) {
		this.loggingType = loggingType;
	}
	private String appURL;
	private String mediaURL;
	
	

	public String getMediaURL() {
		return mediaURL;
	}

	public void setMediaURL(String mediaURL) {
		this.mediaURL = mediaURL;
	}

	public String getAppURL() {
		return appURL;
	}

	public void setAppURL(String appURL) {
		this.appURL = appURL;
	}

	public String getOs() {
		return os;
	}

	public void setOs(String os) {
		this.os = os;
	}
	public String getEnvironment() {
		return environment;
	}

	public String getOsVersion() {
		return osVersion;
	}

	public void setOsVersion(String osVersion) {
		this.osVersion = osVersion;
	}

	public String getBrowserVersion() {
		return browserVersion;
	}

	public void setBrowserVersion(String browserVersion) {
		this.browserVersion = browserVersion;
	}

	public String getBrowserStackLocal() {
		return browserStackLocal;
	}

	public void setBrowserStackLocal(String browserStackLocal) {
		this.browserStackLocal = browserStackLocal;
	}

	public String getDevice() {
		return device;
	}

	public void setDevice(String device) {
		this.device = device;
	}

	private String configuration;
	private String configuration2;

	private String fileIdentifier;
	private int order;

	public String getExecutionType() {
		return executionType;
	}

	public String getConfiguration2() {
		return configuration2;
	}

	public void setConfiguration2(String configuration2) {
		this.configuration2 = configuration2;
	}

	public void setExecutionType(String executionType) {
		this.executionType = executionType.trim();
	}

	public String getBrowser() {
		return browser;
	}

	public void setBrowser(String browser) {
		this.browser = browser.trim();
	}

	public String getResolution() {
		return resolution;
	}

	public void setResolution(String resolution) {
		this.resolution = resolution.trim();
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address.trim();
	}

	public String getPort() {
		return port;
	}

	public void setPort(String port) {
		this.port = port.trim();
	}

	public boolean isSlackUpdate() {
		return slackUpdate;
	}

	public void setSlackUpdate(boolean slackUpdate) {
		this.slackUpdate = slackUpdate;
	}

	public boolean isBrowserMob() {
		return browserMob;
	}

	public void setBrowserMob(boolean browserMob) {
		this.browserMob = browserMob;
	}

	public String getSlackChannel() {
		return slackChannel;
	}

	public void setSlackChannel(String slackChannel) {
		this.slackChannel = slackChannel.trim();
	}

	public String getRunner() {
		return runner;
	}

	public void setRunner(String runner) {
		this.runner = runner.trim();
	}

	public boolean isToggle() {
		return toggle;
	}

	public void setToggle(boolean toggle) {
		this.toggle = toggle;
	}

	public String getScenarioName() {
		return scenarioName.trim();
	}

	public void setScenarioName(String scenarioName) {
		this.scenarioName = scenarioName;
	}

	public String getFeatureName() {
		return featureName;
	}

	public void setFeatureName(String featureName) {
		this.featureName = featureName.trim();
	}

	public String getDataSetName() {
		return dataSetName;
	}

	public void setDataSetName(String dataSetName) {
		this.dataSetName = dataSetName.trim();
	}

	public boolean isRunnerActive() {
		return isRunnerActive;
	}

	public void setRunnerActive(boolean isRunnerActive) {
		this.isRunnerActive = isRunnerActive;
	}

	public int getOrder() {
		return order;
	}

	public void setOrder(int order) {
		this.order = order;
	}

	public int getRetryCount() {
		return retryCount;
	}

	public void setRetryCount(int retryCount) {
		this.retryCount = retryCount;
	}

	public String getIdentifier() {
		String id = featureName.trim() + "." + scenarioName.trim() + "." + dataSetName.trim();
		return id.toLowerCase();
	}

	public String getFileIdentifier() {
		return this.fileIdentifier;
	}

	public void setFileIdentifier(String identifier) {
		this.fileIdentifier = identifier;
	}

	public String getConfiguration() {
		return configuration;
	}

	public void setConfiguration(String configuration) {
		this.configuration = configuration;
	}

	

	public String getPlatform() {
		return platform;
	}

	public void setPlatform(String platform) {
		this.platform = platform;
	}
	public void setEnvironment(String environment) {
		this.environment = environment;
	}

	
	@Override
	public String toString() {
		return "ExecutionProperty [executionType=" + executionType + ", browser=" + browser + ", resolution="
				+ resolution + ", address=" + address + ", port=" + port + ", slackUpdate=" + slackUpdate
				+ ", browserMob=" + browserMob + ", slackChannel=" + slackChannel + ", runner=" + runner + ", toggle="
				+ toggle + ", scenarioName=" + scenarioName + ", featureName=" + featureName + ", dataSetName="
				+ dataSetName + ", isRunnerActive=" + isRunnerActive + ", retryCount=" + retryCount + ", os=" + os
				+ ", osVersion=" + osVersion + ", browserVersion=" + browserVersion + ", browserStackLocal="
				+ browserStackLocal + ", device=" + device + ", platform=" + platform + ", configuration="
				+ configuration + ", fileIdentifier=" + fileIdentifier + ", order=" + order + "]";
	}
	
	



}