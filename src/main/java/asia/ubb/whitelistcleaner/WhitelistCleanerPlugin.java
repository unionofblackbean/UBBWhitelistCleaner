package asia.ubb.whitelistcleaner;

import asia.ubb.whitelistcleaner.task.CleanTask;
import asia.ubb.whitelistcleaner.utils.TicksUtils;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;

public class WhitelistCleanerPlugin extends JavaPlugin {

    private final File logFile = new File(getDataFolder(), "log.txt");

    private void saveLog() {
        if (!logFile.exists()) {
            try {
                logFile.createNewFile();
            } catch (IOException e) {
                getLogger().log(Level.SEVERE, "Could not save " + logFile.getName() + ".", e);
            }
        } else {
            getLogger().warning("Could not save " + logFile.getName() + " because " + logFile.getName() + " already exists.");
        }
    }

    @Override
    public void onEnable() {
        saveDefaultConfig();
        saveLog();

        String cleanIntervalStr = getConfig().getString("clean-interval");
        String offlineBeforeCleanStr = getConfig().getString("offline-before-clean");
        if (cleanIntervalStr != null && offlineBeforeCleanStr != null) {
            long cleanInterval = TicksUtils.parseTicks(cleanIntervalStr);
            long offlineBeforeClean = TicksUtils.parseTicks(offlineBeforeCleanStr);
            getLogger().info("Clean Interval (Seconds):         " + cleanInterval / 20);
            getLogger().info("Offline Before Clean (Seconds):   " + offlineBeforeClean / 20);

            BukkitScheduler scheduler = getServer().getScheduler();
            scheduler.scheduleSyncRepeatingTask(
                    this, new CleanTask(this, offlineBeforeClean, logFile),
                    0L, cleanInterval);
        } else
            throw new IllegalArgumentException("Option clean-interval or offline-before-clean is not found in config.yml.");
    }

}
