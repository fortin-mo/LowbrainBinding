package lowbrain.binding.common;

import lowbrain.library.fn;
import org.apache.commons.lang.NullArgumentException;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class BindingManager {
    private JavaPlugin plugin;
    private Map<UUID, HashMap<Integer, Bind>> binds;
    private FileConfiguration data;

    /**
     * initialize the handler
     * @param plugin instance of the plugin
     */
    public BindingManager(JavaPlugin plugin, FileConfiguration data){
        this.plugin = plugin;
        this.binds = new HashMap<>();
        this.data = data;

        Bukkit.getServer().getScheduler().runTaskTimer(plugin, new Runnable() {
            @Override
            public void run() {
                save();
            }
        }, 0,180 * 20);
    }

    public void bind(Player who, Bind bind) {
        if (!getBinds().containsKey(who.getUniqueId()))
            getBinds().put(who.getUniqueId(), new HashMap<Integer, Bind>());

        HashMap<Integer, Bind> _binds = getBinds().get(who.getUniqueId());

        _binds.put(bind.getSlot(), bind);
    }

    public void unbind(Player who, int slot) {
        if (!getBinds().containsKey(who.getUniqueId()))
            getBinds().put(who.getUniqueId(), new HashMap<Integer, Bind>());

        HashMap<Integer, Bind> _binds = getBinds().get(who.getUniqueId());

        _binds.remove(slot);
    }

    public void reload() {
        save();
        this.binds = new HashMap<>();

        for (Player player: Bukkit.getOnlinePlayers())
            load(player);
    }

    public void save() {
        for(UUID uuid : this.binds.keySet())
            save(uuid);
    }

    public void save(UUID uuid) {
        try {
            HashMap<Integer, Bind> _binds = getBinds().getOrDefault(uuid, null);

            if (_binds == null)
                return;

            ConfigurationSection sec = data.getConfigurationSection(uuid.toString());

            if (sec != null)
                data.set(uuid.toString(), null); // delete

            sec = data.createSection(uuid.toString());

            for (Map.Entry<Integer, Bind> r: _binds.entrySet())
                sec.set(r.getKey().toString(), r.getValue().fullCommand());

        } catch (Exception e) {
            this.plugin.getLogger().warning("Error while saving bindings data for " + uuid.toString());
            this.plugin.getLogger().warning(e.getStackTrace().toString());
        }
    }

    public void load(Player player) {
        if (player == null)
            throw new NullArgumentException("data file configuration required !");

        if (binds == null)
            binds = new HashMap<>();

        HashMap<Integer, Bind> _binds = new HashMap<>();
        String uuid = player.getUniqueId().toString();

        ConfigurationSection section = data.getConfigurationSection(uuid);

        if (section == null) {
            binds.put(player.getUniqueId(), _binds);
            return;
        }

        for (String sslot: section.getKeys(false)) {
            int slot = fn.toInteger(sslot, -1);

            if (slot < Bind.MAX_SLOT || slot > Bind.MAX_SLOT) {
                this.plugin.getLogger().warning("Invalid slot for uuid : " + uuid);
                continue;
            }

            String a = section.getString(sslot, "").trim();

            if (a.length() == 0) {
                this.plugin.getLogger().warning("Invalid command for uuid : " + uuid);
                continue;
            }

            String[] full = a.split(" ");

            String cmd = full[0];
            String[] args;

            if (full.length > 1) {
                args = new String[full.length - 1];
                System.arraycopy(full, 0, args, 1, args.length);
            } else {
                args = new String[0];
            }

            _binds.put(slot, new Bind(player, cmd, slot, args));
        }
        binds.put(UUID.fromString(uuid), _binds);
    }

    public Map<UUID, HashMap<Integer, Bind>> getBinds() {
        if (binds == null)
            binds = new HashMap<>();
        return binds;
    }
}
