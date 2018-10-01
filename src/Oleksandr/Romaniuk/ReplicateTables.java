package Oleksandr.Romaniuk;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.logging.Level;

public class ReplicateTables
{
	private static ReplicateTables instance;
	
	private ReplicateTables()
	{
	}
	
	public static ReplicateTables getInstance()
	{
		if(instance == null)
			instance = new ReplicateTables();
		
		return instance;
	}
	
	public void replicate(Connection sourceConnection, Connection targetConnection)
	{
		
	ArrayList<String> tableList = new ArrayList<String>();
		
	ResultSet rs = null;
	
	try
	{
		rs = sourceConnection.getMetaData().getTables(sourceConnection.getCatalog(), null, "%", new String[]{"TABLE"});
		while (rs.next())
		{
			tableList.add(rs.getString(3));
		}
	} catch (SQLException e)
	{
		System.out.println("SQLException: " + e.getMessage());
	    System.out.println("SQLState: " + e.getSQLState());
	    System.out.println("VendorError: " + e.getErrorCode());
	    Log.getLog().doLogging(Level.SEVERE, e);
	}
	finally
	{
		try
		{
			rs.close();
		} catch (SQLException e)
		{
			System.out.println("Can not close the resulSset");
			System.out.println("SQLException: " + e.getMessage());
		    System.out.println("SQLState: " + e.getSQLState());
		    System.out.println("VendorError: " + e.getErrorCode());
		    Log.getLog().doLogging(Level.SEVERE, e);
		}
	}

	for (String tableName: tableList)
	{
		if(!ifTableExist(tableName.toLowerCase(), targetConnection))
		{
		workOutTables(tableName, sourceConnection, targetConnection);
		}
		else
		{
			Log.getLog().doLogging(Level.SEVERE, "The table "+tableName+" exists. \n");
		}
	}
	}
	
	private boolean ifTableExist(String tableName, Connection targetConnection)
	{
		ArrayList<String> tableList = new ArrayList<String>();
		boolean tableExist = false;
		ResultSet rs = null;
		
		try
		{
			rs = targetConnection.getMetaData().getTables(targetConnection.getCatalog(), null, "%", new String[]{"TABLE"});
			
			while (rs.next())
			{
				tableList.add(rs.getString(3));
			}
		} catch (SQLException e)
		{
			System.out.println("SQLException: " + e.getMessage());
		    System.out.println("SQLState: " + e.getSQLState());
		    System.out.println("VendorError: " + e.getErrorCode());
		    Log.getLog().doLogging(Level.SEVERE, e);
		}
		
		for(String tableNameTagret: tableList)
		{
			if(tableNameTagret.equals(tableName))
				tableExist = true;
		}
		return tableExist;
		
	}

	private void workOutTables(String tableName, Connection sourceConnection, Connection targetConnection)
	{
	
	Statement stmt = null;
	ResultSet rs = null;
	ResultSetMetaData rsmd = null;
	PreparedStatement prsm = null;
	
	try
	{
		stmt = sourceConnection.createStatement();
	
	String sql = "select * from "+tableName;
	rs = stmt.executeQuery(sql);
	rsmd = rs.getMetaData();
	int columnCount = rsmd.getColumnCount();
	
	StringBuilder sb = new StringBuilder();
	
	if (columnCount > 0) 
	{ 
	    sb.append("Create table ").append(rsmd.getTableName(1)).append(" (");
	}
	
	for (int i = 1; i <= columnCount; i ++) 
	{
	    if (i > 1) sb.append(", ");
	    String columnName = rsmd.getColumnLabel(i);
	    
	    String columnTypeName = rsmd.getColumnTypeName(i);
	    
	    if(columnTypeName.contains(" "))
	    	columnTypeName = columnTypeName.substring(0, columnTypeName.indexOf(" "));
	    
	    String columnType = DataTypes.valueOf(columnTypeName).getDataType();
	    
	    
	    
	    sb.append(columnName).append( " " );
	    
	    if (!rsmd.isAutoIncrement(i))
	    	{
	    		sb.append(columnType);
	    	}
	    
	    int precision = rsmd.getPrecision(i);
	    
	    if (precision != 0 && columnType != "INTEGER" && columnType != "SMALLINT" && columnType != "REAL" && columnType != "TEXT")
	    {
	        sb.append("(").append(precision).append(") ");
	    }
	    
	    if (rsmd.isAutoIncrement(i))
	    {
	    	sb.append(" SERIAL");
	    }
	    else
	    {
	    	if (rsmd.isNullable(i) == 1)
	    	{
	    		sb.append(" NULL");
	    	}
	    	else 
	    	{
	    		sb.append(" NOT NULL");
	    	}
	    }
	} 
	sb.append( ") " );
	
	prsm = targetConnection.prepareStatement(sb.toString());
	
	prsm.execute();
	
	} catch (SQLException e)
	{
		System.out.println("SQLException: " + e.getMessage());
	    System.out.println("SQLState: " + e.getSQLState());
	    System.out.println("VendorError: " + e.getErrorCode());
	    Log.getLog().doLogging(Level.SEVERE, e);
	}
	finally
	{
		try
		{
			if(stmt != null)
			stmt.close();
			if(rs != null)
			rs.close();
			if(prsm != null)
			prsm.close();
		} catch (SQLException e)
		{
			System.out.println("SQLException: " + e.getMessage());
		    System.out.println("SQLState: " + e.getSQLState());
		    System.out.println("VendorError: " + e.getErrorCode());
		    Log.getLog().doLogging(Level.SEVERE, e);
		}
	}
	}
}

enum DataTypes
{
	TINYINT("SMALLINT"),
	SMALLINT("SMALLINT"),
	MEDIUMINT("INTEGER"),
	BIGINT("BIGINT"),
	BIT("BOOLEAN"),
	INT("INTEGER"),
	FLOAT("REAL"),
	DOUBLE("DOUBLE PRECISION"),
	DECIMAL("DECIMAL"),
	CHAR("CHAR"),
	LONGTEXT("CHAR"),
	VARCHAR("VARCHAR"),
	DATE("DATE"),
	TIME("TIME"),
	DATETIME("TIMESTAMP"),
	LONGBLOB("BYTEA"),
	TEXT("TEXT"),
	TIMESTAMP("TIMESTAMP"),
	GEOMETRY("TEXT"),
	YEAR("VARCHAR"),
	BLOB("VARCHAR");
	
	private String dataType;
	
	public String getDataType() 
	{
		return this.dataType;
	}
	
	DataTypes(String dataType) 
	{
		this.dataType = dataType;
	}
	
}