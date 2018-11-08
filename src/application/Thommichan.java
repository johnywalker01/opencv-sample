package application;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;
import org.opencv.videoio.VideoCapture;

import javafx.embed.swing.SwingFXUtils;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import utils.ToolBox;

public class Thommichan {
	
	private Button buttonSelectFile = new Button("Select File");
	private Button buttonAnalyzeVideo = new Button("Process Video");
	
	private File selectedFile;
	private ImageView imageView = new ImageView();
	private Timer videoTimer = new Timer();
	private RadioButton rb1 = new RadioButton(ToolBox.FACE_DETECTION);
	private RadioButton rb2 = new RadioButton(ToolBox.EYE_DETECTION);
	private RadioButton rb3 = new RadioButton(ToolBox.FULLBODY_DETECTION);

	private VideoCapture vCapture;
	private Mat matFrame;
	private boolean systemActive = true;

	private static final List<String> FILE_EXTN = new ArrayList<>();

	public Thommichan() {
		FILE_EXTN.add("*.mp4");
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
		fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Video Files (*.mp4)", FILE_EXTN));

		buttonSelectFile.setOnAction(e -> {
			selectedFile = fileChooser.showOpenDialog(primaryStage);
			if (selectedFile != null) {
				System.out.println(selectedFile.getAbsolutePath());
			} else {
				System.out.println("Nothing selected");
				// Utils.showWarning("Information Dialog", null, "Nothing selected");
			}
		});

		buttonAnalyzeVideo.setOnAction(e -> {
			processVideo();
		});

		// Creating the layout.
		HBox buttonBox = new HBox();
		buttonBox.setPadding(new Insets(5, 5, 5, 5));
		buttonBox.setSpacing(10);
		buttonBox.setAlignment(Pos.CENTER);
		buttonBox.getChildren().add(buttonSelectFile);
		buttonBox.getChildren().add(buttonAnalyzeVideo);
//				buttonBox.getChildren().add(timerBox);

		// Algorithm selection
		VBox algoBox = new VBox();
		algoBox.setPadding(new Insets(5, 5, 5, 5));
		algoBox.setSpacing(10);
		algoBox.setAlignment(Pos.BASELINE_LEFT);

		final ToggleGroup group = new ToggleGroup();
		rb1.setToggleGroup(group);
		rb1.setSelected(true);

		rb2.setToggleGroup(group);

		rb3.setToggleGroup(group);

		rb1.setSelected(true); // default selection

		algoBox.getChildren().add(rb1);
		algoBox.getChildren().add(rb2);
		algoBox.getChildren().add(rb3);

		VBox mainContainer = new VBox();
		mainContainer.setPadding(new Insets(10, 15, 10, 15));
		mainContainer.getChildren().add(buttonBox);
		mainContainer.getChildren().add(algoBox);
		mainContainer.getChildren().add(imageView);

		return mainContainer;
	}

	private void processVideo() {
		if (selectedFile != null) {
			System.out.println(selectedFile.getAbsolutePath());
			systemActive = true;

			////// video processing ///////
			vCapture = new VideoCapture(selectedFile.getAbsolutePath());

			if (!vCapture.isOpened()) {
				System.out.println("Error! Camera can't be opened!");
				return;
			}

			matFrame = new Mat();

//			while (camera.isOpened()) {
//				if (camera.read(matFrame)) {
//					System.out.println("Frame Obtained");
//					System.out.println("Captured Frame Width " + matFrame.width() + " Height " + matFrame.height());
//					Imgcodecs.imwrite("camera.jpg", matFrame);
//					System.out.println("OK");
//					break;
//				}
//
//				BufferedImage bufferedImage = matToBufferedImage(matFrame);
//				imageView.setImage(SwingFXUtils.toFXImage(bufferedImage, null));
//			}

			try {
				if (vCapture.isOpened()) {
//					int c = 0;
//					while (vCapture.grab()) {
//						c++;
//						vCapture.retrieve(matFrame);
//						if (!matFrame.empty()) {
//							System.out.println("grabbed frame -> " + c);
//							System.out.println("frame size: " + matFrame.size());
//							clonedFrame = matFrame.clone();
//							showInFrame(clonedFrame, c);
//						}
//					}
//					System.out.println("Releasing Camera...");
//					vCapture.release();

					disableActionButtons(true);
					runVideoTimer(vCapture, matFrame);
				}
			} catch (Exception e) {
				disableActionButtons(false);
				System.out.println("Exception in vCapture " + e.toString());
			}

		} else {
			System.out.println("Nothing selected");
			ToolBox.showWarning("Information Dialog", null, "Nothing selected");
		}
	}
	
