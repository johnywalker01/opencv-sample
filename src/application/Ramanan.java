package application;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import utils.ImageTool;
import utils.ToolBox;

public class Ramanan {
	private ImageTool imgTool = new ImageTool();
	private File selectedFile;
	private ImageView imageView = new ImageView();

	private static final String DETECT_FACES = "Detect faces";
	private static final String DETECT_EYES = "Detect Eyes";
	private static final String DETECT_EDGES = "Detect Edges";
	private static final String DETECT_HOUGH_LINES = "Detect Hough Lines";

	private static final List<String> IMG_EXTN = new ArrayList<>();

	public Ramanan() {
		IMG_EXTN.add("*.jpg");
		IMG_EXTN.add("*.jpeg");
	}

	public Node createCharacter(Stage primaryStage) {
		// Setting the image view
		imageView = new ImageView();

		// setting the fit height and width of the image view
		// imageView.setFitHeight(400);
		imageView.setFitWidth(500);

		// Setting the preserve ratio of the image view
		imageView.setPreserveRatio(true);

		FileChooser fileChooser = new FileChooser();
		fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Image Files", IMG_EXTN));

		Button button = new Button("Select an Image");
		button.setOnAction(e -> {
			selectedFile = fileChooser.showOpenDialog(primaryStage);
			if (selectedFile != null) {
				System.out.println(selectedFile.getAbsolutePath());
				try {
					imageView.setImage(imgTool.loadImage(selectedFile.getAbsolutePath()));
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			} else {
				System.out.println("Nothing selected");
				// Utils.showWarning("Information Dialog", null, "Nothing selected");
			}
		});

		Button faceButton = new Button(DETECT_FACES);
		faceButton.setOnAction(e -> {
			detectFromImage(selectedFile, DETECT_FACES);
		});
		Button eyeButton = new Button(DETECT_EYES);
		eyeButton.setOnAction(e -> {
			detectFromImage(selectedFile, DETECT_EYES);

		});
		Button edgeButton = new Button(DETECT_EDGES);
		edgeButton.setOnAction(e -> {
			detectFromImage(selectedFile, DETECT_EDGES);
		});
		Button houghLineButton = new Button(DETECT_HOUGH_LINES);
		houghLineButton.setOnAction(e -> {
			detectFromImage(selectedFile, DETECT_HOUGH_LINES);
		});

		// Creating the layout.
		VBox buttonBox = new VBox();
		buttonBox.setPadding(new Insets(15, 12, 15, 12));
		buttonBox.setSpacing(10);
		buttonBox.setAlignment(Pos.CENTER_LEFT);
		buttonBox.getChildren().add(button);
		buttonBox.getChildren().add(faceButton);
		buttonBox.getChildren().add(eyeButton);
		buttonBox.getChildren().add(edgeButton);
		buttonBox.getChildren().add(houghLineButton);

		HBox mainContainer = new HBox();
		mainContainer.setPadding(new Insets(15, 12, 15, 12));
		mainContainer.setAlignment(Pos.CENTER_LEFT);
		mainContainer.getChildren().add(buttonBox);
		mainContainer.getChildren().add(imageView);

		return mainContainer;
	}

	private void detectFromImage(final File selectedFile, final String algorithType) {

		if (selectedFile != null) {
			System.out.println(selectedFile.getAbsolutePath());
			try {
				switch (algorithType) {
				case DETECT_FACES:
					imageView.setImage(imgTool.loadImage(imgTool.detectFaces(selectedFile.getAbsolutePath())));
					break;
				case DETECT_EYES:
					imageView.setImage(imgTool.loadImage(imgTool.detectEyes(selectedFile.getAbsolutePath())));
					break;
				case DETECT_EDGES:
					imageView.setImage(imgTool.loadImage(imgTool.detecteCannyEdges(selectedFile.getAbsolutePath())));
					break;
				case DETECT_HOUGH_LINES:
					imageView.setImage(imgTool.loadImage(imgTool.houghLineTest(selectedFile.getAbsolutePath())));
					break;

				default:
					break;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			System.out.println("Nothing selected");
			ToolBox.showWarning("Information Dialog", null, "Nothing selected");
		}
	}

}
