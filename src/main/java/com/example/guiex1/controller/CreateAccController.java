package com.example.guiex1.controller;

import com.example.guiex1.HelloApplication;
import com.example.guiex1.domain.Utilizator;
import com.example.guiex1.domain.UtilizatorValidator;
import com.example.guiex1.domain.ValidationException;
import com.example.guiex1.repository.Repository;
import com.example.guiex1.repository.dbrepo.UtilizatorDbRepository;
import com.example.guiex1.services.UtilizatorService;
import com.example.guiex1.utils.events.UtilizatorEntityChangeEvent;
import com.example.guiex1.utils.observer.Observer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class CreateAccController implements Observer<UtilizatorEntityChangeEvent> {
    public TextField text_FirstName, text_LastName, text_email, text_password;

    public void handleAddUtilizator(ActionEvent actionEvent) {

        try {
            String username = "postgres";
            String pasword = "postgres";
            String url = "jdbc:postgresql://localhost:5432/social";
            UtilizatorDbRepository utilizatorRepository =
                    new UtilizatorDbRepository(url, username, pasword, new UtilizatorValidator());
            UtilizatorService service = new UtilizatorService(utilizatorRepository);
            Utilizator user = new Utilizator(text_FirstName.getText(), text_LastName.getText(), text_email.getText(), text_password.getText());
            if (service.findEmailPass(text_email.getText(), text_password.getText()) != null) {
                MessageAlert.showErrorMessage(null, "Choose different email and password!");
                return;
            }
            Utilizator saved = service.addUtilizator(user);
            ((Stage) (((Button) actionEvent.getSource()).getScene().getWindow())).close();
            FXMLLoader fxmlLoader = new FXMLLoader();

            fxmlLoader.setLocation(HelloApplication.class.getResource("views/MainView.fxml"));
            AnchorPane root = fxmlLoader.load();
            MainViewController ctrl = fxmlLoader.getController();
            Long id = user.getId();
            ctrl.setValues(id);

            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setTitle("Main View");
            stage.setScene(scene);
            stage.show();

        } catch (ValidationException e) {
            MessageAlert.showErrorMessage(null, e.getMessage());
            return;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void update(UtilizatorEntityChangeEvent utilizatorEntityChangeEvent) {
//        initModel();
    }
}
