package View;

import algorithms.mazeGenerators.Maze;
import algorithms.mazeGenerators.Position;
import algorithms.search.AState;
import algorithms.search.Solution;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.util.Pair;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;

public class MazeCanvas extends Canvas {
    private int [][] maze;
    private boolean hint= false;
    private int row_player ;
    private int col_player ;
    private Position GoalPos;
    private Solution sol;

    StringProperty imageFileNameWall = new SimpleStringProperty();
    StringProperty imageFileNamePlayer = new SimpleStringProperty();

    public String getImageFileNameWall() {
        return imageFileNameWall.get();
    }

    public void setImageFileNameWall(String imageFileNameWall) {
        this.imageFileNameWall.set(imageFileNameWall);
    }

    public String getImageFileNamePlayer() {
        return imageFileNamePlayer.get();
    }

    public void setImageFileNamePlayer(String imageFileNamePlayer) {
        this.imageFileNamePlayer.set(imageFileNamePlayer);
    }




    public int getRow_player() {
        return row_player;
    }

    public int getCol_player() {
        return col_player;
    }

    public void set_player_position(int row, int col){
        this.row_player = row;
        this.col_player = col;
        draw();
    }

    public void setHint(boolean hint) {
        this.hint = hint;
    }

    public void drawMaze(int [][] maze, int roschar, int colchar, Solution sol,Position Goalpos)
    {
        this.sol=sol;
        this.row_player=roschar;
        this.col_player=colchar;
        this.maze = maze;
        this.GoalPos=Goalpos;
        draw();
    }

    public void draw()
    {
        if( maze!=null)
        {
            double canvasHeight = getHeight();
            double canvasWidth = getWidth();
            int row = maze.length;
            int col = maze[0].length;
            double cellHeight = canvasHeight/row;
            double cellWidth = canvasWidth/col;
            GraphicsContext graphicsContext = getGraphicsContext2D();
            graphicsContext.clearRect(0,0,canvasWidth,canvasHeight);
            graphicsContext.setFill(Color.RED);
            double w,h;
            //Draw Maze
            Image wallImage = null;
//            try {
//                wallImage = new Image(new FileInputStream(getImageFileNameWall()));
//            } catch (FileNotFoundException e) {
//                System.out.println("There is no file....");
//            }
            ArrayList<AState> solPath=sol.getSolutionPath();
            ArrayList<Pair<Integer,Integer>> pathcord=new ArrayList<Pair<Integer,Integer>>();
            for (AState state:solPath
                 ) {
                Pair<Integer,Integer> cord=new Pair<Integer, Integer>(((Position)state.getState()).getRowIndex(),((Position)state.getState()).getColumnIndex());
                pathcord.add(cord);

            }
            for(int i=0;i<row;i++)
            {
                for(int j=0;j<col;j++)
                {
                    if(maze[i][j] == 1) // Wall
                    {
                        h = i * cellHeight;
                        w = j * cellWidth;
                        if (wallImage == null){
                            graphicsContext.fillRect(w,h,cellWidth,cellHeight);
                        }else{
                            graphicsContext.drawImage(wallImage,w,h,cellWidth,cellHeight);
                        }
                    }
                    else {
                        Pair<Integer, Integer> cord = new Pair<Integer, Integer>(i, j);
                        if (this.hint && pathcord.contains(cord)) {
                            graphicsContext.setFill(Color.BLUE);
                            h = i * cellHeight;
                            w = j * cellWidth;
                            if (wallImage == null) {
                                graphicsContext.fillRect(w, h, cellWidth, cellHeight);
                            } else {
                                graphicsContext.drawImage(wallImage, w, h, cellWidth, cellHeight);
                            }
                            graphicsContext.setFill(Color.RED);
                        }
                    }
                    if(GoalPos.getRowIndex()==i && GoalPos.getColumnIndex()==j){
                        graphicsContext.setFill(Color.BLACK);
                        h = i * cellHeight;
                        w = j * cellWidth;
                        if (wallImage == null){
                            graphicsContext.fillRect(w,h,cellWidth,cellHeight);
                        }else{
                            graphicsContext.drawImage(wallImage,w,h,cellWidth,cellHeight);
                        }
                        graphicsContext.setFill(Color.RED);
                    }



                }
            }

            graphicsContext.setFill(Color.GREEN);
            double h_player = getRow_player() * cellHeight;
            double w_player = getCol_player() * cellWidth;
            Image playerImage = null;
//            try {
//                playerImage = new Image(new FileInputStream(getImageFileNamePlayer()));
//            } catch (FileNotFoundException e) {
//                System.out.println("There is no Image player....");
//            }
//            graphicsContext.drawImage(playerImage,w_player,h_player,cellWidth,cellHeight);
            graphicsContext.fillRect(w_player,h_player,cellWidth,cellHeight);
        }
    }
}
