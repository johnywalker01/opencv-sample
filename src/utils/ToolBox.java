package utils;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;

import org.opencv.core.Mat;

import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;

/**
 * Provide general purpose methods for handling OpenCV-JavaFX data conversion. Moreover, expose some "low level" methods for
 * matching few JavaFX behavior.
 *
 * @version 1.0 (2018-07-05)
 * @since 1.0
 * 
 */
public final class ToolBox {
	public static final String FACE_DETECTION = "Frontal Face Detection";
	public static final String IMPROVED_FACE_DETECTION = "Frontal Face Improved Detection";
	public static final String EYE_DETECTION = "Eye Detection";
	public static final String EYE_TREE_DETECTION = "Eye Tree Eyeglasses Detection";
	public static final String FULLBODY_DETECTION = "Fullbody Detection";

	public static final String XML_FACE_DETECTION = "resources/lbpcascades/lbpcascade_frontalface.xml";
	public static final String XML_IM_FACE_DETECTION = "resources/lbpcascades/lbpcascade_frontalface_improved.xml";
	public static final String XML_EYE_DETECTION = "resources/haarcascades_cuda/haarcascade_eye.xml";
	public static final String XML_ETE_DETECTION = "resources/haarcascades/haarcascade_eye_tree_eyeglasses.xml";
	public static final String XML_FULLBODY_DETECTION = "resources/haarcascades/haarcascade_fullbody.xml";
	
	/**
	 * Convert a Mat object (OpenCV) in the corresponding Image for JavaFX
	 *
	 * @param frame the {@link Mat} representing the current frame
	 * @return the {@link Image} to show
	 */
	public static Image mat2Image(Mat frame) {
		try {
			return SwingFXUtils.toFXImage(matToBufferedImage(frame), null);
		} catch (Exception e) {
			System.err.println("Cannot convert the Mat object: " + e);
			return null;
		}
	}

	/**
	 * Generic method for putting element running on a non-JavaFX thread on the JavaFX thread, to properly update the UI
	 * 
	 * @param property a {@link ObjectProperty}
	 * @param value    the value to set for the given {@link ObjectProperty}
	 */
	public static <T> void onFXThread(final ObjectProperty<T> property, final T value) {
		Platform.runLater(() -> {
			property.set(value);
		});
	}

	/**
	 * Support for the {@link mat2image()} method
	 * 
	 * @param original the {@link Mat} object in BGR or grayscale
	 * @return the corresponding {@link BufferedImage}
	 */
	private static BufferedImage matToBufferedImage(Mat original) {
		// init
		BufferedImage image = null;
		int width = original.width(), height = original.height(), channels = original.channels();
		byte[] sourcePixels = new byte[width * height * channels];
		original.get(0, 0, sourcePixels);

		if (original.channels() > 1) {
			image = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);
		} else {
			image = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_GRAY);
		}
		final byte[] targetPixels = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
		System.arraycopy(sourcePixels, 0, targetPixels, 0, sourcePixels.length);

		return image;
	}

	public static void showWarning(final String title, final String headerText, final String content) {
		Alert alert = new Alert(AlertType.WARNING);
		alert.setTitle(title);
		alert.setHeaderText(headerText);
		alert.setContentText(content);

		alert.showAndWait();
	}
}
