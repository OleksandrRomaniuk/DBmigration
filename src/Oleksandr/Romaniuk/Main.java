package Oleksandr.Romaniuk;


import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javafx.application.Application;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.stage.Stage;


public class Main extends Application
{

	public static void main(String[] args) 
	{	
		
		
		Application.launch(Main.class, args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception
	{
		primaryStage.setTitle("Data migration");
		primaryStage.setScene(JavaInterface.getInstance().getScene());
		primaryStage.show();
	}

	
}
