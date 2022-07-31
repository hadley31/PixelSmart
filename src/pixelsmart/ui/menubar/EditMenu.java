package pixelsmart.ui.menubar;

import java.awt.event.KeyEvent;

import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;

import pixelsmart.commands.CommandExecutor;
import pixelsmart.ui.ImagePanel;

public class EditMenu extends JMenu {
    private static final long serialVersionUID = 3180545437387906156L;

    public EditMenu() {
        super("Edit");

        JMenuItem copy = new JMenuItem("Copy");
        copy.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_K, KeyEvent.CTRL_DOWN_MASK));
        copy.addActionListener(e -> {
            ImagePanel.get().getClipboard().copy();
        });

        JMenuItem cut = new JMenuItem("Cut");
        cut.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X, KeyEvent.CTRL_DOWN_MASK));
        cut.addActionListener(e -> {
            ImagePanel.get().getClipboard().cut();
        });

        JMenuItem paste = new JMenuItem("Paste");
        paste.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_P, KeyEvent.CTRL_DOWN_MASK));
        paste.addActionListener(e -> {
            ImagePanel.get().getClipboard().paste();
        });

        JMenuItem undo = new JMenuItem("Undo");
        undo.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Z, KeyEvent.CTRL_DOWN_MASK));
        undo.addActionListener(e -> {
            CommandExecutor.get().undo();
        });

        JMenuItem redo = new JMenuItem("Redo");
        redo.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Y, KeyEvent.CTRL_DOWN_MASK));
        redo.addActionListener(e -> {
            CommandExecutor.get().redo();
        });

        this.add(copy);
        this.add(cut);
        this.add(paste);
        this.add(undo);
        this.add(redo);
    }
}