package pixelsmart.tools;

import pixelsmart.commands.Command;
import pixelsmart.ui.ImagePanel;

public abstract class AbstractTool {
    public abstract void startAction(ImagePanel panel);

    public abstract void updateAction(ImagePanel panel);

    public abstract Command finishAction(ImagePanel panel);
}
