package pixelsmart.commands;

import pixelsmart.image.Layer;

public class SetLayerPositionCommand implements Command {
    private Layer layer;
    private int x, y, previousX, previousY;
    private boolean previousProvided = false;

    public SetLayerPositionCommand(Layer layer, int x, int y) {
        this.layer = layer;
        this.x = x;
        this.y = y;
    }

    public SetLayerPositionCommand(Layer layer, int oldX, int oldY, int x, int y) {
        this.layer = layer;
        this.previousX = oldX;
        this.previousY = oldY;
        this.previousProvided = true;
        this.x = x;
        this.y = y;
    }

    @Override
    public void execute() {
        if (!previousProvided){
            previousX = layer.getX();
            previousY = layer.getY();
        }

        layer.setX(x);
        layer.setY(y);
    }

    @Override
    public void undo() {
        layer.setX(previousX);
        layer.setY(previousY);
    }

}
