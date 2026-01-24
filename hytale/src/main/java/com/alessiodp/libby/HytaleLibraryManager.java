package com.alessiodp.libby;

import com.alessiodp.libby.classloader.URLClassLoaderHelper;
import com.alessiodp.libby.logging.adapters.FloggerAdapter;
import com.alessiodp.libby.logging.adapters.JDKLogAdapter;
import com.alessiodp.libby.logging.adapters.LogAdapter;
import com.hypixel.hytale.server.core.plugin.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.InputStream;
import java.net.URLClassLoader;
import java.nio.file.Path;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;

import static java.util.Objects.requireNonNull;

/**
 * A runtime dependency manager for Hytale plugins.
 */
public class HytaleLibraryManager extends LibraryManager {
    /**
     * Plugin classpath helper
     */
    @NotNull
    private final URLClassLoaderHelper classLoader;
    @NotNull
    private final JavaPlugin plugin;

    /**
     * Creates a new Hytale library manager.
     *
     * @param plugin the plugin to manage
     */
    public HytaleLibraryManager(@NotNull JavaPlugin plugin) {
        this(plugin, "lib");
    }

    /**
     * Creates a new Hytale library manager.
     *
     * @param plugin the plugin to manage
     * @param directoryName download directory name
     */
    public HytaleLibraryManager(@NotNull JavaPlugin plugin, @NotNull String directoryName) {
        this(plugin, directoryName, new FloggerAdapter(requireNonNull(plugin, "plugin").getLogger()));
    }
    
    /**
     * Creates a new Hytale library manager.
     *
     * @param plugin the plugin to manage
     * @param directoryName download directory name
     * @param logAdapter the log adapter to use
     */
    public HytaleLibraryManager(@NotNull JavaPlugin plugin, @NotNull String directoryName, @NotNull LogAdapter logAdapter) {
        super(logAdapter, plugin.getDataDirectory(), directoryName);
        classLoader = new URLClassLoaderHelper(plugin.getClassLoader(), this);
        this.plugin = plugin;
    }

    /**
     * Adds the HytaleModding repository.
     */
    public void addHytaleModding(){
        this.addRepository("https://maven.hytalemodding.dev/releases");
    }

    /**
     * Adds a file to the Hytale plugin's classpath.
     *
     * @param file the file to add
     */
    @Override
    protected void addToClasspath(@NotNull Path file) {
        classLoader.addToClasspath(file);
    }

    @Override
    protected InputStream getResourceAsStream(@NotNull String path) {
        //TODO - maybe there is a better way?
        try {
            JarFile jar = new JarFile(plugin.getFile().toFile());
            ZipEntry entry = jar.getEntry(path);

            if (entry == null) {
                jar.close();
                getLogger().error("Resource not found: " + path + " in " + plugin.getFile());
                return null;
            }
            return jar.getInputStream(entry);
        } catch (IOException e) {
            getLogger().error("Unable to load resource: " + path + " in " + plugin.getFile());
            return null;
        }
    }
}
