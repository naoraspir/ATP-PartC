package ViewModel;

import Model.IModel;
import Model.MyModel;
import algorithms.mazeGenerators.Maze;
import algorithms.mazeGenerators.Position;
import algorithms.search.Solution;
import javafx.scene.input.KeyEvent;

import java.io.UnsupportedEncodingException;
import java.util.Observable;
import java.util.Observer;

public class MyViewModel extends Observable implements Observer {

    private MyModel model;
    private int rowChar;
    private int colChar;
    private Position GoalPos;
    private Maze maze;
    private Solution sol;
    private boolean hint= false;


    public MyViewModel(IModel model) {
        this.model = (MyModel)model;
        this.model.assignObserver(this);
        this.maze = null;
    }

    public Maze getMaze() {
        return maze;
    }

    public Solution getSol() {
        return sol;
    }

    public void setMaze(Maze maze) {
        this.maze = maze;
    }

    public int getRowChar() {
        return rowChar;
    }

    public int getColChar() {
        return colChar;
    }

    public void setHint() {
        this.model.SetHint();
    }

    public boolean isHint() {
        return hint;
    }

    @Override
    public void update(Observable o, Object arg) {
        if(o instanceof IModel)
        {
            String argu=(String)arg;
            switch (argu){
                case "generated":
                    this.maze = ((MyModel)model).getMaze();
                    rowChar=maze.getStartPosition().getRowIndex();
                    colChar=maze.getStartPosition().getColumnIndex();
                    GoalPos=maze.getGoalPosition();
                    this.sol=model.getSol();
                    setChanged();
                    notifyObservers("generated");
                    return;
                case "hint":
                    boolean hint=model.isHint();
                    this.hint=hint;
                    setChanged();
                    notifyObservers("hint");
                    return;
                case "Moved":
                    int rowChar = model.getRowChar();
                    int colChar = model.getColChar();
                    this.rowChar = rowChar;
                    this.colChar = colChar;
                    setChanged();
                    notifyObservers("Moved");
                    return;
                case "Saved":
                    setChanged();
                    notifyObservers("Saved");
                    return;
                case "Loaded":
                    this.maze = ((MyModel)model).getMaze();
                    this.rowChar=((MyModel)model).getCurPos().getRowIndex();
                    this.colChar=((MyModel)model).getCurPos().getColumnIndex();
                    GoalPos=maze.getGoalPosition();
                    this.sol=model.getSol();
                    setChanged();
                    notifyObservers("Loaded");
                    return;

            }
        }
    }


    public void generateMaze(int row,int col)
    {
        this.model.GenerateMaze(row,col);
    }

    public void moveCharacter(KeyEvent keyEvent)
    {
        int direction = -1;

        switch (keyEvent.getCode()){
            case NUMPAD1:
                direction = 1;
                break;
            case NUMPAD2:
                direction = 2;
                break;
            case NUMPAD3:
                direction = 3;
                break;
            case NUMPAD4:
                direction = 4;
                break;
            case NUMPAD6:
                direction = 6;
                break;
            case NUMPAD7:
                direction = 7;
                break;
            case NUMPAD8:
                direction = 8;
                break;
            case NUMPAD9:
                direction = 9;
                break;
        }

        model.updateCharacterLocation(direction);
    }

    public Position getGoalPos() {
        return GoalPos;
    }

    public void setGoalPos(Position goalPos) {
        GoalPos = goalPos;
    }

    public void solveMaze(int [][] maze)
    {
//        model.solveMaze(maze);
    }

    public void getSolution()
    {
//        model.getSolution();
    }

    public void SaveCurMaze() throws UnsupportedEncodingException {
        model.SaveCurMaze();
    }

    public void BrowesMaze() {
        model.BrowseMaze();
    }
}
