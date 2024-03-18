import java.sql.Connection;
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

public class CreateFlight extends BorderPane{
	private static Connection conn = null;
	private static Stage mainWindow = null;
	String departDateStr = "";
	String arriveDateStr = "";

	static Scene flightResultScene;
	static Scene viewRouteScene;
	
	public CreateFlight(Connection connection, Stage s) {
		conn = connection;
		mainWindow = s;
		
		//=============================== TOP CONTENT ================================
		//Menu Label
		Label menuLabel = new Label();
		menuLabel.setText("Create a Flight");
		menuLabel.setFont(new Font(30));
		
		//============================ CENTER CONTENT ================================
		//GridPane for inputs
		GridPane gridPane = new GridPane();
		gridPane.setAlignment(Pos.CENTER);
		gridPane.setVgap(10);
		gridPane.setHgap(10);
		
        //Flight RouteID Label and Input
        Label routeLabel = new Label("ROUTE ID");
        GridPane.setConstraints(routeLabel, 0, 0);
		
        TextField routeInput = new TextField();
        routeInput.setPrefWidth(150);
        GridPane.setConstraints(routeInput, 0, 1);
        
        //Find RouteID Button
        Button findRouteButton = new Button();
        findRouteButton.setText("Find RouteID");
        findRouteButton.setPrefWidth(150);
        GridPane.setConstraints(findRouteButton, 0, 2);
        findRouteButton.setOnAction(e -> {
        	BorderPane viewRoute = new ViewRoute(conn, mainWindow, routeInput); 
        	viewRouteScene = new Scene(viewRoute,970,650);
        	mainWindow.setScene(viewRouteScene);
        });
        
        
		//Depart Date Label and Input
        Label departDateLabel = new Label("DEPART DATE");
        GridPane.setConstraints(departDateLabel, 0, 3);
		
        DatePicker departDateInput = new DatePicker();
        GridPane.setConstraints(departDateInput, 0, 4);
        departDateInput.setOnAction(e -> {
        	LocalDate date = departDateInput.getValue();
        	departDateStr = date.toString();
        });
		
		//DepartTime Label and Input
        Label departTimeLabel = new Label("DEPART TIME (24HR)");
        GridPane.setConstraints(departTimeLabel, 0, 5);
		
        TextField departTimeInput = new TextField();
        GridPane.setConstraints(departTimeInput, 0, 6);
        
		       
        //Arrive Date Label and Input
        Label arriveDateLabel = new Label("ARRIVE DATE");
        GridPane.setConstraints(arriveDateLabel, 0, 7);
		
        DatePicker arriveDateInput = new DatePicker();
        GridPane.setConstraints(arriveDateInput, 0, 8);
        arriveDateInput.setOnAction(e -> {
        	LocalDate date = arriveDateInput.getValue();
        	arriveDateStr = date.toString();
        });
        
        //ArriveTime Label and Input
        Label arriveTimeLabel = new Label("ARRIVE TIME (24HR)");
        GridPane.setConstraints(arriveTimeLabel, 0, 9);
		
        TextField arriveTimeInput = new TextField();
        GridPane.setConstraints(arriveTimeInput, 0, 10);
       
        gridPane.getChildren().addAll(routeLabel, routeInput, findRouteButton, 
        		departDateLabel, departDateInput, departTimeLabel, departTimeInput,
        		arriveDateLabel, arriveDateInput, arriveTimeLabel, arriveTimeInput);
        
                
        //=============================== BOTTOM MENU ================================
        //Search button
        Button createButton = new Button("CREATE FLIGHT");
        createButton.setPrefWidth(250);
        createButton.setOnAction(e -> {
        	String errorMessage = "";
        	if(routeInput.getText().equals("")) errorMessage += "RouteID is required!\n";
        	if(departDateStr.equals("")) errorMessage += "Depart date is Required!\n";
        	if(departTimeInput.getText().equals("")) errorMessage += "Depart time is required!\n";
        	if(arriveDateStr.equals("")) errorMessage += "Arrive date is Required!\n";
        	if(arriveTimeInput.getText().equals("")) errorMessage += "Arrive time is required!\n";
        	
        	if(!errorMessage.equals("")) AlertBox.display("Error", errorMessage);
        	else if(setFlight(routeInput, departTimeInput, arriveTimeInput)){
        		AlertBox.display("Success", "The new flight is successfully created!");
        		routeInput.setText("");
        		departDateInput.getEditor().clear();
        		arriveDateInput.getEditor().clear();
        		departTimeInput.setText("");
        		arriveTimeInput.setText("");
        	}else AlertBox.display("Error", "Error creating a flight with given information");
        	
        });
        
		//Back buttons
		Button backButton = new Button("BACK");
		backButton.setPrefWidth(100);
		backButton.setOnAction(e -> mainWindow.setScene(Main.adminScene));
		
		//GridPane for bottom buttons
		GridPane gridPane2 = new GridPane();
		gridPane2.setHgap(20);
		GridPane.setConstraints(backButton, 0, 0);
		GridPane.setConstraints(createButton, 1, 0);
		gridPane2.setAlignment(Pos.CENTER_RIGHT);				
		gridPane2.getChildren().addAll(backButton, createButton);
		
		
		//=============================== BORDER PANE ================================
		setTop(menuLabel);
		setAlignment(menuLabel, Pos.CENTER);
		setMargin(menuLabel, new Insets(10,0,0,0));
		
		setCenter(gridPane);
		
		setBottom(gridPane2);
		setAlignment(gridPane2, Pos.CENTER);
		setMargin(gridPane2, new Insets(0,20,20,10));
	}//CreateFlight Constructor
	
	
	private boolean setFlight(TextField routeInput, TextField departTime, TextField arriveTime) {
		
		UserRequests requests = new UserRequests();
		try{
			String startTime = departTime.getText().trim();
			String endTime = arriveTime.getText().trim();
			int route = Integer.parseInt(routeInput.getText().trim());
			int result = requests.createFlight(conn, departDateStr, startTime, arriveDateStr, endTime, route);
			if(result == 1){
				return true;
			}else {
				return false;
			}
		}catch (SQLException e){};
		
		return false;
	}
	
}
