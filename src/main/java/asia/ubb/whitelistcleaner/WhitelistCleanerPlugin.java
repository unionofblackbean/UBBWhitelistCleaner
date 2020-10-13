package asia.ubb.whitelistcleaner;

import asia.ubb.whitelistcleaner.task.CleanTask;
import asia.ubb.whitelistcleaner.utils.TicksUtils;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;

import java.io.File;

public class WhitelistCleanerPlugin extends JavaPlugin {

    @Override
    public void onEnable() {
        saveDefaultConfig();

        String cleanIntervalStr = getConfig().getString("clean-interval");
        String offlineBeforeCleanStr = getConfig().getString("offline-before-clean");
        if (cleanIntervalStr != null && offlineBeforeCleanStr != null) {
            long cleanInterval = TicksUtils.parseTicks(cleanIntervalStr);
            long offlineBeforeClean = TicksUtils.parseTicks(offlineBeforeCleanStr);
            getLogger().info("Clean Interval (Seconds):         " + cleanInterval / 20);
            getLogger().info("Offline Before Clean (Seconds):   " + offlineBeforeClean / 20);

            BukkitScheduler scheduler = getServer().getScheduler();
            scheduler.scheduleSyncRepeatingTask(
                    this, new CleanTask(this, offlineBeforeClean),
                    0L, cleanInterval);
        } else
            throw new IllegalArgumentException("Option clean-interval or offline-before-clean is not found in config.yml.");
    }

}