	private void disableActionButtons(Boolean disable) {
		buttonSelectFile.setDisable(disable);
		buttonAnalyzeVideo.setDisable(disable);
	}

	private void runVideoTimer(VideoCapture vCapture, Mat matFrame) {
		videoTimer = new Timer();
//		videoTimer.scheduleAtFixedRate(new TimerTask() {
//			@Override
//			public void run() {
//				int c = 0;
//				while (vCapture.grab()) {
//					c++;
//					vCapture.retrieve(matFrame);
//					try {
//						Thread.sleep(16);
//					} catch (InterruptedException e) {
//						e.printStackTrace();
//					}
//					if (!matFrame.empty()) {
//						System.out.println("grabbed frame -> " + c);
//						System.out.println("frame size: " + matFrame.size());
//						Mat clonedFrame = matFrame.clone();
//						detectFacesFromFrame(clonedFrame);
//					}
//				}
//			}
//		}, 10, 100);
		videoTimer.scheduleAtFixedRate(frameAnalyseTask, 10, 100);
	}

	private void detectFacesFromFrame(Mat clonedFrame) {
		try {
			showInFrame(detectFaces(clonedFrame));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void showInFrame(Mat clonedFrame) {
		BufferedImage bufferedImage = matToBufferedImage(clonedFrame);
		imageView.setImage(SwingFXUtils.toFXImage(bufferedImage, null));
	}

	private BufferedImage matToBufferedImage(Mat frame) {
		int type = 0;
		if (frame.channels() == 1) {
			type = BufferedImage.TYPE_BYTE_GRAY;
		} else if (frame.channels() == 3) {
			type = BufferedImage.TYPE_3BYTE_BGR;
		}
		BufferedImage image = new BufferedImage(frame.width(), frame.height(), type);
		WritableRaster raster = image.getRaster();
		DataBufferByte dataBuffer = (DataBufferByte) raster.getDataBuffer();
		byte[] data = dataBuffer.getData();
		frame.get(0, 0, data);

		return image;
	}

	public Mat detectFaces(Mat imgSrc) throws IOException {

		// Instantiating the CascadeClassifier
		String xmlFile = getAlgorithmSelected();
		CascadeClassifier classifier = new CascadeClassifier(xmlFile);

		// Detecting the face in the snap
		MatOfRect detections = new MatOfRect();
		classifier.detectMultiScale(imgSrc, detections);
//		System.out.println(String.format("Detected %s ", detections.toArray().length));

		// Drawing boxes
		for (Rect rect : detections.toArray()) {
			Imgproc.rectangle(imgSrc, // where to draw the box
					new Point(rect.x, rect.y), // bottom left
					new Point(rect.x + rect.width, rect.y + rect.height), // top right
					new Scalar(0, 0, 255), 3 // RGB colour
			);
		}

//		System.out.println("Image Processed");

		return imgSrc;
	}

	private String getAlgorithmSelected() {
		String algorithmSelected = ToolBox.XML_FACE_DETECTION;

		if (rb1.isSelected())
			algorithmSelected = ToolBox.XML_FACE_DETECTION;
		if (rb2.isSelected())
			algorithmSelected = ToolBox.XML_EYE_DETECTION;
		if (rb3.isSelected())
			algorithmSelected = ToolBox.XML_FULLBODY_DETECTION;

		return algorithmSelected;
	}

	public void stop() throws Exception {
		System.out.println("exiting...");
		systemActive = false;
		try {
			frameAnalyseTask.cancel();
			videoTimer.cancel();
			videoTimer.purge();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private TimerTask frameAnalyseTask = new TimerTask() {
		public void run() {
//			int c = 0;
			while (vCapture.grab()) {
//				c++;
				vCapture.retrieve(matFrame);
				try {
					Thread.sleep(26);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				if (!matFrame.empty()) {
//					System.out.println("grabbed frame -> " + c);
//					System.out.println("frame size: " + matFrame.size());
					Mat clonedFrame = matFrame.clone();
					detectFacesFromFrame(clonedFrame);
				}

				if (!systemActive) {
					closeOngoing();
				}
			}
			closeOngoing();

		}
	};

	private void closeOngoing() {
		System.out.println("Releasing Camera...");
		frameAnalyseTask.cancel();
		videoTimer.cancel();
		videoTimer.purge();

		// releasing the camera.
		vCapture.release();
		
		disableActionButtons(false);
	}
}
