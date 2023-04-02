package com.example.guiex1;

import com.example.guiex1.controller.MainViewController;
import com.example.guiex1.controller.MessageAlert;
import com.example.guiex1.domain.PrietenieValidator;
import com.example.guiex1.domain.Utilizator;
import com.example.guiex1.domain.UtilizatorValidator;
import com.example.guiex1.repository.Repository;
import com.example.guiex1.repository.dbrepo.FriendshipsDbRepository;
import com.example.guiex1.repository.dbrepo.UtilizatorDbRepository;
import com.example.guiex1.services.PrietenieService;
import com.example.guiex1.services.UtilizatorService;
import com.example.guiex1.utils.Encryptor;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.security.NoSuchAlgorithmException;
import java.util.Objects;
import java.util.ResourceBundle;

public class LoginController implements Initializable {
    public TextField textPassword;
    public TextField textEmail;
    public Label textErr;

    public void onLogin(ActionEvent actionEvent) throws IOException {
        String emailS = textEmail.getText().toString();
        String passwordS = textPassword.getText().toString();
            try {
                String username="postgres";
                String pasword="postgres";
                String url="jdbc:postgresql://localhost:5432/social";
                UtilizatorDbRepository utilizatorRepository =
                        new UtilizatorDbRepository(url,username, pasword,  new UtilizatorValidator());
                UtilizatorService service=new UtilizatorService(utilizatorRepository);
                Long id =service.login(new Utilizator("","",emailS,passwordS));
                if(id==null){
                    textErr.setText("incorrect email or password");
                    MessageAlert.showErrorMessage(null, "incorrect email or password!");

                return;
                }

                ((Stage)(((Button)actionEvent.getSource()).getScene().getWindow())).close();
                FXMLLoader fxmlLoader = new FXMLLoader();

                fxmlLoader.setLocation(HelloApplication.class.getResource("views/MainView.fxml"));
                AnchorPane root=fxmlLoader.load();
                MainViewController ctrl=fxmlLoader.getController();
                ctrl.setValues(id);
                FriendshipsDbRepository prietenieRepo=new FriendshipsDbRepository(url,username,pasword,new PrietenieValidator());
                PrietenieService prietenieSrv=new PrietenieService(prietenieRepo);
                UtilizatorService userSrv=new UtilizatorService(utilizatorRepository);
                ctrl.setUtilizatorService1(prietenieSrv,userSrv);
                Scene scene = new Scene(root);
                Stage stage = new Stage();
                stage.setTitle("Main View");
                stage.setScene(scene);
                stage.show();
            }catch(Exception re){
            }
    }
    public void onCreateAccount(ActionEvent actionEvent) throws IOException {
        HelloApplication.changeScene("views/createacc.fxml",actionEvent);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}
