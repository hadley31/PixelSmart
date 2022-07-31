package pixelsmart.commands;

import pixelsmart.image.Layer;

public class SetLayerIndexCommand implements Command{
    private Layer layer;
    private int index, previousIndex;

    public SetLayerIndexCommand(Layer layer, int index) {
        this.layer = layer;
        this.index = index;
    }

    @Override
    public void execute() {
        previousIndex = layer.getIndex();

        layer.setIndex(index);
    }

    @Override
    public void undo() {
        layer.setIndex(previousIndex);
    }
}
