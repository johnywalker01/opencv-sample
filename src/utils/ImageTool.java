package utils;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.core.MatOfRect;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.WritableImage;

public class ImageTool {
	public ImageTool() {
		super();
		// Loading the OpenCV core library
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
	}

	public static final String IMG_FILE_1 = new String("C:/JavaTest/2018-02-19 175717.jpg");
	public static final String IMG_FILE_2 = new String("C:/JavaTest/sample_resaved.jpg");

	public WritableImage loadImage() throws IOException {
		// Reading the Image from the file and storing it in to a Matrix object
		String file = IMG_FILE_1;
		Mat image = Imgcodecs.imread(file);

		// Encoding the image
		MatOfByte matOfByte = new MatOfByte();
		Imgcodecs.imencode(".jpg", image, matOfByte);

		// Storing the encoded Mat in a byte array
		byte[] byteArray = matOfByte.toArray();

		// Displaying the image
		InputStream in = new ByteArrayInputStream(byteArray);
		BufferedImage bufImage = ImageIO.read(in);

		System.out.println("Image Loaded");
		WritableImage writableImage = SwingFXUtils.toFXImage(bufImage, null);
		return writableImage;

	}

	public WritableImage loadImage(String imgLocation) throws IOException {
		// Reading the Image from the file and storing it in to a Matrix object
		Mat image = Imgcodecs.imread(imgLocation);

		// Encoding the image
		MatOfByte matOfByte = new MatOfByte();
		Imgcodecs.imencode(".jpg", image, matOfByte);

		// Storing the encoded Mat in a byte array
		byte[] byteArray = matOfByte.toArray();

		// Displaying the image
		InputStream in = new ByteArrayInputStream(byteArray);
		BufferedImage bufImage = ImageIO.read(in);

		System.out.println("Image Loaded");
		WritableImage writableImage = SwingFXUtils.toFXImage(bufImage, null);

		return writableImage;
	}

	public void saveImage() {
		// Reading the Image from the file and storing it in to a Matrix object
		String file = IMG_FILE_1;
		Mat matrix = Imgcodecs.imread(file);

		System.out.println("Image Loaded ..........");
		String file2 = IMG_FILE_2;

		// Writing the image
		Imgcodecs.imwrite(file2, matrix);
		System.out.println("Image Saved ............");
	}

	/**
	 * Method for detecting Faces
	 * 
	 * @param imgLocation as {@link String} (eg. 'C:/Sample/sample_image.jpg')
	 * @return {@link Mat} object
	 * @throws IOException
	 */
	public Mat detectFaces(String imgLocation) throws IOException {

		// Reading the Image from the file and storing it in to a Matrix object
		String file = imgLocation;
		Mat src = Imgcodecs.imread(file);

		// Instantiating the CascadeClassifier
		String xmlFile = ToolBox.XML_FACE_DETECTION;
		CascadeClassifier classifier = new CascadeClassifier(xmlFile);

		// Detecting the face in the snap
		MatOfRect faceDetections = new MatOfRect();
		classifier.detectMultiScale(src, faceDetections);
		System.out.println(String.format("Detected %s faces", faceDetections.toArray().length));

		// Drawing boxes
		for (Rect rect : faceDetections.toArray()) {
			Imgproc.rectangle(src, // where to draw the box
					new Point(rect.x, rect.y), // bottom left
					new Point(rect.x + rect.width, rect.y + rect.height), // top right
					new Scalar(0, 0, 255), 3 // RGB colour
			);
		}

		System.out.println("Image Processed");

		return src;

	}

