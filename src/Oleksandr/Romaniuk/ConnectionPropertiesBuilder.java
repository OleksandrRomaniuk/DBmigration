package Oleksandr.Romaniuk;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class ConnectionPropertiesBuilder
{
	private static ConnectionPropertiesBuilder instance = null;
	
	private ConnectionPropertiesBuilder() 
	{
		setFile();
	};
	
	public static ConnectionPropertiesBuilder getInstance()
	{
		if (instance == null)
			instance = new ConnectionPropertiesBuilder();
		
		return instance;
	}
	
	Document doc = null;
	
	public void addElement(String dbName, String dbType)
	{
		if(!checkIfExist(dbName))
			if(dbName != null)
				addElements(dbName, dbType);
			else
				Log.getLog().doLogging(Level.WARNING, "Can not add a new connection property. Please choose the db.");
		else
		{
			Log.getLog().doLogging(Level.WARNING, "Can not add a new connection property. The database with such name already exists.");
		}
	}
	
	public void deleteElement(String dbName)
	{
		if(checkIfExist(dbName))
		{
			
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
			
			NodeList connections = document.getElementsByTagName("Connection");
				
						for(int i = 0; i < connections.getLength(); i++)
						{
							Node connection = connections.item(i);
							String con = connection.getAttributes().getNamedItem("ConnectionName").getNodeValue();
							if(con.equals(dbName))
							{
								connection.getParentNode().removeChild(connection);
								//document.getDocumentElement().removeChild(connection);
								break;
							}
						}	
						
						TransformerFactory transformerFactory = TransformerFactory.newInstance();
				        Transformer transformer = null;
						
				        try
						{
							transformer = transformerFactory.newTransformer();
						} catch (TransformerConfigurationException e)
						{
							System.out.println(e.getMessage());
							Log.getLog().doLogging(Level.SEVERE, e);
						}
						
						DOMSource source = new DOMSource(document);
				        StreamResult result = new StreamResult(file);
				        
				        try
						{
							transformer.transform(source, result);
						} catch (TransformerException e)
						{
							System.out.println(e.getMessage());
							Log.getLog().doLogging(Level.SEVERE, e);
						}	
		}
		else
		{
			Log.getLog().doLogging(Level.WARNING, "The DB property does not exist.");
		}
	}
	
	private void addElements(String dbName, String dbType)
	{
		String urls = null;
		String drivers = null;
		String usernames = null;
		String passwords = null;
		
		if(dbType == "mysql")
		{
			urls = "jdbc:mysql://localhost:3306/"+dbName+"?allowPublicKeyRetrieval=true&useSSL=false";
			drivers = "com.mysql.cj.jdbc.Driver";
			usernames = "root";
			passwords = "Ilovemylife101088";
			addNewElement(dbName, dbType, urls, drivers, usernames, passwords);
		}
		else if(dbType == "postgresql")
		{
			urls = "jdbc:postgresql://localhost:5432/"+dbName+"?allowPublicKeyRetrieval=true&useSSL=false";
			drivers = "org.postgresql.Driver";
			usernames = "postgres";
			passwords = "Ilovemylife101088";
			addNewElement(dbName, dbType, urls, drivers, usernames, passwords);
		}	
		else
		{
			Log.getLog().doLogging(Level.WARNING, "Can not add a new connection property. The type of the DTB is not supported.");
		}
	}
	
	private void addNewElement(String dbName, String dbType, String urls, String drivers, String usernames, String passwords)
	{
		String filepath = "DBproperties.xml";
		File file = new File("DBproperties.xml");
		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder = null;
		Document doc = null;
		try
		{
			docBuilder = docFactory.newDocumentBuilder();
			
			if(file.length() != 0)
			{
				doc = docBuilder.parse(filepath);
			}
			else
				doc = docBuilder.newDocument();
			
		} catch (SAXException | IOException | ParserConfigurationException e)
		{
			System.out.println(e.getMessage());
		}
		
		Node connections = doc.getDocumentElement();
		
		Element connection = doc.createElement("Connection");
		connections.appendChild(connection);
		Attr conName = doc.createAttribute("ConnectionName");
		conName.setValue(dbName);
		connection.setAttributeNode(conName);
		
		Element url = doc.createElement("url");
		url.appendChild(doc.createTextNode(urls));
		connection.appendChild(url);
		Attr urlAtr = doc.createAttribute("url");
		urlAtr.setValue("url");
		url.setAttributeNode(urlAtr);
		
		Element driver = doc.createElement("driver");
		driver.appendChild(doc.createTextNode(drivers));
		connection.appendChild(driver);
		Attr dtbType = doc.createAttribute("DatabaseType");
		dtbType.setValue(dbType);
		driver.setAttributeNode(dtbType);
		
		Element password = doc.createElement("password");
		password.appendChild(doc.createTextNode(passwords));
		connection.appendChild(password);
		Attr passAtr = doc.createAttribute("password");
		passAtr.setValue("password");
		password.setAttributeNode(passAtr);
		
		Element username = doc.createElement("username");
		username.appendChild(doc.createTextNode(usernames));
		connection.appendChild(username);
		Attr usernameAtr = doc.createAttribute("username");
		usernameAtr.setValue("username");
		username.setAttributeNode(usernameAtr);
		
		TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = null;
		
        try
		{
			transformer = transformerFactory.newTransformer();
		} catch (TransformerConfigurationException e)
		{
			System.out.println(e.getMessage());
			Log.getLog().doLogging(Level.SEVERE, e);
		}
        
        DOMSource source = new DOMSource(doc);
        StreamResult result = new StreamResult(new File(filepath));
        
        try
		{
			transformer.transform(source, result);
		} catch (TransformerException e)
		{
			System.out.println(e.getMessage());
		}
	}
	
	private void setFile()
	{
		File file = new File("DBproperties.xml");//C:/Users/Oleksandr Romaniuk/eclipse-workspace/ProjectJDBC/
		if(!file.exists())
		{
			setNewFile();
		}
	}
	
	private void setNewFile()
	{
		
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = null;
		
		try
		{
			builder = factory.newDocumentBuilder();
		} catch (ParserConfigurationException e)
		{
			System.out.println(e.getMessage());
			Log.getLog().doLogging(Level.SEVERE, e);
		}
		Document doc = builder.newDocument();
		
		Element connections = doc.createElement("Connections");
		doc.appendChild(connections);
		
		DOMSource domSource = new DOMSource(doc);
		TransformerFactory tf = TransformerFactory.newInstance();
		Transformer transformer = null;
		try
		{
			transformer = tf.newTransformer();
		} catch (TransformerConfigurationException e)
		{
			System.out.println(e.getMessage());
			Log.getLog().doLogging(Level.SEVERE, e);
		}
		transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION,
				"yes");
		transformer.setOutputProperty(OutputKeys.METHOD, "xml");
		transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
		
		File file = new File("DBproperties.xml");//C:/Users/Oleksandr Romaniuk/eclipse-workspace/ProjectJDBC/
		StreamResult result = new StreamResult(file);
		
		try
		{
			transformer.transform(domSource, result);
		} catch (TransformerException e)
		{
			System.out.println(e.getMessage());
			Log.getLog().doLogging(Level.SEVERE, e);
		}
	}
	
	private boolean checkIfExist(String connectionName)
	{
		ArrayList<String> conList = getConnectionsList();
		for(String conName: conList)
		{
			if(conName.equals(connectionName)) 
				return true;
		}
		return false;
		
	}
	
	public ArrayList<String> getConnectionsList()
	{
		ArrayList<String> conList = new ArrayList();
		
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
			Log.getLog().doLogging(Level.SEVERE, e);
		}
			
		Node root = document.getDocumentElement();

		NodeList connections = root.getChildNodes();
			
					for(int i = 0; i < connections.getLength(); i++)
					{
						Node connection = connections.item(i);
						String con = connection.getAttributes().getNamedItem("ConnectionName").getNodeValue();
						conList.add(con);
					}
					return conList;
	}
}