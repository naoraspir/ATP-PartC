package View;
import javafx.application.HostServices;
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
import javafx.scene.control.RadioButton;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.*;
import java.net.URL;
import java.util.Observable;
import java.util.Properties;
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

    public void switchSceneRules(ActionEvent actionEvent) throws IOException {
        File imagefile=new File("src/main/resources/Images/help.jpg");
        Image image= new Image(imagefile.toURI().toString());
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


    public void AboutShow(ActionEvent actionEvent) {
        Alert a=new Alert(Alert.AlertType.INFORMATION);
        a.setResizable(true);
        a.setTitle("About");
        a.setContentText("This Game Was Made By:\n" +
                "Tal Avital\n"+"Naor Aspir\n"+"The maze generator was: MyMazeGenerator\n"+"The Algorithm That was used for the maze solving: BestFirstSearch\n"+"Creation Date: June 2020");
        a.showAndWait();
        return;
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
        a.setContentText("Programe Properties:\n"+"ISearchingAlgorithm->"+wef[0]+"\n" +"IMazeGenerator->"+
                wef[1]+"\n"+"ThreadPool Count->"+wef[2]+"\n");
        a.showAndWait();
        return;
    }
}
