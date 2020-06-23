package Model;


import Client.*;
import IO.MyDecompressorInputStream;
import Server.*;
import ViewModel.MyViewModel;
import algorithms.mazeGenerators.IMazeGenerator;
import algorithms.mazeGenerators.Maze;
import algorithms.mazeGenerators.MyMazeGenerator;
import algorithms.mazeGenerators.Position;
import algorithms.search.*;
import javafx.stage.FileChooser;
import javafx.util.Pair;

import javax.jnlp.FileContents;
import java.io.*;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

public class MyModel extends Observable implements IModel {
    private int rowChar;
    private int colChar;
    private int Gender;
    private boolean fileWasChosen;
    private IMazeGenerator gen;
    private Solution sol;
    private Maze  maze;
    private ISearchingAlgorithm solver;
    private boolean hint;
    private SearchableMaze Smaze;
    private Position curPos;
    private Position GoalPos;
    private Server mazeGeneratingServer;
    private Server solveSearchProblemServer;

    public MyModel() {
        mazeGeneratingServer = new Server(5400, 1000, new ServerStrategyGenerateMaze());
        solveSearchProblemServer = new Server(5401, 1000, new ServerStrategySolveSearchProblem());
        solveSearchProblemServer.start();
        mazeGeneratingServer.start();
        maze=null;
        rowChar = 0;
        colChar = 0;
        hint= false;

    }
    public void GenerateMaze(int rows,int cols){
        ClientGenStratg(rows, cols);

        rowChar=maze.getStartPosition().getRowIndex();
        colChar=maze.getStartPosition().getColumnIndex();
        curPos= maze.getStartPosition();
        GoalPos=maze.getGoalPosition();
        ClientSolStratg();
        setChanged();
        notifyObservers("generated");
    }

    public int getGender() {
        return Gender;
    }

    public void setGender(int gender) {
        Gender = gender;
        setChanged();
        notifyObservers("Gender");
    }

    private void ClientGenStratg(int row, int col){
        try {
            Client client = new Client(InetAddress.getLocalHost(), 5400, new IClientStrategy() {
                public void clientStrategy(InputStream inFromServer, OutputStream outToServer) {
                    try {
                        ObjectOutputStream toServer = new ObjectOutputStream(outToServer);
                        ObjectInputStream fromServer = new ObjectInputStream(inFromServer);
                        toServer.flush();
                        int[] mazeDimensions = new int[]{row, col};
                        toServer.writeObject(mazeDimensions);
                        toServer.flush();
                        byte[] compressedMaze = (byte[])((byte[])fromServer.readObject());
                        InputStream is = new MyDecompressorInputStream(new ByteArrayInputStream(compressedMaze));
                        byte[] decompressedMaze = new byte[1000000];
                        is.read(decompressedMaze);
                        maze = new Maze(decompressedMaze);
                        Smaze=new SearchableMaze(maze);
                    } catch (Exception var10) {
                        var10.printStackTrace();
                    }

                }
            });
            client.communicateWithServer();
        } catch (UnknownHostException var1) {
            var1.printStackTrace();
        }

    }
    private void ClientSolStratg(){
        try {
            Client client = new Client(InetAddress.getLocalHost(), 5401, new IClientStrategy() {
                public void clientStrategy(InputStream inFromServer, OutputStream outToServer) {
                    try {
                        ObjectOutputStream toServer = new ObjectOutputStream(outToServer);
                        ObjectInputStream fromServer = new ObjectInputStream(inFromServer);
                        toServer.flush();
                        toServer.writeObject(maze);
                        toServer.flush();

                        sol = (Solution)fromServer.readObject();
                    } catch (Exception var10) {
                        var10.printStackTrace();
                    }

                }
            });
            client.communicateWithServer();
        } catch (UnknownHostException var1) {
            var1.printStackTrace();
        }

    }
    public void SetHint(){
        this.hint=!this.hint;
        setChanged();
        notifyObservers("hint");
    }

    public boolean isHint() {
        return hint;
    }

