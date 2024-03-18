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
import javafx.scene.control.ChoiceBox;
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

public class ViewPassenger extends BorderPane{
	private static Connection conn = null;
	private static Stage mainWindow = null;
	TableView<Passenger> passengerTable;
	private static int passID = 0;
	private static int flightID = 0;
	private String classType = "All";

	@SuppressWarnings("unchecked")
	public ViewPassenger(Connection connection, Stage s) {
		conn = connection;
		mainWindow = s;
		
		//======================================= TOP CONTENT =======================================
		
		//Flight Number Text Field
		Label flightLabel = new Label();
		flightLabel.setFont(new Font(18));
		flightLabel.setText("Flight Number");
		
		TextField flightNumber = new TextField();
		flightNumber.setPromptText("Flight#");
		flightNumber.setPrefWidth(130);
		
		//Class Type Choice Box
		Label choiceLabel = new Label();
		choiceLabel.setFont(new Font(18));
		choiceLabel.setText("Class Type");
		
		final String[] classArray = new String[]{"All", "First", "Economy"};
		ChoiceBox<String> classChoice = new ChoiceBox<String>(FXCollections.observableArrayList(classArray));
		classChoice.setPrefWidth(130);
		classChoice.getSelectionModel().selectFirst();
		classChoice.getSelectionModel().selectedIndexProperty()
        .addListener(new ChangeListener<Number>() {
          @SuppressWarnings("rawtypes")
		public void changed(ObservableValue ov, Number value, Number new_value) {
        	  classType = classArray[new_value.intValue()];
          }
        });
		
		//Submit Button
		Button submit = new Button("SUBMIT");
		submit.setPrefWidth(100);
		submit.setOnAction(e -> {
			if(!getFlightPassengers(flightNumber)){
				AlertBox.display("Error", "No flights are found\nPlease check the flight number");
				flightNumber.setText("");
			}
		});
		
		//GridPane for top elements
		GridPane gridPaneTop = new GridPane();
		gridPaneTop.setHgap(30);
		GridPane.setConstraints(flightLabel, 0, 0);
		GridPane.setConstraints(flightNumber, 0, 1);
		
		GridPane.setConstraints(choiceLabel, 1, 0);
		GridPane.setConstraints(classChoice, 1, 1);
		
		GridPane.setConstraints(submit, 2, 1);
		
		gridPaneTop.setAlignment(Pos.CENTER_LEFT);				
		gridPaneTop.getChildren().addAll(flightLabel, flightNumber, choiceLabel, classChoice, submit);		
		
		
		//==================================== CENTER CONTENT ========================================
		//Menu Label
		Label menuLabel = new Label();
		menuLabel.setText("Passenger List");
		menuLabel.setFont(new Font(30));
		menuLabel.setPadding(new Insets(0,0,10,0));
		
		//Declaring columns
        TableColumn<Passenger, Integer> passengerID = new TableColumn<>("Passenger ID");
        passengerID.setMinWidth(140);
        passengerID.setCellValueFactory(new PropertyValueFactory<>("passengerID"));
		
        TableColumn<Passenger, String> first = new TableColumn<>("First Name");
        first.setMinWidth(160);
        first.setCellValueFactory(new PropertyValueFactory<>("firstName"));
		
        TableColumn<Passenger, String> last = new TableColumn<>("Last Name");
        last.setMinWidth(160);
        last.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        
        TableColumn<Passenger, String> email = new TableColumn<>("Email");
        email.setMinWidth(350);
        email.setCellValueFactory(new PropertyValueFactory<>("email"));
        
        TableColumn<Passenger, Integer> age = new TableColumn<>("Age");
        age.setMinWidth(100);
        age.setCellValueFactory(new PropertyValueFactory<>("age"));
        
        //Declaring TableView
        passengerTable = new TableView<>();
        passengerTable.getColumns().addAll(passengerID, first, last,email, age);
        passengerTable.getSelectionModel().selectedItemProperty().addListener(
        	new ChangeListener<Passenger>() {
				@Override
				public void changed(ObservableValue<? extends Passenger> observable, Passenger oldValue, Passenger newValue) {
					passID = passengerTable.getSelectionModel().getSelectedItem().getPassengerID();
				}
			}        		
        );
        
        VBox vBox = new VBox();
        vBox.getChildren().addAll(menuLabel, passengerTable);

		//====================================== BOTTOM MENU ======================================
		//Back button
		Button backButton = new Button("BACK");
		backButton.setPrefWidth(100);
		backButton.setOnAction(e -> mainWindow.setScene(Main.adminScene));
		
		//Done Button
		Button removeButton = new Button("DONE");
		removeButton.setPrefWidth(250);
		removeButton.setOnAction(e -> {
			mainWindow.setScene(Main.adminScene);
		});
		
		//GridPane for bottom buttons
		GridPane gridPane2 = new GridPane();
		gridPane2.setHgap(20);
		GridPane.setConstraints(backButton, 0, 0);
		GridPane.setConstraints(removeButton, 1, 0);
		gridPane2.setAlignment(Pos.CENTER_RIGHT);				
		gridPane2.getChildren().addAll(backButton, removeButton);
		
		//=============================== BORDER PANE ================================
		setTop(gridPaneTop);
		setAlignment(gridPaneTop, Pos.TOP_LEFT);
		setMargin(gridPaneTop, new Insets(20,0,20,20));
		
		setCenter(vBox);
		setAlignment(vBox, Pos.CENTER);
		setMargin(vBox, new Insets(0,20,0,20));
		
		setBottom(gridPane2);
		setAlignment(gridPane2, Pos.CENTER);
		setMargin(gridPane2, new Insets(0,20,20,10));
		
	}//Constructor
	
    /**=================================== getFlightPassengers ===================================
     * 
     * @return true if valid flight number is found, false otherwise
     */
    private boolean getFlightPassengers(TextField flightInput){
    	if(!flightInput.getText().trim().equals("")){
    		try{
    			flightID = Integer.parseInt(flightInput.getText().trim());
        		passengerTable.setItems(getPassenger());
        		return true;
    		}catch(NumberFormatException e){
    			return false;
    		}
    	}
    	return false;
    }//getFlightPassengers	
	
	//Get all of the passengers
    public ObservableList<Passenger> getPassenger(){
        ObservableList<Passenger> passengers = FXCollections.observableArrayList();
        UserRequests requests = new UserRequests();
        ResultSet passengerRS = null;
        try{	
        	if(classType.equals("All")){
        		passengerRS = requests.getPassengersByFlight(conn, flightID);
        	}else if(classType.equals("First")){
        		passengerRS = requests.getFirstPassengers(conn, flightID);
        	}else if(classType.equals("Economy")){
        		passengerRS = requests.getEconomyPassengers(conn, flightID);
        	}
        	
    		while(passengerRS.next()){
    			int passengerID = passengerRS.getInt("passengerID");
    			String first = passengerRS.getString("firstName");
    			String last = passengerRS.getString("lastName");
    			String email = passengerRS.getString("email");
    			int age = passengerRS.getInt("age");
    			passengers.add(new Passenger(passengerID, first, last, email, age));
    		}
		}catch (SQLException e){};
		return passengers;        
    }//ObservableList

	public static int getPassID() {
		return passID;
	}

	public static void setPassID(int passID) {
		ViewPassenger.passID = passID;
	}

}//CancelFlight
