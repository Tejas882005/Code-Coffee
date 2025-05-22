package com.atmapp.ui;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;


public class MainUI extends Application {

    @Override
    public void start(Stage primaryStage) {
        Label welcomeLabel = new Label("Welcome to ATM System");
        
        StackPane root = new StackPane();
        root.getChildren().add(welcomeLabel);
        
        Scene scene = new Scene(root, 400, 300);
        
        primaryStage.setTitle("ATM System");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}