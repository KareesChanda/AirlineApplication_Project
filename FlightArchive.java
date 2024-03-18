import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class FlightArchive extends BorderPane{
	private static Connection conn = null;
	private static Stage mainWindow = null;
	private String archiveDateStr = "";
	
	public FlightArchive(Connection connection, Stage s) {
		conn = connection;
		mainWindow = s;
		
		//=============================== TOP CONTENT ================================
		//Menu Label
		Label menuLabel = new Label();
		menuLabel.setText("Archive Flights");
		menuLabel.setFont(new Font(30));
		
		//============================ CENTER CONTENT ================================
		//GridPane for inputs
		GridPane gridPane = new GridPane();
		gridPane.setAlignment(Pos.CENTER);
		gridPane.setVgap(10);
		
		//Archive Date Label and Input
        Label archiveLabel = new Label("CHOOSE A DATE");
        GridPane.setConstraints(archiveLabel, 0, 0);
		
        DatePicker archiveInput = new DatePicker();
        GridPane.setConstraints(archiveInput, 0, 1);
        archiveInput.setOnAction(e -> {
        	LocalDate date = archiveInput.getValue();
        	archiveDateStr = date.toString();
        });
		
        //Add everything to grid
        gridPane.getChildren().addAll(archiveLabel,archiveInput);
        
        //=============================== BOTTOM MENU ================================
        //Archive Button
        Button archiveButton = new Button("ARCHIVE FLIGHT");
        archiveButton.setPrefWidth(200);
        archiveButton.setOnAction(e -> {
        	String errorMessage = "";
        	if(archiveDateStr.equals("")) errorMessage += "Date is required!\n";

        	
        	if(!errorMessage.equals("")) AlertBox.display("Error", errorMessage);
        	else if(archiveFlights(archiveDateStr)){ 
        		AlertBox.display("Success", "The flights before "+ archiveDateStr + " have been archived");
        		archiveInput.getEditor().clear();
        		
        	}else AlertBox.display("Error", "Error archiving the flights" + 
        									"\nPlease check and try again!");        	        	
        });
        
		//Back buttons
		Button backButton = new Button("BACK");
		backButton.setPrefWidth(100);
		backButton.setOnAction(e -> mainWindow.setScene(Main.adminScene));
		
		//GridPane for bottom buttons
		GridPane gridPane2 = new GridPane();
		gridPane2.setHgap(20);
		GridPane.setConstraints(backButton, 0, 0);
		GridPane.setConstraints(archiveButton, 1, 0);
		gridPane2.setAlignment(Pos.CENTER_RIGHT);				
		gridPane2.getChildren().addAll(backButton, archiveButton);
		
		
		//=============================== BORDER PANE ================================
		setTop(menuLabel);
		setAlignment(menuLabel, Pos.CENTER);
		setMargin(menuLabel, new Insets(10,0,0,0));
		
		setCenter(gridPane);
		
		setBottom(gridPane2);
		setAlignment(gridPane2, Pos.CENTER);
		setMargin(gridPane2, new Insets(0,20,20,10));
	}//BookFlight Constructor
	
	
	/**=================================== archiveFlights ===================================
	 * Archive flights before a given dates
	 * @return true if archived successfully, else false
	 */
	private boolean archiveFlights(String date){
        UserRequests requests = new UserRequests();
        try{		
        	requests.archiveFlights(conn, date);
		}catch (SQLException e){return false;}
		
		return true;
	}//archiveFlights
	
}
