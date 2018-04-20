package milliday;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class DayCentClock extends Application {

    public static void main(String[] args) {
        launch(args);
    }
    
    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Hello, World!");
        var root = new StackPane();

        var text = new Text("Yay");
        var text2 = new Text("Yay");
        text.setFont(new Font(20));
        text2.setFont(new Font(20));
//        root.getChildren().add(text);
//        root.getChildren().add(text2);
        
        var c = new Canvas(300, 300);
        var twoD = c.getGraphicsContext2D();


        drawClock(twoD, 0);
        
        var box = new VBox();
        box.getChildren().add(c);
        box.getChildren().add(text);
        box.getChildren().add(text2);
        box.alignmentProperty().set(Pos.CENTER);
        
        root.getChildren().add(box);

        var timeline = new Timeline(
                new KeyFrame(Duration.seconds(0),
                        actionEvent -> {
                            var time = Calendar.getInstance();
                            var simpleDateFormat = new SimpleDateFormat("HH:mm:ss");
                            text.setText("Old clock: " + simpleDateFormat.format(time.getTime()));
                            
                            int h = time.get(Calendar.HOUR_OF_DAY);
                            int m = time.get(Calendar.MINUTE);
                            int s = time.get(Calendar.SECOND);
                            int ms = time.get(Calendar.MILLISECOND);
                            
                            double millidays = Math.round(100 * 1000 * ((((double) ms / 1000 +  s) / 60 + m) / 60 + h) / 24) / 1000d;
                            text2.setText("DayCent clock: " + String.format("%.3f", millidays));

                            drawClock(twoD, millidays);
                        }
                ),
                new KeyFrame(Duration.seconds(.1))
        );
        
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
        
        primaryStage.setScene(new Scene(root, 400, 400));
        primaryStage.show();
    }

    private void drawClock(GraphicsContext twoD, double time) {
        
        double fat = time / 100;
        double lng = time / 10;
        double micro = time * 10;
        
        var x0 = 150;
        var y0 = 150;
        
        twoD.clearRect(0, 0, 300, 300);
        twoD.setFill(Paint.valueOf("red"));
        twoD.fillOval(50, 50, 200, 200);
        twoD.setFill(Paint.valueOf("black"));
        twoD.fillOval(140, 140, 20, 20);

        twoD.setStroke(Paint.valueOf("black"));
        for (int i = 0; i < 100; i++) {
            int len = (i % 10 == 0) ? 8 : 5;
            int w = (i % 10 == 0) ? 2 : 1;
            twoD.setLineWidth(w);
            twoD.strokeLine(x0 + (100 - len) * Math.sin(i * 2 * Math.PI / 100), y0 - (100 - len) * Math.cos(i * 2 * Math.PI / 100), 
                    x0 + 100 * Math.sin(i * 2 * Math.PI / 100), y0 - 100 * Math.cos(i * 2 * Math.PI / 100));
            
            if (i % 10 == 0) {
                twoD.strokeText((i / 10 + 9) % 10 + 1 + "", x0 + 115 * Math.sin(i * 2 * Math.PI / 100) - 5, y0 - 115 * Math.cos(i * 2 * Math.PI / 100) + 3);
            }
            
        }

        twoD.setStroke(Paint.valueOf("black"));
        twoD.setLineWidth(3);
        twoD.strokeLine(x0, y0, x0 + 92 * Math.sin(lng * 2 * Math.PI), y0 - 92 * Math.cos(lng * 2 * Math.PI));
        
        twoD.setLineWidth(5);
        twoD.strokeLine(x0, y0, x0 + 50 * Math.sin(fat * 2 * Math.PI), y0 - 50 * Math.cos(fat * 2 * Math.PI));

        twoD.setLineWidth(1);
        twoD.strokeLine(x0, y0, x0 + 110 * Math.sin(micro * 2 * Math.PI), y0 - 110 * Math.cos(micro * 2 * Math.PI));
    }
}
