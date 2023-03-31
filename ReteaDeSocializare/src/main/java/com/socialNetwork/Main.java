package com.socialNetwork;

import com.socialNetwork.controllers.LogInViewController;
import com.socialNetwork.domain.Friendship;
import com.socialNetwork.domain.User;
import com.socialNetwork.domain.validators.FriendshipValidator;
import com.socialNetwork.domain.validators.UserValidator;
import com.socialNetwork.domain.validators.Validator;
import com.socialNetwork.network.MainNetwork;
import com.socialNetwork.network.Network;
import com.socialNetwork.repository.Repository;
import com.socialNetwork.repository.FriendshipDBRepository;
import com.socialNetwork.repository.UserDBRepository;
import com.socialNetwork.service.MainService;
import com.socialNetwork.service.Service;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {
    String url = "jdbc:postgresql://localhost:5432/lab4";
    String userName = "postgres";
    String password = "1234";

    Validator<User> userValidator = UserValidator.getInstance();
    Validator<Friendship> friendshipValidator = FriendshipValidator.getInstance();

    Repository<Long, User> userRepository = new UserDBRepository(url, userName, password);
    Repository<Long, Friendship> friendshipRepository = new FriendshipDBRepository(url, userName, password);
    Network network = new MainNetwork();
    Service service = new MainService(userValidator, friendshipValidator, userRepository, friendshipRepository, network);

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("LogIn-SignUp_view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 350, 500);
        LogInViewController logInViewController = fxmlLoader.getController();
        logInViewController.setService(service);
        stage.setTitle("ReteaDeSocializare");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
