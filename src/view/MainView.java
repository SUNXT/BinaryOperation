package view;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

import javafx.stage.Stage;

import java.io.IOException;

public class MainView extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/view/fxml/main_view.fxml"));
        primaryStage.setTitle("二进制计算器");
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
//        primaryStage.setX(100);
//        primaryStage.setY(100);
        primaryStage.show();
    }

}
