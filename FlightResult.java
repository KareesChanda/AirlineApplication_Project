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
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class FlightResult extends BorderPane{
	private static Connection conn = null;
	private static Stage mainWindow = null;
	private static int flightID = 0;
	static Scene flightResultContScene;
	
	TableView<Flight> flightTable;
	
	@SuppressWarnings("unchecked")
	public FlightResult(Connection connection, Stage s, ResultSet rs) {
		conn = connection;
		mainWindow = s;
		
		//Menu Label
		Label menuLabel = new Label();
		menuLabel.setText("Select Departing Flight:");
		menuLabel.setFont(new Font(30));
		//======================================================================
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
        flightTable.setItems(getFlights(rs));
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
		Button backButton = new Button("BACK");
		backButton.setPrefWidth(100);
		backButton.setOnAction(e -> mainWindow.setScene(UserMenu.bookFlightScene));
		
		//Book Flight buttons
		Button bookButton = new Button("BOOK SELECTED FLIGHT");
		bookButton.setPrefWidth(250);
		bookButton.setOnAction(e -> {
			if(flightID == 0) AlertBox.display("Error", "Please select a flight");
			else{
				BorderPane flightResultCont = new FlightResultCont(conn, mainWindow);
				flightResultContScene = new Scene(flightResultCont,970, 650);
				mainWindow.setScene(flightResultContScene);
			}	
		});
		
		//GridPane for bottom buttons
		GridPane gridPane = new GridPane();
		gridPane.setHgap(20);
		GridPane.setConstraints(backButton, 0, 0);
		GridPane.setConstraints(bookButton, 1, 0);
		gridPane.setAlignment(Pos.CENTER_RIGHT);
				
		gridPane.getChildren().addAll(backButton, bookButton);
		
		//BorderPane
		setTop(menuLabel);
		setAlignment(menuLabel, Pos.TOP_LEFT);
		setMargin(menuLabel, new Insets(20,0,20,20));
		
		setCenter(vBox);
		setAlignment(vBox, Pos.CENTER);
		setMargin(vBox, new Insets(0,20,0,20));
		
		setBottom(gridPane);
		setAlignment(gridPane, Pos.CENTER);
		setMargin(gridPane, new Insets(0,20,20,10));		
	}
	
	
	//Get all of the flights
    public ObservableList<Flight> getFlights(ResultSet resultSet){
        ObservableList<Flight> flights = FXCollections.observableArrayList();
        
        try{			
			while(resultSet.next()){
				int flightNum = resultSet.getInt("flightNumber");
				String departureTime = resultSet.getString("departureTime");
				String departureAirport = resultSet.getString("departureAirport");
				String arrivalTime = resultSet.getString("arrivalTime");
				String arrivalAirport = resultSet.getString("arrivalAirport");
				String airlineName = resultSet.getString("airlineName");
				flights.add(new Flight(flightNum, departureTime, departureAirport, arrivalTime, arrivalAirport, airlineName));
			}
			
		}catch (SQLException e){};
		
		return flights;        
    }//ObservableList
    
    public static int getflightID(){
		return flightID;
	}
	
}
