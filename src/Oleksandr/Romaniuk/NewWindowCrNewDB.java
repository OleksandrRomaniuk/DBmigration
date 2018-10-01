package Oleksandr.Romaniuk;

import java.sql.Connection;
import java.sql.SQLException;

import javax.swing.plaf.basic.BasicTabbedPaneUI.MouseHandler;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class NewWindowCrNewDB
{
	ObservableList<String> connections = null;
	
	private static NewWindowCrNewDB instance;
	
	private NewWindowCrNewDB()
	{
	}
	
	public static NewWindowCrNewDB getInstance()
	{
		if(instance == null)
			instance = new NewWindowCrNewDB();
		
		return instance;
	}
	
	public void displayMigrlWindow()
	{
		Stage window = new Stage();
		window.initModality(Modality.APPLICATION_MODAL);
		
		window.setTitle("Migartion of data");
		window.setMinWidth(300);
		window.setMinHeight(300);
		
		GridPane gridPane = new GridPane();
        gridPane.setAlignment(Pos.CENTER); 
        gridPane.setHgap(10);
        gridPane.setVgap(12);
        ColumnConstraints column1 = new ColumnConstraints();
        column1.setHalignment(HPos.RIGHT);  
        gridPane.getColumnConstraints().add(column1); 

        ColumnConstraints column2 = new ColumnConstraints();
        column2.setHalignment(HPos.LEFT);  
        gridPane.getColumnConstraints().add(column2); 

        HBox hbButtons = new HBox();
        hbButtons.setSpacing(10.0);
        hbButtons.setAlignment(Pos.CENTER);  

        Button btnReplicate = new Button("Submit");
        Button btnCancel = new Button("Cancel");
        btnReplicate.setStyle("-fx-font-size: 10pt;");
        btnCancel.setStyle("-fx-font-size: 10pt;");
        
        btnCancel.setOnAction(event -> window.close());
        
        Label lblSource = new Label("Please choose the source DB:");
        
        Label lblTarget = new Label("Please choose the target DB:");
        
        
        connections = FXCollections.observableArrayList(ConnectionPropertiesBuilder.getInstance().getConnectionsList());
        ComboBox cbxSource = new ComboBox<String>(connections);
        cbxSource.setMinWidth(150);
        
        cbxSource.setOnMouseClicked(event -> 
			{
				connections = FXCollections.observableArrayList(ConnectionPropertiesBuilder.getInstance().getConnectionsList());
				cbxSource.setItems(connections);
			});
        
        ComboBox cbxTarget = new ComboBox<String>(connections);
        cbxSource.setMinWidth(150);
        
        cbxTarget.setOnMouseClicked(event -> 
			{
				connections = FXCollections.observableArrayList(ConnectionPropertiesBuilder.getInstance().getConnectionsList());
				cbxSource.setItems(connections);
			});
        
        btnReplicate.setOnAction(event -> 
        {
        	if(cbxSource.getValue() == null)
        		alertWindow("Source DB", "Please choose the source connection");
        	else if(cbxTarget.getValue() == null)
        		alertWindow("Target DB", "Please choose the target connection");
        	else
	        	{
        		Migration.getInstance().migrate(
        				ConnectToDTB.getInstance().getConnection(cbxSource.getValue().toString()), 
        				ConnectToDTB.getInstance().getConnection(cbxTarget.getValue().toString())
        				);
	        	}
        });
        
        
        hbButtons.getChildren().addAll(btnReplicate, btnCancel);
        gridPane.add(lblSource, 0, 0);
        gridPane.add(cbxSource, 1, 0);
        gridPane.add(lblTarget, 0, 1);
        gridPane.add(cbxTarget, 1, 1);
        gridPane.add(hbButtons, 0, 2, 2, 1);

		Scene scene = new Scene(gridPane, 500, 200);
		window.setScene(scene);
		window.show();
	}
	
	public void displayReplWindow()
	{
		Stage window = new Stage();
		window.initModality(Modality.APPLICATION_MODAL);
		
		window.setTitle("Create new DB");
		window.setMinWidth(300);
		window.setMinHeight(300);
		
		GridPane gridPane = new GridPane();
        gridPane.setAlignment(Pos.CENTER); 
        gridPane.setHgap(10);
        gridPane.setVgap(12);
        ColumnConstraints column1 = new ColumnConstraints();
        column1.setHalignment(HPos.RIGHT);  
        gridPane.getColumnConstraints().add(column1); 

        ColumnConstraints column2 = new ColumnConstraints();
        column2.setHalignment(HPos.LEFT);  
        gridPane.getColumnConstraints().add(column2); 

        HBox hbButtons = new HBox();
        hbButtons.setSpacing(10.0);
        hbButtons.setAlignment(Pos.CENTER);  

        Button btnReplicate = new Button("Replicate");
        Button btnCancel = new Button("Cancel");
        btnReplicate.setStyle("-fx-font-size: 10pt;");
        btnCancel.setStyle("-fx-font-size: 10pt;");
        
        btnCancel.setOnAction(event -> window.close());
        
        Label lblSource = new Label("Please choose the source DB:");
        
        Label lblTarget = new Label("Please choose the target DB:");
        
        
        connections = FXCollections.observableArrayList(ConnectionPropertiesBuilder.getInstance().getConnectionsList());
        ComboBox cbxSource = new ComboBox<String>(connections);
        cbxSource.setMinWidth(150);
        
        cbxSource.setOnMouseClicked(event -> 
			{
				connections = FXCollections.observableArrayList(ConnectionPropertiesBuilder.getInstance().getConnectionsList());
				cbxSource.setItems(connections);
			});
        
        ComboBox cbxTarget = new ComboBox<String>(connections);
        cbxSource.setMinWidth(150);
        
        cbxTarget.setOnMouseClicked(event -> 
			{
				connections = FXCollections.observableArrayList(ConnectionPropertiesBuilder.getInstance().getConnectionsList());
				cbxSource.setItems(connections);
			});
        
        btnReplicate.setOnAction(event -> 
        {
        	if(cbxSource.getValue() == null)
        		alertWindow("Source DB", "Please choose the source connection");
        	else if(cbxTarget.getValue() == null)
        		alertWindow("Target DB", "Please choose the target connection");
        	else
	        	{
        		TableBuilder.getInstance().replicateTables(
        				ConnectToDTB.getInstance().getConnection(cbxSource.getValue().toString()), 
        				ConnectToDTB.getInstance().getConnection(cbxTarget.getValue().toString())
        				);
	        	}
        });
        
        
        hbButtons.getChildren().addAll(btnReplicate, btnCancel);
        gridPane.add(lblSource, 0, 0);
        gridPane.add(cbxSource, 1, 0);
        gridPane.add(lblTarget, 0, 1);
        gridPane.add(cbxTarget, 1, 1);
        gridPane.add(hbButtons, 0, 2, 2, 1);

		Scene scene = new Scene(gridPane, 500, 200);
		window.setScene(scene);
		window.show();
        
	}
	
	public void displayCreateDBWindow()
	{
		Stage window = new Stage();
		window.initModality(Modality.APPLICATION_MODAL);
		
		window.setTitle("Create new DB");
		window.setMinWidth(300);
		window.setMinHeight(300);
		
		GridPane gridPane = new GridPane();
        gridPane.setAlignment(Pos.CENTER); 
        gridPane.setHgap(10);
        gridPane.setVgap(12);
        
        ColumnConstraints column1 = new ColumnConstraints();
        column1.setHalignment(HPos.RIGHT);  
        gridPane.getColumnConstraints().add(column1); 

        ColumnConstraints column2 = new ColumnConstraints();
        column2.setHalignment(HPos.LEFT);  
        gridPane.getColumnConstraints().add(column2); 

        HBox hbButtons = new HBox();
        hbButtons.setSpacing(10.0);
        hbButtons.setAlignment(Pos.CENTER);  

        Button btnSubmit = new Button("Submit");
        Button btnCancel = new Button("Cancel");
        btnSubmit.setStyle("-fx-font-size: 10pt;");
        btnCancel.setStyle("-fx-font-size: 10pt;");
        
        btnCancel.setOnAction(event -> window.close());
        
        Label lblName = new Label("Please enter the name of the new DB:");
        TextField tfName = new TextField();
        Label lblType = new Label("Please choose the type of the DB:");
        tfName.setMinWidth(150);
        
        ObservableList<String> types = FXCollections.observableArrayList("mysql", "postgresql");
        ComboBox cbxType = new ComboBox<String>(types);
        cbxType.setMinWidth(150);

        btnSubmit.setOnAction(event -> 
        {
        	if(tfName.getText().equals(""))
        		alertWindow("Name of DB", "Please enter the name of DB.");
        	else if(cbxType.valueProperty().getValue() == null)
        		alertWindow("Type of DB", "Please choose the type of DB.");
        	else
	        	{
        			if(cbxType.valueProperty().getValue().toString().equals("mysql"))
	        			{
	        				CreateDTB.getInstance().createNewDTB(
	        						tfName.getText(), 
	        						ConnectToDTB.getInstance().getDefaultConnection("mysql"), 
	        						"mysql");
	        				JavaInterface.getInstance().refreshMigrListViews();
	        			}
        			else if(cbxType.valueProperty().getValue().toString().equals("postgresql"))
	        			{
        				
        					CreateDTB.getInstance().createNewDTB(
        						tfName.getText(), 
        						ConnectToDTB.getInstance().getDefaultConnection("postgresql"), 
        						"postgresql");
        					JavaInterface.getInstance().refreshMigrListViews();
	        			}
        			else
        				alertWindow("Type of DB", "The type of DB is not supported.");
	        	}
        });
        
        hbButtons.getChildren().addAll(btnSubmit, btnCancel);
        gridPane.add(lblName, 0, 0);
        gridPane.add(tfName, 1, 0);
        gridPane.add(lblType, 0, 1);
        gridPane.add(cbxType, 1, 1);
        gridPane.add(hbButtons, 0, 2, 2, 1);

		Scene scene = new Scene(gridPane, 500, 200);
		window.setScene(scene);
		window.show();
	}
	
	public void displayXMLWindow()
	{
		Stage window = new Stage();
		window.initModality(Modality.APPLICATION_MODAL);
		
		window.setTitle("Download to XML");
		window.setMinWidth(300);
		window.setMinHeight(300);
		
		GridPane gridPane = new GridPane();
        gridPane.setAlignment(Pos.CENTER); 
        gridPane.setHgap(10);
        gridPane.setVgap(12);
        
        ColumnConstraints column1 = new ColumnConstraints();
        column1.setHalignment(HPos.RIGHT);  
        gridPane.getColumnConstraints().add(column1); 

        ColumnConstraints column2 = new ColumnConstraints();
        column2.setHalignment(HPos.LEFT);  
        gridPane.getColumnConstraints().add(column2); 

        HBox hbButtons = new HBox();
        hbButtons.setSpacing(10.0);
        hbButtons.setAlignment(Pos.CENTER);  

        Button btnSubmit = new Button("Submit");
        Button btnCancel = new Button("Cancel");
        btnSubmit.setStyle("-fx-font-size: 10pt;");
        btnCancel.setStyle("-fx-font-size: 10pt;");
        
        btnCancel.setOnAction(event -> window.close());
        
        Label lblType = new Label("Please choose the type of the DB:");
        
        ObservableList<String> types = FXCollections.observableArrayList(ConnectionPropertiesBuilder.getInstance().getConnectionsList());
        ComboBox cbxType = new ComboBox<String>(types);
        cbxType.setMinWidth(150);

        btnSubmit.setOnAction(event -> 
        {
        	if(cbxType.valueProperty().getValue() == null)
        		alertWindow("Choose DB", "Please choose the DB.");
        	else
	        	{
        			ToXML.getInstance().fromTableToXML(ConnectToDTB.getInstance().getConnection(cbxType.valueProperty().getValue().toString()));
	        	}	
        });
        
        hbButtons.getChildren().addAll(btnSubmit, btnCancel);
        gridPane.add(lblType, 0, 1);
        gridPane.add(cbxType, 1, 1);
        gridPane.add(hbButtons, 0, 2, 2, 1);

		Scene scene = new Scene(gridPane, 500, 200);
		window.setScene(scene);
		window.show();
	}
	
	public void alertWindow(String title, String message)
	{
		Stage window = new Stage();
		window.initModality(Modality.APPLICATION_MODAL);
		
		window.setTitle(title);
		window.setMinWidth(100);
		window.setMinHeight(100);
		
		
		GridPane gridPane = new GridPane();
        gridPane.setAlignment(Pos.CENTER); 
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        
        Label lblName = new Label(message);
        lblName.setStyle("-fx-font-size: 15pt;");
        gridPane.add(lblName, 3, 3);

        Button btnOk = new Button("OK");
        btnOk.setStyle("-fx-font-size: 15pt;");
        btnOk.setAlignment(Pos.CENTER);
        btnOk.setMinWidth(150.0);
        gridPane.add(btnOk, 3, 4);
        
        btnOk.setOnAction(event -> window.close());
        
        Scene scene = new Scene(gridPane, 500, 200);
		window.setScene(scene);
		window.show();
	}
}
