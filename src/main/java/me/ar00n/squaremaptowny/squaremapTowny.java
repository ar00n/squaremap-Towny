/*
 * Copyright (c) 2021 Silverwolfg11
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package me.ar00n.squaremaptowny;

import com.palmergames.bukkit.towny.Towny;
import me.ar00n.squaremaptowny.events.MapReloadEvent;
import me.ar00n.squaremaptowny.listeners.TownClaimListener;
import me.ar00n.squaremaptowny.managers.TownyLayerManager;
import me.ar00n.squaremaptowny.objects.MapConfig;
import me.ar00n.squaremaptowny.tasks.RenderTownsTask;
import org.bukkit.Bukkit;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.logging.Level;

public final class squaremapTowny extends JavaPlugin {

    private TownyLayerManager layerManager;
    private MapConfig config;

    @Override
    public void onEnable() {
        // Check that Towny is enabled
        // While the plugin.yml makes sure Towny is installed, Towny can be disabled
        if (!Bukkit.getPluginManager().isPluginEnabled("Towny")) {
            setEnabled(false);
            return;
        }

        // Plugin startup logic
        try {
            config = MapConfig.loadConfig(getDataFolder(), getLogger());
        } catch (IOException e) {
            // IOException caused by creating new file usually, so disabling is a valid option
            getLogger().log(Level.SEVERE, "Error loading config. Disabling plugin...", e);
            setEnabled(false);
            return;
        }

        // Load layer manager
        layerManager = new TownyLayerManager(this);

        // Register command
        TownyMapCommand commandExec = new TownyMapCommand(this);
        PluginCommand command = getCommand("squaremaptowny");
        command.setExecutor(commandExec);
        command.setTabCompleter(commandExec);

        // Register listeners
        Bukkit.getPluginManager().registerEvents(new TownClaimListener(this), this);

        // If towny is in safe-mode, do not attempt to render towns.
        if (!Towny.getPlugin().isError()) {
            // Schedule render task
            new RenderTownsTask(this).runTaskTimer(this, 0, config.getUpdatePeriod() * 20L * 60);
        }
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        if (layerManager != null)
            layerManager.close();
    }

    @NotNull
    public MapConfig config() {
        return config;
    }

    @NotNull
    public TownyLayerManager getLayerManager() {
        return layerManager;
    }

    public void reload() throws IOException {
        config = MapConfig.loadConfig(getDataFolder(), getLogger());
        Bukkit.getScheduler().cancelTasks(this);
        layerManager.close();
        layerManager = new TownyLayerManager(this);
        Bukkit.getPluginManager().callEvent(new MapReloadEvent()); // API Event
        new RenderTownsTask(this).runTaskTimer(this, 0, config.getUpdatePeriod() * 20L * 60);
    }

    public void async(Runnable run) {
        Bukkit.getScheduler().runTaskAsynchronously(this, run);
    }
}
