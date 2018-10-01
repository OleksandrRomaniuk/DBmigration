package Oleksandr.Romaniuk;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Table
{
	private static Table instance;
	
	private Table() {}
	
	public static Table getInstance()
	{
		if (instance == null)
			instance = new Table();
		
		return instance;
	}
	
	public void copyTable(String tableName, Connection sourceConnection, Connection targetConnection, String DTBtype)
	{
        PreparedStatement ps = null;
        ResultSet rs = null;
        PreparedStatement inst = null;
        
        try
        {
        	if (!DTBtype.equals("mysql"))
        	{
        		Log.getLog().doLogging(Level.SEVERE, new Exception("No solution for this type of DTB"));
        		throw new Exception("No solution for this type of DTB");
        	}
        	else 
        		tableName = tableName.toLowerCase();
        }
        catch (Exception e)
        {
        	System.out.println(e.getMessage());
        }
        
        String sql = "select * from " + tableName;
        
        try
		{
			ps = sourceConnection.prepareStatement(sql);
		
        rs = ps.executeQuery();
        
        Log.getLog().doLogging(Level.INFO, Thread.currentThread().getStackTrace()[1].getClassName()+": "
        		+Thread.currentThread().getStackTrace()[1].getMethodName()+" - "+"Execute query successful: "+sql);
        
        ResultSetMetaData metadata = ps.getMetaData();
        int colCount = metadata.getColumnCount();

        StringBuilder sqlInsert = new StringBuilder("INSERT INTO " + tableName + "(");
        
        for (int c = 1; c <= colCount; c++) 
        {
            sqlInsert.append(((c == 1 ? "" : ",") + metadata.getColumnName(c).toLowerCase()));
        }
        
        sqlInsert.append(") VALUES (");
        
        for (int c = 1; c <= colCount; c++) 
        {
            sqlInsert.append(((c == 1 ? "" : ",") + "?"));
        }
        
        sqlInsert.append(")");

        //int row = 0;
        Object[] line = new Object[colCount];
        inst = targetConnection.prepareStatement(sqlInsert.toString());
        while (rs.next())
        {
            for (int c = 1; c <= colCount; c++) 
            {
                //Object ceilvalue = rs.getObject(c);
                line[c - 1] = rs.getObject(c);
            }
            insertRow(inst, line);
            
            
        }
		} catch (SQLException e)
		{
			System.out.println("Could not insert the data into the DTB");
			System.out.println("SQLException: " + e.getMessage());
		    System.out.println("SQLState: " + e.getSQLState());
		    System.out.println("VendorError: " + e.getErrorCode());
		    Log.getLog().doLogging(Level.SEVERE, e);
		}
        finally
        {
        	try
        	{
        		inst.close();
        		rs.close();
        		ps.close();
        	} catch (SQLException e)
        	{
        		System.out.println("SQLException: " + e.getMessage());
    		    System.out.println("SQLState: " + e.getSQLState());
    		    System.out.println("VendorError: " + e.getErrorCode());
    		    Log.getLog().doLogging(Level.SEVERE, e);
        	}
        }
    }

	private void insertRow(PreparedStatement ps, Object[] line) 
	{
		try
		{
        for (int n = 1; n <= line.length; n++) {
            
				ps.setObject(n, line[n - 1]);
        }
    
	        ps.execute();
	        
	        Log.getLog().doLogging(Level.INFO, Thread.currentThread().getStackTrace()[1].getClassName()+": "
	        		+Thread.currentThread().getStackTrace()[1].getMethodName()+" - "+"Execute query successful: "+ps.toString());
		} catch (SQLException e)
		{
			System.out.println("Could not add the row");
			System.out.println("SQLException: " + e.getMessage());
		    System.out.println("SQLState: " + e.getSQLState());
		    System.out.println("VendorError: " + e.getErrorCode());
		    Log.getLog().doLogging(Level.SEVERE, e);
		}
    }
}
