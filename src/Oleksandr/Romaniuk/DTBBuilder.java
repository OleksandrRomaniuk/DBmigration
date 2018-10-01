package Oleksandr.Romaniuk;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Level;

import sun.reflect.Reflection;

public class DTBBuilder
{
	private static DTBBuilder instance;
	
	private DTBBuilder()
	{
	}
	
	public static DTBBuilder getInstance()
	{
		if(instance == null)
			instance = new DTBBuilder();
		
		return instance;
	}
	
	public void creatDTB(String databaseName, Connection targetConnection)
	{
		
		try
		{	
			String DTBtypes = DTBtype.getInstance().getDTBtype(targetConnection);
		
			CreateDTB.getInstance().createNewDTB(databaseName, targetConnection, DTBtypes);
		}
		catch (Exception e)
		{
			System.out.println(e.getMessage());
			Log.getLog().doLogging(Level.SEVERE, e);
		}
		finally
		{
			try
			{
				if(!(targetConnection == null))
					targetConnection.close();
			} catch (SQLException e)
			{
				System.out.println("Can not close the target connection to the database");
				System.out.println("SQLException: " + e.getMessage());
				System.out.println("SQLState: " + e.getSQLState());
				System.out.println("VendorError: " + e.getErrorCode());
				Log.getLog().doLogging(Level.SEVERE, e);
			}
		}
	}
}
