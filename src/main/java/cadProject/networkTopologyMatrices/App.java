package cadProject.networkTopologyMatrices;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * JavaFX App
 */
public class App extends Application {

    private static Scene scene;

    @SuppressWarnings("exports")
	@Override
    public void start(Stage stage) throws IOException {
    	 FXMLLoader fxmlLoader =loadFXML("primary");
        scene = new Scene(fxmlLoader.load());
        NetworkGraphController controller= fxmlLoader.getController();
        stage.setOnCloseRequest(e->{
        	controller.close();
        });
        stage.setScene(scene);
        stage.show();
    }

    private static FXMLLoader loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader;
    }

    public static void main(String[] args) {
        launch();
    }

}