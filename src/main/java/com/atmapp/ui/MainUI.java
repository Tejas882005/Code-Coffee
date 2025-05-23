package com.atmapp.ui;

   import java.sql.SQLException;

   import javafx.application.Application;
   import javafx.geometry.Insets;
   import javafx.geometry.Pos;
   import javafx.scene.Scene;
   import javafx.scene.control.Button;
   import javafx.scene.image.Image;
   import javafx.scene.layout.Background;
   import javafx.scene.layout.BackgroundImage;
   import javafx.scene.layout.BackgroundPosition;
   import javafx.scene.layout.BackgroundRepeat;
   import javafx.scene.layout.BackgroundSize;
   import javafx.scene.layout.VBox;
   import javafx.scene.text.Text;
   import javafx.stage.Stage;

   public class MainUI extends Application {
       @Override
       public void start(Stage primaryStage) {
           Text welcomeText = new Text("Welcome to Horizon Bank ATM");
           welcomeText.getStyleClass().add("welcome-text");

           // Create Login button
           Button loginButton = new Button("Login");
           loginButton.getStyleClass().add("primary-button");
           loginButton.setOnAction(event -> {
               try {
                   new LoginUI(primaryStage).show();
               } catch (SQLException e) {
                   System.err.println("Database error: " + e.getMessage());
               }
           });

           VBox layout = new VBox(20, welcomeText, loginButton);
           layout.setAlignment(Pos.CENTER);
           layout.setPadding(new Insets(20));

           Image backgroundImage;
           try {
               backgroundImage = new Image(getClass().getResource("/images/atm-background.jpg").toExternalForm());
           } catch (NullPointerException e) {
               System.err.println("Error: Could not load background image. Ensure 'atm-background.jpg' is in src/main/resources/images/");
               backgroundImage = null;
           }
           if (backgroundImage != null) {
               BackgroundImage background = new BackgroundImage(
                       backgroundImage,
                       BackgroundRepeat.NO_REPEAT,
                       BackgroundRepeat.NO_REPEAT,
                       BackgroundPosition.CENTER,
                       new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, false, false, true, true)
               );
               layout.setBackground(new Background(background));
           } else {
               layout.setStyle("-fx-background-color:rgb(84, 1, 1);"); 
           }

           // Scene and stage
           Scene scene = new Scene(layout, 600, 400);
           scene.getStylesheets().add("css/style.css");
           primaryStage.setTitle("Horizon Bank ATM");
           primaryStage.setScene(scene);
           primaryStage.show();
       }

       public static void main(String[] args) {
           launch(args);
       }
   }