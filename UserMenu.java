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

public class UserMenu extends BorderPane{
	private static Connection conn = null;
	private static Stage mainWindow = null;
	static Scene bookFlightScene;
	static Scene cancelFlightScene;
	
	public UserMenu(Connection connection, Stage s) {
		// TODO Auto-generated constructor stub
		conn = connection;
		mainWindow = s;
		
		//Menu Label
		Label menuLabel = new Label();
		menuLabel.setText("MENU");
		menuLabel.setFont(new Font(30));
		
		GridPane gridPane = new GridPane();
		gridPane.setAlignment(Pos.CENTER);
		gridPane.setVgap(20);
		
		//Book flights button
		Button book = new Button("Book Flights");
		GridPane.setConstraints(book, 0, 0);
		book.setPrefWidth(300);
		book.setOnAction(e -> {
			BorderPane bookFlight = new BookFlight(conn, mainWindow);
			bookFlightScene = new Scene(bookFlight,970,650);
			mainWindow.setScene(bookFlightScene);
		});
			
		//Cancel flights button
		Button cancel = new Button("Cancel Flight Reservations");
		GridPane.setConstraints(cancel, 0, 1);
		cancel.setPrefWidth(300);
		cancel.setPrefWidth(300);
		cancel.setOnAction(e -> {
			BorderPane cancelFlight = new CancelFlight(conn, mainWindow);
			cancelFlightScene = new Scene(cancelFlight,970,650);
			mainWindow.setScene(cancelFlightScene);
		});
		
		//Logout button
		Button logoutButton = new Button("Logout");
		GridPane.setConstraints(logoutButton, 0, 2);
		logoutButton.setPrefWidth(300);
		logoutButton.setPrefWidth(300);
		logoutButton.setOnAction(e -> {
			Main.setAdmin(false);
			Main.setUserID(0);
			mainWindow.setScene(Main.loginScene);
		});
				
		
		gridPane.getChildren().addAll(book, cancel, logoutButton);
		
		//BorderPane
		setTop(menuLabel);
		setCenter(gridPane);
		setAlignment(menuLabel, Pos.CENTER);
		setMargin(menuLabel, new Insets(10,0,0,0));
		
	}//Menu Constructor
}
