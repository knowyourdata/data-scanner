package at.fwd.data_scanner.service;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.log4j.Logger;

import at.fwd.data_scanner.dto.ColumnDTO;
import at.fwd.data_scanner.dto.TableDTO;

public class ScannerJdbcService {
	
	private static final Logger log = Logger.getLogger(ScannerJdbcService.class);
	
	public List<TableDTO>  checkDatabase(String jdbcUrl, String username, String password, List<String> excludeTables, boolean checkForeignkeys) throws SQLException {
		List<TableDTO> tableList = new ArrayList<TableDTO>();
		
		Connection conn = getConnection(jdbcUrl, username, password);
		DatabaseMetaData databaseMetaData = conn.getMetaData();
		
		// TABLE_TYPE "TABLE"
		ResultSet resultSet = databaseMetaData.getTables(null, null, null, new String[] { "TABLE" });
		while (resultSet.next()) {
			String tablename = resultSet.getString("TABLE_NAME");
			//log.info("tablename: " + tablename);
			if (!excludeTables.contains(tablename)) {
				TableDTO tableDTO = readColumns(databaseMetaData, tablename, checkForeignkeys);
				
//				for (int i = 1; i < 15; i++) {
//					log.info("rs: " + i + ":" + resultSet.getString(i));
//				}
//				System.exit(1);
				
				String remarks = resultSet.getString("REMARKS");
				tableDTO.setRemarks(remarks);
				//log.info("remarks: " + remarks);
				tableList.add(tableDTO);	
			}
			
		}
		resultSet.close();
		
		// TABLE_TYPE "VIEW"
		boolean processViews = false;
		if (processViews) {
			resultSet = databaseMetaData.getTables(null, null, null, new String[] { "VIEW" });
			while (resultSet.next()) {
				String tablename = resultSet.getString("TABLE_NAME");
				
				
				
				if (!excludeTables.contains(tablename)) {
					TableDTO tableDTO = readColumns(databaseMetaData, tablename, checkForeignkeys);
					
					String remarks = resultSet.getString("REMARKS");
					tableDTO.setRemarks(remarks);
					
					tableList.add(tableDTO);	
				}
			}
			resultSet.close();
		}
		
		conn.close();
		
		return tableList;
	}


	protected Connection getConnection(String jdbcUrl, String username, String password) throws SQLException {

		Connection conn = null;
		Properties connectionProps = new Properties();
		connectionProps.put("user", username);
		connectionProps.put("password", password);

		conn = DriverManager.getConnection(jdbcUrl, connectionProps);
		//System.out.println("Connected to database");
		return conn;
	}
	
	protected TableDTO readColumns(DatabaseMetaData databaseMetaData, String tablename, boolean checkForeignKeys) throws SQLException {
		TableDTO tableDTO = new TableDTO();
		tableDTO.setTablename(tablename);
				
		List<ColumnDTO> columnList = new ArrayList<ColumnDTO>();
		ResultSet columns = databaseMetaData.getColumns(null,null, tablename, null);
		
//		int columnsNumber = columns.getMetaData().getColumnCount();
//		System.out.println(columnsNumber);
//		for (int i = 1; i < columnsNumber; i++) {
//			System.out.println("col(" + i + "):" + columns.getMetaData().getColumnName(i));
//		}
		
		while(columns.next()) {
//			for (int i = 1; i < columnsNumber; i++) {
//				System.out.println("col (" + i + "): " + columns.getString(i));
//			}
			
		    String columnname = columns.getString("COLUMN_NAME");
		    String datatype = columns.getString("DATA_TYPE");
		    String type_name = columns.getString("TYPE_NAME");
		    String columnsize = columns.getString("COLUMN_SIZE");
		    String decimaldigits = columns.getString("DECIMAL_DIGITS");
		    String isNullable = columns.getString("IS_NULLABLE");
		    String is_autoIncrment = columns.getString("IS_AUTOINCREMENT");
		    String remarks = columns.getString("REMARKS");
		    //Printing results
		    log.debug(columnname + "---" + datatype + "---" + columnsize + "---" + decimaldigits + "---" + isNullable + "---" + is_autoIncrment + "---" + remarks);
		    
		    ColumnDTO columnDTO = new ColumnDTO();
		    columnDTO.setColumnname(columnname);
		    columnDTO.setTypename(type_name);
		    columnDTO.setColumnsize(columnsize);
			columnDTO.setIsNullable(isNullable);
			columnDTO.setIsAutoIncrement(is_autoIncrment);
			columnDTO.setRemarks(remarks);
		    
		    columnList.add(columnDTO);
//		    System.exit(1);
			
		}
		
		columns.close();
		
		tableDTO.setColumnList(columnList);
		
		
		return tableDTO;
		
		

	}

	
}
