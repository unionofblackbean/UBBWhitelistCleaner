package asia.ubb.ubbwhitelistcleaner;

import asia.ubb.ubbwhitelistcleaner.tasks.CleanTask;
import asia.ubb.ubbwhitelistcleaner.utils.TicksUtils;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;

public class UBBWhitelistCleanerPlugin extends JavaPlugin {

    private final File logFile = new File(getDataFolder(), getConfig().getString("logging.file.filename", "log.txt"));

    private void saveLog() {
        try {
            if (!logFile.createNewFile())
                getLogger().warning("Could not save " + logFile.getName() + " because " + logFile.getName() + " already exists.");
        } catch (IOException e) {
            getLogger().log(Level.SEVERE, "Could not save " + logFile.getName() + ".", e);
        }
    }

    @Override
    public void onEnable() {
        // save default config
        saveDefaultConfig();
        // save cleaner log file
        saveLog();

        // get clean interval from config
        long cleanInterval = TicksUtils.parseTicks(
                getConfig().getString("cleaner.interval", "1d"));
        // get offline allowance from config
        long offlineAllowance = TicksUtils.parseTicks(
                getConfig().getString("cleaner.offline-allowance", "3M"));
        // print clean interval and offline allowance to console
        getLogger().info("Clean Interval (Seconds):         " + cleanInterval / 20);
        getLogger().info("Offline Before Clean (Seconds):   " + offlineAllowance / 20);

        // schedule clean whitelist task
        getServer().getScheduler().scheduleSyncRepeatingTask(
                this, new CleanTask(this, offlineAllowance, logFile),
                0L, cleanInterval);
    }

}
