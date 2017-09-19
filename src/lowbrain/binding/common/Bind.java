package lowbrain.binding.common;

import org.apache.commons.lang.NullArgumentException;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Contract;

public class Bind {
    public final static int MAX_SLOT = 17;
    public final static int MIN_SLOT = 0;

    private Player who;
    private String command;
    private int slot;
    private String[] arguments;

    /**
     * bind constructor
     * @param who command sender
     * @param command command
     * @param slot slot #
     * @param args additional arguments
     */
    @Contract("null, _, _, _ -> fail; !null, null, _, _ -> fail")
    public Bind(Player who, String command, int slot, String[] args) {
        if (who == null || command == null || command.trim().length() == 0 || slot < MIN_SLOT || slot > MAX_SLOT)
            throw new NullArgumentException("Invalid arguments !");

        this.who = who;
        this.command = command.trim();
        this.slot = slot;
        this.arguments = args;
    }

    /**
     * bind constructor
     * @param who command sender
     * @param command command
     * @param slot slot #
     * @param args additional arguments as string (split @ space)
     */
    @Contract("null, _, _, _ -> fail; !null, null, _, _ -> fail")
    public Bind(Player who, String command, int slot, String args) {
        this(who, command, slot, args.split(" "));
    }

    public String fullCommand() {
        String cmd = "" + command;
        String split = " ";

        for (String arg : arguments)
            cmd += split + arg;

        return cmd;
    }

    public int getSlot() {
        return slot;
    }

    public Player getWho() {
        return who;
    }

    public String getCommand() {
        return command;
    }

    public String[] getArguments() {
        return arguments;
    }
}