	/**
	 * Method for detecting Eyes
	 * 
	 * @param imgLocation as {@link String} (eg. 'C:/Sample/sample_image.jpg')
	 * @return {@link Mat} object
	 * @throws IOException
	 */
	public Mat detectEyes(String imgLocation) throws IOException {

		// Reading the Image from the file and storing it in to a Matrix object
		String file = imgLocation;
		Mat src = Imgcodecs.imread(file);

		// Instantiating the CascadeClassifier
		String xmlFile = ToolBox.XML_EYE_DETECTION;
		CascadeClassifier classifier = new CascadeClassifier(xmlFile);

		// Detecting the face in the snap
		MatOfRect eyeDetections = new MatOfRect();
		classifier.detectMultiScale(src, eyeDetections);
		System.out.println(String.format("Detected %s Eyes", eyeDetections.toArray().length));

		// Drawing boxes
		for (Rect rect : eyeDetections.toArray()) {
			Imgproc.rectangle(src, // where to draw the box
					new Point(rect.x, rect.y), // bottom left
					new Point(rect.x + rect.width, rect.y + rect.height), // top right
					new Scalar(0, 0, 255), 3 // RGB colour
			);
		}

		System.out.println("Image Processed");

		return src;

	}

	/**
	 * Method can return a {@link WritableImage} from a {@link Mat} image.
	 * 
	 * @param image as {@link Mat}
	 * @return {@link WritableImage} object
	 * @throws IOException
	 */
	public WritableImage loadImage(Mat image) throws IOException {

		// Encoding the image
		MatOfByte matOfByte = new MatOfByte();
		Imgcodecs.imencode(".jpg", image, matOfByte);

		// Storing the encoded Mat in a byte array
		byte[] byteArray = matOfByte.toArray();

		// Displaying the image
		InputStream in = new ByteArrayInputStream(byteArray);
		BufferedImage bufImage = ImageIO.read(in);

		System.out.println("Image Loaded");
		WritableImage writableImage = SwingFXUtils.toFXImage(bufImage, null);
		return writableImage;

	}
	
	public Mat detecteCannyEdges(String imgLocation) {

		// Reading the image
		Mat src = Imgcodecs.imread(imgLocation);

		// Creating an empty matrix to store the result
		Mat gray = new Mat();

		// Converting the image from color to Gray
		Imgproc.cvtColor(src, gray, Imgproc.COLOR_BGR2GRAY);
		Mat edges = new Mat();

		// Detecting the edges
		Imgproc.Canny(gray, edges, 60, 60 * 3);

		System.out.println("Image Processed");
		return edges;
	}

	public Mat houghLineTest(String imgLocation) throws Exception {

		Mat cannyColor = new Mat();
		try {
			// Reading the image
			Mat src = Imgcodecs.imread(imgLocation, 0);

			// Detecting edges of it
			Mat canny = new Mat();
			Imgproc.Canny(src, canny, 50, 200, 3, false);

			// Changing the color of the canny
			Imgproc.cvtColor(canny, cannyColor, Imgproc.COLOR_GRAY2BGR);

			// Detecting the hough lines from (canny)
			Mat lines = new Mat();
			Imgproc.HoughLines(canny, lines, 1, Math.PI / 180, 100);

			System.out.println(lines.rows());
			System.out.println(lines.cols());

			// Drawing lines on the image
			double[] data;
			double rho, theta;
			Point pt1 = new Point();
			Point pt2 = new Point();
			double a, b;
			double x0, y0;

			for (int i = 0; i < lines.cols(); i++) {
				data = lines.get(0, i);
				rho = data[0];
				theta = data[1];

				a = Math.cos(theta);
				b = Math.sin(theta);
				x0 = a * rho;
				y0 = b * rho;

				pt1.x = Math.round(x0 + 1000 * (-b));
				pt1.y = Math.round(y0 + 1000 * (a));
				pt2.x = Math.round(x0 - 1000 * (-b));
				pt2.y = Math.round(y0 - 1000 * (a));
				Imgproc.line(cannyColor, pt1, pt2, new Scalar(0, 0, 255), 6);
			}

			System.out.println("Image Processed");

		} catch (Exception e) {
			throw e;
		}

		return cannyColor;
	}
}
