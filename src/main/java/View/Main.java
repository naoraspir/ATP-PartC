package View;

import Model.IModel;
import Model.MyModel;
import ViewModel.MyViewModel;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class Main extends Application {
        private IModel model;
    @Override
    public void start(Stage primaryStage) throws Exception{
        FXMLLoader fxmlLoader=new FXMLLoader(getClass().getResource("../View/MyView.fxml"));
        Parent root = fxmlLoader.load();
        primaryStage.setTitle("The Maze");
        Scene scene= new Scene(root,400,400);

        //scene.getStylesheets().add("newstyle.css");
        primaryStage.setScene(scene);

        //MyViewController controller=(MyViewController)fxmlLoader.getController();
//        BorderPane pane = (BorderPane) fxmlLoader.getNamespace().get("menu");
//        pane.ba("-fx-background-image: url('src/main/resources/Images/Background.jpg');");
        primaryStage.show();
        primaryStage.setResizable(true);

        model=new MyModel();
        MyViewModel vm=new MyViewModel(model);
        MyViewController view=(MyViewController)fxmlLoader.getController();
        view.setVm(vm);
        vm.addObserver(view);
    }

    @Override
    public void stop() throws Exception {
        super.stop();
        model.StopServers();
        //TODO sends to model to stop server.
    }


    public static void main(String[] args) {
        launch(args);
    }
}
