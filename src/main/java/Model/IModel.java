package Model;

import ViewModel.MyViewModel;
import algorithms.mazeGenerators.Maze;

import java.util.Observable;
import java.util.Observer;

public interface IModel {
    void assignObserver(Observer o);
    void StopServers();
}
