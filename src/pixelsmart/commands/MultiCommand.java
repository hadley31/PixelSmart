package pixelsmart.commands;

import java.util.List;

public class MultiCommand implements Command {
    List<Command> commands;

    public MultiCommand(List<Command> commands) {
        this.commands = commands;
    }

    public MultiCommand(Command... commands) {
        this.commands = List.of(commands);
    }

    @Override
    public void execute() {
        for (int i = 0; i < commands.size(); i++) {
            commands.get(i).execute();
        }
    }

    @Override
    public void undo() {
        for (int i = commands.size() - 1; i >= 0; i--) {
            commands.get(i).undo();
        }
    }

}
