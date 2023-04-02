package com.example.guiex1.controller;

import com.example.guiex1.HelloApplication;
import com.example.guiex1.domain.FriendRequest;
import com.example.guiex1.domain.Prietenie;
import com.example.guiex1.domain.Tuple;
import com.example.guiex1.domain.Utilizator;
import com.example.guiex1.services.PrietenieService;
import com.example.guiex1.services.UtilizatorService;
import com.example.guiex1.utils.events.FriendRequestEntityChangeEvent;
import com.example.guiex1.utils.events.PrietenieEntityChangeEvent;
import com.example.guiex1.utils.observer.Observer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.StreamSupport;

public class RelationshipsController implements Observer<PrietenieEntityChangeEvent> {
    @FXML
    TableColumn<FriendRequest, LocalDateTime> tableColumnDate;
    @FXML
    TableView<FriendRequest> tableViewRequests;
    @FXML

    TableColumn<FriendRequest,String> tableColumnTypeFriendship;
    @FXML

    TableColumn<FriendRequest,String> tableColumnFirstNameFr;
    @FXML

    TableColumn<FriendRequest,String> tableColumnLastNameFr;
    Long id;

    public void setId(Long id) {
        this.id = id;
    }

    PrietenieService service;
    UtilizatorService userSrv;
    ObservableList<FriendRequest> model = FXCollections.observableArrayList();
    public void setUtilizatorService(PrietenieService service,UtilizatorService userSrv) {
        this.service = service;
        this.userSrv=userSrv;
        service.addObserver(this);
        initModel();
    }
    public void initialize() {
        tableColumnTypeFriendship.setCellValueFactory(new PropertyValueFactory<FriendRequest, String>("typeFriendship"));
        tableColumnFirstNameFr.setCellValueFactory(new PropertyValueFactory<FriendRequest, String>("FirstNameFr"));
        tableColumnLastNameFr.setCellValueFactory(new PropertyValueFactory<FriendRequest, String>("LastNameFr"));
        tableColumnDate.setCellValueFactory(new PropertyValueFactory<FriendRequest, LocalDateTime>("Date"));
        tableViewRequests.setItems(model);
    }
    public void handleAcceptFriend(ActionEvent actionEvent) {
        var selected=tableViewRequests.getSelectionModel().getSelectedItem();
        if(selected!=null) {
            var usr = this.userSrv.findByName(selected.getFirstNameFr(), selected.getLastNameFr());
            var res=this.service.getOne(usr.getId(),this.id);
            if(res!=null) {
                var old=this.service.deleteFriend(usr,this.id);
                Prietenie p=new Prietenie(LocalDateTime.now());
                p.setId(old.getId());
                p.setType("accepted");
                this.service.addPrietenie2(p);
            }
//            else
//                this.service.getOne(this.id,usr.getId()).setType("accepted");
        }
    }

    public void handleDeclineFriend(ActionEvent actionEvent) {
        var selected=tableViewRequests.getSelectionModel().getSelectedItem();
        if(selected!=null) {
            var usr = this.userSrv.findByName(selected.getFirstNameFr(), selected.getLastNameFr());
            this.service.deleteFriend(usr, this.id);
        }
    }


    public void handleBack(ActionEvent actionEvent) throws IOException {
        ((Stage)(((Button)actionEvent.getSource()).getScene().getWindow())).close();
        FXMLLoader fxmlLoader = new FXMLLoader();

        fxmlLoader.setLocation(HelloApplication.class.getResource("views/MainView.fxml"));
        AnchorPane root=fxmlLoader.load();
        MainViewController ctrl=fxmlLoader.getController();
        ctrl.setValues(id);
        ctrl.setUtilizatorService1(this.service,this.userSrv);
        Scene scene = new Scene(root);
        Stage stage = new Stage();
        stage.setTitle("MainView");
        stage.setScene(scene);
        stage.show();

    }

    private void initModel() {
        List<FriendRequest> pr= new ArrayList<>();
        Iterable<Prietenie> messages1 = service.getAll();
        for(var m: messages1){
            if(Objects.equals(m.getType(), "pending")){
//                if(Objects.equals(m.getId1(), this.id)){
//                    var fr=this.userSrv.getOne(m.getId2());
//                    pr.add(new FriendRequest(fr.getFirstName(),fr.getLastName(),"pending"));
//                }
//                 else
                     if (Objects.equals(m.getId2(), this.id)) {
                    var fr=this.userSrv.getOne(m.getId1());
                    pr.add(new FriendRequest(fr.getFirstName(),fr.getLastName(),"pending",m.getDate()));
                }
            }
        }
        model.setAll(pr);
    }

    @Override
    public void update(PrietenieEntityChangeEvent prietenieEntityChangeEvent) {
        initModel();
    }
}
