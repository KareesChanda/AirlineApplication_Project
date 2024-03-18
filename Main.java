import java.sql.*;
//JavaFX
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class Main extends Application
{ 
	//JDBC driver name and database URL
	static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";  
	static final String DB_URL = "jdbc:mysql://localhost/flight_reservation";

	//Database credentials
	static final String USER = "root";
	static final String PASS = "myfirstDB";
	private static Connection conn = null;
	private static PreparedStatement preparedStatement = null;
	
	static Stage mainWindow;
	private static int userID = 0;
	private static boolean isAdmin = false;
	static Scene loginScene;
	static Scene menuScene;
	static Scene adminScene;
	
	public static void main(String[] args) throws SQLException {
		
		try{			
			conn = DriverManager.getConnection(DB_URL, USER, PASS);
			if (conn != null) {
				System.out.println("Welcome to the SEK Airline Reservation System");
			}
			launch(args);
			
		}catch(SQLException se){se.printStackTrace(); }
		catch(Exception e){ e.printStackTrace(); }
		finally{
			try{ if(preparedStatement!=null) preparedStatement.close(); }
			catch(SQLException se2){ }// nothing we can do
			
			try{ if(conn!=null) conn.close(); }
			catch(SQLException se){ se.printStackTrace(); }
		}		
	}//main

	@Override
	public void start(Stage primaryStage) throws Exception {
		mainWindow = primaryStage;
		mainWindow.setTitle("SEK Airline System");
				
		//Log-in and Sign-up Scene
		GridPane loginGrid = new LoginSignup(conn);		
		loginGrid.setAlignment(Pos.CENTER);	
		
		loginScene = new Scene(loginGrid, 970,650);		
		
		BorderPane menuGrid = new UserMenu(conn, mainWindow);
		menuScene = new Scene(menuGrid, 970,650);
		
		BorderPane adminGrid = new AdminMenu(conn, mainWindow);
		adminScene = new Scene(adminGrid, 970,650);

		mainWindow.setScene(loginScene);
		//mainWindow.setScene(menuScene);
		//mainWindow.setScene(adminScene);
		mainWindow.show();
		
	}//start

	public static void setUserID(int num){
		userID = num;		
	}
	
	public static int getUserID(){
		return userID;
	}

	public static boolean isAdmin() {
		return isAdmin;
	}

	public static void setAdmin(boolean isAdmin) {
		Main.isAdmin = isAdmin;
	}
}//Main