package com.zensar.utilities;

import java.net.InetAddress;

public class ExecutionEnvironment {
	private ExecutionEnvironment(){}
	
	private static boolean isPropertyAvailable(String key) {
		try {
			return (System.getenv(key) != null);
		} catch (Exception e) {
			return false;
		}
	}
	
	public static boolean isJenkinsBuild() {
		try {
			return isPropertyAvailable("BUILD_NUMBER");
		} catch (Exception e) {
			return false;
		}
	}
	
	public static String getBuildNumber() {
		try {
			return System.getenv("BUILD_NUMBER");
		} catch (Exception e) {
			return null;
		}
	}
	
	public static String getJobName() {
		try {
			return System.getenv("JOB_BASE_NAME");
		} catch (Exception e) {
			return null;
		}
	}
	
	public static String getTags() {
		try {
			return System.getenv("TAGS");
		} catch (Exception e) {
			return null;
		}
	}
	
	public static String getRunConfiguration() {
		try {
			return System.getenv("EXE_CONF");
		} catch (Exception e) {
			return null;
		}
	}
	
	public static String getHostIp() {
		try {
			InetAddress inetAddress = InetAddress.getLocalHost();
			return inetAddress.getHostAddress();
		} catch (Exception e) {
			return null;
		}
	}
	
	public static String getHostName() {
		try {
			InetAddress inetAddress = InetAddress.getLocalHost();
			return inetAddress.getHostName();
		} catch (Exception e) {
			return null;
		}
	}
	
	public static String getWindowsUserName() {
		try {
			return System.getProperty("user.name");
		}catch (Exception e) {
			return null;
		}
	}
	
	public static void main(String[] args) {
		System.out.println(getWindowsUserName());
	}
	
	
}
