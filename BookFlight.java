import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class BookFlight extends BorderPane{
	private static Connection conn = null;
	private static Stage mainWindow = null;
	String departDateStr = "";
	static Scene flightResultScene;
	
	public BookFlight(Connection connection, Stage s) {
		conn = connection;
		mainWindow = s;
		
		//=============================== TOP CONTENT ================================
		//Menu Label
		Label menuLabel = new Label();
		menuLabel.setText("Book Flights");
		menuLabel.setFont(new Font(30));
		
		//============================ CENTER CONTENT ================================
		//GridPane for inputs
		GridPane gridPane = new GridPane();
		gridPane.setAlignment(Pos.CENTER);
		gridPane.setVgap(10);
		
		//Depart Label and Input
        Label departLabel = new Label("DEPART");
        GridPane.setConstraints(departLabel, 0, 0);
		
        TextField departInput = new TextField();
        departInput.setPrefWidth(350.0);
        GridPane.setConstraints(departInput, 0, 1);
		       
        //Arrive Label and Input
        Label arriveLabel = new Label("ARRIVE");
        GridPane.setConstraints(arriveLabel, 0, 2);
		
        TextField arriveInput = new TextField();
        GridPane.setConstraints(arriveInput, 0, 3);
        
        //Depart Date Label and Input
        Label departDateLabel = new Label("DEPART DATE");
        GridPane.setConstraints(departDateLabel, 0, 4);
		
        DatePicker departDateInput = new DatePicker();
        GridPane.setConstraints(departDateInput, 0, 5);
        departDateInput.setOnAction(e -> {
        	LocalDate date = departDateInput.getValue();
        	departDateStr = date.toString();
        	
        });
        gridPane.getChildren().addAll(departLabel, departInput, arriveLabel, arriveInput,
				departDateLabel, departDateInput);
        
        //=============================== BOTTOM MENU ================================
        //Search button
        Button searchButton = new Button("SEARCH FLIGHTS");
        searchButton.setPrefWidth(250);
        searchButton.setOnAction(e -> {
        	String errorMessage = "";
        	if(departInput.getText().equals("")) errorMessage += "Depart location is required!\n";
        	if(arriveInput.getText().equals("")) errorMessage += "Arrive location is required!\n";
        	if(departDateStr.equals("")) errorMessage += "Depart date is Required!\n";
        	
        	if(!errorMessage.equals("")) AlertBox.display("Error", errorMessage);
        	else if(searchFlights(departInput, arriveInput, departDateStr)){
        	
        	}else AlertBox.display("Error", "No flights are found with given information");
        	
        });
        
		//Back buttons
		Button backButton = new Button("BACK");
		backButton.setPrefWidth(100);
		backButton.setOnAction(e -> mainWindow.setScene(Main.menuScene));
		
		//GridPane for bottom buttons
		GridPane gridPane2 = new GridPane();
		gridPane2.setHgap(20);
		GridPane.setConstraints(backButton, 0, 0);
		GridPane.setConstraints(searchButton, 1, 0);
		gridPane2.setAlignment(Pos.CENTER_RIGHT);				
		gridPane2.getChildren().addAll(backButton, searchButton);
		
		
		//=============================== BORDER PANE ================================
		setTop(menuLabel);
		setAlignment(menuLabel, Pos.CENTER);
		setMargin(menuLabel, new Insets(10,0,0,0));
		
		setCenter(gridPane);
		
		setBottom(gridPane2);
		setAlignment(gridPane2, Pos.CENTER);
		setMargin(gridPane2, new Insets(0,20,20,10));
	}//BookFlight Constructor
	
	
	private boolean searchFlights(TextField departInput, TextField arriveInput, String departDateStr) {
		UserRequests requests = new UserRequests();
		try{
			String origin = departInput.getText().trim();
			String destination = arriveInput.getText().trim();
			
			ResultSet resultSet = requests.getFlightsByRouteDate(conn, origin, destination, departDateStr);
			if(resultSet.next()){
				resultSet.previous();
				BorderPane flightResult = new FlightResult(conn, mainWindow, resultSet);
				flightResultScene = new Scene(flightResult,970,650);
				mainWindow.setScene(flightResultScene);
				return true;
			}
		}catch (SQLException e){};
		
		return false;
	}
	
}
