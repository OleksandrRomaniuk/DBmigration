package Oleksandr.Romaniuk;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Properties;
import java.util.logging.Level;

public class CreateDTB
{
	private static CreateDTB instance;
	
	private CreateDTB()
	{
	}
	
	public static CreateDTB getInstance()
	{
		if(instance == null)
			instance = new CreateDTB();
		return instance;
	}
	
		public void createNewDTB(String dbName, Connection connection, String dbType)
		{
			
			
			if (dbType.equals("mysql") || dbType.equals("postgresql"))
			{
				
				if(!checkIfDTBExist(dbName, dbType))
				{
					if(creatingNewDTB(dbName, ConnectToDTB.getInstance().getDefaultConnection(dbType), dbType))
						NewWindowCrNewDB.getInstance().alertWindow("DB created", "The database has been successfully created.");
					else
						NewWindowCrNewDB.getInstance().alertWindow("DB created", "The database was not created.");
				}
				else 
				{
					NewWindowCrNewDB.getInstance().alertWindow("DB exists", "The database with such name already exists");
				}
			}
			else
			{
				Log.getLog().doLogging(Level.WARNING, "We have not created a solution to create a DTB for this type. We appologize for that.");
			}
		}
	
		private boolean creatingNewDTB(String dbName, Connection connection, String DTBtype)
		{
			Boolean result = false;
			Statement stmt = null;

				try
				{	
					stmt = connection.createStatement();
					stmt.executeUpdate("CREATE DATABASE " + dbName);
					ConnectionPropertiesBuilder.getInstance().addElement(dbName, DTBtype);
					result = true;
				} catch (SQLException e)
				{
					System.out.println("SQLException: " + e.getMessage());
				    System.out.println("SQLState: " + e.getSQLState());
				    System.out.println("VendorError: " + e.getErrorCode());
				    result = false;
				    Log.getLog().doLogging(Level.SEVERE, e);
				}
				finally
				{
					try
					{
						if(stmt != null)
						stmt.close();
					} catch (SQLException e)
					{
						System.out.println("SQLException: " + e.getMessage());
					    System.out.println("SQLState: " + e.getSQLState());
					    System.out.println("VendorError: " + e.getErrorCode());
						System.out.println("The statement has not been closed");
						Log.getLog().doLogging(Level.SEVERE, e);
					}
				}
			
			return result;
		}
		
		public void removeDB(String dbName, Connection connection, String dbType)
		{
			if (dbType.equals("mysql") || dbType.equals("postgresql"))
			{
				if(checkIfDTBExist(dbName, dbType))
				{
					if (removeDTB(dbName, connection, dbType))
						NewWindowCrNewDB.getInstance().alertWindow("DB removed", "The database has been successfully removed.");
					else
						NewWindowCrNewDB.getInstance().alertWindow("DB removed", "The database was not removed.");
				}
				else 
				{
					NewWindowCrNewDB.getInstance().alertWindow("DB does not exist", "The database with such name does not exist");
				}
			}
			else
			{
				Log.getLog().doLogging(Level.SEVERE, "We have not created a solution to create a DTB for this type. We appologize for that.");
			}
		}
	
		private boolean removeDTB(String dbName, Connection connection, String DTBtype)
		{
			boolean result = false;
			Statement stmt = null;
			
				try
				{	
					stmt = connection.createStatement();
					stmt.executeUpdate("DROP DATABASE " + dbName);
					ConnectionPropertiesBuilder.getInstance().deleteElement(dbName);
					result = true;
				} catch (SQLException e)
				{
					System.out.println("SQLException: " + e.getMessage());
				    System.out.println("SQLState: " + e.getSQLState());
				    System.out.println("VendorError: " + e.getErrorCode());
				    result = false;
				    Log.getLog().doLogging(Level.SEVERE, e);
				}
				finally
				{
					try
					{
						if(stmt != null)
						stmt.close();
						
					} catch (SQLException e)
					{
						System.out.println("SQLException: " + e.getMessage());
					    System.out.println("SQLState: " + e.getSQLState());
					    System.out.println("VendorError: " + e.getErrorCode());
						System.out.println("The statement has not been closed");
						Log.getLog().doLogging(Level.SEVERE, e);
					}
				}
				return result;
		}
		
		private boolean checkIfDTBExist(String dbName, String dbType)
		{
			boolean result = false;
			
			ArrayList<String> dbList = ConnectToDTB.getInstance().getListOfDBs(dbType); 
			
			for(String name: dbList) 
				{
				    if(name.equals(dbName))
				    {
				        result = true;
				        break;
				    }
				}
			return result;
		}
	}