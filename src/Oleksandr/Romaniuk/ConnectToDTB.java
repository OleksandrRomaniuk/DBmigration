package Oleksandr.Romaniuk;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Properties;
import java.util.logging.Level;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;

public class ConnectToDTB
{
		
		private ArrayList<Connection> conList = new ArrayList();
		
		private static ConnectToDTB instance;
	
		private ConnectToDTB()
		{
		}
		
		public static ConnectToDTB getInstance()
		{
			if(instance == null)
				instance = new ConnectToDTB();
			
			return instance;
		}
		
		public void closeAll()
		{
			for(Connection con: conList)
			{
				try
				{
					if (con != null || !con.isClosed())
					{
						con.close();
					}
				} catch (SQLException e)
				{
					System.out.println("SQLException: " + e.getMessage());
				    System.out.println("SQLState: " + e.getSQLState());
				    System.out.println("VendorError: " + e.getErrorCode());
				    Log.getLog().doLogging(Level.SEVERE, e);
				}
			}
		}
		
		public Connection getDefaultConnection(String DBtype)
		{
			Connection connection = setDefaultConnection(DBtype);
			conList.add(connection);
			
			return connection;
		}
		
		public Connection getConnection(String DBname)
		{
		
			Connection connection = setConnection(DBname);
			conList.add(connection);
			
			return connection;
		}
		
		private Connection setConnection(String DBname)
		{
			String url = null;
			String username = null;
			String password = null;
			String driver = null;
			
			File file = new File("DBproperties.xml");
			
			DocumentBuilder documentBuilder = null;
			Document document = null;
			
			try
			{
				documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
				document = documentBuilder.parse(file);
				
			} catch (ParserConfigurationException | SAXException | IOException e)
			{
				System.out.println(e.getMessage());
			}
				
			Node root = document.getDocumentElement();

			NodeList connections = root.getChildNodes();
				
						for(int i = 0; i < connections.getLength(); i++)
						{
							Node connection = connections.item(i);
							
							if(connection.getAttributes().getNamedItem("ConnectionName").getNodeValue().equals(DBname))
							{
								if (connection.getNodeType() != Node.TEXT_NODE) 
								{
				                    NodeList attributes = connection.getChildNodes();
				                    
				                    for(int j = 0; j < attributes.getLength(); j++) 
				                    {
				                        Node attribute = attributes.item(j);
				                        if (attribute.getNodeType() != Node.TEXT_NODE) 
				                        {
				                        		String attributeName = attribute.toString();
				                        		if(attributeName.contains("url")) url = attribute.getTextContent();
				                        		else if(attributeName.contains("username")) username = attribute.getTextContent();		
				                        		else if(attributeName.contains("password")) password = attribute.getTextContent();
				                        		else if(attributeName.contains("driver")) driver = attribute.getTextContent();
				                        }
				                    }
								}
							}
						}
				
			if (driver != null)
			{
				try
				{
					Class.forName(driver).newInstance();
				} catch (ClassNotFoundException | InstantiationException | IllegalAccessException e)
				{
					Log.getLog().doLogging(Level.SEVERE, "Can not load the driver: " + e.getMessage());
				}
			}
			else
			{
				Log.getLog().doLogging(Level.SEVERE, "Can not load the driver");
			}
			
			Connection connection = null;
			
			try
			{
				connection = DriverManager.getConnection(url, username, password);
			} catch (SQLException e)
			{
				System.out.println("Can not get the connection to the database");
				System.out.println("SQLException: " + e.getMessage());
			    System.out.println("SQLState: " + e.getSQLState());
			    System.out.println("VendorError: " + e.getErrorCode());
			    Log.getLog().doLogging(Level.SEVERE, e);
			}
			
			return connection;
		}
		
		private void showDrivers()
		{
			Enumeration<Driver> en = DriverManager.getDrivers();
			Driver dr = null;
			while(en.hasMoreElements())
			{
				dr = en.nextElement();
				
				System.out.println(dr.toString()+" "+dr.jdbcCompliant());
			}
		}
		
		private Connection setDefaultConnection(String DBtype)
		{
			String url = null;
			String username = null;
			String password = null;
			String driver = null;
			
			if(DBtype.equals("mysql"))
			{
				url = "jdbc:mysql://localhost:3306/?allowPublicKeyRetrieval=true&useSSL=false";
				driver = "com.mysql.cj.jdbc.Driver";
				username = "root";
				password = "Ilovemylife101088";
			}
			else if (DBtype.equals("postgresql"))
			{
				url = "jdbc:postgresql://localhost:5432/?allowPublicKeyRetrieval=true&useSSL=false";
				driver = "org.postgresql.Driver";
				username = "postgres";
				password = "Ilovemylife101088";
			}
			else
			{
				Log.getLog().doLogging(Level.WARNING, "Can not set up the connection. The connection type is not supported.");
			}
			
			if (driver != null)
			{
				try
				{
					Class.forName(driver).newInstance();
				} catch (ClassNotFoundException | InstantiationException | IllegalAccessException e)
				{
					Log.getLog().doLogging(Level.SEVERE, "Can not load the driver: " + e.getMessage());
				}
			}
			else
			{
				Log.getLog().doLogging(Level.SEVERE, "Can not load the driver");
			}
			
			Connection connection = null;
			try
			{
				connection = DriverManager.getConnection(url, username, password);
			} catch (SQLException e)
			{
				System.out.println("Can not get the connection to the database");
				System.out.println("SQLException: " + e.getMessage());
			    System.out.println("SQLState: " + e.getSQLState());
			    System.out.println("VendorError: " + e.getErrorCode());
			    Log.getLog().doLogging(Level.SEVERE, e);
			}
			
			return connection;
		}
		
		public ArrayList<String> getListOfDBs(String DBtype)
		{
			ArrayList<String> dbList = new ArrayList();
			Connection connection = getDefaultConnection(DBtype);
			
			ResultSet rs = null;
			DatabaseMetaData meta = null;
			
			if(DBtype.equals("mysql"))
				{
				try
					{
					  meta = connection.getMetaData();
					  rs = meta.getCatalogs();
					
					while (rs.next()) 
					{
						dbList.add(rs.getString(1));
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
					System.out.println("SQLException: " + e.getMessage());
				    System.out.println("SQLState: " + e.getSQLState());
				    System.out.println("VendorError: " + e.getErrorCode());
				    Log.getLog().doLogging(Level.SEVERE, e);
				}
				}
				}
			if(DBtype.equals("postgresql"))
				{
					PreparedStatement pstmt = null;
					try
					{
						pstmt = connection.prepareStatement("SELECT datname FROM pg_database");
					} catch (SQLException e)
					{
						System.out.println(e.getMessage());
						Log.getLog().doLogging(Level.SEVERE, e);
					}
					finally
					{
						try
						{
							rs = pstmt.executeQuery();
							while(rs.next())
								{
									dbList.add(rs.getString(1));
								}
							
						} catch (SQLException e)
						{
							System.out.println(e.getMessage());
							Log.getLog().doLogging(Level.SEVERE, e);
						}
					}
				}
				
			
			return dbList;
		}
	}