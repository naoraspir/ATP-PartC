package View;

import Model.IModel;
import Model.MyModel;
import ViewModel.MyViewModel;
import algorithms.mazeGenerators.Maze;
import algorithms.mazeGenerators.Position;
import algorithms.search.Solution;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.awt.*;
import java.io.*;
import java.net.URL;
import java.util.Observable;
import java.util.Properties;
import java.util.ResourceBundle;

public class MazeDisplayer implements IView {
    @FXML
    private MazeCanvas MazeCanvas;
    @FXML
    private Button Hint;
    @FXML
    private  Button BackToMain;
    private MyViewModel vm;
    private int rowChar;
    private int colChar;
    private Position GoalPos;
    private Maze maze;
    private boolean hint= false;
    private Solution sol;
    @FXML
    private  BorderPane MazeWindow;
    @FXML
    private Pane bPane;
    @FXML
    private Pane TPain;




    @Override
    public void update(Observable o, Object arg) {
        String argu=(String)arg;
        if(argu=="generated"||argu=="Moved"||argu=="hint" ) {
            this.rowChar = vm.getRowChar();
            this.colChar = vm.getColChar();
            this.maze = vm.getMaze();
            this.sol = vm.getSol();
            this.hint = vm.isHint();
            this.MazeCanvas.setHint(this.hint);
            GoalPos = vm.getGoalPos();
            MazeCanvas.requestFocus();
            this.MazeCanvas.drawMaze(maze.getMaze(), rowChar, colChar, sol, GoalPos);//TODO case of goal reached.
        }
        if(GoalPos.getColumnIndex()==colChar&&GoalPos.getRowIndex()==rowChar){
            Alert a=new Alert(Alert.AlertType.CONFIRMATION);
            a.setContentText("Congratulation,you solved the maze!!! ");
            a.show();
            //TODO congrats music
        }

        if(argu=="Saved"){
            Alert a=new Alert(Alert.AlertType.CONFIRMATION);
            a.setContentText("Maze has been saved!");
            a.show();
        }
        if(argu=="Loaded"){
            this.rowChar=vm.getRowChar();
            this.colChar=vm.getColChar();
            this.maze=vm.getMaze();
            this.sol=vm.getSol();
            this.hint=vm.isHint();
            this.MazeCanvas.setHint(this.hint);
            GoalPos=vm.getGoalPos();
            MazeCanvas.requestFocus();
        }

    }
    public void bindMazeSize(){
        MazeCanvas.widthProperty().addListener(observable -> DrawAgain());
        MazeCanvas.heightProperty().addListener(observable -> DrawAgain());
        MazeCanvas.widthProperty().bind(bPane.widthProperty());
        MazeCanvas.heightProperty().bind(bPane.heightProperty());
    }

    public void zoom(Node node, double factor, double x, double y) {
        // determine scale
        Timeline timeline=new Timeline();
        double oldScale = node.getScaleX();
        double scale = oldScale * factor;
        double f = (scale / oldScale) - 1;
        // determine offset that we will have to move the node
        Bounds bounds = node.localToScene(node.getBoundsInLocal());
        double dx = (x - (bounds.getWidth() / 2 + bounds.getMinX()));
        double dy = (y - (bounds.getHeight() / 2 + bounds.getMinY()));
        // timeline that scales and moves the node

        timeline.getKeyFrames().clear();
        timeline.getKeyFrames().addAll(
                new KeyFrame(Duration.millis(100), new KeyValue(node.translateXProperty(), node.getTranslateX() - f * dx)),
                new KeyFrame(Duration.millis(100), new KeyValue(node.translateYProperty(), node.getTranslateY() - f * dy)),
                new KeyFrame(Duration.millis(100), new KeyValue(node.scaleXProperty(), scale)),
                new KeyFrame(Duration.millis(100), new KeyValue(node.scaleYProperty(), scale))
        );
        timeline.play();
    }

