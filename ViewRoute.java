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

public class ViewRoute extends BorderPane{
	private static Connection conn = null;
	private static Stage mainWindow = null;
	private static int departAirportID = 0;
	private static int arriveAirportID = 0;
	private static int routeID = 0;
	TextField routeInput;

	static Scene flightResultContScene;
	
	TableView<Airport> departAirportTable, arriveAirportTable;
	TableView<Route> routeTable;
	
	@SuppressWarnings("unchecked")
	public ViewRoute(Connection connection, Stage s, TextField routeInput) {
		conn = connection;
		mainWindow = s;
		this.routeInput = routeInput;
		
		//======================================= DEPART MENU CONTENT =======================================
		//Depart City Label and TextField
		Label departCityLabel = new Label();
		departCityLabel.setFont(new Font(15));
		departCityLabel.setText("Departing City");
		
		TextField departCityInput = new TextField();
		departCityInput.setPromptText("City");
		departCityInput.setPrefWidth(200);
		
		//Depart Country Label and TextField
		Label departCountryLabel = new Label();
		departCountryLabel.setFont(new Font(15));
		departCountryLabel.setText("Departing Country");
		
		TextField departCountryInput = new TextField();
		departCountryInput.setPromptText("Country");
		departCountryInput.setPrefWidth(200);
		
		//Submit Button
		Button submit = new Button("FIND AIRPORT");
		submit.setPrefWidth(200);
		submit.setOnAction(e -> {
			if(!getAirports(departCityInput, departCountryInput, departAirportTable)){
				AlertBox.display("Error", "No airports are found\nPlease check again");
			}
		});
		
		//GridPane for top elements
		GridPane departGridPane = new GridPane();
		departGridPane.setPadding(new Insets(10,0,10,0));
		departGridPane.setHgap(30);
		GridPane.setConstraints(departCityLabel, 0, 0);
		GridPane.setConstraints(departCityInput, 0, 1);
		GridPane.setConstraints(departCountryLabel, 1, 0);
		GridPane.setConstraints(departCountryInput, 1, 1);
		GridPane.setConstraints(submit, 2, 1);
		
		departGridPane.setAlignment(Pos.CENTER_LEFT);				
		departGridPane.getChildren().addAll(departCityLabel, departCityInput, 
							departCountryLabel, departCountryInput, submit);		
		
		//==================================== DEPART TABLE CONTENT ========================================
		//Menu Label
		Label menuLabel = new Label();
		menuLabel.setText("1. Select Departing Airport");
		menuLabel.setFont(new Font(20));

		//Declaring columns
        TableColumn<Airport, Integer> airportID = new TableColumn<>("AirportID");
        airportID.setMinWidth(60);
        airportID.setCellValueFactory(new PropertyValueFactory<>("airportID"));
		
        TableColumn<Airport, String> airportName = new TableColumn<>("Name");
        airportName.setMinWidth(200);
        airportName.setCellValueFactory(new PropertyValueFactory<>("name"));
		
        TableColumn<Airport, String> airportCity = new TableColumn<>("City");
        airportCity.setMinWidth(150);
        airportCity.setCellValueFactory(new PropertyValueFactory<>("city"));
        
        TableColumn<Airport, String> airportCountry = new TableColumn<>("Country");
        airportCountry.setMinWidth(150);
        airportCountry.setCellValueFactory(new PropertyValueFactory<>("country"));
        
        TableColumn<Airport, String> airportCode = new TableColumn<>("Code");
        airportCode.setMinWidth(50);
        airportCode.setCellValueFactory(new PropertyValueFactory<>("code"));
    
        //Declaring TableView
        departAirportTable = new TableView<>();
        departAirportTable.setMaxWidth(660);
        departAirportTable.setPrefHeight(180);
        departAirportTable.getColumns().addAll(airportID, airportName, airportCity,
        								airportCountry, airportCode);
        departAirportTable.getSelectionModel().selectedItemProperty().addListener(
        		new ChangeListener<Airport>() {
					@Override
					public void changed(ObservableValue<? extends Airport> observable, Airport oldValue, Airport newValue) {
						departAirportID = departAirportTable.getSelectionModel().getSelectedItem().getAirportID();
						if(arriveAirportID != 0) 
							routeTable.setItems(getRouteList());
					}
				}        		
        );
        VBox vBox = new VBox();
        vBox.getChildren().addAll(menuLabel, departGridPane, departAirportTable);
        
        
		//======================================= ARRIVE MENU CONTENT =======================================
		//Depart City Label and TextField
		Label arriveCityLabel = new Label();
		arriveCityLabel.setFont(new Font(15));
		arriveCityLabel.setText("Arriving City");
		
		TextField arriveCityInput = new TextField();
		arriveCityInput.setPromptText("City");
		arriveCityInput.setPrefWidth(200);
		
		//Depart Country Label and TextField
		Label arriveCountryLabel = new Label();
		arriveCountryLabel.setFont(new Font(15));
		arriveCountryLabel.setText("Arriving Country");
		
		TextField arriveCountryInput = new TextField();
		arriveCountryInput.setPromptText("Country");
		arriveCountryInput.setPrefWidth(200);
		
		//Submit Button
		Button submit2 = new Button("FIND AIRPORT");
		submit2.setPrefWidth(200);
		submit2.setOnAction(e -> {
			if(!getAirports(arriveCityInput, arriveCountryInput, arriveAirportTable)){
				AlertBox.display("Error", "No airports are found\nPlease check again");
			}
		});
		
		//GridPane for top elements
		GridPane arriveGridPane = new GridPane();
		arriveGridPane.setPadding(new Insets(10,0,10,0));
		arriveGridPane.setHgap(30);
		GridPane.setConstraints(arriveCityLabel, 0, 0);
		GridPane.setConstraints(arriveCityInput, 0, 1);
		GridPane.setConstraints(arriveCountryLabel, 1, 0);
		GridPane.setConstraints(arriveCountryInput, 1, 1);
		GridPane.setConstraints(submit2, 2, 1);
		
		arriveGridPane.setAlignment(Pos.CENTER_LEFT);				
		arriveGridPane.getChildren().addAll(arriveCityLabel, arriveCityInput, 
							arriveCountryLabel, arriveCountryInput, submit2);		
        
		//==================================== ARRIVE TABLE CONTENT ========================================
		//Menu Label
		Label menuLabel2 = new Label();
		menuLabel2.setText("2. Select Arriving Airport");
		menuLabel2.setPadding(new Insets(20,0,0,0));
		menuLabel2.setFont(new Font(20));
        
		//Declaring columns
        TableColumn<Airport, Integer> airportID2 = new TableColumn<>("AirportID");
        airportID2.setMinWidth(60);
        airportID2.setCellValueFactory(new PropertyValueFactory<>("airportID"));
		
        TableColumn<Airport, String> airportName2 = new TableColumn<>("Name");
        airportName2.setMinWidth(200);
        airportName2.setCellValueFactory(new PropertyValueFactory<>("name"));
		
        TableColumn<Airport, String> airportCity2 = new TableColumn<>("City");
        airportCity2.setMinWidth(150);
        airportCity2.setCellValueFactory(new PropertyValueFactory<>("city"));
        
        TableColumn<Airport, String> airportCountry2 = new TableColumn<>("Country");
        airportCountry2.setMinWidth(150);
        airportCountry2.setCellValueFactory(new PropertyValueFactory<>("country"));
        
        TableColumn<Airport, String> airportCode2 = new TableColumn<>("Code");
        airportCode2.setMinWidth(50);
        airportCode2.setCellValueFactory(new PropertyValueFactory<>("code"));
 		
		
		//Declaring Table View
		arriveAirportTable = new TableView<>();
		arriveAirportTable.setMaxWidth(660);
        arriveAirportTable.setPrefHeight(180);
        arriveAirportTable.getColumns().addAll(airportID2, airportName2, airportCity2,
        								airportCountry2, airportCode2);
        arriveAirportTable.getSelectionModel().selectedItemProperty().addListener(
        		new ChangeListener<Airport>() {
					@Override
					public void changed(ObservableValue<? extends Airport> observable, Airport oldValue, Airport newValue) {
						arriveAirportID = arriveAirportTable.getSelectionModel().getSelectedItem().getAirportID();
						if(departAirportID != 0) 
							routeTable.setItems(getRouteList());
					}
				}        		
        );
        vBox.getChildren().addAll(menuLabel2, arriveGridPane, arriveAirportTable);
        
        //==================================== ARRIVE TABLE CONTENT ========================================
        //Menu Label
        Label menuLabel3 = new Label();
        menuLabel3.setText("3. Select Your Route");
        menuLabel3.setFont(new Font(20));	
        menuLabel3.setPadding(new Insets(0,0,10,0));
		
		//Declaring Route columns
        TableColumn<Route, Integer> routeIDCol = new TableColumn<>("RouteID");
        routeIDCol.setMinWidth(60);
        routeIDCol.setCellValueFactory(new PropertyValueFactory<>("routeID"));
		
        TableColumn<Route, String> airline = new TableColumn<>("Airline Name");
        airline.setMinWidth(200);
        airline.setCellValueFactory(new PropertyValueFactory<>("airline"));
		
		//Declaring Route Table View
		routeTable = new TableView<>();
		routeTable.setPrefHeight(500);
		
		routeTable.getColumns().addAll(routeIDCol, airline);
		routeTable.getSelectionModel().selectedItemProperty().addListener(
        		new ChangeListener<Route>() {
					@Override
					public void changed(ObservableValue<? extends Route> observable, Route oldValue, Route newValue) {
						if(departAirportID != 0 & arriveAirportID != 0)
							routeID = routeTable.getSelectionModel().getSelectedItem().getRouteID();
					}
				}        		
        );
        VBox vBox2 = new VBox();
        vBox2.getChildren().addAll(menuLabel3,routeTable);
       
        //=================================== BOTTOM MENU ====================================
        //Back Button
		Button backButton = new Button("BACK");
		backButton.setPrefWidth(100);
		backButton.setOnAction(e -> mainWindow.setScene(AdminMenu.createFlightScene));
		
		//Cancel Flight Button
		Button useRouteButton = new Button("USE THIS ROUTE");
		useRouteButton.setPrefWidth(250);
		useRouteButton.setOnAction(e -> {
			if(routeID == 0) AlertBox.display("Error", "Please select a routeID");
			else{
				mainWindow.setScene(AdminMenu.createFlightScene);
				routeInput.setText(String.valueOf(routeID));
			}	
		});
		
		//GridPane for bottom buttons
		GridPane gridPane = new GridPane();
		gridPane.setHgap(20);
		GridPane.setConstraints(backButton, 0, 0);
		GridPane.setConstraints(useRouteButton, 1, 0);
		gridPane.setAlignment(Pos.CENTER_RIGHT);
		gridPane.getChildren().addAll(backButton, useRouteButton);
		
		//BorderPane
		setLeft(vBox);
		setAlignment(vBox, Pos.TOP_LEFT);
		setMargin(vBox, new Insets(20,20,20,20));

		setCenter(vBox2);
		setAlignment(vBox2, Pos.CENTER);
		setMargin(vBox2, new Insets(20,20,20,0));
		
		setBottom(gridPane);
		setAlignment(gridPane, Pos.CENTER);
		setMargin(gridPane, new Insets(0,20,20,10));		
	}//ViewRoute Constructor
	
