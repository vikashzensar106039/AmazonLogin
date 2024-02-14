package com.zensar.utilities;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Inet4Address;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

import org.openqa.selenium.Proxy;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.logging.LoggingPreferences;
import org.openqa.selenium.ie.InternetExplorerOptions;
import org.openqa.selenium.remote.BrowserType;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import com.zensar.datatypes.ExecutionProperty;
import com.zensar.excel.ExcelExecutionProperty;
import com.zensar.scriptutils.ExecutionState;
import com.zensar.scriptutils.ScenarioState;

import io.github.bonigarcia.wdm.WebDriverManager;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.windows.WindowsDriver;
import net.lightbody.bmp.BrowserMobProxy;
import net.lightbody.bmp.BrowserMobProxyServer;
import net.lightbody.bmp.client.ClientUtil;
import net.lightbody.bmp.core.har.Har;
import net.lightbody.bmp.proxy.CaptureType;

public class DriverHandler implements StringLiterals {
	public static ThreadLocal<RemoteWebDriver> threadDriver=new ThreadLocal<>();

	enum CONFS {
		CAP_ID, Execution_Type, Platform, Device, OS, OS_Version, Browser, Browser_Version, Logging_type, App_URL, Media_URL
	}
	public enum TYPE {
		Mobile,WebBrowser		
	}

	private RemoteWebDriver driver = null;
	private RemoteWebDriver mobileDriver = null;
	private RemoteWebDriver windowsDriver = null;
	private String environment;

	private String hubURL;
	private String hubPort;
	private String browserStackUrl;
	private String os;
	public boolean isBrowserMobEnabled = false;
	private DesiredCapabilities capabilities;
	private ScenarioState scenarioState;

	private String username;
	private String access_key;

	private String browser;
	private String browserVersion;
	private String osVersion;
	private String platform;
	private String device;
	public String sessionId;
	private String loggingType;
	private String configurationId;
	private String appURL;
	private String mediaURL;
	public boolean isBrowserStackExecution = false;
	private ExecutionProperty executionProperty;
	private boolean isConfig2Present=false;
	private String autoWebDriverManager;

	public RemoteWebDriver getDriver() {
		System.out.println("Thread Local getDrive()");
		return threadDriver.get();
	}
	public RemoteWebDriver getMobileDriver() {
		return mobileDriver;
	}
	public RemoteWebDriver getWindowsDriver() {
		return windowsDriver;
	}

