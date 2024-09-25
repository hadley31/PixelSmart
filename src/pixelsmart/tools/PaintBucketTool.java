package pixelsmart.tools;

import java.awt.Point;
import java.awt.Shape;
import java.awt.image.BufferedImage;
import java.util.LinkedList;
import java.util.Queue;

import pixelsmart.commands.Command;
import pixelsmart.commands.UpdateLayerDataCommand;
import pixelsmart.image.Layer;
import pixelsmart.ui.ImagePanel;

public class PaintBucketTool extends LayerModifierTool {

	BufferedImage newData;
	Layer layer;
	int tc, rc;
	Shape clipShape;

	@Override
	public Command finishAction(final ImagePanel panel) {
		if (panel.getActiveLayer() == null) {
			return null;
		}

		clipShape = panel.getClip(ImagePanel.RELATIVE_TO_LAYER);

		int mx = panel.getMouseX(ImagePanel.RELATIVE_TO_LAYER);
		int my = panel.getMouseY(ImagePanel.RELATIVE_TO_LAYER);
		layer = panel.getActiveLayer();
		int ix = layer.getImage().getWidth();
		int iy = layer.getImage().getHeight();
		if (mx < 0 || my < 0 || mx > ix || my > iy)
			return null;
		newData = layer.copyData();
		tc = newData.getRGB(mx, my);
		rc = getColor().getRGB();
		if (tc == rc)
			return null;
		floodFill(mx, my);
		return new UpdateLayerDataCommand(layer, newData);
	}

	public void floodFill(int x, int y) {
		Queue<Point> queue = new LinkedList<Point>();
		queue.add(new Point(x, y));

		while (!queue.isEmpty()) {
			Point p = queue.remove();
			if (p.x < 0)
				continue;
			if (p.y < 0)
				continue;
			if (p.x >= newData.getWidth() || p.y >= newData.getHeight())
				continue;
			if (newData.getRGB(p.x, p.y) != tc)
				continue;

			if (clipShape != null) {
				if (!clipShape.contains(p.x, p.y))
					continue;
			}

			newData.setRGB(p.x, p.y, rc);
			// System.out.println("X: " + p.x + " Y: " + p.y + " RGB: " + rc);
			queue.add(new Point(p.x + 1, p.y));
			queue.add(new Point(p.x - 1, p.y));
			queue.add(new Point(p.x, p.y + 1));
			queue.add(new Point(p.x, p.y - 1));
		}
	}

	public BufferedImage getTemporaryLayerData(Layer layer) {
		return layer.getData();
	}
}
