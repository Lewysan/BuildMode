package me.tyranzx.buildmode;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;

public final class StellarCore extends JavaPlugin implements Listener, CommandExecutor {

    public String c(String msg){
        return ChatColor.translateAlternateColorCodes('&', msg);
    }
    private final ArrayList<Player> state = new ArrayList<>();

    @Override
    public void onEnable() {
        Bukkit.getConsoleSender().sendMessage(c("&aBuildMode &factivado &8| &cVersion: &f1.0"));
        this.getCommand("build").setExecutor(this);
        Bukkit.getServer().getPluginManager().registerEvents(this, this);
    }
    @EventHandler
    public void jugadoresJoin(PlayerJoinEvent e) {
        state.add(e.getPlayer());
    }
    @EventHandler
    public void jugadoresBreak(BlockBreakEvent e){
        if (state.contains(e.getPlayer())){
            e.setCancelled(true);
        }
    }
    @EventHandler
    public void jugadoresBuild(BlockPlaceEvent e){
        if (state.contains(e.getPlayer())){
            e.setCancelled(true);
            e.getBlock().setType(Material.AIR);
            e.getBlockPlaced().setType(Material.AIR);
        }
    }
    @EventHandler
    public void jugadoresBuckets(PlayerInteractEvent e){
        if (state.contains(e.getPlayer())){
            if (e.getPlayer().getInventory().getItemInHand().getType().equals(Material.LAVA_BUCKET)){
                e.setCancelled(true);
                e.getPlayer().getInventory().getItemInHand().setType(Material.LAVA_BUCKET);
                return;
            }
            if (e.getPlayer().getInventory().getItemInHand().getType().equals(Material.WATER_BUCKET)) {
                e.setCancelled(true);
                e.getPlayer().getInventory().getItemInHand().setType(Material.WATER_BUCKET);
            }
        }
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String a, String[] arg) {
        int args = arg.length;
        if (!(sender instanceof Player)) {
            sender.sendMessage(this.c("&cSolo jugadores tienen acceso a este comando."));
            return true;
        }
        Player p = (Player) sender;
        if (!p.hasPermission("essentials.staff")){
            p.sendMessage(this.c("&cNo tienes permiso para este comando."));
            return true;
        }
        if (command.getName().equalsIgnoreCase("build")){
            if (args < 1){
                if (this.state.contains(p)){
                    this.state.remove(p);
                    p.sendMessage(c("&7Modo &2builder &7activado!"));
                    return true;
                } else {
                    this.state.add(p);
                    p.sendMessage(c("&7Modo &2builder &7desactivado!"));
                }
            } else {
                p.sendMessage(c("&cUso correcto: &e/build &7# Activar y desactivar el modo builder"));
            }
        }
        return false;
    }
}
