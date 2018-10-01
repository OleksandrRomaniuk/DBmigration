package Oleksandr.Romaniuk;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class ToSQL
{

	public ToSQL()
	{
		// TODO Auto-generated constructor stub
	}

	public static void fromXMLtoMySQL(Connection connection)
	{
		
		File file = new File("MyFirstMXL.xml");
		
		Document document = null;
		try
		{
			DocumentBuilder documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
		
			document = documentBuilder.parse(file);
		} catch (SAXException | IOException |  ParserConfigurationException e)
		{
			System.out.println(e.getMessage());
			Log.getLog().doLogging(Level.SEVERE, e);
		}

		Node root = document.getDocumentElement();

		NodeList tables = root.getChildNodes();
			
		for(int i = 0; i < tables.getLength(); i++)
			{
				Node table = tables.item(i);
		
				StringBuffer ddl = new StringBuffer("create table "
				+ table.getAttributes().getNamedItem("TableName").getTextContent() + " (");
		
		StringBuffer dml = new StringBuffer("insert into  "
				+ table.getAttributes().getNamedItem("TableName").getTextContent() + " (");
		
		NodeList tableChildren = table.getChildNodes(); //.getElementsByTagName("TableStructure");
		
		
		int no_of_columns = tableStructure.item(0).getChildNodes().getLength();
		
		for (int i = 0; i < no_of_columns; i++) 
		{
			ddl.append(document.getElementsByTagName("ColumnName").item(i)
					.getTextContent()
					+ " "
					+ document.getElementsByTagName("ColumnType").item(i)
							.getTextContent()
					+ "("
					+ document.getElementsByTagName("Length").item(i)
							.getTextContent() + "),");
			dml.append(document.getElementsByTagName("ColumnName").item(i)
					.getTextContent()
					+ ",");
		}
		
			ddl = ddl.replace(ddl.length() - 1, ddl.length(), ")");
			dml = dml.replace(dml.length() - 1, dml.length(), ") values(");
			
			for (int k = 0; k < no_of_columns; k++)
				dml.append("?,");
			
			dml = dml.replace(dml.length() - 1, dml.length(), ")");
			
			Statement stmt = null;
			
			try
			{
				stmt = connection.createStatement();
				stmt.executeUpdate(ddl.toString());
			} catch (SQLException e)
			{
				System.out.println(e.getMessage());
				Log.getLog().doLogging(Level.SEVERE, e);
			}
			
			NodeList tableData = document.getElementsByTagName("TableData");
			
			int tdlen = tableData.item(0).getChildNodes().getLength();
			
			PreparedStatement prepStmt = null;
			
			try
			{
				prepStmt = connection.prepareStatement(dml.toString());
			} catch (SQLException e)
			{
				System.out.println(e.getMessage());
				Log.getLog().doLogging(Level.SEVERE, e);
			}
			
			String colName = null;
			
			for (int i = 0; i < tdlen; i++) 
			{
	 
				for (int j = 0; j < tableStructure.item(0).getChildNodes()
						.getLength(); j++) 
				{
	 
					colName = document.getElementsByTagName("ColumnName").item(j)
							.getTextContent();
					try
					{
						prepStmt.setString(j + 1, document.getElementsByTagName(colName)
								.item(i).getTextContent());
					} catch (DOMException | SQLException e)
					{
						System.out.println(e.getMessage());
						Log.getLog().doLogging(Level.SEVERE, e);
					} 
				}
				try
				{
					prepStmt.addBatch();
				} catch (SQLException e)
				{
					System.out.println(e.getMessage());
					Log.getLog().doLogging(Level.SEVERE, e);
				}
				 
			}
			
			try
			{
				prepStmt.executeBatch();
			} catch (SQLException e)
			{
				System.out.println(e.getMessage());
				Log.getLog().doLogging(Level.SEVERE, e);
			}
			
	}
	
}
