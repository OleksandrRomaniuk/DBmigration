package Oleksandr.Romaniuk;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Enumeration;
//import com.mysql.jdbc.Driver;

public class readAndUpdateDTB
{
	
	private ConnectToDTB connect = null;
	private Connection connection = null;
	
	
	public readAndUpdateDTB(String url, String login, String password)
	{
		connect = ConnectToDTB.getInstance(url, login, password);
		connection = connect.getConnection();
	}
	
	
	
	public void selectAll(String table)
	{
		
		Statement stmt=null;
		ResultSet rs=null;
		ResultSetMetaData rsmd = null;
		StringBuilder stb = null;
		try
		{
			
			stmt = connection.createStatement();
			rs = stmt.executeQuery("SELECT * FROM "+table);
			
			rsmd = rs.getMetaData(); //to retrieve table name, column name, column type and column precision, etc..
			int colCount = rsmd.getColumnCount();
			
				while (rs.next())
				{
					for (int i = 1; i <= colCount; i++)
					{
					
					System.out.printf("%-10s \t\t", rs.getString(i).trim());
					
					}
					System.out.println();
				}
			
			
		} catch (SQLException e)
		{
			System.out.println("SQLException: " + e.getMessage());
		    System.out.println("SQLState: " + e.getSQLState());
		    System.out.println("VendorError: " + e.getErrorCode());
		}
		finally
		{
			if(rs!=null)
				try
				{
					rs.close();
				} catch (SQLException e1)
				{
					System.out.println("Could not release the ResultSet. SQLException: " + e1.getMessage());
				}
					
			if(stmt!=null)
				try
				{
					stmt.close();
				} catch (SQLException e1)
				{
					System.out.println("Could not release the Statement. SQLException: " + e1.getMessage());
				}
			/*
			try
			{
				connection.close();
				
			} catch (SQLException e)
			{
				System.out.println("Could not close the connection. SQLException: " + e.getMessage());
			}
			*/
		}
	}
	
	public void insert(String table, int ID, String Name, String CountryCode, String District, int Population)
	{
		
		PreparedStatement stmt = null;
		try
		{
			stmt = connection.prepareStatement("INSERT INTO "+table+" (ID, Name, CountryCode, District, Population) VALUES (?, ?, ?, ?, ?)");
	        stmt.setInt(1, ID);
	        stmt.setString(2, Name);
	        stmt.setString(3, CountryCode);
	        stmt.setString(4, District);
	        stmt.setInt(5, Population);
	        stmt.executeUpdate();
		} catch (SQLException e)
		{
			System.out.println("SQLException: " + e.getMessage());
		}
        finally
        {
        	
        	if(stmt!=null)
        try
		{
			stmt.close();
		} catch (SQLException e)
		{
			System.out.println("Could not close the Statement. SQLException: " + e.getMessage());
		}
        	/*
        	try
			{
				connection.close();
				
			} catch (SQLException e)
			{
				System.out.println("Could not close the connection. SQLException: " + e.getMessage());
			}
			*/
        }
	}
	
	public void deleteAll(String dtb_table)
	{
		PreparedStatement stmt = null;
		try
		{
			stmt = connection.prepareStatement("DELETE FROM "+dtb_table);
			stmt.executeUpdate();
		} catch (SQLException e)
		{
			System.out.println("Could not delete rows. SQLException: " + e.getMessage());
		}
		finally
		{
			if(stmt!=null)
		        try
				{
					stmt.close();
				} catch (SQLException e)
				{
					System.out.println("Could not close the Statement. SQLException: " + e.getMessage());
				}
			/*
			try
			{
				connection.close();
				
			} catch (SQLException e)
			{
				System.out.println("Could not close the connection. SQLException: " + e.getMessage());
			}
			*/
		}
	}
	
	public void replicateAll(String dtb_table, String dtb_table_source)
	{
		Statement stmt=null;
		ResultSet rs=null;
		try
		{
			stmt = connection.createStatement();
			rs = stmt.executeQuery("SELECT * FROM "+dtb_table_source);
		} catch (SQLException e)
		{
			System.out.println("SQLException: " + e.getMessage());
		    System.out.println("SQLState: " + e.getSQLState());
		    System.out.println("VendorError: " + e.getErrorCode());
		}
		
		
		try
		{
			
			while (rs.next())
			{
				System.out.println(rs.getObject(2));
			}
		} catch (SQLException e)
			{
				
				e.printStackTrace();
			}
		
		
	 }
}
		

