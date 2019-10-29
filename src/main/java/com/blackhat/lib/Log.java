package com.blackhat.lib;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Log {
  private final Logger LOGGER;

  public Log(String className) {
      this.LOGGER = LogManager.getLogger(className);
  }

  public void debug(String message) {
    this.LOGGER.debug(message);
  }

  public void fatal(String message) {
    this.LOGGER.fatal(message);
  }

  public void info(String message) {
    this.LOGGER.info(message);
  }

  public void trace(String message) {
    this.LOGGER.trace(message);
  }

  public void warn(String message) {
    this.LOGGER.warn(message);
  }

  public void error(String message, Exception exception) {
    this.LOGGER.error(message, exception);
  }
       
 
}