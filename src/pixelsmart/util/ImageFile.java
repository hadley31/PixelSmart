package pixelsmart.util;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.DataBufferInt;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.JFileChooser;

import pixelsmart.image.Image;
import pixelsmart.image.Layer;
import pixelsmart.ui.ImagePanel;
import pixelsmart.ui.MainWindow;

public class ImageFile {

	public static boolean exportWithDialog(BufferedImage image, String format) {
		JFileChooser fileChooser = new JFileChooser();

		int result = fileChooser.showSaveDialog(MainWindow.getInstance());
		if (result == JFileChooser.APPROVE_OPTION) {
			File file = fileChooser.getSelectedFile();
			return export(image, format, file);
		}

		return false;
	}

	public static boolean export(BufferedImage image, String format, File file) {
		if (image == null)
			return false;
		try {
			return ImageIO.write(image, format, file);
		} catch (IOException e) {
			return false;
		}
	}

	public static BufferedImage load(File file) {
		try {
			return ImageIO.read(file);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static Image loadPSF(File file) {
		Image img = null;
		try {
			FileInputStream fr = new FileInputStream(file);

			img = loadHeader(fr);
			if (img != null) {
				loadLayers(img, fr);
			}
			fr.close();

		} catch (Exception e) {
			// e.printStackTrace();
		}
		return img;
	}

	public static void savePSF(Image image, File file) {
		// everything will be in a try catch cause I don't want to
		// write a try catch for each write.
		try {
			FileOutputStream fw = new FileOutputStream(file);

			// save header
			String HeaderString = getHeaderString();
			String LayersString = getLayersString();

			fw.write(HeaderString.getBytes(), 0, HeaderString.length());
			fw.write(LayersString.getBytes(), 0, LayersString.length());

			fw.close();
		} catch (Exception e) {
			// e.printStackTrace();
		}
	}

	private static String getStringFromBytes(byte[] b) {
		String n = "";
		for (int i = 0; i < b.length; i++)
			n += (char) (b[i] & 0xFF);

		return n;
	}

	private static Image loadHeader(FileInputStream fr) {
		try {
			// load header
			// check if valid file
			String testThing = "";
			testThing += (char) fr.read();
			testThing += (char) fr.read();
			testThing += (char) fr.read();

			if (!testThing.equalsIgnoreCase("psf")) {
				return null;
			}

			String widthVal = getStringFromBytes(fr.readNBytes(4));

			String heightVal = getStringFromBytes(fr.readNBytes(4));

			Image img = new Image(MathUtil.byteStringToInt(widthVal), MathUtil.byteStringToInt(heightVal));

			return img;
		} catch (Exception e) {
			// e.printStackTrace();
		}
		return null;
	}

	private static String getHeaderString() {
		// Format for header
		// psf - Identifier
		// width - 4 bytes
		// height - 4 bytes
		// layers - 4 bytes

		String k = "psf";
		Image img = ImagePanel.get().getImage();
		k += MathUtil.intToByteString(img.getWidth());
		k += MathUtil.intToByteString(img.getHeight());
		k += MathUtil.intToByteString(img.getLayers().size());

		return k;
	}

	private static void loadLayers(Image img, FileInputStream fr) {
		try {
			// note, even though the amount of layers is considered apart of the header,
			// it is here for convenience

			String layers = getStringFromBytes(fr.readNBytes(4));
			int addAmount = MathUtil.byteStringToInt(layers);

			for (int i = 0; i < addAmount; i++) {
				// for now, skip the identifier for layers in a file
				fr.read();
				fr.read();

				String xString = getStringFromBytes(fr.readNBytes(4));

				int x = MathUtil.byteStringToInt(xString);

				String yString = getStringFromBytes(fr.readNBytes(4));
				int y = MathUtil.byteStringToInt(yString);

				String widString = getStringFromBytes(fr.readNBytes(4));
				int width = MathUtil.byteStringToInt(widString);

				String heiString = getStringFromBytes(fr.readNBytes(4));
				int height = MathUtil.byteStringToInt(heiString);

				String nameSizeString = getStringFromBytes(fr.readNBytes(4));
				int nameSize = MathUtil.byteStringToInt(nameSizeString);

				String layerName = "";
				for (int j = 0; j < nameSize; j++) {
					layerName += (char) fr.read();
				}

				boolean isVis = ((char) fr.read() == '1');
				boolean isBase = ((char) fr.read() == '1');

				if (i == 0) {
					// must get rid of original base layer
					String tempLayerName = layerName + "1";
					img.addLayer(tempLayerName);
					img.getLayerByName(tempLayerName).setAsBaseLayer();

					img.removeLayer("Layer 1");

					img.addLayer(layerName);
					img.getLayerByName(layerName).setAsBaseLayer();
					img.removeLayer(tempLayerName);
				} else {
					img.addLayer(layerName);
				}

				Layer p = img.getLayerByIndex(i);
				p.setVisible(isVis);
				if (isBase)
					p.setAsBaseLayer();

				p.setX(x);
				p.setY(y);
				p.setWidth(width);
				p.setHeight(height);

				// skip the id for pixel values
				fr.read();
				fr.read();
				fr.read();

				for (int tempY = 0; tempY < height; tempY++) {
					for (int tempX = 0; tempX < width; tempX++) {
						String colorString = getStringFromBytes(fr.readNBytes(4));

						int colorVal = MathUtil.byteStringToInt(colorString);
						Color c = new Color(colorVal, true);
						p.setPixelColor(tempX, tempY, c);
					}
				}

			}
		} catch (Exception e) {
			// e.printStackTrace();
		}
	}

	private static String getLayersString() {
		String k = "";
		Image img = ImagePanel.get().getImage();
		ArrayList<Layer> layers = img.getLayers();

		for (int i = 0; i < layers.size(); i++) {
			Layer currentLayer = layers.get(i);
			k += "la";
			k += MathUtil.intToByteString(currentLayer.getX());
			k += MathUtil.intToByteString(currentLayer.getY());
			k += MathUtil.intToByteString(currentLayer.getWidth());
			k += MathUtil.intToByteString(currentLayer.getHeight());
			k += MathUtil.intToByteString(currentLayer.getName().length());
			k += currentLayer.getName();
			k += currentLayer.isVisible() == true ? (byte) 1 : (byte) 0;
			k += currentLayer.isBaseLayer() == true ? (byte) 1 : (byte) 0;

			k += "pix";

			int[] pixels = ((DataBufferInt) currentLayer.getData().getRaster().getDataBuffer()).getData();

			ByteBuffer buf = ByteBuffer.allocate(pixels.length * 4);
			IntBuffer originalValues = buf.asIntBuffer();
			originalValues.put(pixels);

			byte[] finalArray = buf.array();
			k += new String(finalArray);
		}

		return k;
	}
}