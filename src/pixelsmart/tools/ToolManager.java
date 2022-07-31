package pixelsmart.tools;

import java.awt.Color;

import pixelsmart.commands.Command;
import pixelsmart.commands.CommandExecutor;
import pixelsmart.ui.ImagePanel;
import pixelsmart.ui.MainWindow;
import pixelsmart.ui.Input;

public class ToolManager {

    private static ToolManager instance;
    private Color primaryBrushColor = Color.BLACK;
    private Color secondaryBrushColor = Color.WHITE;
    private int brushSize = 10;
    private AbstractTool tool;

    private ToolManager() {
        this.tool = new PencilTool();
    }

    public static synchronized ToolManager get() {
        if (instance == null) {
            instance = new ToolManager();
        }
        return instance;
    }

    public void update() {
        ImagePanel panel = ImagePanel.get();
        if (panel.getImage() != null && tool != null) {
            if (Input.getAnyMouseButtonDown()) {
                tool.startAction(panel);
            } else if (Input.getAnyMouseButton()) {
                tool.updateAction(panel);
            } else if (Input.getAnyMouseButtonUp()) {
                final Command c = tool.finishAction(panel);
                if (c != null) {
                    CommandExecutor.get().execute(c);
                }
            }
        }
    }

    public Color getPrimaryBrushColor() {
        return this.primaryBrushColor;
    }

    public Color getSecondaryBrushColor() {
        return this.secondaryBrushColor;
    }

    public int getBrushSize() {
        return this.brushSize;
    }

    public void setPrimaryBrushColor(Color color) {
        this.primaryBrushColor = color;
        MainWindow.getInstance().setColorButtonColor(color);
    }

    public void setSecondaryBrushColor(Color color) {
        this.secondaryBrushColor = color;
    }

    public void setBrushSize(int size) {
        this.brushSize = size;
    }

    public void setTool(AbstractTool tool) {
        this.tool = tool;
    }

    public AbstractTool getTool() {
        return this.tool;
    }
}