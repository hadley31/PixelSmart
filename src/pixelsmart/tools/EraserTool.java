package pixelsmart.tools;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Path2D;
import java.awt.image.BufferedImage;

import pixelsmart.commands.Command;
import pixelsmart.commands.UpdateLayerDataCommand;
import pixelsmart.image.Layer;
import pixelsmart.ui.ImagePanel;

public class EraserTool extends LayerModifierTool {
	private Path2D.Double finalStrokeShape;

	@Override
	public void startAction(ImagePanel panel) {
		if (panel.getActiveLayer() == null) {
			return;
		}

		finalStrokeShape = new Path2D.Double();
		int mx = panel.getMouseX(ImagePanel.RELATIVE_TO_LAYER);
		int my = panel.getMouseY(ImagePanel.RELATIVE_TO_LAYER);
		finalStrokeShape.moveTo(mx, my);
	}

	@Override
	public void updateAction(ImagePanel panel) {
		if (panel.getActiveLayer() == null) {
			return;
		}

		int mx = panel.getMouseX(ImagePanel.RELATIVE_TO_LAYER);
		int my = panel.getMouseY(ImagePanel.RELATIVE_TO_LAYER);

		finalStrokeShape.lineTo(mx, my);
	}

	@Override
	public Command finishAction(ImagePanel panel) {
		if (panel.getActiveLayer() == null) {
			return null;
		}

		Layer layer = panel.getActiveLayer();

		return new UpdateLayerDataCommand(layer, getUpdatedImageData(panel, layer));
	}

	@Override
	public BufferedImage getTemporaryLayerData(Layer layer) {
		if (finalStrokeShape == null) {
			return layer.getData();
		}

		return getUpdatedImageData(ImagePanel.get(), layer);
	}

	private BufferedImage getUpdatedImageData(ImagePanel panel, Layer layer) {
		var data = layer.copyData();
		Graphics2D g = data.createGraphics();

		g.setClip(panel.getClip(ImagePanel.RELATIVE_TO_LAYER));
		g.setColor(new Color(0, 0, 0, 0));
		g.setComposite(AlphaComposite.getInstance(AlphaComposite.CLEAR));
		BasicStroke stroke = new BasicStroke(getBrushSize(), BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
		g.setStroke(stroke);

		g.draw(finalStrokeShape);

		g.dispose();

		return data;
	}
}
