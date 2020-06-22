package View;

import ViewModel.MyViewModel;
import algorithms.mazeGenerators.Maze;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.*;
import java.net.URL;
import java.util.Observable;
import java.util.Properties;
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
    private boolean fileWasChosen;


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
        if(!fileWasChosen){
            Alert a=new Alert(Alert.AlertType.ERROR);
            a.setContentText("You Must Choose a File!!!");
            a.showAndWait();
            return;
        }
        FXMLLoader fxmlLoader=new  FXMLLoader(getClass().getResource("../View/MazeWindow.fxml"));

        Parent root = fxmlLoader.load();
        Stage primaryStage= (Stage) LoadGameWin.getScene().getWindow();
        primaryStage.setTitle("GamePlay");
        Scene scene= new Scene(root,600,600);
        primaryStage.setScene(scene);
        primaryStage.show();
        primaryStage.setResizable(true);
        primaryStage.setMinHeight(600);
        primaryStage.setMinWidth(600);
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
        String argu=(String)arg;
        if(argu=="LoadError"){
            Alert a=new Alert(Alert.AlertType.ERROR);
            a.setContentText("The File You Have Chosen Is Incompitable,\n Please Choose a Maze File!!!");
            a.showAndWait();
            return;
        }
        if(argu=="Loaded"){
            fileWasChosen=vm.isFileWasChosen();
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void closeWin(ActionEvent actionEvent) {
        Platform.exit();
    }

    public void switchSceneRules(ActionEvent actionEvent) throws IOException {
        File imagefile=new File("src/main/resources/Images/help.jpg");
        javafx.scene.image.Image image= new Image(imagefile.toURI().toString());
        ImageView imageView= new ImageView(image);
        BorderPane pane=new BorderPane();
        pane.setCenter(imageView);
        Scene scene=new Scene(pane);
        Stage primaryStage=new Stage();
        primaryStage.setScene(scene);
        primaryStage.showAndWait();
    }

    public void BrowseMaze(ActionEvent actionEvent) throws IOException, ClassNotFoundException {
        vm.BrowesMaze();
    }

    public void ShowConfig(ActionEvent actionEvent) {
        String [] wef=new String[3];
        Properties properties= new Properties();
        try{
            InputStream input = new FileInputStream("src/main/resources/config.properties");
            properties.load(input);
            wef[0]=properties.getProperty("ISearchingAlgorithm");
            wef[1]=properties.getProperty("IMazeGenerator");
            wef[2]=properties.getProperty("ThreadPool");
        } catch (IOException e) {
            e.printStackTrace();
        }
        Alert a=new Alert(Alert.AlertType.INFORMATION);
        a.setResizable(true);
        a.onShownProperty().addListener(e ->{
            Platform.runLater(()->a.setResizable(false));
        });
        a.setTitle("Properties");
        a.setContentText("Program Properties:\n"+"ISearchingAlgorithm->"+wef[0]+"\n" +"IMazeGenerator->"+
                wef[1]+"\n"+"ThreadPool Count->"+wef[2]+"\n");
        a.showAndWait();
        return;
    }

    public void setVm(MyViewModel vm) {
        this.vm=vm;
    }
    public void AboutShow(ActionEvent actionEvent) {
        Alert a=new Alert(Alert.AlertType.INFORMATION);
        a.setResizable(true);
        a.setTitle("About");
        a.setContentText("This Game Was Made By:\n" +
                "Tal Avital\n"+"Naor Aspir\n"+"The maze generator was: MyMazeGenerator\n"+"The Algorithm That was used for the maze solving: BestFirstSearch\n"+"Creation Date: June 2020");
        a.showAndWait();
        return;
    }
}
