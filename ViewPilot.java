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

public class ViewPilot extends BorderPane{
	private static Connection conn = null;
	private static Stage mainWindow = null;
	private static int pilotID = 0;
	static Scene flightResultContScene;
	
	TableView<Pilot> pilotTable;
	
	@SuppressWarnings("unchecked")
	public ViewPilot(Connection connection, Stage s) {
		conn = connection;
		mainWindow = s;
		
		//======================================= TOP CONTENT =======================================

		//Most Experienced Pilot Button
		Button button1 = new Button("MOST EXPERIENCED PILOT");
		button1.setPrefWidth(250);
		button1.setOnAction(e -> {
			pilotTable.setItems(getPilotList("Most"));
		});
	
		//Least Experienced Pilot Button
		Button button2 = new Button("LEAST EXPERIENCED PILOT");
		button2.setPrefWidth(250);
		button2.setOnAction(e -> {
			pilotTable.setItems(getPilotList("Least"));
		});

		//GridPane for top elements
		GridPane gridPaneTop = new GridPane();
		gridPaneTop.setHgap(30);
				
		GridPane.setConstraints(button1, 0, 0);
		GridPane.setConstraints(button2, 1, 0);
		
		gridPaneTop.setAlignment(Pos.CENTER_LEFT);				
		gridPaneTop.getChildren().addAll(button1, button2);		
		
		//==================================== CENTER CONTENT ========================================
		//Menu Label
		Label menuLabel = new Label();
		menuLabel.setText("Pilot List");
		menuLabel.setFont(new Font(25));
		menuLabel.setPadding(new Insets(0,0,10,0));

		//Declaring columns
        TableColumn<Pilot, Integer> pilot = new TableColumn<>("Pilot ID");
        pilot.setMinWidth(80);
        pilot.setCellValueFactory(new PropertyValueFactory<>("pilotID"));

        TableColumn<Pilot, String> first = new TableColumn<>("First Name");
        first.setMinWidth(150);
        first.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        
        TableColumn<Pilot, String> last = new TableColumn<>("Last Name");
        last.setMinWidth(150);
        last.setCellValueFactory(new PropertyValueFactory<>("lastName"));

        TableColumn<Pilot, Integer> years = new TableColumn<>("Years of Experience");
        years.setMinWidth(200);
        years.setCellValueFactory(new PropertyValueFactory<>("experience"));
		
       
        //Declaring TableView
        pilotTable = new TableView<>();
        pilotTable.setPlaceholder(new Label("No Pilot found"));

        //pilotTable.setItems(getPilotList("%"));
        pilotTable.getColumns().addAll(pilot, first, last,years);
        pilotTable.getSelectionModel().selectedItemProperty().addListener(
        		new ChangeListener<Pilot>() {
					@Override
					public void changed(ObservableValue<? extends Pilot> observable, Pilot oldValue, Pilot newValue) {
						try{
							pilotID = pilotTable.getSelectionModel().getSelectedItem().getPilotID();
						}catch(Exception e){}//Do Nothing
					}
				}        		
        );
        VBox vBox = new VBox();
        vBox.getChildren().addAll(menuLabel, pilotTable);
        

        //=================================== BOTTOM MENU ====================================
        //Back Button
		Button backButton = new Button("BACK");
		backButton.setPrefWidth(100);
		backButton.setOnAction(e -> mainWindow.setScene(Main.adminScene));
		
		//Cancel Pilot Button
		Button deleteButton = new Button("DONE");
		deleteButton.setPrefWidth(250);
		deleteButton.setOnAction(e -> {
			mainWindow.setScene(Main.adminScene);
		});
		
		//GridPane for bottom buttons
		GridPane gridPane = new GridPane();
		gridPane.setHgap(20);
		GridPane.setConstraints(backButton, 0, 0);
		GridPane.setConstraints(deleteButton, 1, 0);
		gridPane.setAlignment(Pos.CENTER_RIGHT);
				
		gridPane.getChildren().addAll(backButton, deleteButton);
		
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
	}//ViewPilot Constructor

	//Get all of the pilots
    public ObservableList<Pilot> getPilotList(String type){
        ObservableList<Pilot> pilots = FXCollections.observableArrayList();
        UserRequests requests = new UserRequests();
        ResultSet pilotRS = null;        
        try{		
        	if(type.equals("Most")){
        		pilotRS = requests.getMostExperiencedPilots(conn);
        	} else if(type.equals("Least")){
        		pilotRS = requests.getLeastExperiencedPilots(conn);
        	}
        	
			while(pilotRS.next()){
				
				int id = pilotRS.getInt("pilotID");
				String first = pilotRS.getString("firstName");
				String last = pilotRS.getString("lastName");
				int years = pilotRS.getInt("experience");
				
				pilots.add(new Pilot(id, first, last, years));
			}
			
		}catch (SQLException e){};
		return pilots;        
    }//ObservableList

	public static int getPilotID() {
		return pilotID;
	}
}
