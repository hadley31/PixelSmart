package pixelsmart.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JToolBar;
import javax.swing.border.EmptyBorder;

import pixelsmart.image.Image;
import pixelsmart.tools.ToolManager;
import pixelsmart.ui.menubar.MenuBar;

public class MainWindow extends JFrame {
    private static final long serialVersionUID = -2835920738241308199L;
    private JPanel contentPane;
    private ImagePanel imagePanel;
    private static MainWindow currentWindow;
    private JButton colorWheelButton;

    /**
     * Create the frame.
     */
    private MainWindow() {
        this.setTitle("PixelSmart");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(1000, 800);
        this.setLocationRelativeTo(null);

        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(0, 0, 0, 0));
        contentPane.setLayout(new BorderLayout(0, 0));
        this.setContentPane(contentPane);

        imagePanel = new ImagePanel();

        JToolBar attributeToolbar = new JToolBar("Tools");

        colorWheelButton = new JButton(" ");
        colorWheelButton.addActionListener(e -> {
            if (ToolManager.get() == null) {
                return;
            }
            Color color = JColorChooser.showDialog(null, "Select Color",
                    ToolManager.get().getPrimaryBrushColor());
            colorWheelButton.setBackground(color);
            ToolManager.get().setPrimaryBrushColor(color);
        });

        colorWheelButton.setPreferredSize(new Dimension(50, 50));
        colorWheelButton.setOpaque(true);
        colorWheelButton.setBorderPainted(false);
        colorWheelButton.setBackground(Color.black);
        attributeToolbar.add(new JLabel("Color"));
        attributeToolbar.add(colorWheelButton);
        attributeToolbar.add(new BrushSizePanel());

        this.setJMenuBar(new MenuBar());

        LayerList layerList = new LayerList(imagePanel);

        contentPane.add(new StencilShapePanel(), BorderLayout.WEST);
        contentPane.add(new Toolbar(), BorderLayout.NORTH);
        contentPane.add(attributeToolbar, BorderLayout.SOUTH);
        contentPane.add(layerList, BorderLayout.EAST);
        contentPane.add(imagePanel, BorderLayout.CENTER);
    }

    public static synchronized MainWindow getInstance() {
        if (currentWindow == null) {
            currentWindow = new MainWindow();
        }
        return currentWindow;
    }

    public void run() {
        Input.getInstance().update();
        while (running()) {
            // Update
            Input.getInstance().update();
            ToolManager.get().update();

            // Render
            imagePanel.repaint();

            // Sleep
            try {
                Thread.sleep(16);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public ImagePanel getPanel() {
        return imagePanel;
    }

    private boolean running() {
        return this.isDisplayable();
    }

    public void setColorButtonColor(Color c) {
        colorWheelButton.setBackground(c);
    }

}
