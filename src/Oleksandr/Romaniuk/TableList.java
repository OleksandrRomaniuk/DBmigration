package Oleksandr.Romaniuk;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;

public class TableList
{
	private static TableList instance;
	
	private TableList() {}
	
	public static TableList getInstance()
	{
		if (instance == null)
			instance =new TableList();
		
		return instance;
	}
	
	public String[] getTableList(Connection connection, String DTBtype) 
	{
		ArrayList<String> list = new ArrayList<String>();
        PreparedStatement ps = null;
        ResultSet rs = null;
        String sql = null;
    
	if (DTBtype.equals("oracle"))
	{
			
            sql = "select table_name from user_tables";
	}
	
        else if (DTBtype.equals("mysql")) 
	{
            try
			{
				sql = "select table_name from INFORMATION_SCHEMA.TABLES "
				        + "where table_type='BASE TABLE' and table_schema='"
				        + connection.getCatalog() + "'";
			} catch (SQLException e)
			{
				System.out.println("SQLException: " + e.getMessage());
			    System.out.println("SQLState: " + e.getSQLState());
			    System.out.println("VendorError: " + e.getErrorCode());
			    Log.getLog().doLogging(Level.SEVERE, e);
			}
        } 
	
        else if (DTBtype.equals("postgresql")) 
    	{
                try
    			{
    				sql = "select table_name from INFORMATION_SCHEMA.TABLES "
    				        + "where table_type='BASE TABLE' and table_schema='public' and table_catalog='"
    				        + connection.getCatalog() + "'";
    			} catch (SQLException e)
    			{
    				System.out.println("SQLException: " + e.getMessage());
    			    System.out.println("SQLState: " + e.getSQLState());
    			    System.out.println("VendorError: " + e.getErrorCode());
    			    Log.getLog().doLogging(Level.SEVERE, e);
    			}
            }
	
	else if (DTBtype.equals("sqlserver")) 
	{
            sql = "select table_name from INFORMATION_SCHEMA.TABLES "
                    + "where table_type='BASE TABLE'";
    } 
	
	else if (DTBtype.equals("hsqldb")) 
	{
            sql = "select table_name from INFORMATION_SCHEMA.SYSTEM_TABLES where TABLE_TYPE='TABLE'";

        } 

	else 
	{
            System.out.println("ERROR: Unknown data dictionary:" + connection.getClass().getCanonicalName());
    }
            try
            {
            	if (sql != null)
            	{
				ps = connection.prepareStatement(sql);
				rs = ps.executeQuery();
				
						while (rs.next()) 
						{
		                list.add(rs.getString(1));
		            	}
				
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
					ps.close();
				} catch (SQLException e)
				{
					System.out.println("SQLException: " + e.getMessage());
				    System.out.println("SQLState: " + e.getSQLState());
				    System.out.println("VendorError: " + e.getErrorCode());
				    Log.getLog().doLogging(Level.SEVERE, e);
				}
            }

        String[] tabList = new String[list.size()];
        int n = 0;
        for (String t : list) 
        {
            tabList[n++] = t;
        }
        
	return tabList;
    }
}