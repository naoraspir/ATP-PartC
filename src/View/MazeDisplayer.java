package View;

import Model.IModel;
import Model.MyModel;
import ViewModel.MyViewModel;
import algorithms.mazeGenerators.Maze;
import algorithms.mazeGenerators.Position;
import algorithms.search.Solution;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.awt.*;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.Observable;
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
    public Pane bPane;



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
        MazeCanvas.widthProperty().bind(bPane.widthProperty());
        MazeCanvas.heightProperty().bind(bPane.heightProperty());
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
//    public void DrawLoaded(){
//        this.rowChar=vm.getRowChar();
//        this.colChar=vm.getColChar();
//        this.maze=vm.getMaze();
//        this.sol=vm.getSol();
//        this.hint=vm.isHint();
//        this.MazeCanvas.setHint(this.hint);
//        GoalPos=vm.getGoalPos();
//        MazeCanvas.requestFocus();
//        this.MazeCanvas.drawMaze(maze.getMaze(),rowChar,colChar, sol,GoalPos);
//    }
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
}
