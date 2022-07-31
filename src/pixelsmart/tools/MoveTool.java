package pixelsmart.tools;

import java.awt.Rectangle;

import pixelsmart.commands.Command;
import pixelsmart.commands.SetLayerPositionCommand;
import pixelsmart.image.Layer;
import pixelsmart.ui.ImagePanel;
import pixelsmart.ui.Input;

public class MoveTool extends AbstractTool {
    private static final int MAX_SNAP_DISTANCE = 5;

    private int oldPositionX, oldPositionY;
    private int mouseOffsetX, mouseOffsetY;
    private int newPositionX, newPositionY;

    @Override
    public void startAction(final ImagePanel panel) {
        final Layer layer = panel.getActiveLayer();
        final int layerX = layer.getX();
        final int layerY = layer.getY();

        oldPositionX = layerX;
        oldPositionY = layerY;

        mouseOffsetX = layerX - panel.getMouseX(ImagePanel.RELATIVE_TO_IMAGE);
        mouseOffsetY = layerY - panel.getMouseY(ImagePanel.RELATIVE_TO_IMAGE);
    }

    @Override
    public void updateAction(final ImagePanel panel) {
        final Layer layer = panel.getActiveLayer();

        // TODO: Snaps to other layers well, but does not snap to image.
        // Just need to check with 0,0 and image.getWidth() and image.getHeight()
        // Draw purple lines where it is wanting to snap to

        final int lx = panel.getMouseX(ImagePanel.RELATIVE_TO_IMAGE) + mouseOffsetX;
        final int ly = panel.getMouseY(ImagePanel.RELATIVE_TO_IMAGE) + mouseOffsetY;

        newPositionX = lx;
        newPositionY = ly;

        if (Input.getMouseButtonDown(Input.RIGHT_MOUSE)) {
            final Rectangle rect = layer.getRect();
            final int maxX = lx + rect.width;
            final int maxY = ly + rect.height;
            int dist;
            int closestX = Integer.MAX_VALUE;
            int closestY = Integer.MAX_VALUE;
            for (Layer l : panel.getImage()) {
                if (layer == l)
                    continue;

                final Rectangle otherRect = l.getRect();
                final int otherMaxX = otherRect.x + otherRect.width;
                final int otherMaxY = otherRect.y + otherRect.height;

                // Test X, X
                dist = Math.abs(lx - otherRect.x);
                if (dist <= MAX_SNAP_DISTANCE && dist < closestX) {
                    newPositionX = otherRect.x;
                    closestX = dist;
                }

                // Test X, Width
                dist = Math.abs(lx - otherMaxX);
                if (dist <= MAX_SNAP_DISTANCE && dist < closestX) {
                    newPositionX = otherMaxX;
                    closestX = dist;
                }

                // Test Max X, X
                dist = Math.abs(maxX - otherRect.x);
                if (dist <= MAX_SNAP_DISTANCE && dist < closestX) {
                    newPositionX = otherRect.x - rect.width;
                    closestX = dist;
                }

                // Test Width, Width
                dist = Math.abs(maxX - otherMaxX);
                if (dist <= MAX_SNAP_DISTANCE && dist < closestX) {
                    newPositionX = otherMaxX - rect.width;
                    closestX = dist;
                }

                // Test Y, Y
                dist = Math.abs(ly - otherRect.y);
                if (dist <= MAX_SNAP_DISTANCE && dist < closestY) {
                    newPositionY = otherRect.y;
                    closestY = dist;
                }

                // Test Y, Height
                dist = Math.abs(ly - otherMaxY);
                if (dist <= MAX_SNAP_DISTANCE && dist < closestY) {
                    newPositionY = otherMaxY;
                    closestY = dist;
                }

                // Test Height, Y
                dist = Math.abs(maxY - otherRect.y);
                if (dist <= MAX_SNAP_DISTANCE && dist < closestY) {
                    newPositionY = otherRect.y - rect.height;
                    closestY = dist;
                }

                // Test Height, Height
                dist = Math.abs(maxY - otherMaxY);
                if (dist <= MAX_SNAP_DISTANCE && dist < closestY) {
                    newPositionY = otherMaxY - rect.width;
                    closestY = dist;
                }
            }
        }

        layer.setPosition(newPositionX, newPositionY);
    }

    @Override
    public Command finishAction(ImagePanel panel) {
        final Layer layer = panel.getActiveLayer();

        return new SetLayerPositionCommand(layer, oldPositionX, oldPositionY, newPositionX, newPositionY);
    }
}