	public void tearDown(boolean status) {

		if (isBrowserMobEnabled) {

			File harFolder = new File(ExecutionState.getCurrentReportsPath() + "/HAR");
			if (!harFolder.exists())
				harFolder.mkdirs();
			Har har = proxy.getHar();

			String filename = harFolder + "/" + scenarioState.getFeatureName() + " - " + scenarioState.getScenarioName()
					+ " - " + scenarioState.getDataSetName() + " " + scenarioState.getId();

			File harFile = new File(filename + ".har");
			try {
				har.writeTo(harFile);
			} catch (IOException e) {
				e.printStackTrace();
			}
			proxy.stop();
		}
		try {
				if(mobileDriver!=null)
					mobileDriver.quit();
				if(windowsDriver!=null)
					closeWindowsDriver();
				if(driver!=null)
					driver.quit();
				
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	public DriverHandler(ScenarioState scenarioState) {
		this.scenarioState = scenarioState;
		executionProperty = scenarioState.getExecutionProperty();
		//Init Driver for 2nd config if present
		configurationId=executionProperty.getConfiguration2();
		if(configurationId!=null)
			if (!configurationId.equals("")) {
//				configurationId=executionProperty.getConfiguration2();
				readPropertiesFromConfig();
				buildDriver();
				isConfig2Present = true;
			}
		//Init Driver for normal config
		configurationId = executionProperty.getConfiguration();
		if (ExecutionEnvironment.getRunConfiguration() != null)
			configurationId = ExecutionEnvironment.getRunConfiguration();
		readProperties();
		buildDriver();
	}

	public DriverHandler(String browser) {
		configurationId = "1";
		if (ExecutionEnvironment.getRunConfiguration() != null)
			configurationId = ExecutionEnvironment.getRunConfiguration();
		readProperties();
		isBrowserMobEnabled = false;
		this.browser = browser;
		buildDriver();
	}

	private void readProperties() {
		environment = executionProperty.getExecutionType();
		browser = executionProperty.getBrowser();
		hubURL = executionProperty.getAddress();
		hubPort = executionProperty.getPort();
		browserStackUrl = getExcecutionProperty("Remote Address");
		username = getExcecutionProperty("Username");
		access_key = getExcecutionProperty("Access Key");
		browserStackUrl = browserStackUrl.replaceAll("<USERNAME>", username);
		browserStackUrl = browserStackUrl.replaceAll("<KEY>", access_key);

		os = executionProperty.getOs();
		osVersion = executionProperty.getOsVersion();
		browser = executionProperty.getBrowser();
		browserVersion = executionProperty.getBrowserVersion();
		platform = executionProperty.getPlatform();
		device = executionProperty.getDevice();
		loggingType = executionProperty.getLoggingType();
		autoWebDriverManager=ExcelExecutionProperty.getGlobalExecutionProperty("AutoWebDriverManager");
		appURL = executionProperty.getAppURL();
		mediaURL=executionProperty.getMediaURL().trim();
		if (scenarioState != null)
			isBrowserMobEnabled = this.scenarioState.isMobProxyEnabled();
		
	}
	
	private void readPropertiesFromConfig() {		
		environment = getConfiguration(CONFS.Execution_Type);
		browserStackUrl = getExcecutionProperty("Remote Address");
		username = getExcecutionProperty("Username");
		access_key = getExcecutionProperty("Access Key");
		browserStackUrl = browserStackUrl.replaceAll("<USERNAME>", username);
		browserStackUrl = browserStackUrl.replaceAll("<KEY>", access_key);

		os = getConfiguration(CONFS.OS);
		osVersion = getConfiguration(CONFS.OS_Version);
		browser = getConfiguration(CONFS.Browser);
		browserVersion = getConfiguration(CONFS.Browser_Version);
		platform = getConfiguration(CONFS.Platform);
		device = getConfiguration(CONFS.Device);
		appURL = getConfiguration(CONFS.App_URL);
		mediaURL=getConfiguration(CONFS.Media_URL);
		
		if (scenarioState != null)
			isBrowserMobEnabled = this.scenarioState.isMobProxyEnabled();
	}

	private String getExcecutionProperty(String key) {
		return ExcelExecutionProperty.fetchProperty("Global", key);
	}

	 private String getConfiguration(CONFS conf) {
	 return ExcelExecutionProperty.fetchData("Configuration_List",configurationId, conf.name());
	 }

	public void startNewPage(String lastPage) {
		if (!isBrowserMobEnabled)
			return;
		proxy.newPage(lastPage);
	}

	private void buildDriver() {
		capabilities = new DesiredCapabilities();
		if (isBrowserMobEnabled) {
			Proxy seleniumProxy = getSeleniumProxy(getProxyServer());
			capabilities.setCapability(CapabilityType.PROXY, seleniumProxy);
			proxy.newHar();
		}
		switch (environment) {
		case "Grid":
			buildGrid();
			break;
		case "Browser Stack":
			buildBrowserStack();
			break;
		case "Windows":
			buildWinApp();
			windowsDriver.manage().timeouts().implicitlyWait(PAGE_IMPLICIT_WAIT, TimeUnit.SECONDS);
			break;
		default:
			buildLocal();
			break;
		}

		if(driver!=null)
			threadDriver.get().manage().timeouts().implicitlyWait(PAGE_IMPLICIT_WAIT, TimeUnit.SECONDS);
		if(mobileDriver!=null)
			mobileDriver.manage().timeouts().implicitlyWait(PAGE_IMPLICIT_WAIT, TimeUnit.SECONDS);
		

	}
	
	private void closeWindowsDriver() throws Exception {
		windowsDriver.quit();
		Runtime.getRuntime().exec("cmd /C cmd.exe /K taskkill /IM winappdriver.exe /F");
	}
	
	private void buildWinApp() {
		capabilities.setCapability("app", appURL);
		capabilities.setCapability("ms:waitForAppLaunch", "5");
		capabilities.setCapability("ms:experimental-webdriver", true);
		capabilities.setCapability("platformName", "Windows");
		capabilities.setCapability("automationName", "Windows");
		capabilities.setCapability("deviceName", "WindowsPC");
		try {
			
			String driverPath="C:\\Program Files (x86)\\Windows Application Driver\\WinAppDriver.exe";
			String driverArguments="localhost 4723/wd/hub";
			String runDriverAsAdmin="powershell -Command \"Start-Process '"+driverPath+"' -ArgumentList '"+driverArguments+"' -Verb RunAs\"";
			Process p;
			p=Runtime.getRuntime().exec(runDriverAsAdmin);
			p.waitFor();
			BufferedReader r = new BufferedReader(new InputStreamReader(p.getInputStream()));
	        String line = r.readLine();
	        if (line != null)
	        	System.out.println(line);
			
			windowsDriver= new WindowsDriver(new URL("http://localhost:4723/wd/hub"), capabilities);
			if(!isConfig2Present)
				driver=windowsDriver;
			threadDriver.set(driver);
			
			
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	private void buildBrowserStack() {
		isBrowserStackExecution = true;
		switch (platform) {
		case "Desktop":
			capabilities.setCapability("os", os);
			capabilities.setCapability("os_version", osVersion);
			capabilities.setCapability("browser", browser);
			capabilities.setCapability("browser_version", browserVersion);
			capabilities.setCapability("browserstack.local", "false");
			capabilities.setCapability("resolution", "1920x1080");
			break;
		case "Mobile":
			capabilities.setCapability("os_version", osVersion);
			capabilities.setCapability("device", device);
			capabilities.setCapability("real_mobile", "true");
			capabilities.setCapability("browserstack.local", "false");
			break;
		case "App":
			capabilities.setCapability("app", appURL);
			capabilities.setCapability("os_version", osVersion);
			capabilities.setCapability("device", device);
			capabilities.setCapability("build", scenarioState.getRunnerName());
			capabilities.setCapability("name", scenarioState.getScenarioName());
			capabilities.setCapability("browserstack.appium_version", "1.22.0");
			String Sceneriolangname = scenarioState.getDataSetName();
			if (Sceneriolangname.equals("TC")) {
				capabilities.setCapability("language", "zh-Hant");
				capabilities.setCapability("locale", "zh-Hant_TW");
			}
			else
			{
				capabilities.setCapability("language", "es");
			}
			if (!mediaURL.equals(""))
				capabilities.setCapability("browserstack.uploadMedia", mediaURL.split(","));
			else
				System.err.println("No media url found!, media upload skipped");
		default:
			break;
		}
		capabilities.setCapability("project", "Amazon.com");
		capabilities.setCapability("browserstack.debug", "true");
		capabilities.setCapability("browserstack.console", "errors");
		capabilities.setCapability("browserstack.networkLogs", "true");

		try {
			if (platform.equals("App")) {
				mobileDriver = new IOSDriver(new URL(browserStackUrl), capabilities);
				if(!isConfig2Present)
					driver=mobileDriver; 
			}
			else
				driver = new RemoteWebDriver(new URL(browserStackUrl), capabilities);
			System.out.println("Setting up threadriver");
			threadDriver.set(driver);
			// driver.manage().window().maximize();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
	}

	private void buildGrid() {
		String seleniumHubURL = "http://" + hubURL + ":" + hubPort + "/wd/hub/";
		System.out.println("-----------------------------------------------------------\n\n");

		System.out.println(seleniumHubURL);
		System.out.println(executionProperty.getBrowser());
		System.out.println("\n\n-----------------------------------------------------------\n\n");
		switch (browser) {
		case "Chrome":
			capabilities.setCapability(CapabilityType.BROWSER_NAME, BrowserType.CHROME);
			capabilities.setCapability("enableVNC", true);
			
			break;
		case "Chrome-headless":
			final ChromeOptions chromeOptions = new ChromeOptions();
			capabilities.setCapability(CapabilityType.BROWSER_NAME, BrowserType.CHROME);
			chromeOptions.addArguments("headless");
			capabilities.setCapability(ChromeOptions.CAPABILITY, chromeOptions);
			break;
		case "Firefox":
			capabilities.setCapability(CapabilityType.BROWSER_NAME, BrowserType.FIREFOX);
			break;
		case "IE":
			capabilities.setCapability(CapabilityType.BROWSER_NAME, BrowserType.IE);
			capabilities.setCapability(CapabilityType.ForSeleniumServer.ENSURING_CLEAN_SESSION, true);
			capabilities.setCapability(InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS, true);
			capabilities.setCapability("ignoreZoomSetting", true);
			capabilities.setCapability("ignoreProtectedModeSettings", true);
			capabilities.setCapability("requireWindowFocus", true);
			break;
		default:
		}

		try {
			driver = new RemoteWebDriver(new URL(seleniumHubURL), capabilities);
			threadDriver.set(driver);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
	}
	
	private void buildLocal() {
		switch (browser) {
		case "Chrome":
			if(autoWebDriverManager.toLowerCase().equals("yes")) {
				WebDriverManager.chromedriver().setup();
			}else {
				if (!System.getProperty("os.name").equalsIgnoreCase("linux")) {
					System.setProperty("webdriver.chrome.driver", "driver\\chromedriver.exe");
				} else {
					System.setProperty("webdriver.chrome.driver", "linuxgrid/chromedriver");
				}
			}
			HashMap<String, Object> chromePrefs = new HashMap<String, Object>();
			chromePrefs.put("profile.default_content_settings.popups", 0);
			chromePrefs.put("download.default_directory", ExecutionState.getCurrentOutputPath());
			ChromeOptions options = new ChromeOptions();
			options.setExperimentalOption("prefs", chromePrefs);

			if(loggingType.equalsIgnoreCase("local")) {
				LoggingPreferences logPreferences= new LoggingPreferences();
				logPreferences.enable(LogType.PERFORMANCE, Level.ALL);
				options.setCapability( "goog:loggingPrefs", logPreferences );
				
			}else if(loggingType.equalsIgnoreCase("proxy")) {
				
				proxy = new BrowserMobProxyServer();
				proxy.setTrustAllServers(true);
				proxy.waitForQuiescence(35, 60, TimeUnit.SECONDS);
				proxy.start(0);
				Proxy seleniumProxy = ClientUtil.createSeleniumProxy(proxy);
				
				capabilities.setCapability(CapabilityType.PROXY, seleniumProxy);
				options.setAcceptInsecureCerts(true);
				
				options.addArguments("--ignore-certificate-errors");
				options.addArguments("acceptSslCerts");
	
			}
			
			
			options.merge(capabilities);
			driver = new ChromeDriver(options);
			 
			 

			driver.manage().window().maximize();
			break;
		case "IE":
			capabilities.setCapability("requireWindowFocus", true);
			capabilities.setCapability(InternetExplorerDriver.IGNORE_ZOOM_SETTING, true);
			capabilities.setCapability("ie.ensureCleanSession", true);
			if(autoWebDriverManager.toLowerCase().equals("yes")) {
				WebDriverManager.iedriver().setup();
			}else
				System.setProperty("webdriver.ie.driver", "driver\\IEDriverServer.exe");

			InternetExplorerOptions ieOptions=new InternetExplorerOptions(capabilities);
			driver = new InternetExplorerDriver(ieOptions);
			break;
		case "chromeheadless":
			if(autoWebDriverManager.toLowerCase().equals("yes")) {
				WebDriverManager.chromedriver().setup();
			}else
				System.setProperty("webdriver.chrome.driver", "driver\\chromedriver.exe");
			
			final ChromeOptions chromeOptions = new ChromeOptions();
			capabilities.setCapability(CapabilityType.BROWSER_NAME, BrowserType.CHROME);
			capabilities.setCapability(ChromeOptions.CAPABILITY, chromeOptions);
			break;
		case "Firefox":
			if (!System.getProperty("os.name").equalsIgnoreCase("linux")) {
				System.setProperty("webdriver.gecko.driver", "driver\\geckodriver.exe");
			} else {
				System.setProperty("webdriver.gecko.driver", "linuxgrid/geckodriver");
			}
			capabilities.setCapability(CapabilityType.BROWSER_NAME, BrowserType.FIREFOX);
			driver = new FirefoxDriver();
			break;
		default:
			if (!System.getProperty("os.name").equalsIgnoreCase("linux")) {
				System.setProperty("webdriver.chrome.driver", "driver\\chromedriver.exe");
			} else {
				System.setProperty("webdriver.chrome.driver", "linuxgrid/chromedriver");
			}
			break;
		}
		threadDriver.set(driver);

	}

	public BrowserMobProxy getProxy() {
		return proxy;
	}
	public BrowserMobProxy getProxyServer() {

		proxy = new BrowserMobProxyServer();
		proxy.enableHarCaptureTypes(CaptureType.getAllContentCaptureTypes());
		proxy.enableHarCaptureTypes(CaptureType.getCookieCaptureTypes());
		proxy.enableHarCaptureTypes(CaptureType.getRequestCaptureTypes());
		proxy.enableHarCaptureTypes(CaptureType.getResponseCaptureTypes());
		proxy.enableHarCaptureTypes(CaptureType.getBinaryContentCaptureTypes());
		proxy.enableHarCaptureTypes(CaptureType.getNonBinaryContentCaptureTypes());
		proxy.setTrustAllServers(true);
		ExecutionState.mobProxyPorts++;
		proxy.start(ExecutionState.mobProxyPorts);

		return proxy;
	}

	public Proxy getSeleniumProxy(BrowserMobProxy proxyServer) {
		Proxy seleniumProxy = ClientUtil.createSeleniumProxy(proxyServer);
		try {
			String hostIp = Inet4Address.getLocalHost().getHostAddress();
			System.out.println(Inet4Address.getLocalHost().getHostAddress() + ":" + proxyServer.getPort());
			seleniumProxy.setHttpProxy(hostIp + ":" + proxyServer.getPort());
			seleniumProxy.setSslProxy(hostIp + ":" + proxyServer.getPort());
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		return seleniumProxy;
	}

	public boolean isMobileBrowser() {
		return platform.equalsIgnoreCase("Mobile");
	}

	private BrowserMobProxy proxy;

	

}
