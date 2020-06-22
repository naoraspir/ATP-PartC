package View;

import Model.IModel;
import Model.MyModel;
import ViewModel.MyViewModel;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.awt.*;
import java.io.*;
import java.net.URL;
import java.util.Observable;
import java.util.Properties;
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

    public void closeWin(ActionEvent actionEvent) {
        Platform.exit();
    }

    public void StartMaze(Event e) throws IOException {
        String rows=rowNum.getText();
        String cols=colNum.getText();
        int r,c;
        if(!rows.matches("[0-9]+")||!cols.matches("[0-9]+")){
            Alert a=new Alert(Alert.AlertType.ERROR);
            a.setContentText("Text Field must contain numbers only!");
            a.showAndWait();
            return;
        }
        r=Integer.parseInt(rows);
        c=Integer.parseInt(cols);
        if(c>1000||c<1||r>1000||r<1){
            Alert a=new Alert(Alert.AlertType.ERROR);
            a.setContentText("Maze sizes must be 1-1000!");
            a.showAndWait();
            return;
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
        primaryStage.setMinHeight(600);
        primaryStage.setMinWidth(600);
        MazeDisplayer ControllerDisplayer=(MazeDisplayer) fxmlLoader.getController();
        ControllerDisplayer.setVm(this.vm);
        vm.addObserver(ControllerDisplayer);
        ControllerDisplayer.bindMazeSize();
//        primaryStage.maximizedProperty().addListener((obs, oldVal, newVal) -> {
//
//            ControllerDisplayer.DrawAgain();
//        });
//        primaryStage.widthProperty().addListener((obs, oldVal, newVal) -> {
//            ControllerDisplayer.DrawAgain();
//        });
//        primaryStage.heightProperty().addListener((obs, oldVal, newVal) -> {
//            ControllerDisplayer.DrawAgain();
//        });

        vm.generateMaze(r,c);
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
        } catch (FileNotFoundException e) {
            e.printStackTrace();
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

    @Override
    public void update(Observable o, Object arg) {

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void setVm(MyViewModel vm) {
        this.vm = vm;
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
