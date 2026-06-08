package org.zonagamer.ui;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class MainApp extends Application {

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("🕹 GameZone");
        BorderPane root = new BorderPane();
        Scene scene = new Scene(root, 1100, 700);
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
        MainMenu menu = new MainMenu(primaryStage);
        root.setCenter(menu.getView());
    }

    public static void main(String[] args) {
        launch(args);
    }
}