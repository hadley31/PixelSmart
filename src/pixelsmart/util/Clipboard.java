package pixelsmart.util;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.image.BufferedImage;

import pixelsmart.commands.Command;
import pixelsmart.commands.CommandExecutor;
import pixelsmart.commands.MultiCommand;
import pixelsmart.commands.SetClipShapeCommand;
import pixelsmart.commands.UpdateLayerDataCommand;
import pixelsmart.image.Image;
import pixelsmart.image.Layer;
import pixelsmart.ui.ImagePanel;

public class Clipboard {

    private BufferedImage data;

    public Clipboard() {

    }

    public void copy() {
        // System.out.println("Copied!");
        if (ImagePanel.get().getClip(ImagePanel.RELATIVE_TO_LAYER) == null) {
            return;
        }

        Shape selectionClip = ImagePanel.get().getClip(ImagePanel.RELATIVE_TO_LAYER);
        Rectangle rectClip = selectionClip.getBounds();
        Layer activeLayer = ImagePanel.get().getActiveLayer();

        rectClip.x = MathUtil.clamp(rectClip.x, 0, ImagePanel.get().getActiveLayer().getWidth());
        rectClip.y = MathUtil.clamp(rectClip.y, 0, ImagePanel.get().getActiveLayer().getHeight());

        if (rectClip.x + rectClip.width > ImagePanel.get().getActiveLayer().getWidth())
            rectClip.width = ImagePanel.get().getActiveLayer().getWidth() - rectClip.x;
        if (rectClip.y + rectClip.height > ImagePanel.get().getActiveLayer().getHeight())
            rectClip.height = ImagePanel.get().getActiveLayer().getHeight() - rectClip.y;

        data = activeLayer.copyData().getSubimage(rectClip.x, rectClip.y, rectClip.width, rectClip.height);

        // System.out.format("Copied Image! X: %d, Y: %d, X2: %d, Y2: %d\n", rectClip.x,
        // rectClip.y, rectClip.width, rectClip.height);
    }

    public void cut() {
        if (ImagePanel.get().getClip(ImagePanel.RELATIVE_TO_LAYER) == null) {
            return;
        }

        Shape selectionClip = ImagePanel.get().getClip(ImagePanel.RELATIVE_TO_LAYER);
        Rectangle rectClip = selectionClip.getBounds();
        Layer activeLayer = ImagePanel.get().getActiveLayer();
        BufferedImage copyData = activeLayer.copyData();
        Graphics2D g = copyData.createGraphics();
        g.setBackground(new Color(255, 255, 255, 0));

        rectClip.x = MathUtil.clamp(rectClip.x, 0, ImagePanel.get().getActiveLayer().getWidth());
        rectClip.y = MathUtil.clamp(rectClip.y, 0, ImagePanel.get().getActiveLayer().getHeight());

        if (rectClip.x + rectClip.width > ImagePanel.get().getActiveLayer().getWidth())
            rectClip.width = ImagePanel.get().getActiveLayer().getWidth() - rectClip.x;
        if (rectClip.y + rectClip.height > ImagePanel.get().getActiveLayer().getHeight())
            rectClip.height = ImagePanel.get().getActiveLayer().getHeight() - rectClip.y;

        g.clearRect(rectClip.x, rectClip.y, rectClip.width, rectClip.height);
        data = activeLayer.copyData().getSubimage(rectClip.x, rectClip.y, rectClip.width, rectClip.height);

        Command c = new MultiCommand(new UpdateLayerDataCommand(activeLayer, copyData), new SetClipShapeCommand(null));
        CommandExecutor.get().execute(c);
    }

    public void paste() {
        if (data == null) {
            return;
        }

        Image img = ImagePanel.get().getImage();
        String name = img.getUniqueLayerName();

        img.addLayer(name);
        ImagePanel.get().setActiveLayer(name);
        Layer activeLayer = ImagePanel.get().getActiveLayer();
        BufferedImage copyData = activeLayer.copyData();
        Graphics2D g = copyData.createGraphics();
        g.drawImage(data, 0, 0, null);
        Rectangle newClip = new Rectangle(0, 0, data.getWidth(), data.getHeight());

        Command c = new MultiCommand(new SetClipShapeCommand(newClip),
                new UpdateLayerDataCommand(activeLayer, copyData));
        CommandExecutor.get().execute(c);

        g.dispose();
    }
}
