package com.example.quality.refactoring.after;

import java.util.Locale;

/** After "Introduce Explaining Variable": each condition gets a name. */
public class BrowserCheck {

  private final boolean initialized;

  /** Creates the check. */
  public BrowserCheck(boolean initialized) {
    this.initialized = initialized;
  }

  /** Returns true when the special-case code path runs. */
  public boolean shouldApplyWorkaround(String platform, String browser, int resize) {
    final boolean isMacOs = platform.toUpperCase(Locale.ROOT).indexOf("MAC") > -1;
    final boolean isIeBrowser = browser.toUpperCase(Locale.ROOT).indexOf("IE") > -1;
    final boolean wasResized = resize > 0;

    return isMacOs && isIeBrowser && wasInitialized() && wasResized;
  }

  private boolean wasInitialized() {
    return initialized;
  }
}
