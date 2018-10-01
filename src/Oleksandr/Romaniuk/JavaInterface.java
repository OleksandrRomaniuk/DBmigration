package Oleksandr.Romaniuk;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.PasswordField;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.input.InputMethodEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class JavaInterface
{
	private TextArea textArea = null;
	private TextArea textAreaPrintChildren = null;
	private TextArea enterText = null;
	private StackPane stackPane = null;
	private ObservableList<String> connections = null;
	private ComboBox<String> connectionsComboBox = null;
	private TreeView<String> tree = null;
	private BorderPane borderPane = null;
	private VBox vbox = null;
	private Button buttons[] = null;
	private Group root = null;
	private TextArea textAreaSelect = null;
	private ComboBox<String> conComboBox = null;
	private ObservableList<String> cons = null;
	private TextArea infoText = null;
	private HashMap <ListView<String>, String> viewList = new HashMap();
	private static JavaInterface instance;
	
	private JavaInterface() 
	{
		
	}
	
	public static JavaInterface getInstance()
	{
		if (instance == null)
			instance =new JavaInterface();
		
		return instance;
	}
	
	private TabPane buildScene()
	{
		TabPane tabs = new TabPane();
        Tab tabTrees = new Tab();
        tabTrees.setText("Trees");
        tabTrees.setContent(treesSample());
        
        Tab tabMigration = new Tab();
        tabMigration.setText("Migration");
        tabMigration.setContent(migrationSample());
        
        tabs.getTabs().addAll(tabTrees, tabMigration);
		
		return tabs;		
	}
	
	
	
	private Pane migrationSample()
	{
		GridPane gridPane = new GridPane();
        gridPane.setAlignment(Pos.TOP_LEFT); 
        gridPane.setHgap(3);
        gridPane.setVgap(3);
        
        VBox hbButtons = new VBox();
        hbButtons.setAlignment(Pos.TOP_LEFT); 

        Button btnCrDB = new Button("Creat DB");
        Button btnRepl = new Button("Replicate tables");
        Button btnMigr = new Button("Migrate data");
        Button btnRef = new Button("Refresh");
        Button btnXML = new Button("Download to XML");
        btnCrDB.setStyle("-fx-font-size: 10pt;");
        btnRepl.setStyle("-fx-font-size: 10pt;");
        btnMigr.setStyle("-fx-font-size: 10pt;");
        btnRef.setStyle("-fx-font-size: 10pt;");
        btnXML.setStyle("-fx-font-size: 10pt;");
        btnCrDB.setMaxWidth(150.0);
        btnCrDB.setMinWidth(150.0);
        btnRepl.setMaxWidth(150.0);
        btnRepl.setMinWidth(150.0);
        btnMigr.setMaxWidth(150.0);
        btnMigr.setMinWidth(150.0);
        btnRef.setMaxWidth(150.0);
        btnRef.setMinWidth(150.0);
        btnXML.setMaxWidth(150.0);
        btnXML.setMinWidth(150.0);
        
        ListView<String> mysqlDBList = new ListView<String>(); 
        ObservableList<String> mysqlItems = FXCollections.observableArrayList(ConnectToDTB.getInstance().getListOfDBs("mysql"));
        viewList.put(mysqlDBList, "mysql");
        mysqlDBList.setItems(mysqlItems);
        mysqlDBList.setMinWidth(200.0);
        mysqlDBList.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        Button btnDelmysql = new Button("Delete");
        btnDelmysql.setAlignment(Pos.BOTTOM_LEFT);
        gridPane.add(btnDelmysql, 0, 22);
	    GridPane.setMargin(btnDelmysql, new Insets(0,0,0,10));
	    Button btnPropmysql = new Button("Create pr-ties");
	    btnPropmysql.setAlignment(Pos.BOTTOM_LEFT);
        gridPane.add(btnPropmysql, 0, 23);
	    GridPane.setMargin(btnPropmysql, new Insets(0,0,0,10));
	    
        
        ListView<String> postgresqlDBList = new ListView<String>(); 
        ObservableList<String> postgresqlItems = FXCollections.observableArrayList(ConnectToDTB.getInstance().getListOfDBs("postgresql"));
        viewList.put(postgresqlDBList, "postgresql");
        postgresqlDBList.setItems(postgresqlItems);
        postgresqlDBList.setMinWidth(200.0);
        postgresqlDBList.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        Button btnDelpostgresql = new Button("Delete");
        btnDelpostgresql.setAlignment(Pos.BOTTOM_LEFT);
        gridPane.add(btnDelpostgresql, 20, 22);
	    GridPane.setMargin(btnDelpostgresql, new Insets(0,0,0,10));
	    Button btnProppostrgesql = new Button("Create pr-ties");
	    btnProppostrgesql.setAlignment(Pos.BOTTOM_LEFT);
        gridPane.add(btnProppostrgesql, 20, 23);
	    GridPane.setMargin(btnProppostrgesql, new Insets(0,0,0,10));
	    
        ListView<String> dbPropertiesList = new ListView<String>(); 
        ObservableList<String> dbPropertiesItems = FXCollections.observableArrayList(ConnectionPropertiesBuilder.getInstance().getConnectionsList());
        viewList.put(dbPropertiesList, "dbproperties");
        dbPropertiesList.setItems(dbPropertiesItems);
        dbPropertiesList.setMinWidth(200.0);
        dbPropertiesList.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        Button btnDelprop = new Button("Delete");
        btnDelprop.setAlignment(Pos.BOTTOM_LEFT);
        gridPane.add(btnDelprop, 40, 23);
	    GridPane.setMargin(btnDelprop, new Insets(0,0,0,10));
	    
	    btnDelprop.setOnMouseClicked(MouseEvent -> {
	    ConnectionPropertiesBuilder.getInstance().deleteElement(dbPropertiesList.getSelectionModel().getSelectedItem());
	    dbPropertiesList.setItems(FXCollections.observableArrayList(ConnectionPropertiesBuilder.getInstance().getConnectionsList()));
	    });
        
	    btnDelmysql.setOnMouseClicked(MouseEvent -> {
		    CreateDTB.getInstance().removeDB(mysqlDBList.getSelectionModel().getSelectedItem(), ConnectToDTB.getInstance().getDefaultConnection("mysql"), "mysql");
		    mysqlDBList.setItems(FXCollections.observableArrayList(FXCollections.observableArrayList(ConnectToDTB.getInstance().getListOfDBs("mysql"))));
		    dbPropertiesList.setItems(FXCollections.observableArrayList(ConnectionPropertiesBuilder.getInstance().getConnectionsList()));
		    });
	    
	    btnPropmysql.setOnMouseClicked(MouseEvent -> {
	    	ConnectionPropertiesBuilder.getInstance().addElement(mysqlDBList.getSelectionModel().getSelectedItem(), "mysql");
	    	dbPropertiesList.setItems(FXCollections.observableArrayList(ConnectionPropertiesBuilder.getInstance().getConnectionsList()));
	    	});
	    
	    btnProppostrgesql.setOnMouseClicked(MouseEvent -> {
	    	ConnectionPropertiesBuilder.getInstance().addElement(postgresqlDBList.getSelectionModel().getSelectedItem(), "postgreesql");
	    	dbPropertiesList.setItems(FXCollections.observableArrayList(ConnectionPropertiesBuilder.getInstance().getConnectionsList()));
	    	});
	    
	    btnDelpostgresql.setOnMouseClicked(MouseEvent -> {
		    CreateDTB.getInstance().removeDB(postgresqlDBList.getSelectionModel().getSelectedItem(), ConnectToDTB.getInstance().getDefaultConnection("postgresql"), "postgresql");
		    postgresqlDBList.setItems(FXCollections.observableArrayList(FXCollections.observableArrayList(ConnectToDTB.getInstance().getListOfDBs("postgresql"))));
		    dbPropertiesList.setItems(FXCollections.observableArrayList(ConnectionPropertiesBuilder.getInstance().getConnectionsList()));
		    });
	    
        btnCrDB.setOnMouseClicked(MouseEvent ->	NewWindowCrNewDB.getInstance().displayCreateDBWindow());
        
        btnRepl.setOnMouseClicked(MouseEvent ->	NewWindowCrNewDB.getInstance().displayReplWindow());
        
        btnMigr.setOnMouseClicked(MouseEvent ->	NewWindowCrNewDB.getInstance().displayMigrlWindow());
        
        btnXML.setOnMouseClicked(MouseEvent ->	NewWindowCrNewDB.getInstance().displayXMLWindow());
        
        btnRef.setOnMouseClicked(MouseEvent ->	
        {
            mysqlDBList.setItems(FXCollections.observableArrayList(ConnectToDTB.getInstance().getListOfDBs("mysql")));
            postgresqlDBList.setItems(FXCollections.observableArrayList(ConnectToDTB.getInstance().getListOfDBs("postgresql")));
            dbPropertiesList.setItems(FXCollections.observableArrayList(ConnectionPropertiesBuilder.getInstance().getConnectionsList()));
            
        });
        
        hbButtons.getChildren().addAll(btnCrDB, btnRepl, btnMigr, btnRef, btnXML);
        
        gridPane.add(hbButtons, 0, 0);
        GridPane.setMargin(hbButtons, new Insets(10));
        
        infoText = new TextArea();
        
        Label lblmysql = new Label("The list of mysql DBs: ");
        lblmysql.setAlignment(Pos.BOTTOM_LEFT);
        gridPane.add(lblmysql, 0, 24);
	    GridPane.setMargin(lblmysql, new Insets(0,0,0,10));
	    
	    Label lblpostgre = new Label("The list of postgresql DBs: ");
	    lblpostgre.setAlignment(Pos.BOTTOM_LEFT);
        gridPane.add(lblpostgre, 20, 24);
	    GridPane.setMargin(lblpostgre, new Insets(0,0,0,10));
	    
	    Label lblprop = new Label("The list of properties of connections: ");
	    lblprop.setAlignment(Pos.BOTTOM_LEFT);
        gridPane.add(lblprop, 40, 24);
	    GridPane.setMargin(lblprop, new Insets(0,0,0,10));
	    
        gridPane.add(infoText, 20, 0, 25, 20);
	    GridPane.setMargin(infoText, new Insets(10));
	    GridPane.setHgrow(infoText, Priority.ALWAYS);
	    GridPane.setVgrow(infoText, Priority.ALWAYS);
        
        gridPane.add(mysqlDBList, 0, 25);
	    GridPane.setMargin(mysqlDBList, new Insets(0,10,0,10));
	    GridPane.setHgrow(mysqlDBList, Priority.ALWAYS);
	    GridPane.setVgrow(mysqlDBList, Priority.ALWAYS);
        
	    gridPane.add(postgresqlDBList, 20, 25);
	    GridPane.setMargin(postgresqlDBList, new Insets(0,10,0,10));
	    GridPane.setHgrow(postgresqlDBList, Priority.ALWAYS);
	    GridPane.setVgrow(postgresqlDBList, Priority.ALWAYS);
	    
	    gridPane.add(dbPropertiesList, 40, 25);
	    GridPane.setMargin(dbPropertiesList, new Insets(0,10,0,10));
	    GridPane.setHgrow(dbPropertiesList, Priority.ALWAYS);
	    GridPane.setVgrow(dbPropertiesList, Priority.ALWAYS);
	    
        return gridPane;
	}
	
	private Pane treesSample()
	{
		GridPane gridPane = new GridPane();
		gridPane.setHgap(10);
	    gridPane.setVgap(10);
	    gridPane.setAlignment(Pos.CENTER);
        
        stackPane = new StackPane();
        tree = new TreeView<String>();
        stackPane.getChildren().add(tree);
        stackPane.setAlignment(Pos.CENTER);
        stackPane.setMinWidth(350.0);
        
		textAreaPrintChildren = new TextArea();
		textAreaPrintChildren.setMinWidth(350.0);
		
		HBox hboxLeft = new HBox();
		cons = FXCollections.observableArrayList(ConnectionPropertiesBuilder.getInstance().getConnectionsList());
	    conComboBox = new ComboBox<String>(cons);
	    conComboBox.setMaxWidth(Control.USE_PREF_SIZE);
	    Label lblCon = new Label("Database: ");
	    hboxLeft.getChildren().addAll(lblCon, conComboBox);
	    hboxLeft.setAlignment(Pos.CENTER_LEFT);
	    
	    conComboBox.valueProperty().addListener(Event -> showTree());
	    conComboBox.setOnMouseClicked(event -> 
	    							{
	    							cons = FXCollections.observableArrayList(ConnectionPropertiesBuilder.getInstance().getConnectionsList());
	    							conComboBox.setItems(cons);
	    							});
	    
	    gridPane.add(hboxLeft, 0, 1);
	    GridPane.setMargin(hboxLeft, new Insets(10));

	    textAreaSelect = new TextArea();
		textAreaSelect.setMaxWidth(150.0);
		textAreaSelect.setMaxHeight(10.0);
		Trees.getInstance();
		textAreaSelect.textProperty().addListener(
				Event -> 
				{
					if(conComboBox.valueProperty().getValue() != null && !textAreaSelect.getText().equals(""))
					{
					Trees.printItems(
						textAreaSelect.getText(),
						Trees.getInstance().getRootItem());
					}
				});
	    
	    HBox hboxRight = new HBox();
		Label lblSelectItems = new Label("Please enter the criteria: ");
	    hboxRight.getChildren().addAll(lblSelectItems, textAreaSelect);
	    hboxRight.setAlignment(Pos.CENTER_LEFT);
	    
	    gridPane.add(hboxRight, 4, 1);
	    GridPane.setMargin(hboxRight, new Insets(10));

	    Label lblStr = new Label("Structure of the database: ");
	    lblStr.setAlignment(Pos.BOTTOM_LEFT);
	    
	    Label lblSelect = new Label("Selected items: ");
	    lblSelect.setAlignment(Pos.BOTTOM_LEFT);
	    
	    gridPane.add(lblStr, 0, 2);
	    GridPane.setMargin(lblStr, new Insets(0,0,0,10));
	    
	    gridPane.add(lblSelect, 4, 2);
	    GridPane.setMargin(lblSelect, new Insets(0,0,0,10));
	    
	    gridPane.add(stackPane, 0, 3);
	    GridPane.setMargin(stackPane, new Insets(10));
	    GridPane.setHgrow(stackPane, Priority.ALWAYS);
	    GridPane.setVgrow(stackPane, Priority.ALWAYS);
	   
	    gridPane.add(textAreaPrintChildren, 4, 3);
	    GridPane.setMargin(textAreaPrintChildren, new Insets(10));
	    GridPane.setHgrow(textAreaPrintChildren, Priority.ALWAYS);
	    GridPane.setVgrow(textAreaPrintChildren, Priority.ALWAYS);
	    
        return gridPane;
	}

	public Scene getScene()
	{
			
		Scene scene = new Scene(buildScene(), 800, 500);
		
		return scene;
	}
	
	public void textUpdate(String text)
	{
		new Thread(new Runnable() 
		{
			
	        @Override
	        public void run() 
	        {
	        	infoText.appendText(text);
	        }
	    }).start();
	}
	
	public void printChildren(String item)
	{
		new Thread(new Runnable() 
		{
			
	        @Override
	        public void run() 
	        {
	        	
	        	textAreaPrintChildren.clear(); 
	        	textAreaPrintChildren.appendText(item+"\n");
	        	
	        }
	    }).start();
	}
	
	public void showTree()
	{
		new Thread(new Runnable() 
		{
			
	        @Override
	        public void run() 
	        {
	        		Platform.runLater(() -> 
	        		tree.setRoot(Trees.getInstance().tree(ConnectToDTB.getInstance().getConnection(conComboBox.valueProperty().getValue()))));
	        		
	        }
	    }).start();
	}

	public void refreshMigrListViews()
	{
		new Thread(new Runnable() 
		{
	        @Override
	        public void run() 
	        {
	        	Platform.runLater(() -> 
	        	{
	        			for(Entry<ListView<String>, String> entry:viewList.entrySet())
	        			{ 
	        				ListView<String> key = entry.getKey();
	        				String value = entry.getValue();
	        				if(value.equals("mysql"))
	        				{
	        					key.setItems(FXCollections.observableArrayList(ConnectToDTB.getInstance().getListOfDBs("mysql")));
	        				}
	        				else if (value.equals("postgresql"))
	        				{
	        					key.setItems(FXCollections.observableArrayList(ConnectToDTB.getInstance().getListOfDBs("postgresql")));
	        				}
	        				else
	        					key.setItems(FXCollections.observableArrayList(ConnectionPropertiesBuilder.getInstance().getConnectionsList()));
	        			};
	        	});
	        }
	    }).start();
	}
}
