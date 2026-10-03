package com.example.quality.refactoring.before;

/** Before "Introduce Explaining Variable". */
public class BrowserCheck {

  private final boolean initialized;

  /** Creates the check. */
  public BrowserCheck(boolean initialized) {
    this.initialized = initialized;
  }

  /** Returns true when the special-case code path runs. */
  public boolean shouldApplyWorkaround(String platform, String browser, int resize) {
    if ((platform.toUpperCase().indexOf("MAC") > -1)
        && (browser.toUpperCase().indexOf("IE") > -1)
        && wasInitialized() && resize > 0) {
      // do something
      return true;
    }
    return false;
  }

  private boolean wasInitialized() {
    return initialized;
  }
}