    /**=================================== getAirports ===================================
     * 
     */
    private boolean getAirports(TextField cityInput,TextField countryInput, TableView<Airport> table){
    	String tempCity = cityInput.getText().trim();
    	String tempCountry = countryInput.getText().trim();
    	if(tempCity.equals("") && tempCountry.equals("")){
    		return false;
    	}
    	try{
    		//User LIKE String comparison
    		tempCity = tempCity.concat("%");
    		tempCountry = tempCountry.concat("%");
        	table.setItems(getFlightList(tempCity, tempCountry));
        	return true;
    	}catch(Exception e){
    		return false;
    	}
    }//getFlights

	//Get all of the airports
    public ObservableList<Airport> getFlightList(String cityInput, String countryInput){
        ObservableList<Airport> airports = FXCollections.observableArrayList();
        UserRequests requests = new UserRequests();
        ResultSet airportsRS = null;        
        try{		

        	airportsRS = requests.getAirportByCityCountry(conn, cityInput, countryInput);
			while(airportsRS.next()){

				int airportID = airportsRS.getInt("airportID");
				String name = airportsRS.getString("name");
				String city = airportsRS.getString("city");
				String country = airportsRS.getString("country");
				String code = airportsRS.getString("code");
				airports.add(new Airport(airportID, name, city, country, code));
			}
			
		}catch (SQLException e){};
		return airports;        
    }//ObservableList
    
	//Get all of the routes
    public ObservableList<Route> getRouteList(){
        ObservableList<Route> routes = FXCollections.observableArrayList();
        UserRequests requests = new UserRequests();
        ResultSet routesRS = null;        
        try{		
        	routesRS = requests.getRouteByAirline(conn, departAirportID, arriveAirportID);
        	while(routesRS.next()){
				int tempRouteID = routesRS.getInt("routeID");
				String airlineName = routesRS.getString("name");
				routes.add(new Route(tempRouteID, airlineName));
			}
			
		}catch (SQLException e){};
		return routes;        
    }//ObservableList
    
    
    public static int getDepartAirportID(){
		return departAirportID;
	}//getDepartAirportID
	
    public static int getArriveAirportID(){
		return arriveAirportID;
	}//getArriveAirportID
    
}
