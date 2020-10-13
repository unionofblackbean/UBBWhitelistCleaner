package asia.ubb.whitelistcleaner.task;

import org.bukkit.OfflinePlayer;
import org.bukkit.plugin.Plugin;

import java.util.Date;
import java.util.Set;

public class CleanTask implements Runnable {

    private final Plugin plugin;
    private final long offlineBeforeClean;

    public CleanTask(Plugin plugin, long offlineBeforeClean) {
        this.plugin = plugin;
        this.offlineBeforeClean = offlineBeforeClean;
    }

    @Override
    public void run() {
        plugin.getLogger().info("----------------------------------------");

        boolean enabled = plugin.getConfig().getBoolean("enabled", false);
        if (enabled) {
            Date now = new Date();
            Set<OfflinePlayer> whitelistedPlayers = plugin.getServer().getWhitelistedPlayers();
            for (OfflinePlayer whitelistedPlayer : whitelistedPlayers) {
                long offlineTime = (now.getTime() - whitelistedPlayer.getLastPlayed()) / 1000 * 20;

                plugin.getLogger().info("Player cleaned from whitelist:");
                if (offlineTime >= offlineBeforeClean && !whitelistedPlayer.isOnline()) {
                    whitelistedPlayer.setWhitelisted(false);
                    plugin.getServer().reloadWhitelist();
                    plugin.getLogger().info(whitelistedPlayer.getName());
                }

            }
        } else {
            plugin.getLogger().info("Whitelist cleaner disabled.");
            plugin.getLogger().info("Clean skipped.");
        }

        plugin.getLogger().info("----------------------------------------");
    }

}
