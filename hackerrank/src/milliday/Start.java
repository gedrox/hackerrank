package milliday;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Start extends Application {

    public static void main(String[] args) {
        launch(args);
    }
    
    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Hello, World!");
        var root = new StackPane();

        var cb = new javafx.scene.control.ChoiceBox<>();
        var cb2 = new javafx.scene.control.ComboBox<>();
        cb2.getItems().add("CP+");
        cb2.getItems().add("TCA");
        cb2.getItems().add("publisher");
        
        var text = new Text("Yay");
        var text2 = new Text("Yay");
        text.setFont(new Font(20));
        text2.setFont(new Font(20));
        
        var box = new VBox();
        box.getChildren().add(text);
        box.getChildren().add(text2);
        box.getChildren().add(cb);
        box.getChildren().add(cb2);
        box.alignmentProperty().set(Pos.CENTER);
        
        root.getChildren().add(box);


        primaryStage.setScene(new Scene(root, 400, 400));
        primaryStage.show();
    }

}