    public void updateCharacterLocation(int direction) {
        /*
            direction = 1 -> Left Down
            direction = 2 -> Down
            direction = 3 -> Right Down
            direction = 4 -> Left
            direction = 6 -> Right
            direction = 7-> Up Left
            direction = 8 -> Up
            direction = 9 -> Up Right
         */
        AState curState=new MazeState(curPos,0);
        ArrayList<AState> posibMoves=Smaze.getAllPossibleStates(curState);
        Position newPos;
        MazeState newState;
        switch (direction) {
            case 1: //Down Left
                newPos=new Position(rowChar+1,colChar-1);
                newState= new MazeState(newPos,0);
                if(LegalMove(posibMoves,newState)){
                    rowChar++;
                    colChar--;
                    curPos=newPos;
                }
                break;

            case 2: //Down
                newPos=new Position(rowChar+1,colChar);
                newState= new MazeState(newPos,0);
                if(LegalMove(posibMoves,newState)){
                    rowChar++;
                    curPos=newPos;
                }
                break;

            case 3: //Down Right
                newPos=new Position(rowChar+1,colChar+1);
                newState= new MazeState(newPos,0);
                if(LegalMove(posibMoves,newState)){
                    rowChar++;
                    colChar++;
                    curPos=newPos;
                }
                break;

            case 4: //Left
                newPos=new Position(rowChar,colChar-1);
                newState= new MazeState(newPos,0);
                if(LegalMove(posibMoves,newState)){
                    colChar--;
                    curPos=newPos;
                }
                break;

            case 6: //Right
                newPos=new Position(rowChar,colChar+1);
                newState= new MazeState(newPos,0);
                if(LegalMove(posibMoves,newState)){
                    colChar++;
                    curPos=newPos;
                }
                break;

            case 7: //Up Left
                newPos=new Position(rowChar-1,colChar-1);
                newState= new MazeState(newPos,0);
                if(LegalMove(posibMoves,newState)){
                    rowChar--;
                    colChar--;
                    curPos=newPos;
                }
                break;

            case 8: //Up
                newPos=new Position(rowChar-1,colChar);
                newState= new MazeState(newPos,0);
                if(LegalMove(posibMoves,newState)){
                    rowChar--;
                    curPos=newPos;
                }
                break;

            case 9: //Up Right
                newPos=new Position(rowChar-1,colChar+1);
                newState= new MazeState(newPos,0);
                if(LegalMove(posibMoves,newState)){
                    rowChar--;
                    colChar++;
                    curPos=newPos;
                }
                break;
        }

        setChanged();
        notifyObservers("Moved");
    }

    public boolean LegalMove(ArrayList<AState> posMoves,AState moveTo){
        for (AState state:posMoves
             ) {
            if(((Position)state.getState()).equals(((Position)moveTo.getState()))){
                return true;
            }
        }
        return false;
    }
    @Override
    public void assignObserver(Observer o) {
        this.addObserver(o);
    }

    @Override
    public void StopServers() {
        mazeGeneratingServer.stop();
        solveSearchProblemServer.stop();
    }

    public Maze getMaze() {
        return maze;
    }

    public int getRowChar() {
        return rowChar;
    }

    public int getColChar() {
        return colChar;
    }

    public IMazeGenerator getGen() {
        return gen;
    }

    public Solution getSol() {
        return sol;
    }

    public ISearchingAlgorithm getSolver() {
        return solver;
    }

    public void SaveCurMaze() throws UnsupportedEncodingException {
        FileChooser fc= new FileChooser();
        fc.setTitle("save the maze");
        fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("Maze Files","*.Maze"));
        File file= fc.showSaveDialog(null);

        String maze_name = Integer.toString((new String(maze.toByteArray(), "ISO-8859-1")).hashCode());
        String tempdir = file.getPath();
        String maze_path = tempdir ;
        //File file = new File(maze_path);
        try {
            FileOutputStream fout=new FileOutputStream(maze_path);
            ObjectOutputStream maze_file = new ObjectOutputStream(fout);
            Pair<Maze,Position> mazePositionPair=new Pair<Maze,Position>(maze,curPos);
            maze_file.writeObject(mazePositionPair);
            maze_file.flush();
            maze_file.close();
            fout.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        setChanged();
        notifyObservers("Saved");
    }

    public void BrowseMaze() {
        FileChooser fileChooser=new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Maze Files","*.Maze"));
        File file= fileChooser.showOpenDialog(null);
        if(file==null){return;}
        String filepath=file.getPath();
        FileInputStream fileIn = null;
        try {
            fileIn = new FileInputStream(filepath);
            ObjectInputStream objectIn = new ObjectInputStream(fileIn);
            Pair<Maze,Position> mazePositionPair= (Pair<Maze,Position>) objectIn.readObject();
            maze =mazePositionPair.getKey();

            Smaze=new SearchableMaze(maze);
            rowChar=mazePositionPair.getValue().getRowIndex();
            colChar=mazePositionPair.getValue().getColumnIndex();
            curPos=mazePositionPair.getValue();
            GoalPos=maze.getGoalPosition();
            ClientSolStratg();

        } catch (FileNotFoundException e) {
            setChanged();
            notifyObservers("LoadError");
            e.printStackTrace();
            return;
        } catch (IOException e) {
            setChanged();
            notifyObservers("LoadError");
            e.printStackTrace();
            return;
        } catch (ClassNotFoundException e) {
            setChanged();
            notifyObservers("LoadError");
            e.printStackTrace();
            return;
        }
        fileWasChosen=true;
        setChanged();
        notifyObservers("Loaded");
    }

    public boolean isFileWasChosen() {
        return fileWasChosen;
    }

    public void setFileWasChosen(boolean fileWasChosen) {
        this.fileWasChosen = fileWasChosen;
    }

    public SearchableMaze getSmaze() {
        return Smaze;
    }

    public Position getCurPos() {
        return curPos;
    }

    public Position getGoalPos() {
        return GoalPos;
    }


}