import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class AlertBox {
	
	public static void display(String status, String message) {
        Stage window = new Stage();

        //Block events to other windows
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle("Message");
        window.setMinWidth(350);
        window.setMinHeight(200);

        Label label = new Label();
        label.setText(message);
        label.setFont(new Font(18));
        if(status.equals("Error"))
        	label.setTextFill(Color.RED);
        else label.setTextFill(Color.GREEN);
        
        Button closeButton = new Button();
        closeButton.setText("Close");
        closeButton.setOnAction(e -> window.close());

        VBox layout = new VBox(10);
        layout.setPadding(new Insets(10,10,10,10));
        layout.getChildren().addAll(label, closeButton);
        layout.setAlignment(Pos.CENTER);
                        
        //Display window and wait for it to be closed before returning
        Scene scene = new Scene(layout);
        window.setScene(scene);
        window.showAndWait();
    }//display
	

}
