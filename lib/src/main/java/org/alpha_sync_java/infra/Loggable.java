package org.alpha_sync_java.infra;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public interface Loggable {
    default Logger logger(){
        return LoggerFactory.getLogger(this.getClass());
    }
}
