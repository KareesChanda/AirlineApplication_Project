import java.sql.Connection;


import javafx.geometry.Insets;

import javafx.geometry.Pos;

import javafx.scene.Scene;

import javafx.scene.control.Button;

import javafx.scene.control.Label;

import javafx.scene.layout.BorderPane;

import javafx.scene.layout.GridPane;

import javafx.scene.text.Font;

import javafx.stage.Stage;


public class AdminMenu extends BorderPane{

	private static Connection conn = null;

	private static Stage mainWindow = null;

	static Scene bookFlightScene, createFlightScene, viewPassengerScene;

	static Scene viewFlightScene, viewBookingScene, viewTotalFlightsScene, viewPilotsScene, archiveScene;

	
	public AdminMenu(Connection connection, Stage s) {

		conn = connection;

		mainWindow = s;

		
		//Menu Label
		Label menuLabel = new Label();

		menuLabel.setText("ADMIN MENU");

		menuLabel.setFont(new Font(30));

		
		GridPane gridPane = new GridPane();

		gridPane.setAlignment(Pos.CENTER);

		gridPane.setVgap(20);

		
		//View Passenger Button

		Button getPassengerButton = new Button("View Passengers");

		GridPane.setConstraints(getPassengerButton, 0, 0);

		getPassengerButton.setPrefWidth(300);

		getPassengerButton.setOnAction(e -> {

			BorderPane viewPassenger = new ViewPassenger(conn, mainWindow);

			viewPassengerScene = new Scene(viewPassenger,970,650);

			mainWindow.setScene(viewPassengerScene);

		});


		//View Flights Button

		Button viewFlightButton = new Button("View Flights");

		GridPane.setConstraints(viewFlightButton, 0, 1);

		viewFlightButton.setPrefWidth(300);

		viewFlightButton.setOnAction(e -> {

			BorderPane viewFlight = new ViewFlight(conn, mainWindow);

			viewFlightScene = new Scene(viewFlight,970,650);

			mainWindow.setScene(viewFlightScene);

		});

		
		//Create a flight button

		Button createFlightButton = new Button("Create a Flight");

		GridPane.setConstraints(createFlightButton, 0, 2);

		createFlightButton.setPrefWidth(300);

		createFlightButton.setPrefWidth(300);

		createFlightButton.setOnAction(e -> {

			BorderPane createFlight = new CreateFlight(conn, mainWindow);

			createFlightScene = new Scene(createFlight,970,650);

			mainWindow.setScene(createFlightScene);

		});


		//Create a flight button

		Button viewBookingButton = new Button("View Bookings");

		GridPane.setConstraints(viewBookingButton, 0, 3);

		viewBookingButton.setPrefWidth(300);

		viewBookingButton.setPrefWidth(300);

		viewBookingButton.setOnAction(e -> {

			BorderPane viewBooking = new ViewBooking(conn, mainWindow);

			viewBookingScene = new Scene(viewBooking,970,650);

			mainWindow.setScene(viewBookingScene);

		});
		
		
		//Create a flight button

		Button viewTotalFlightsButton = new Button("View Total Flights Per Airline");

		GridPane.setConstraints(viewTotalFlightsButton, 0, 4);

		viewTotalFlightsButton.setPrefWidth(300);

		viewTotalFlightsButton.setPrefWidth(300);

		viewTotalFlightsButton.setOnAction(e -> {

			BorderPane viewTotal = new ViewTotalFlightsPerAirline(conn, mainWindow);

			viewTotalFlightsScene = new Scene(viewTotal,970,650);

			mainWindow.setScene(viewTotalFlightsScene);

		});
		
		
		
		//View Pilot button

		Button viewPilotsButton = new Button("View Pilots");

		GridPane.setConstraints(viewPilotsButton, 0, 5);

		viewPilotsButton.setPrefWidth(300);

		viewPilotsButton.setPrefWidth(300);

		viewPilotsButton.setOnAction(e -> {

			BorderPane viewPilot = new ViewPilot(conn, mainWindow);

			viewPilotsScene = new Scene(viewPilot,970,650);

			mainWindow.setScene(viewPilotsScene);

		});
		
		
		//Archive Flight Button

		Button archiveFlightButton = new Button("Archive Flights");

		GridPane.setConstraints(archiveFlightButton, 0, 6);

		archiveFlightButton.setPrefWidth(300);

		archiveFlightButton.setPrefWidth(300);

		archiveFlightButton.setOnAction(e -> {

			BorderPane flightArchive = new FlightArchive(conn, mainWindow);

			archiveScene = new Scene(flightArchive,970,650);

			mainWindow.setScene(archiveScene);

		});		
		
		
		
//Logout button

		Button logoutButton = new Button("Logout");

		GridPane.setConstraints(logoutButton, 0, 7);

		logoutButton.setPrefWidth(300);

		logoutButton.setPrefWidth(300);

		logoutButton.setOnAction(e -> {

			Main.setAdmin(false);

			Main.setUserID(0);

			mainWindow.setScene(Main.loginScene);

		});

		
		gridPane.getChildren().addAll(getPassengerButton, viewFlightButton, createFlightButton,
	viewBookingButton,viewTotalFlightsButton, viewPilotsButton, archiveFlightButton, logoutButton);

		
		//BorderPane

		setTop(menuLabel);

		setCenter(gridPane);

		setAlignment(menuLabel, Pos.CENTER);

		setMargin(menuLabel, new Insets(10,0,0,0));

		
	}
//Admin Constructor

}
