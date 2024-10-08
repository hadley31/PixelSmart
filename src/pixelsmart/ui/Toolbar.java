package pixelsmart.ui;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JToolBar;

import pixelsmart.tools.*;

public class Toolbar extends JToolBar {
        /**
         *
         */
        private static final long serialVersionUID = -833808924744341165L;
        JToolBar toolbar;
        JButton pencil;
        JButton eraser;
        JButton fill;
        JButton color_picker;
        JButton line;
        JButton zoom;
        JButton square_select;
        JButton lasso_select;
        JButton move;

        public Toolbar() {
                ImageIcon pencilImage = new ImageIcon("res/images/pencil x2.png");
                ImageIcon eraserImage = new ImageIcon("res/images/eraser x2.png");
                ImageIcon fillImage = new ImageIcon("res/images/Fill x2.png");
                ImageIcon color_pickerImage = new ImageIcon("res/images/color picker x2.png");
                ImageIcon lineImage = new ImageIcon("res/images/line x2.png");
                ImageIcon zoomImage = new ImageIcon("res/images/Zoom x2.png");
                ImageIcon square_selectImage = new ImageIcon("res/images/Select x2.png");
                ImageIcon lasso_selectImage = new ImageIcon("res/images/lasso x2.png");
                ImageIcon moveImage = new ImageIcon("res/images/move x2.png");

                pencil = new JButton(pencilImage);
                eraser = new JButton(eraserImage);
                fill = new JButton(fillImage);
                color_picker = new JButton(color_pickerImage);
                line = new JButton(lineImage);
                zoom = new JButton(zoomImage);
                square_select = new JButton(square_selectImage);
                lasso_select = new JButton(lasso_selectImage);
                move = new JButton(moveImage);

                pencil.addActionListener(e -> {
                        ToolManager.get().setTool(new PencilTool());
                });
                eraser.addActionListener(e -> {
                        ToolManager.get().setTool(new EraserTool());
                });
                fill.addActionListener(e -> {
                        ToolManager.get().setTool(new PaintBucketTool());
                });
                color_picker.addActionListener(e -> {
                        ToolManager.get().setTool(new ColorPickerTool());
                });
                line.addActionListener(e -> {
                        ToolManager.get().setTool(new LineTool());
                });
                zoom.addActionListener(e -> {
                        ToolManager.get().setTool(new ZoomTool());
                });
                square_select.addActionListener(e -> {
                        ToolManager.get().setTool(new BoxSelectTool());
                });
                lasso_select.addActionListener(e -> {
                        ToolManager.get().setTool(new LassoTool());
                });
                move.addActionListener(e -> {
                        ToolManager.get().setTool(new MoveTool());
                });

                this.add(pencil);
                this.add(eraser);
                this.add(line);
                this.add(color_picker);
                this.add(fill);
                this.add(square_select);
                this.add(lasso_select);
                this.add(move);
                this.add(zoom);
        }
}