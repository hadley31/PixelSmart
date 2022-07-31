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
		BufferedImage newData = layer.copyData();
		Graphics2D g = newData.createGraphics();

		g.setClip(panel.getClip(ImagePanel.RELATIVE_TO_LAYER));
		g.setColor(new Color(0, 0, 0, 0));
		g.setComposite(AlphaComposite.getInstance(AlphaComposite.CLEAR));
		BasicStroke stroke = new BasicStroke(getBrushSize(), BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
		g.setStroke(stroke);

		g.draw(finalStrokeShape);

		g.dispose();
		return new UpdateLayerDataCommand(layer, newData);
	}

	public BufferedImage getTemporaryLayerData(Layer layer) {
        if (finalStrokeShape == null) {
            return layer.getData();
        }

        var data = layer.copyData();
        var g = data.createGraphics();
        
        g.setClip(ImagePanel.get().getClip(ImagePanel.RELATIVE_TO_LAYER));

        g.setColor(getColor());
        final BasicStroke stroke = new BasicStroke(getBrushSize(), BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
        g.setStroke(stroke);

        g.draw(finalStrokeShape);

        return data;
	}
}