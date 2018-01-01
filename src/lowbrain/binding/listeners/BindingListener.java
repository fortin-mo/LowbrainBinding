package lowbrain.binding.listeners;

import lowbrain.binding.common.Bind;
import lowbrain.binding.main.LowbrainBinding;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashMap;

public class BindingListener implements Listener {
    LowbrainBinding plugin;

    public BindingListener (LowbrainBinding plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        plugin.getBindingManager().load(e.getPlayer());
    }

    @EventHandler
    public void onPlayerDisconnect(PlayerQuitEvent e) {
        plugin.getBindingManager().unload(e.getPlayer());
    }

    @EventHandler
    public void onInvClick(InventoryClickEvent e) {
        if (!(e.getInventory().getHolder() instanceof Player))
            return;

        Player player = (Player)e.getInventory().getHolder();

        if (!player.hasPermission("lb.binding.use"))
            return;

        if (e.isRightClick() && e.getSlot() == -999 && e.getInventory().getName().equalsIgnoreCase("container.crafting")) {
            Inventory inventory = createInventory(player);
            player.openInventory(inventory);
            player.updateInventory();
            return;
        }

        if (!e.getInventory().getName().equalsIgnoreCase("Binds"))
            return;

        e.setCancelled(true);

        if (e.isShiftClick()) {
            player.sendMessage(ChatColor.AQUA + "This is slot: " + ChatColor.GOLD + e.getSlot());
            return;
        }

        Bind bind = plugin.getBindingManager().getBinds(player).getOrDefault(e.getSlot(), null);

        if (bind == null)
            return;

        player.performCommand(bind.fullCommand());

    }

    @EventHandler
    public void PlayerInteractEvent(PlayerInteractEvent event) {
        /*
        Player player = event.getPlayer();

        if (player.getOpenInventory().getType() != InventoryType.PLAYER
                || player.isSneaking()
                || event.getAction() != Action.LEFT_CLICK_AIR
                || !player.hasPermission("lb.binding.use"))
            return;
        
        Inventory inventory = createInventory(player);
        player.openInventory(inventory);
        player.updateInventory();
        */
    }
    
    private Inventory createInventory(Player player) {
        Inventory inventory = Bukkit.createInventory(player, 18, "Binds");
        HashMap<Integer, Bind> _binds = plugin.getBindingManager().getBinds(player);
        for (int i = 0; i < Bind.getMaxSlot(); i++) {
            Material material;

            if (i <= 15)
                material = Material.WOOL;
            else if (i <= 30)
                material = Material.BANNER;
            else
                material = Material.STAINED_GLASS;

            short color = (short)(0 - (Math.floor(i / 15) * 15) + i);

            ItemStack item = new ItemStack(material, 1, color);
            ItemMeta iMeta = item.getItemMeta();

            if (_binds.containsKey(i))
                iMeta.setDisplayName(_binds.get(i).fullCommand());
            else
                iMeta.setDisplayName("Slot #" + i);

            item.setItemMeta(iMeta);
            inventory.addItem(item);
        }

        return inventory;
    }
}
