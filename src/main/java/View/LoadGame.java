package View;

import ViewModel.MyViewModel;
import algorithms.mazeGenerators.Maze;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.*;
import java.net.URL;
import java.util.Observable;
import java.util.ResourceBundle;

public class LoadGame implements IView {
    @FXML
    private Button BackToMenu;
    @FXML
    private Button letsGoLoad;
    @FXML
    private Pane LoadGameWin;
    private Maze loadedMaze;
    private MyViewModel vm;
    private MazeDisplayer ControllerDisplayer;
    private Stage primaryStage;


    public void BackToMenu(Event e) throws IOException {

        FXMLLoader fxmlLoader=new FXMLLoader(getClass().getResource("../View/MyView.fxml"));
        Parent root = fxmlLoader.load();
        Stage primaryStage=(Stage)LoadGameWin.getScene().getWindow();
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
        //take from saved maze
        FXMLLoader fxmlLoader=new  FXMLLoader(getClass().getResource("../View/MazeWindow.fxml"));

        Parent root = fxmlLoader.load();
        Stage primaryStage= (Stage) LoadGameWin.getScene().getWindow();
        primaryStage.setTitle("GamePlay");
        Scene scene= new Scene(root,600,600);
        primaryStage.setScene(scene);
        primaryStage.show();
        primaryStage.setResizable(true);
        ControllerDisplayer=(MazeDisplayer) fxmlLoader.getController();
        ControllerDisplayer.setVm(this.vm);
        vm.addObserver(ControllerDisplayer);
        ControllerDisplayer.bindMazeSize();
        primaryStage.widthProperty().addListener((obs, oldVal, newVal) -> {
            ControllerDisplayer.DrawAgain();
        });
        primaryStage.heightProperty().addListener((obs, oldVal, newVal) -> {
            ControllerDisplayer.DrawAgain();
        });
        int r,c;
//        r=loadedMaze.getSize()[0];
//        c=loadedMaze.getSize()[1];
//        vm.generateMaze(r,c);
        ControllerDisplayer.DrawAgain();

    }

    @Override
    public void update(Observable o, Object arg) {
//        if(o instanceof MyViewModel){
//            this.ControllerDisplayer.setMaze(vm.getMaze());
//            this.ControllerDisplayer.setColChar(vm.getColChar());
//            this.ControllerDisplayer.setRowChar(vm.getRowChar());
//            this.ControllerDisplayer.setSol(vm.getSol());
//
//        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void BrowseMaze(ActionEvent actionEvent) throws IOException, ClassNotFoundException {
//        FileChooser fileChooser=new FileChooser();
//        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Maze Files","*.Maze"));
//        File file= fileChooser.showOpenDialog(null);
//        String filepath=file.getPath();
//        FileInputStream fileIn = new FileInputStream(filepath);
//        ObjectInputStream objectIn = new ObjectInputStream(fileIn);
//
//        loadedMaze = (Maze) objectIn.readObject();
//        FXMLLoader fxmlLoader=new  FXMLLoader(getClass().getResource("../View/MazeWindow.fxml"));
//        ControllerDisplayer=(MazeDisplayer) fxmlLoader.getController();
//        ControllerDisplayer.setVm(this.vm);
//        vm.addObserver(ControllerDisplayer);
//        ControllerDisplayer.bindMazeSize();
        vm.BrowesMaze();
    }

    public void setVm(MyViewModel vm) {
        this.vm=vm;
    }
}
