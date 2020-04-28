package xyz.lucasallegri.launcher;

import java.awt.Image;
import java.io.IOException;

import javax.swing.ImageIcon;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import mdlaf.MaterialLookAndFeel;
import mdlaf.themes.JMarsDarkTheme;
import mdlaf.themes.MaterialLiteTheme;
import xyz.lucasallegri.dialog.DialogError;
import xyz.lucasallegri.discord.DiscordInstance;
import xyz.lucasallegri.launcher.mods.ModList;
import xyz.lucasallegri.launcher.mods.ModLoader;
import xyz.lucasallegri.launcher.settings.Settings;
import xyz.lucasallegri.launcher.settings.SettingsProperties;
import xyz.lucasallegri.logging.KnightLog;
import xyz.lucasallegri.util.DesktopUtil;
import xyz.lucasallegri.util.FileUtil;
import xyz.lucasallegri.util.INetUtil;
import xyz.lucasallegri.util.ImageUtil;
import xyz.lucasallegri.util.SteamUtil;
import xyz.lucasallegri.util.SystemUtil;

public class Boot {
	
	public static void onBootStart() {
		
		checkStartLocation();
		setupHTTPSProtocol();
		
		try {
			KnightLog.setup();
			SettingsProperties.setup();
			SettingsProperties.loadFromProp();
		} catch (IOException ex) {
			KnightLog.logException(ex);
		}
		
		setupLauncherStyle();
		Language.setup();
		checkDirectories();
		checkShortcut();
		DiscordInstance.start();
		Fonts.setup();
	}
	
	public static void onBootEnd() {
		
		ModLoader.checkInstalled();
		if(Settings.doRebuilds && ModLoader.rebuildFiles) ModLoader.startFileRebuild();
		
		DiscordInstance.setPresence(Language.getValue("presence.launch_ready", String.valueOf(ModList.installedMods.size())));
		
		loadOnlineAssets();
	}
	
	private static void checkDirectories() {
		FileUtil.createDir("mods");
		FileUtil.createDir("KnightLauncher/logs/");
	}
	
	/*
	 * Checking if we're being ran inside the game's directory, "getdown.txt" should always be present if so.
	 */
	private static void checkStartLocation() {
		if(!FileUtil.fileExists("getdown-pro.jar")) {
			DialogError.push("You need to place this .jar inside your Spiral Knights main directory."
					+ System.lineSeparator() + SteamUtil.getGamePathWindows());
			DesktopUtil.openDir(SteamUtil.getGamePathWindows());
			System.exit(1);
		}
	}
	
	/*
	 * Create a shortcut to the application if there's none.
	 */
	private static void checkShortcut() {
		if(SystemUtil.isWindows() && Settings.createShortcut
				&& !FileUtil.fileExists(DesktopUtil.getPathToDesktop() + "/" + LauncherConstants.LNK_FILE_NAME)) {
			
			DesktopUtil.createShellLink(System.getProperty("java.home") + "\\bin\\javaw.exe", 
										"-jar \"" + LauncherConstants.USER_DIR + "\\KnightLauncher.jar\"", 
										LauncherConstants.USER_DIR, 
										LauncherConstants.USER_DIR + "\\icon-128.ico", 
										"Start KnightLauncher", 
										LauncherConstants.LNK_FILE_NAME
			);
		}
	}
	
	private static void checkVersion() {
		String latestVer = INetUtil.getWebpageContent(LauncherConstants.VERSION_QUERY_URL);
		if(latestVer == null) {
			Settings.offlineMode = true;
		} else if (!latestVer.equalsIgnoreCase(LauncherConstants.VERSION)) {
			Settings.isOutdated = true;
			LauncherGUI.updateButton.setVisible(true);
		}
	}
	
	private static void setupLauncherStyle() {
		try {
			UIManager.setLookAndFeel(new MaterialLookAndFeel());
			
			switch(Settings.launcherStyle) {
			case "dark":
				MaterialLookAndFeel.changeTheme(new JMarsDarkTheme());
				break;
			case "light":
				MaterialLookAndFeel.changeTheme(new MaterialLiteTheme());
				break;
			default:
				MaterialLookAndFeel.changeTheme(new MaterialLiteTheme());
				break;
			}
		} catch (UnsupportedLookAndFeelException e) {
			KnightLog.logException(e);
		}
	}
	
	private static void setupHTTPSProtocol() {
		System.setProperty("https.protocols", "TLSv1,TLSv1.1,TLSv1.2");
		System.setProperty("http.agent", "Mozilla/5.0");
		System.setProperty("https.agent", "Mozilla/5.0");
	}
	
	private static void loadOnlineAssets() {
		Thread oassetsThread = new Thread(new Runnable() {
			public void run() {
				
				checkVersion();
				
				if(Settings.offlineMode) {
					LauncherGUI.tweetsContainer.setText(Language.getValue("error.tweets_retrieve"));
					LauncherGUI.playerCountLabel.setText(Language.getValue("error.get_player_count"));
					LauncherGUI.imageContainer.setText(Language.getValue("error.event_image_missing"));
				}
				
				String tweets = INetUtil.getWebpageContent(LauncherConstants.TWEETS_URL);
				String styledTweets = tweets.replaceFirst("FONT_FAMILY", LauncherGUI.tweetsContainer.getFont().getFamily())
											.replaceFirst("COLOR", Settings.launcherStyle.equals("dark") ? "#ffffff" : "#000000");
				LauncherGUI.tweetsContainer.setText(styledTweets);
				LauncherGUI.tweetsContainer.setCaretPosition(0);
				
				LauncherGUI.playerCountLabel.setText(Language.getValue("m.player_count", new String[] { SteamUtil.getCurrentPlayersApproximateTotal("99900"), SteamUtil.getCurrentPlayers("99900") }));
				
				String eventImageLang = Settings.lang.startsWith("es") ? "es" : "en";
				Image eventImage = ImageUtil.getImageFromURL(LauncherConstants.EVENT_QUERY_URL + eventImageLang + ".png", 515, 300);
				LauncherGUI.imageContainer.setIcon(new ImageIcon(eventImage));
			}
		});
		oassetsThread.start();
	}

}
