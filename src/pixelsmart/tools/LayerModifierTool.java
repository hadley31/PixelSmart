package pixelsmart.tools;

import java.awt.Color;
import java.awt.image.BufferedImage;

import pixelsmart.image.Layer;

public abstract class LayerModifierTool extends ToolAdapter {

    protected int getBrushSize() {
        return ToolManager.get().getBrushSize();
    }

    protected Color getColor() {
        return ToolManager.get().getPrimaryBrushColor();
    }

    protected Color getSecondaryColor() {
        return ToolManager.get().getSecondaryBrushColor();
    }

    public abstract BufferedImage getTemporaryLayerData(Layer layer);
}