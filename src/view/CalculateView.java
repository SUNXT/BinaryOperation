package view;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class CalculateView extends Application {

    public static void main(String[] args){
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.initModality(Modality.APPLICATION_MODAL);//设置为模式窗口，上一个界面不可点击
        Parent root = FXMLLoader.load(getClass().getResource("/view/fxml/calculate_view.fxml"));
        primaryStage.setTitle("计算过程");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }

}