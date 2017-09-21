package lowbrain.binding.common;

import lowbrain.binding.main.LowbrainBinding;
import lowbrain.library.command.Command;
import lowbrain.library.command.CommandHandler;
import lowbrain.library.fn;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class BindingHandler extends CommandHandler {

    public BindingHandler(JavaPlugin plugin) {
        super(plugin, "bind");
    }

    @Override
    public Command.CommandStatus execute(CommandSender who, String[] args) {
        if (!(who instanceof Player)) {
            who.sendMessage("This command is only available as player");
            return getStatus();
        }

        if (args.length < 2)
            return Command.CommandStatus.INVALID;

        int slot = fn.toInteger(args[0], -1);

        if (slot < Bind.MIN_SLOT || slot > Bind.MAX_SLOT)
            return Command.CommandStatus.INVALID;

        String cmd = args[1];

        String[] arguments;

        if (args.length > 2) {
            arguments = new String[args.length - 2];
            System.arraycopy(args, 2, arguments, 0, arguments.length);
        } else {
            arguments = new String[0];
        }

        Bind bind = new Bind((Player)who, cmd, slot, arguments);

        ((LowbrainBinding)this.getPlugin()).getBindingManager().bind((Player)who, bind);

        return Command.CommandStatus.VALID;
    }
}
