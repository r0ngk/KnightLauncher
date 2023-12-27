package com.lucasallegri.launcher;

import com.lucasallegri.launcher.settings.Settings;

import java.awt.*;
import java.io.IOException;
import java.io.InputStream;

import static com.lucasallegri.launcher.Log.log;

public class Fonts {

  private static final String fontPathRegular = "/fonts/Figtree-Regular.ttf";
  private static final String fontPathMedium = "/fonts/Figtree-SemiBold.ttf";
  private static final String fontPathRegularJP = "/fonts/NotoSansJP-Regular.otf";
  private static final String fontPathMediumJP = "/fonts/NotoSansJP-Medium.otf";
  private static final String fontPathRegularAR = "/fonts/Lemonada-Regular.ttf";
  private static final String fontPathMediumAR = "/fonts/Lemonada-Medium.ttf";
  private static final String fontPathCodeRegular = "/fonts/SourceCodePro-Regular.ttf";
  public static Font fontRegSmall = null;
  public static Font fontReg = null;
  public static Font fontRegBig = null;
  public static Font fontMed = null;
  public static Font fontMedIta = null;
  public static Font fontMedBig = null;
  public static Font fontMedGiant = null;
  public static Font fontCodeReg = null;
  private static float sizeMultiplier = 1.0f;

  public static void setup() {

    InputStream fontRegSmallIs;
    InputStream fontRegIs;
    InputStream fontRegBigIs;
    InputStream fontMedIs;
    InputStream fontMedItaIs;
    InputStream fontMedBigIs;
    InputStream fontMedGiantIs;
    InputStream fontCodeRegIs;

    if (Settings.lang.equalsIgnoreCase("jp")) {
      fontRegSmallIs = LauncherGUI.class.getResourceAsStream(fontPathRegularJP);
      fontRegIs = LauncherGUI.class.getResourceAsStream(fontPathRegularJP);
      fontRegBigIs = LauncherGUI.class.getResourceAsStream(fontPathRegularJP);
      fontMedIs = LauncherGUI.class.getResourceAsStream(fontPathMediumJP);
      fontMedItaIs = LauncherGUI.class.getResourceAsStream(fontPathMediumJP);
      fontMedBigIs = LauncherGUI.class.getResourceAsStream(fontPathMediumJP);
      fontMedGiantIs = LauncherGUI.class.getResourceAsStream(fontPathMediumJP);
      sizeMultiplier = 1.1f;
    } else if (Settings.lang.equalsIgnoreCase("ar")) {
      fontRegSmallIs = LauncherGUI.class.getResourceAsStream(fontPathRegularAR);
      fontRegIs = LauncherGUI.class.getResourceAsStream(fontPathRegularAR);
      fontRegBigIs = LauncherGUI.class.getResourceAsStream(fontPathRegularAR);
      fontMedIs = LauncherGUI.class.getResourceAsStream(fontPathMediumAR);
      fontMedItaIs = LauncherGUI.class.getResourceAsStream(fontPathMediumAR);
      fontMedBigIs = LauncherGUI.class.getResourceAsStream(fontPathMediumAR);
      fontMedGiantIs = LauncherGUI.class.getResourceAsStream(fontPathMediumAR);
    } else if (Settings.lang.equalsIgnoreCase("zh-hans")
        || Settings.lang.equalsIgnoreCase("zh-hant")) {
      // Use OS default fonts
      fontRegSmall = new Font(null);
      fontReg = new Font(null);
      fontRegBig = new Font(null);
      fontMed = new Font(null);
      fontMedIta = new Font(null);
      fontMedBig = new Font(null);
      fontMedGiant = new Font(null);
      return;
    } else {
      fontRegSmallIs = LauncherGUI.class.getResourceAsStream(fontPathRegular);
      fontRegIs = LauncherGUI.class.getResourceAsStream(fontPathRegular);
      fontRegBigIs = LauncherGUI.class.getResourceAsStream(fontPathRegular);
      fontMedIs = LauncherGUI.class.getResourceAsStream(fontPathMedium);
      fontMedItaIs = LauncherGUI.class.getResourceAsStream(fontPathMedium);
      fontMedBigIs = LauncherGUI.class.getResourceAsStream(fontPathMedium);
      fontMedGiantIs = LauncherGUI.class.getResourceAsStream(fontPathMedium);
      sizeMultiplier = 1.2f;
    }

    fontCodeRegIs = LauncherGUI.class.getResourceAsStream(fontPathCodeRegular);

    try {

      fontRegSmall = Font.createFont(Font.TRUETYPE_FONT, fontRegSmallIs);
      fontRegSmall = fontRegSmall.deriveFont(9.0f * sizeMultiplier);
      fontRegSmall = fontRegSmall.deriveFont(Font.ITALIC);

      fontReg = Font.createFont(Font.TRUETYPE_FONT, fontRegIs);
      fontReg = fontReg.deriveFont(11.0f * sizeMultiplier);
      fontReg = fontReg.deriveFont(Font.ITALIC);

      fontRegBig = Font.createFont(Font.TRUETYPE_FONT, fontRegBigIs);
      fontRegBig = fontRegBig.deriveFont(14.0f * sizeMultiplier);
      fontRegBig = fontRegBig.deriveFont(Font.ITALIC);

      fontMed = Font.createFont(Font.TRUETYPE_FONT, fontMedIs);
      fontMed = fontMed.deriveFont(11.0f * sizeMultiplier);

      fontMedIta = Font.createFont(Font.TRUETYPE_FONT, fontMedItaIs);
      fontMedIta = fontMedIta.deriveFont(11.0f * sizeMultiplier);
      fontMedIta = fontMedIta.deriveFont(Font.ITALIC);

      fontMedBig = Font.createFont(Font.TRUETYPE_FONT, fontMedBigIs);
      fontMedBig = fontMedBig.deriveFont(14.0f * sizeMultiplier);

      fontMedGiant = Font.createFont(Font.TRUETYPE_FONT, fontMedGiantIs);
      fontMedGiant = fontMedGiant.deriveFont(40.0f * sizeMultiplier);

      fontCodeReg = Font.createFont(Font.TRUETYPE_FONT, fontCodeRegIs);
      fontCodeReg = fontCodeReg.deriveFont(12.0f * sizeMultiplier);

    } catch (FontFormatException | IOException e) {
      log.error(e);
    }

  }

}