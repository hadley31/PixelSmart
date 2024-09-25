package pixelsmart.tools;

import java.awt.BasicStroke;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import pixelsmart.commands.Command;
import pixelsmart.commands.UpdateLayerDataCommand;
import pixelsmart.image.Layer;
import pixelsmart.ui.ImagePanel;

public class LineTool extends LayerModifierTool {

    private int startMX, startMY;
    private boolean isDrawing = false;

    @Override
    public void startAction(final ImagePanel panel) {
        if (panel.getActiveLayer() == null) {
            return;
        }

        isDrawing = true;
        startMX = panel.getMouseX(ImagePanel.RELATIVE_TO_LAYER);
        startMY = panel.getMouseY(ImagePanel.RELATIVE_TO_LAYER);
    }

    @Override
    public Command finishAction(final ImagePanel panel) {
        Layer layer = panel.getActiveLayer();

        if (layer == null) {
            return null;
        }

        int mx = panel.getMouseX(ImagePanel.RELATIVE_TO_LAYER);
        int my = panel.getMouseY(ImagePanel.RELATIVE_TO_LAYER);

        BufferedImage newData = layer.copyData();

        Graphics2D g = newData.createGraphics();

        g.setClip(panel.getClip(ImagePanel.RELATIVE_TO_LAYER));

        g.setColor(ToolManager.get().getPrimaryBrushColor());
        BasicStroke stroke = new BasicStroke(getBrushSize(), BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
        g.setStroke(stroke);

        g.drawLine(startMX, startMY, mx, my);
        g.dispose();

        isDrawing = false;

        return new UpdateLayerDataCommand(layer, newData);
    }

    public BufferedImage getTemporaryLayerData(Layer layer) {
        if (!isDrawing) {
            return layer.getData();
        }

        int mx = ImagePanel.get().getMouseX(ImagePanel.RELATIVE_TO_LAYER);
        int my = ImagePanel.get().getMouseY(ImagePanel.RELATIVE_TO_LAYER);

        var data = layer.copyData();
        var g = (Graphics2D) data.getGraphics();

        g.setClip(ImagePanel.get().getClip(ImagePanel.RELATIVE_TO_LAYER));

        g.setColor(ToolManager.get().getPrimaryBrushColor());
        BasicStroke stroke = new BasicStroke(getBrushSize(), BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
        g.setStroke(stroke);

        g.drawLine(startMX, startMY, mx, my);

        return data;
    }

}
