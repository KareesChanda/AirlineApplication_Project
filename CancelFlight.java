import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class CancelFlight extends BorderPane{
	private static Connection conn = null;
	private static Stage mainWindow = null;
	TableView<Flight> flightTable;
	private static int flightID = 0;

	@SuppressWarnings("unchecked")
	public CancelFlight(Connection connection, Stage s) {
		conn = connection;
		mainWindow = s;
		
		//=============================== TOP CONTENT ================================
		//Menu Label
		Label menuLabel = new Label();
		menuLabel.setText("Select Flight to Cancel");
		menuLabel.setFont(new Font(30));
		
		//============================ CENTER CONTENT ================================
		
		//Declaring columns
        TableColumn<Flight, Integer> flightNum = new TableColumn<>("Flight#");
        flightNum.setMinWidth(100);
        flightNum.setCellValueFactory(new PropertyValueFactory<>("flightNumber"));
		
        TableColumn<Flight, String> departureTime = new TableColumn<>("Depart");
        departureTime.setMinWidth(50);
        departureTime.setCellValueFactory(new PropertyValueFactory<>("departureTime"));
		
        TableColumn<Flight, String> departureAirport = new TableColumn<>("Depart Airport");
        departureAirport.setMinWidth(240);
        departureAirport.setCellValueFactory(new PropertyValueFactory<>("departureAirport"));
        
        TableColumn<Flight, String> arrivalTime = new TableColumn<>("Arrive");
        arrivalTime.setMinWidth(50);
        arrivalTime.setCellValueFactory(new PropertyValueFactory<>("arrivalTime"));
        
        TableColumn<Flight, String> arrivalAirport = new TableColumn<>("Arrive Airport");
        arrivalAirport.setMinWidth(220);
        arrivalAirport.setCellValueFactory(new PropertyValueFactory<>("arrivalAirport"));
        
        TableColumn<Flight, String> airlineName = new TableColumn<>("Airline");
        airlineName.setMinWidth(200);
        airlineName.setCellValueFactory(new PropertyValueFactory<>("airlineName"));
        
        //Declaring TableView
        flightTable = new TableView<>();
        
        flightTable.setItems(getFlights());
        flightTable.getColumns().addAll(flightNum, departureTime, departureAirport,
        								arrivalTime, arrivalAirport, airlineName);
        flightTable.getSelectionModel().selectedItemProperty().addListener(
        		new ChangeListener<Flight>() {
					@Override
					public void changed(ObservableValue<? extends Flight> observable, Flight oldValue, Flight newValue) {
						flightID = flightTable.getSelectionModel().getSelectedItem().getFlightNumber();
					}
				}        		
        );
        VBox vBox = new VBox();
        vBox.getChildren().addAll(flightTable);

		//=============================== BOTTOM MENU ================================
		//Back button
		Button backButton = new Button("BACK");
		backButton.setPrefWidth(100);
		backButton.setOnAction(e -> mainWindow.setScene(Main.menuScene));
		
		//Continue button
		Button continueButton = new Button("CANCEL RESERVATION");
		continueButton.setPrefWidth(250);
		continueButton.setOnAction(e -> {
			if(cancelFlightReservation()){
				AlertBox.display("Success", "Selected reservation is cancelled successfully");
				mainWindow.setScene(Main.menuScene);
			} else AlertBox.display("Error", "Error cancelling selected reservation");
		});
		
		//GridPane for bottom buttons
		GridPane gridPane2 = new GridPane();
		gridPane2.setHgap(20);
		GridPane.setConstraints(backButton, 0, 0);
		GridPane.setConstraints(continueButton, 1, 0);
		gridPane2.setAlignment(Pos.CENTER_RIGHT);				
		gridPane2.getChildren().addAll(backButton, continueButton);
		
		//=============================== BORDER PANE ================================
		setTop(menuLabel);
		setAlignment(menuLabel, Pos.TOP_LEFT);
		setMargin(menuLabel, new Insets(20,0,20,20));
		
		setCenter(vBox);
		setAlignment(vBox, Pos.CENTER);
		setMargin(vBox, new Insets(0,20,0,20));
		
		setBottom(gridPane2);
		setAlignment(gridPane2, Pos.CENTER);
		setMargin(gridPane2, new Insets(0,20,20,10));
		
	}//Constructor
	
	//Get all of the flights
    public ObservableList<Flight> getFlights(){
        ObservableList<Flight> flights = FXCollections.observableArrayList();
        UserRequests requests = new UserRequests();
        ResultSet bookingRS = null;
        ResultSet flightRS = null;
        try{	
    		bookingRS = requests.getBookingByUser(conn, Main.getUserID());
    		
    		while(bookingRS.next()){
    			int tempFlightID = bookingRS.getInt("flightID");
    			flightRS = requests.getFlightsByFlightID(conn, tempFlightID);
    			
    			while(flightRS.next()){
    				int flightNum = flightRS.getInt("flightNumber");
    				String departureTime = flightRS.getString("departureTime");
    				String departureAirport = flightRS.getString("departureAirport");
    				String arrivalTime = flightRS.getString("arrivalTime");
    				String arrivalAirport = flightRS.getString("arrivalAirport");
    				String airlineName = flightRS.getString("airlineName");
    				flights.add(new Flight(flightNum, departureTime, departureAirport, arrivalTime, arrivalAirport, airlineName));
    			}
    		}
		}catch (SQLException e){};
		
		return flights;        
    }//ObservableList
    
    /**=================================== cancelFlightReservation ===================================
     * Cancel a flight with flightID and userID
     * @return true of flight is cancelled successfully, false otherwise
     */
    private boolean cancelFlightReservation(){
    	
    	try{	
            CallableStatement cs = conn.prepareCall("{CALL cancelFlightReservation(?, ?)}");
            cs.setInt(1, flightID);
    		cs.setInt(2, Main.getUserID());
    		cs.executeQuery();
    		return true;
		}catch (SQLException e){};
    	return false;
    }//cancelFlightReservation
	
}//CancelFlight
