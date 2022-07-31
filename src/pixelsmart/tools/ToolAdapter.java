package pixelsmart.tools;

import pixelsmart.commands.Command;
import pixelsmart.ui.ImagePanel;

public abstract class ToolAdapter extends AbstractTool {
    @Override
    public void startAction(ImagePanel panel) {

    }

    @Override
    public void updateAction(ImagePanel panel) {

    }

    @Override
    public Command finishAction(ImagePanel panel) {
        return null;
    }
}