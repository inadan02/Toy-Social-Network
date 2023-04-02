package com.example.guiex1;

import com.example.guiex1.controller.UtilizatorController;
import com.example.guiex1.domain.Utilizator;
import com.example.guiex1.domain.UtilizatorValidator;
import com.example.guiex1.repository.Repository;
import com.example.guiex1.repository.dbrepo.UtilizatorDbRepository;
import com.example.guiex1.services.UtilizatorService;
import com.example.guiex1.services.config.ApplicationContext;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;


public class HelloApplication extends Application {

    Repository<Long, Utilizator> utilizatorRepository;
    UtilizatorService service;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        System.out.println("Reading data from file");
        String username = "postgres";
        String pasword = "postgres";
        String url = "jdbc:postgresql://localhost:5432/social";
        UtilizatorDbRepository utilizatorRepository =
                new UtilizatorDbRepository(url, username, pasword, new UtilizatorValidator());

//        utilizatorRepository.findAll().forEach(x-> System.out.println(x));
        service = new UtilizatorService(utilizatorRepository);
        initView(primaryStage);
        primaryStage.setWidth(800);
        primaryStage.show();


    }

    private void initView(Stage primaryStage) throws IOException {


        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("views/login.fxml"));
        Scene loginLayout = new Scene(fxmlLoader.load());
        primaryStage.setScene(loginLayout);


    }

    public static void changeScene(String fxml, ActionEvent actionEvent) throws IOException {
        ((Stage) (((Button) actionEvent.getSource()).getScene().getWindow())).close();
        addScene(fxml);
    }

    public static void addScene(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(HelloApplication.class.getResource(fxml));
        Scene scene = new Scene(fxmlLoader.load());
//        scene.getStylesheets().add(Objects.requireNonNull(HelloApplication.class.getResource("StyleSheet.css")).toExternalForm());
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.show();
    }
}