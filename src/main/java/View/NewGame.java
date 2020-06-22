package View;

import Model.IModel;
import Model.MyModel;
import ViewModel.MyViewModel;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.util.Observable;
import java.util.ResourceBundle;

public class NewGame implements IView {
    private int r,c;
    @FXML
    private TextField rowNum;
    @FXML
    private TextField colNum;
    @FXML
    private Button BackToMenu;
    @FXML
    private Button letsGoNew;
    @FXML
    private  Pane NewGameWin;
    private MyViewModel vm;


    public void BackToMenu(Event e) throws IOException {

        FXMLLoader fxmlLoader=new FXMLLoader(getClass().getResource("../View/MyView.fxml"));
        Parent root = fxmlLoader.load();
        Stage primaryStage=(Stage)NewGameWin.getScene().getWindow();
        primaryStage.setTitle("The Maze");
        Scene scene= new Scene(root,400,400);
        primaryStage.setScene(scene);
        primaryStage.show();
        primaryStage.setResizable(true);

        MyViewController view=(MyViewController)fxmlLoader.getController();
        view.setVm(vm);
        vm.addObserver(view);
    }

    public void StartMaze(Event e) throws IOException {
        String rows=rowNum.getText();
        String cols=colNum.getText();
        int r,c;
        if(!rows.matches("[0-9]+")||!cols.matches("[0-9]+")){
            Alert a=new Alert(Alert.AlertType.ERROR);
            a.setContentText("Text Field must contain numbers only!");
            a.show();
        }
        r=Integer.parseInt(rows);
        c=Integer.parseInt(cols);
        if(c>1000||c<1||r>1000||r<1){
            Alert a=new Alert(Alert.AlertType.ERROR);
            a.setContentText("Maze sizes must be 1-1000!");
            a.show();
        }
        this.r=r;
        this.c=c;
        FXMLLoader fxmlLoader=new  FXMLLoader(getClass().getResource("../View/MazeWindow.fxml"));

        Parent root = fxmlLoader.load();
        Stage primaryStage= (Stage) NewGameWin.getScene().getWindow();
        primaryStage.setTitle("GamePlay");
        Scene scene= new Scene(root,600,600);
        primaryStage.setScene(scene);
        primaryStage.show();
        primaryStage.setResizable(true);
        MazeDisplayer ControllerDisplayer=(MazeDisplayer) fxmlLoader.getController();
        ControllerDisplayer.setVm(this.vm);
        vm.addObserver(ControllerDisplayer);
        ControllerDisplayer.bindMazeSize();
        primaryStage.widthProperty().addListener((obs, oldVal, newVal) -> {
            ControllerDisplayer.DrawAgain();
        });
        primaryStage.heightProperty().addListener((obs, oldVal, newVal) -> {
            ControllerDisplayer.DrawAgain();
        });

        vm.generateMaze(r,c);

    }

    @Override
    public void update(Observable o, Object arg) {

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void setVm(MyViewModel vm) {
        this.vm = vm;
    }
}
