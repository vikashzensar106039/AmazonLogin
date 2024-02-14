package com.zensar.excel;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.map.HashedMap;

import com.codoid.products.exception.FilloException;
import com.codoid.products.fillo.Recordset;

public class ExcelOutput extends ExcelConnectionManager{

	
	public synchronized static boolean setOutputDataValue(String sheetName, String key, String value, int currentRowId){
		value = value.replaceAll("'", " ");
		String query = "Update `" + sheetName + "` Set `" + key + "`='" + value + "' where ID='"
				+ currentRowId + "'";
		try {
			
			getOutputSheetConnection().executeUpdate(query);
			return true;
		} catch (FilloException e) {
			System.out.println("Problem while updating output data value");
			e.printStackTrace();
			return false;
		}
	}
	public static synchronized Recordset executeSelectStatement(String query) throws Throwable {
		return executeSelectStatement(query, getOutputSheetConnection());
	}

	public synchronized static List fetchData(String sheetName) {
		System.out.println(">>>Excelout  " + sheetName);
		Recordset recordset;
		String strQuery = "Select * from `" + sheetName + "`";
		try {
			recordset = getOutputSheetConnection().executeQuery(strQuery);

			
			List<Map<String, String>> result = new ArrayList<>();
			while (recordset.next()) {
				Map<String, String> rowCol = new HashedMap();
				for (String name : recordset.getFieldNames()) {
					rowCol.put(name, recordset.getField(name));
				}
				
				result.add(rowCol);
			}
			return result;
		} catch (FilloException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static synchronized boolean insertOutputDataValue(String sheetName, int currentRowId, String value)  {
        value = value.replaceAll("'", " ");
        
        String query = "INSERT INTO " + sheetName + " (Email) "+  "VALUES('"+value+"')";
        try {
			return executeInsertStatement(query, getOutputSheetConnection());
		} catch (FilloException e) {
			
			e.printStackTrace();
			return false;
		}
		
    }
	
	
	
	
	public synchronized static boolean executeInsertStatement(String strQuery) {
		try {
			getOutputSheetConnection().executeUpdate(strQuery);
		} catch (FilloException e) {
			System.out.println("Can't create entry for scenario in Output file.");
			e.printStackTrace();
		}
		return true;
	}
	
	public static void init() {
		try {
			getInputSheetConnection();
		} catch (FilloException e) {
			e.printStackTrace();
		}
	}
	
}
