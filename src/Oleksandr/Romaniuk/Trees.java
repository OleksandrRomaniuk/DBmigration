package Oleksandr.Romaniuk;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.StackPane;

public class Trees
{
	
	private TreeItem<String> rootItem = null;
	private static Trees trees = null;
	private static StringBuilder sb = new StringBuilder();
	
	
	private Trees()
	{}
	
	public static Trees getInstance()
	{
		if(trees == null)
			{
				trees = new Trees();
				return trees;
			}
		else return trees;
	}
	
	public TreeItem<String> getRootItem()
	{
		return rootItem;
	}

	public TreeItem<String> tree(Connection connection)
	{
		DatabaseMetaData dbMeta = null;
		ResultSet rsTables = null;
		ResultSet rsCol = null;
		
	try
	{
		dbMeta = connection.getMetaData();
		String[] types = {"TABLE"};
		rsTables = dbMeta.getTables(null, null, "%", types);
		
		rootItem = new TreeItem<String> ("Schemas");
	    rootItem.setExpanded(true);
     
    while (rsTables.next()) 
    {
         TreeItem<String> itemTable = new TreeItem<String> (rsTables.getString(3));            
         rootItem.getChildren().add(itemTable);
         rsCol = dbMeta.getColumns(null, null, rsTables.getString(3), null);
         
         while (rsCol.next())
         {
        	 TreeItem<String> itemCol = new TreeItem<String> (rsCol.getString(4));            
             itemTable.getChildren().add(itemCol);
         }
    }
     
	} catch (SQLException e)
	{
		System.out.println(e.getMessage());
		Log.getLog().doLogging(Level.SEVERE, e);
	}
    
	return rootItem; 
	}

	public static void printItems(String value, TreeItem<String> rootItem)
	    {
			sb.delete(0, sb.length());
		
			findItems(value, rootItem);
	        
	        JavaInterface.getInstance().printChildren(sb.toString());
	    }
	
	private static void findItems(String value, TreeItem<String> rootItem)
	{
		if (value == null || value.trim().equals(""))
        {
			Log.getLog().doLogging(Level.SEVERE, "Item cannot be empty.");
        }
		else if (rootItem.getChildren().size() > 0)
        {
			for(TreeItem<String> child : rootItem.getChildren())
	        {
	            if (child.getValue().toString().contains(value))
	            	sb.append(child.getValue().toString()+"\n");
	           
	            if (child.getChildren().size() > 0)
	            	Trees.findItems(value, child);
	        }
        }
	}
}