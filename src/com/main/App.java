package com.main;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

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
            loader.setLocation(App.class.getResource("../gui/ui.fxml"));
            rootLayout = (Parent) loader.load();

            // Show the scene containing the root layout.
            Scene scene = new Scene(rootLayout);
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /*
     * public void initRootLayout() { try { // Load root layout from fxml file.
     * FXMLLoader loader = new FXMLLoader();
     * loader.setLocation(App.class.getResource("gui/ui.fxml")); rootLayout =
     * loader.load();
     * 
     * // Show the scene containing the root layout. Scene scene = new Scene scene =
     * new Scene(rootLayout, 960, 720); primaryStage.setScene(scene);
     * primaryStage.show(); } catch (IOException e) { e.printStackTrace(); } }
     */
    public static void main(String[] args) {
        launch(args);
    }

}
