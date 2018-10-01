package Oleksandr.Romaniuk;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Level;

public class TableBuilder
{
	
	private static TableBuilder instance = null;
	
	private TableBuilder() {};
	
	public static TableBuilder getInstance()
	{
		if (instance == null)
			instance = new TableBuilder();
		
		return instance;
	}
	
	public void replicateTables(Connection sourceConnection, Connection targetConnection)
	{
		String sourceConnectionType = DTBtype.getInstance().getDTBtype(sourceConnection);
		String targetConnectionType = DTBtype.getInstance().getDTBtype(targetConnection);
	try
	{
		String[] tableListSource = TableList.getInstance().getTableList(sourceConnection, sourceConnectionType);
		String[] tableListTarget = TableList.getInstance().getTableList(targetConnection, targetConnectionType);
		
		ReplicateTables.getInstance().replicate(sourceConnection, targetConnection);
		ForeignKeys.getInstance().setPrimaryKeys(tableListSource, sourceConnection, targetConnection);
		ForeignKeys.getInstance().setForeignKeyConstraints(tableListSource, tableListTarget, sourceConnection, targetConnection);
	}
	catch(Exception e)
	{
		System.out.println(e.getMessage());
		Log.getLog().doLogging(Level.SEVERE, e);
	}
	finally
	{
		try
		{
			if(targetConnection != null && !targetConnection.isClosed())
			{
				targetConnection.close();
			}
			
		} catch (SQLException e)
		{
			System.out.println("Can not close the target connection to the database");
			System.out.println("SQLException: " + e.getMessage());
			System.out.println("SQLState: " + e.getSQLState());
			System.out.println("VendorError: " + e.getErrorCode());
			Log.getLog().doLogging(Level.SEVERE, e);
		}
		
		try
		{
			if(sourceConnection != null && !sourceConnection.isClosed())
			{
				sourceConnection.close();
			}
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
