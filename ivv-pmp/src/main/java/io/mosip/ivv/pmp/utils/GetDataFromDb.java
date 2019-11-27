package io.mosip.ivv.pmp.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import io.mosip.ivv.core.structures.CallRecord;
import io.mosip.ivv.core.utils.Utils;

public class GetDataFromDb {

	public static void logCallRecord(CallRecord c){
        Utils.auditLog.info(c.getUrl());
        Utils.auditLog.info(c.getInputData());
        Utils.auditLog.info(c.getResponse().asString());
    }
	
	public static Connection dbConnect() {
		String dbUrl = "jdbc:postgresql://localhost:5432/mosip_pmp";
		String dbUser = "postgres";
		String dbPassword = "postgres";
		Connection dbConnectionObject = null;
		try {
			dbConnectionObject = DriverManager.getConnection(dbUrl, dbUser, dbPassword);
		} catch (SQLException e) {
			Utils.auditLog.severe(e.getMessage());
		}
		return dbConnectionObject;
	}
	
	public static String getPolicyId(String name) {
		String sqlStatement = "select * from policy_group where name = " +"'" + name + "'";
		String policyId = "";
		try {
			Connection dbConnection = dbConnect();
			Statement statement = dbConnection.createStatement();
			ResultSet rs = statement.executeQuery(sqlStatement);
			while(rs.next()){
			policyId = rs.getString("id");
			}
			Utils.auditLog.info("Pmp data removed successfully.");
		} catch (SQLException e) {
			Utils.auditLog.severe(e.getMessage());
		}
		
		return policyId;
	}
}
