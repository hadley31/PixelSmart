package pixelsmart.commands;

import java.util.LinkedList;

public class CommandExecutor {
    private static final int AMOUNT_OF_COMMANDS = 50;
    private static CommandExecutor instance;

    private LinkedList<Command> commands = new LinkedList<Command>();

    private int commandIndex = 0;

    private CommandExecutor() {
    }

    public static synchronized CommandExecutor get() {
        if (instance == null)
            instance = new CommandExecutor();
        return instance;
    }

    public void undo() {
        if (commandIndex >= 0 && commandIndex < commands.size()) {
            commands.get(commandIndex--).undo();
        }
    }

    public void redo() {
        if (commandIndex >= -1 && commandIndex < commands.size() - 1) {
            commands.get(++commandIndex).execute();
        }
    }

    public void execute(Command c) {
        while (commands.size() > commandIndex + 1) {
            commands.removeLast();
        }

        commands.add(c);
        c.execute();
        commandIndex = commands.size() - 1;

        while (commands.size() > AMOUNT_OF_COMMANDS) {
            commands.removeFirst();
        }
    }

    public void clear() {
        commands.clear();
    }
}
