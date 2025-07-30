package org.alpha_sync_java.infra;

import ch.qos.logback.classic.Level;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public interface
Loggable {
    default Logger logger(){
        Logger l = LoggerFactory.getLogger(this.getClass());
        if (l instanceof ch.qos.logback.classic.Logger) {
            ((ch.qos.logback.classic.Logger) l).setLevel(Level.DEBUG);
        }
        return l;
    }
}
