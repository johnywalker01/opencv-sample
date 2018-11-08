package application;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;

public class OpenCVApp extends Application {

	Ramanan smile = null;
	Thommichan anger = null;

	@Override
	public void start(Stage primaryStage) {
		try {
			// Creating a Group object
			Group root = new Group();

			// Creating a scene object
			Scene scene = new Scene(root, 800, 800,Color.WHITE);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());

			// Setting title to the Stage
			primaryStage.setTitle("Loading an image");

			// Adding scene to the stage
			primaryStage.setScene(scene);

			// creating custom children
			root = buildUI(root, primaryStage);
			
			// Displaying the contents of the stage
			primaryStage.show();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private Group buildUI(Group root, Stage primaryStage) {
		TabPane tabPane = new TabPane();
		tabPane.getTabs().add(createImageProcessTab(primaryStage));
		tabPane.getTabs().add(createVideoProcessTab(primaryStage));

		BorderPane borderPane = new BorderPane();

		// bind to take available space
		borderPane.prefHeightProperty().bind(primaryStage.getScene().heightProperty());
		borderPane.prefWidthProperty().bind(primaryStage.getScene().widthProperty());

		borderPane.setCenter(tabPane);
		root.getChildren().add(borderPane);

		return root;
	}

	private Tab createImageProcessTab(Stage primaryStage) {
		Tab tab = new Tab();
		tab.setText("Image Algorithms");
		tab.setClosable(false);

		smile = new Ramanan();

		tab.setContent(smile.createCharacter(primaryStage));
		return tab;
	}

	private Tab createVideoProcessTab(Stage primaryStage) {
		Tab tab = new Tab();
		tab.setText("Video Algorithms");
		tab.setClosable(false);

		anger = new Thommichan();

		tab.setContent(anger.createCharacter(primaryStage));
		return tab;
	}

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void stop() throws Exception {

		if (anger != null) {
			anger.stop();
		}

		super.stop();
	}
}
