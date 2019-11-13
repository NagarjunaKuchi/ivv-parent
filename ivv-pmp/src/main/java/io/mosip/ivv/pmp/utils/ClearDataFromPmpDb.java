package io.mosip.ivv.pmp.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import io.mosip.ivv.core.structures.CallRecord;
import io.mosip.ivv.core.utils.Utils;

public abstract class ClearDataFromPmpDb {
	  public static void logCallRecord(CallRecord c){
        Utils.auditLog.info(c.getUrl());
        Utils.auditLog.info(c.getInputData());
        Utils.auditLog.info(c.getResponse().asString());
    }

	public static void deleteData()
			throws SQLException {
		String sqlStatement = "delete from misp_license;delete from misp;delete from partner_policy;delete from partner_policy_request;delete from partner;";
		try {   Connection dbConnection = dbConnect();
				Statement statement = dbConnection.createStatement();
				 statement.executeUpdate(sqlStatement);
				 Utils.auditLog.info("Pmp data removed successfully.");
		 	} catch (SQLException e) {
			Utils.auditLog.severe(e.getMessage());
		}

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
}

