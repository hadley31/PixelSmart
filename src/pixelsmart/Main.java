package pixelsmart;

import javax.swing.UIManager;

import pixelsmart.image.Image;
import pixelsmart.ui.MainWindow;

public class Main {
    public static void main(String[] args) {

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        MainWindow window = MainWindow.getInstance();
        window.setVisible(true);
        MainWindow.getInstance().getPanel().setImage(new Image(512, 512));
        window.run();
    }
}
