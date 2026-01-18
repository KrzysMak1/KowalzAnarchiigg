package cc.dreamcode.kowal.libs.cc.dreamcode.platform.bukkit;

import lombok.Generated;
import java.util.logging.Level;
import java.util.logging.Logger;
import cc.dreamcode.kowal.libs.cc.dreamcode.platform.DreamLogger;

public class DreamBukkitLogger implements DreamLogger
{
    private final Logger logger;
    
    @Override
    public void info(final String text) {
        this.logger.info(text);
    }
    
    @Override
    public void debug(final String text) {
        this.logger.info("[DEBUG] " + text);
    }
    
    @Override
    public void warning(final String text) {
        this.logger.warning(text);
    }
    
    @Override
    public void error(final String text) {
        this.logger.severe(text);
    }
    
    @Override
    public void error(final String text, final Throwable throwable) {
        this.logger.log(Level.SEVERE, text, throwable);
    }
    
    @Generated
    public DreamBukkitLogger(final Logger logger) {
        this.logger = logger;
    }
}
