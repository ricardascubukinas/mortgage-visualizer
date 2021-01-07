package com.main;

import java.io.IOException;

import com.controller.InputController;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.Window;

public class App extends Application {
    private Parent rootLayout;
    private Stage primaryStage;

    @Override
    public void start(Stage primaryStage) throws Exception {
        try {
            // Load root layout from fxml file.
            this.primaryStage = primaryStage;
            this.primaryStage.setTitle("Loan Calculator");
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(App.class.getResource("../gui/UI.fxml"));
            rootLayout = (Parent) loader.load();

            // Show the scene containing the root layout.
            Scene scene = new Scene(rootLayout);
            primaryStage.setScene(scene);
            InputController controller = loader.getController();
            controller.setMainApp(this);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setTitle(String title) {
        primaryStage.setTitle(title);
    }

    public static void main(String[] args) {
        launch(args);
    }

    public Window getPrimaryStage() {
        return primaryStage;
    }

}
