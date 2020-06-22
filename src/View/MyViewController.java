package View;

import ViewModel.MyViewModel;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Observable;
import java.util.ResourceBundle;

public class MyViewController implements IView {
    @FXML
    private Pane menu;
    @FXML
    private RadioButton p1;
    @FXML
    private RadioButton p2;
    @FXML
    private RadioButton p3;
    @FXML
    private Button newBttn;
    @FXML
    private Button loadBttn;
    private MyViewModel vm;

     public void switchSceneNew(Event e) throws IOException {
         FXMLLoader fxmlLoader=new FXMLLoader(getClass().getResource("../View/NewGame.fxml"));
         Parent root = fxmlLoader.load();
         Stage primaryStage=(Stage)menu.getScene().getWindow();
         primaryStage.setTitle("New Game!");
         Scene scene= new Scene(root,400,400);
         primaryStage.setScene(scene);
         primaryStage.show();
         primaryStage.setResizable(true);
         NewGame view=(NewGame)fxmlLoader.getController();
         view.setVm(vm);
         vm.addObserver(view);

     }

    public void switchSceneLoad(Event e) throws IOException {
        FXMLLoader fxmlLoader=new FXMLLoader(getClass().getResource("../View/LoadGame.fxml"));
        Parent root = fxmlLoader.load();
        Stage primaryStage=(Stage)menu.getScene().getWindow();
        primaryStage.setTitle("Load Game!");
        Scene scene= new Scene(root,400,400);
        primaryStage.setScene(scene);
        primaryStage.show();
        primaryStage.setResizable(true);
        LoadGame view=(LoadGame)fxmlLoader.getController();
        view.setVm(vm);
        vm.addObserver(view);

    }

    @Override
    public void update(Observable observable, Object o) {

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    public void setVm(MyViewModel vm) {
        this.vm = vm;
    }
}