    public void Zoom(ScrollEvent event) {
        double m_zoom;
        if (event.isControlDown()) {
            m_zoom = 1.5;
            if (event.getDeltaY() > 0) {
                m_zoom = 1.1*m_zoom;

            } else if (event.getDeltaY() < 0) {
                m_zoom = 1.1/ m_zoom;
            }
            if (MazeCanvas.getScaleX() * m_zoom <0.9)
            {
                MazeCanvas.setScaleX(1);
                MazeCanvas.setScaleY(1);
                MazeCanvas.setTranslateX(0);
                MazeCanvas.setTranslateY(0);
            }
            else
            {
                zoom(MazeCanvas, m_zoom, event.getSceneX(), event.getSceneY());
                MazeCanvas.setScaleX(MazeCanvas.getScaleX() * m_zoom);
                MazeCanvas.setScaleY(MazeCanvas.getScaleY() * m_zoom);
            }
            event.consume();// event handling from the root
            MazeCanvas.requestFocus();
        }
    }

    public void DrawAgain(){
        this.rowChar=vm.getRowChar();
        this.colChar=vm.getColChar();
        this.maze=vm.getMaze();
        this.sol=vm.getSol();
        this.hint=vm.isHint();
        this.MazeCanvas.setHint(this.hint);
        GoalPos=vm.getGoalPos();
        MazeCanvas.requestFocus();
        this.MazeCanvas.drawMaze(maze.getMaze(),rowChar,colChar, sol,GoalPos);
    }

    public View.MazeCanvas getMazeCanvas() {
        return MazeCanvas;
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

    public void BackToMenu(Event e) throws IOException {
        vm.setMaze(null);

        FXMLLoader fxmlLoader=new FXMLLoader(getClass().getResource("../View/MyView.fxml"));
        Parent root = fxmlLoader.load();
        Stage primaryStage=(Stage)MazeWindow.getScene().getWindow();
        primaryStage.setTitle("The Maze");
        Scene scene= new Scene(root,400,400);
        primaryStage.setScene(scene);
        primaryStage.show();
        primaryStage.setResizable(true);

        MyViewController view=(MyViewController)fxmlLoader.getController();
        view.setVm(vm);
        vm.addObserver(view);
    }

    public void SetHint(Event e){
        this.vm.setHint();
    }


    public boolean isHint() {
        return hint;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void setRowChar(int rowChar) {
        this.rowChar = rowChar;
    }

    public void setColChar(int colChar) {
        this.colChar = colChar;
    }

    public void setMaze(Maze maze) {
        this.maze = maze;
    }

    public void setSol(Solution sol) {
        this.sol = sol;
    }

    public MyViewModel getVm() {
        return vm;
    }

    public void setVm(MyViewModel vm) {
        this.vm = vm;
    }

    public void MoveChar(KeyEvent keyEvent) {
        vm.moveCharacter(keyEvent);
    }

    public void MoveFocus(MouseEvent mouseEvent) {

    }

    public void SaveCurMaze(ActionEvent actionEvent) throws UnsupportedEncodingException {
        vm.SaveCurMaze();
    }
    public void switchSceneNew(Event e) throws IOException {
        FXMLLoader fxmlLoader=new FXMLLoader(getClass().getResource("../View/NewGame.fxml"));
        Parent root = fxmlLoader.load();
        Stage primaryStage=(Stage)MazeWindow.getScene().getWindow();
        primaryStage.setTitle("New Game!");
        Scene scene= new Scene(root,400,400);
        primaryStage.setScene(scene);
        primaryStage.show();
        primaryStage.setResizable(true);
        NewGame view=(NewGame)fxmlLoader.getController();
        view.setVm(vm);
        vm.addObserver(view);

    }

    public void closeWin(ActionEvent actionEvent) {
        Platform.exit();
    }

    public void switchSceneLoad(Event e) throws IOException {
        FXMLLoader fxmlLoader=new FXMLLoader(getClass().getResource("../View/LoadGame.fxml"));
        Parent root = fxmlLoader.load();
        Stage primaryStage=(Stage)MazeWindow.getScene().getWindow();
        primaryStage.setTitle("Load Game!");
        Scene scene= new Scene(root,400,400);
        primaryStage.setScene(scene);
        primaryStage.show();
        primaryStage.setResizable(true);
        LoadGame view=(LoadGame)fxmlLoader.getController();
        view.setVm(vm);
        vm.addObserver(view);

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
