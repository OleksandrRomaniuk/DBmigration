package Oleksandr.Romaniuk;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Locale;
import java.util.logging.Level;

public class Migration
{
	private static Migration instance;
	
	private Migration() {}
	
	public static Migration getInstance()
	{
		if (instance == null)
			instance = new Migration();
		
		return instance;
	}
	
	public void migrate(Connection SourceConnection, Connection TargetConnection)
	{
			
        	Locale.setDefault(Locale.ENGLISH);
            
        	String DTBtypeTarget = DTBtype.getInstance().getDTBtype(TargetConnection);
        	String DTBtypeSource = DTBtype.getInstance().getDTBtype(SourceConnection);
        	
        	String[] tableListSource = TableList.getInstance().getTableList(SourceConnection, DTBtypeSource);
        	String[] tableListTarget = TableList.getInstance().getTableList(TargetConnection, DTBtypeTarget);
        	
        	ForeignKeys.getInstance().enableForeignKeys(TargetConnection, false, DTBtypeTarget, tableListTarget);
            Data.getInstance().copyData(SourceConnection, TargetConnection, DTBtypeSource, DTBtypeTarget, tableListSource, tableListTarget);
            ForeignKeys.getInstance().enableForeignKeys(TargetConnection, true, DTBtypeTarget, tableListTarget);
            
            try
			{
            	SourceConnection.close();
            	TargetConnection.close();
			} catch (SQLException e)
			{
				System.out.println("Can not close a connection to a DTB");
				System.out.println("SQLException: " + e.getMessage());
			    System.out.println("SQLState: " + e.getSQLState());
			    System.out.println("VendorError: " + e.getErrorCode());
			    Log.getLog().doLogging(Level.SEVERE, e);
			}     
    }

}
