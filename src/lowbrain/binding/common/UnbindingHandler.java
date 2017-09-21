package lowbrain.binding.common;

import lowbrain.binding.main.LowbrainBinding;
import lowbrain.library.command.Command;
import lowbrain.library.command.CommandHandler;
import lowbrain.library.fn;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class UnbindingHandler extends CommandHandler {

    public UnbindingHandler(JavaPlugin plugin) {
        super(plugin, "unbind");
    }

    @Override
    public Command.CommandStatus execute(CommandSender who, String[] args) {
        if (!(who instanceof Player)) {
            who.sendMessage("This command is only available as player");
            return getStatus();
        }

        if (args.length != 1)
            return Command.CommandStatus.INVALID;

        int slot = fn.toInteger(args[0], -1);

        if (slot < Bind.MIN_SLOT || slot > Bind.MAX_SLOT)
            return Command.CommandStatus.INVALID;

        ((LowbrainBinding)this.getPlugin()).getBindingManager().unbind((Player)who, slot);

        return Command.CommandStatus.VALID;
    }
}
