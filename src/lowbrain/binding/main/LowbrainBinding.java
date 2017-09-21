package lowbrain.binding.main;

import lowbrain.binding.common.BindingHandler;
import lowbrain.binding.common.BindingManager;
import lowbrain.binding.common.UnbindingHandler;
import lowbrain.binding.listeners.BindingListener;
import lowbrain.library.command.Command;
import lowbrain.library.command.CommandHandler;
import lowbrain.library.config.YamlConfig;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.Contract;

public class LowbrainBinding extends JavaPlugin{
    public static LowbrainBinding instance;

    private BindingHandler bindingHandler;
    private UnbindingHandler unbindingHandler;
    private BindingManager bindingManager;
    private YamlConfig config;
    private YamlConfig data;


    @Override
    public void onEnable() {
        instance = this;

        config = new YamlConfig("config.yml", this);
        data = new YamlConfig("data.yml", this);

        this.bindingHandler = new BindingHandler(this);
        this.bindingHandler.addPermission("lb.binding.use");
        this.unbindingHandler = new UnbindingHandler(this);
        this.unbindingHandler.addPermission("lb.binding.use");

        this.bindingManager = new BindingManager(this, data);

        new CommandHandler(this, "unbindall") {
            @Override
            public Command.CommandStatus execute(CommandSender who, String[] args) {
                if (who instanceof Player)
                    getBindingManager().unbindAll((Player)who);

                return Command.CommandStatus.VALID;
            }
        };

        new CommandHandler(this,"lbbind") {
            @Override
            public Command.CommandStatus execute(CommandSender who, String[] args) {
                getBindingManager().save();
                return Command.CommandStatus.VALID;
            }
        }.register("save", new Command("save") {
            @Override
            public CommandStatus execute(CommandSender who, String[] args, String cmd) {
                return null;
            }
        });

        this.getServer().getPluginManager().registerEvents(new BindingListener(this), this);
    }

    @Override
    public void onDisable() {
        this.bindingManager.save();
    }

    public BindingManager getBindingManager() {
        return bindingManager;
    }

    public BindingHandler getBindingHandler() {
        return bindingHandler;
    }

    @Contract(pure = true)
    public static LowbrainBinding getInstance() {
        return instance;
    }

    public UnbindingHandler getUnbindingHandler() {
        return unbindingHandler;
    }
}
