package pixelsmart.tools;

import java.awt.BasicStroke;
import java.awt.Graphics2D;
import java.awt.geom.Path2D;
import java.awt.image.BufferedImage;

import pixelsmart.commands.Command;
import pixelsmart.commands.UpdateLayerDataCommand;
import pixelsmart.image.Layer;
import pixelsmart.ui.ImagePanel;

public class PencilTool extends LayerModifierTool {

    private Path2D.Double finalStrokeShape;

    @Override
    public void startAction(final ImagePanel panel) {
        if (panel.getActiveLayer() == null) {
            return;
        }
        final int mx = panel.getMouseX(ImagePanel.RELATIVE_TO_LAYER);
        final int my = panel.getMouseY(ImagePanel.RELATIVE_TO_LAYER);

        finalStrokeShape = new Path2D.Double();
        finalStrokeShape.moveTo(mx, my);
    }

    @Override
    public void updateAction(final ImagePanel panel) {
        if (panel.getActiveLayer() == null) {
            return;
        }

        final int mx = panel.getMouseX(ImagePanel.RELATIVE_TO_LAYER);
        final int my = panel.getMouseY(ImagePanel.RELATIVE_TO_LAYER);

        finalStrokeShape.lineTo(mx, my);
    }

    @Override
    public Command finishAction(final ImagePanel panel) {
        final Layer layer = panel.getActiveLayer();

        if (layer == null) {
            return null;
        }

        final BufferedImage newData = layer.copyData();
        final Graphics2D g = newData.createGraphics();

        g.setClip(panel.getClip(ImagePanel.RELATIVE_TO_LAYER));

        g.setColor(getColor());
        final BasicStroke stroke = new BasicStroke(getBrushSize(), BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
        g.setStroke(stroke);

        g.draw(finalStrokeShape);
        g.dispose();

        finalStrokeShape = null;

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
        g.dispose();

        return data;
    }
}
