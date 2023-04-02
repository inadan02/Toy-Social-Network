package com.example.guiex1.controller;

import com.example.guiex1.HelloApplication;
import com.example.guiex1.domain.Prietenie;
import com.example.guiex1.domain.PrietenieValidator;
import com.example.guiex1.domain.Utilizator;
import com.example.guiex1.repository.dbrepo.FriendshipsDbRepository;
import com.example.guiex1.services.PrietenieService;
import com.example.guiex1.services.UtilizatorService;
import com.example.guiex1.utils.events.PrietenieEntityChangeEvent;
import com.example.guiex1.utils.observer.Observer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.StreamSupport;

public class MainViewController implements Observer<PrietenieEntityChangeEvent> {

    @FXML
    TableView<Utilizator> tableView1;
    @FXML
    public TableColumn<Utilizator, String> tableColumnFirstName;
    @FXML
    public TableColumn<Utilizator, String> tableColumnLastName;

    PrietenieService service;
    UtilizatorService userSrv;
    ObservableList<Prietenie> model = FXCollections.observableArrayList();
    ObservableList<Utilizator> modelU = FXCollections.observableArrayList();
    @FXML
    TableView<Prietenie> tableView;
    @FXML
    public TableColumn<Prietenie, Long> tableColumnId1;
    @FXML
    public TableColumn<Prietenie, Long> tableColumnId2;
    public TextField text_firstName;
    public TextField text_lastName;
    public Long id;

    public void setUtilizatorService1(PrietenieService service, UtilizatorService userSrv) {
        this.service = service;
        this.userSrv = userSrv;
        service.addObserver(this);
        initModel();
    }

    public void initialize() {
        tableColumnId1.setCellValueFactory(new PropertyValueFactory<Prietenie, Long>("id1"));
        tableColumnId2.setCellValueFactory(new PropertyValueFactory<Prietenie, Long>("id2"));
        tableColumnFirstName.setCellValueFactory(new PropertyValueFactory<Utilizator, String>("FirstName"));
        tableColumnLastName.setCellValueFactory(new PropertyValueFactory<Utilizator, String>("LastName"));
        tableView1.setItems(modelU);
        tableView.setItems(model);
    }

    private void initModel() {
        List<Prietenie> pr = new ArrayList<>();
        Iterable<Prietenie> messages1 = service.getAll();
        for (var m : messages1) {
            if (Objects.equals(m.getId1(), this.id) || Objects.equals(m.getId2(), this.id)) {
                if (Objects.equals(m.getType(), "accepted"))
                    pr.add(m);
            }
        }
        List<Prietenie> prieteni = StreamSupport.stream(((Iterable<Prietenie>) pr).spliterator(), false).toList();
        List<Utilizator> useriPrieteni = new ArrayList<>();
        for (var u : pr) {
            if (Objects.equals(u.getType(), "accepted")) {
                if (Objects.equals(u.getId1(), this.id)) {
                    useriPrieteni.add(this.userSrv.getOne(u.getId2()));
                } else
                    useriPrieteni.add(this.userSrv.getOne(u.getId1()));
            }
        }

        model.setAll(prieteni);
        modelU.setAll(useriPrieteni);
    }

    public void handleAddFriend(ActionEvent actionEvent) {
        String first_name = this.text_firstName.getText();
        String last_name = this.text_lastName.getText();
        var res = this.userSrv.findByName(first_name, last_name);
        if (res != null) {
            Prietenie rez;

            rez = this.service.addPrietenie(this.id, res.getId());

            if (rez != null) {
                MessageAlert.showMessage(null, Alert.AlertType.INFORMATION, "Add", "Friend request sent!");
            } else MessageAlert.showErrorMessage(null, "Incorrect friendship!");
        } else MessageAlert.showErrorMessage(null, "User not found!");
    }

    public void handleDeleteFriend(ActionEvent actionEvent) {
        var selected = tableView1.getSelectionModel().getSelectedItem();
        if (selected != null) {
            Utilizator usr = new Utilizator();
            usr.setId(this.id);
            Long secondId = null;
            for (var aux1 : this.userSrv.getAll()) {
                if (Objects.equals(aux1.getEmail(), selected.getEmail()))
                    secondId = aux1.getId();
            }
            selected.setId(secondId);
            Prietenie deleted = service.deleteFriend(selected, usr.getId());
            if (deleted != null)
                MessageAlert.showMessage(null, Alert.AlertType.INFORMATION, "Delete", "Prietenul a fost sters cu succes!");

        } else MessageAlert.showErrorMessage(null, "Nu ati selectat nici un prieten!");
    }

    public void handleLogOut(ActionEvent actionEvent) throws IOException {
        HelloApplication.changeScene("views/login.fxml", actionEvent);
    }

    public void setValues(Long id) {
        this.id = id;

    }

    @Override
    public void update(PrietenieEntityChangeEvent prietenieEntityChangeEvent) {
        initModel();
    }

    public void handleFriendRequests(ActionEvent actionEvent) throws IOException {
        ((Stage) (((Button) actionEvent.getSource()).getScene().getWindow())).close();
        FXMLLoader fxmlLoader = new FXMLLoader();

        fxmlLoader.setLocation(HelloApplication.class.getResource("views/Relationships.fxml"));
        AnchorPane root = fxmlLoader.load();
        RelationshipsController ctrl = fxmlLoader.getController();
        ctrl.setId(id);
        ctrl.setUtilizatorService(this.service, this.userSrv);
        Scene scene = new Scene(root);
        Stage stage = new Stage();
        stage.setTitle("Relationships view");
        stage.setScene(scene);
        stage.show();
    }
}
