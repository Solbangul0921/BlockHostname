package xyz.solbangul.blockhostname;

import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;
import java.util.List;

public class BlockHostname extends JavaPlugin implements Listener {

    private final MiniMessage miniMessage = MiniMessage.miniMessage();

    private List<String> blockHostnames;
    private boolean isWhitelist = false;
    private List<String> whitelistHostnames;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        getServer().getPluginManager().registerEvents(this, this);
        loadConfigValues();
        getLogger().info("Block Hostname is " + Arrays.toString(blockHostnames.toArray()));
        if (isWhitelist) {
            getLogger().info("Whitelist Hostname is " + Arrays.toString(whitelistHostnames.toArray()));
        }
    }

    @Override
    public void onDisable() {
        saveConfig();
    }

    private void loadConfigValues() {
        blockHostnames = getConfig().getStringList("block-hostname");
        isWhitelist = getConfig().getBoolean("enable-whitelist", false);
        whitelistHostnames = getConfig().getStringList("whitelist-hostname");
    }

    @EventHandler
    public void onLogin(PlayerLoginEvent event) {
        String hostname = event.getHostname().toLowerCase();
        if (shouldBlock(hostname)) {
            event.disallow(PlayerLoginEvent.Result.KICK_OTHER, miniMessage.deserialize("<red>허용되지 않는 도메인으로 접속하실 수 없습니다."));
        }
    }

    private boolean shouldBlock(String hostname) {
        if (blockHostnames.contains(hostname)) {
            return true;
        }
        return isWhitelist && !whitelistHostnames.contains(hostname);
    }
}