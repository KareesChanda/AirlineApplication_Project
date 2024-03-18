import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;


import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class ViewFlight extends BorderPane{
	private static Connection conn = null;
	private static Stage mainWindow = null;
	private static int flightID = 0;
	static Scene flightResultContScene;
	
	TableView<Flight> flightTable;
	
	@SuppressWarnings("unchecked")
	public ViewFlight(Connection connection, Stage s) {
		conn = connection;
		mainWindow = s;
		
		//======================================= TOP CONTENT =======================================
		
		//Airline Text Field
		Label airlineLabel = new Label();
		airlineLabel.setFont(new Font(18));
		airlineLabel.setText("Arline Name");
		
		
		TextField airlineInput = new TextField();
		airlineInput.setPromptText("Airline Name");
		airlineInput.setPrefWidth(200);

		//Submit Button
		Button submit = new Button("SUBMIT");
		submit.setPrefWidth(100);
		submit.setOnAction(e -> {
			if(!getFlights(airlineInput)){
				AlertBox.display("Error", "No flights are found\nPlease check again");
			}
		});
		
		//GridPane for top elements
		GridPane gridPaneTop = new GridPane();
		gridPaneTop.setHgap(30);
		GridPane.setConstraints(airlineLabel, 0, 0);
		GridPane.setConstraints(airlineInput, 0, 1);
				
		GridPane.setConstraints(submit, 2, 1);
		
		gridPaneTop.setAlignment(Pos.CENTER_LEFT);				
		//gridPaneTop.getChildren().addAll(flightLabel, flightNumber, choiceLabel, classChoice, submit);		
		gridPaneTop.getChildren().addAll(airlineLabel, airlineInput, submit);		
		
		//==================================== CENTER CONTENT ========================================
		//Menu Label
		Label menuLabel = new Label();
		menuLabel.setText("Flight List");
		menuLabel.setFont(new Font(25));
		menuLabel.setPadding(new Insets(0,0,10,0));

		//Declaring columns
        TableColumn<Flight, Integer> flightNum = new TableColumn<>("Flight#");
        flightNum.setMinWidth(80);
        flightNum.setCellValueFactory(new PropertyValueFactory<>("flightNumber"));

        TableColumn<Flight, String> departureDate = new TableColumn<>("Depart Date");
        departureDate.setMinWidth(100);
        departureDate.setCellValueFactory(new PropertyValueFactory<>("departDate"));
        
        
        TableColumn<Flight, String> departureTime = new TableColumn<>("Depart Time");
        departureTime.setMinWidth(90);
        departureTime.setCellValueFactory(new PropertyValueFactory<>("departureTime"));
		
        TableColumn<Flight, String> departureAirport = new TableColumn<>("Depart Airport");
        departureAirport.setMinWidth(150);
        departureAirport.setCellValueFactory(new PropertyValueFactory<>("departureAirport"));

        TableColumn<Flight, String> arrivalDate = new TableColumn<>("Arrive Date");
        arrivalDate.setMinWidth(100);
        arrivalDate.setCellValueFactory(new PropertyValueFactory<>("arriveDate"));
        
        
        TableColumn<Flight, String> arrivalTime = new TableColumn<>("Arrive Time");
        arrivalTime.setMinWidth(90);
        arrivalTime.setCellValueFactory(new PropertyValueFactory<>("arrivalTime"));
        
        TableColumn<Flight, String> arrivalAirport = new TableColumn<>("Arrive Airport");
        arrivalAirport.setMinWidth(150);
        arrivalAirport.setCellValueFactory(new PropertyValueFactory<>("arrivalAirport"));
        
        TableColumn<Flight, String> airlineName = new TableColumn<>("Airline");
        airlineName.setMinWidth(200);
        airlineName.setCellValueFactory(new PropertyValueFactory<>("airlineName"));
        
        //Declaring TableView
        flightTable = new TableView<>();
        flightTable.setPlaceholder(new Label("No flight found"));

        //flightTable.setItems(getFlights(rs));
        flightTable.getColumns().addAll(flightNum, departureDate, departureTime, departureAirport,
        								arrivalDate, arrivalTime, arrivalAirport, airlineName);
        flightTable.getSelectionModel().selectedItemProperty().addListener(
        		new ChangeListener<Flight>() {

					@Override
					public void changed(ObservableValue<? extends Flight> observable, Flight oldValue, Flight newValue) {
						flightID = flightTable.getSelectionModel().getSelectedItem().getFlightNumber();
					}
        			
        			
				}        		
        );
        VBox vBox = new VBox();
        vBox.getChildren().addAll(menuLabel, flightTable);
        

        //=================================== BOTTOM MENU ====================================
        //Back Button
		Button backButton = new Button("BACK");
		backButton.setPrefWidth(100);
		backButton.setOnAction(e -> mainWindow.setScene(Main.adminScene));
		
		//Cancel Flight Button
		Button cancelButton = new Button("DONE");
		cancelButton.setPrefWidth(250);
		cancelButton.setOnAction(e -> {
			mainWindow.setScene(Main.adminScene);
		});
		
		//GridPane for bottom buttons
		GridPane gridPane = new GridPane();
		gridPane.setHgap(20);
		GridPane.setConstraints(backButton, 0, 0);
		GridPane.setConstraints(cancelButton, 1, 0);
		gridPane.setAlignment(Pos.CENTER_RIGHT);
				
		gridPane.getChildren().addAll(backButton, cancelButton);
		
		//BorderPane
		setTop(gridPaneTop);
		setAlignment(gridPaneTop, Pos.TOP_LEFT);
		setMargin(gridPaneTop, new Insets(20,0,20,20));
		
		setCenter(vBox);
		setAlignment(vBox, Pos.CENTER);
		setMargin(vBox, new Insets(0,20,0,20));
		
		setBottom(gridPane);
		setAlignment(gridPane, Pos.CENTER);
		setMargin(gridPane, new Insets(0,20,20,10));		
	}//ViewFlight Constructor
	
    /**=================================== getFlights ===================================
     * 
     * @return true if valid flight number is found, false otherwise
     */
    private boolean getFlights(TextField airlineInput){
    	if(!airlineInput.getText().trim().equals("")){
    		try{
    			String airlinename = airlineInput.getText().trim();
    			//User LIKE String comparison
    			airlinename = airlinename.concat("%");
        		flightTable.setItems(getFlightList(airlinename));
        		return true;
    		}catch(Exception e){
    			return false;
    		}
    	}
    	return false;
    }//getFlights

	//Get all of the flights
    public ObservableList<Flight> getFlightList(String airlineName){
        ObservableList<Flight> flights = FXCollections.observableArrayList();
        UserRequests requests = new UserRequests();
        ResultSet flightRS = null;        
        try{		

        	flightRS = requests.getFlightsByAirline(conn, airlineName);
			while(flightRS.next()){

				int flightNum = flightRS.getInt("flightNumber");
				String departureTime = flightRS.getString("departureTime");
				String departureAirport = flightRS.getString("departureAirport");
				String arrivalTime = flightRS.getString("arrivalTime");
				String arrivalAirport = flightRS.getString("arrivalAirport");
				String airlineName2 = flightRS.getString("airlineName");
				String departDate = flightRS.getString("departureDate");
				String arriveDate = flightRS.getString("arrivalDate");

				flights.add(new Flight(flightNum, departureTime, departDate, departureAirport, 
						arrivalTime, arriveDate, arrivalAirport, airlineName2));
			}
			
		}catch (SQLException e){};
		return flights;        
    }//ObservableList
    
    public static int getflightID(){
		return flightID;
	}//getflightID
	
}
