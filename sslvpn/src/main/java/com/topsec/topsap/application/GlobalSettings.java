package com.topsec.topsap.application;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;

public class GlobalSettings {
    private static final String PERF_COLORS = "bookmark.colors";
    private static final String PERF_COLORS_3G = "bookmark.colors_3g";
    private static final String PERF_DESKTOP_COMPOSITION = "bookmark.perf_desktop_composition";
    private static final String PERF_DESKTOP_COMPOSITION_3G = "bookmark.perf_desktop_composition_3g";
    private static final String PERF_ENABLE_3G_SETTINGS = "bookmark.enable_3g_settings";
    private static final String PERF_FONT_SMOOTHING = "bookmark.perf_font_smoothing";
    private static final String PERF_FONT_SMOOTHING_3G = "bookmark.perf_font_smoothing_3g";
    private static final String PERF_HEIGHT = "bookmark.height";
    private static final String PERF_HEIGHT_3G = "bookmark.height_3g";
    private static final String PERF_MENU_ANIMATION = "bookmark.perf_menu_animation";
    private static final String PERF_MENU_ANIMATION_3G = "bookmark.perf_menu_animation_3g";
    private static final String PERF_REMOTEFX = "bookmark.perf_remotefx";
    private static final String PERF_REMOTEFX_3G = "bookmark.perf_remotefx_3g";
    private static final String PERF_RESOLUTION = "bookmark.resolution";
    private static final String PERF_RESOLUTION_3G = "bookmark.resolution_3g";
    private static final String PERF_SECURITY = "bookmark.security";
    private static final String PERF_THEMES = "bookmark.perf_themes";
    private static final String PERF_THEMES_3G = "bookmark.perf_themes_3g";
    private static final String PERF_WALLPAPER = "bookmark.perf_wallpaper";
    private static final String PERF_WALLPAPER_3G = "bookmark.perf_wallpaper_3g";
    private static final String PERF_WIDTH = "bookmark.width";
    private static final String PERF_WIDTH_3G = "bookmark.width_3g";
    private static final String PERF_WINDOW_DRAGGING = "bookmark.perf_window_dragging";
    private static final String PERF_WINDOW_DRAGGING_3G = "bookmark.perf_window_dragging_3g";
    private static final String PREF_CONSOLE_MODE = "bookmark.console_mode";
    private static final String PREF_POWER_DISCONNECTTIMEOUT = "power.disconnect_timeout";
    private static final String PREF_SECURITY_ACCEPTALLCERTIFICATES = "security.accept_certificates";
    private static final String PREF_UI_ASKONEXIT = "ui.ask_on_exit";
    private static final String PREF_UI_AUTOSCROLLTOUCHPOINTER = "ui.auto_scroll_touchpointer";
    private static final String PREF_UI_HIDESTATUSBAR = "ui.hide_status_bar";
    private static final String PREF_UI_HIDEZOOMCONTROLS = "ui.hide_zoom_controls";
    private static final String PREF_UI_INVERTSCROLLING = "ui.invert_scrolling";
    private static final String PREF_UI_SWAPMOUSEBUTTONS = "ui.swap_mouse_buttons";
    private static SharedPreferences settings;

    public static void init(Context context) {
        settings = PreferenceManager.getDefaultSharedPreferences(context);
        initValues();
    }

    private static void initValues() {
        Editor editor = settings.edit();
        if (!settings.contains(PREF_UI_HIDESTATUSBAR)) {
            editor.putBoolean(PREF_UI_HIDESTATUSBAR, false);
        }
        if (!settings.contains(PREF_UI_HIDEZOOMCONTROLS)) {
            editor.putBoolean(PREF_UI_HIDEZOOMCONTROLS, true);
        }
        if (!settings.contains(PREF_UI_SWAPMOUSEBUTTONS)) {
            editor.putBoolean(PREF_UI_SWAPMOUSEBUTTONS, false);
        }
        if (!settings.contains(PREF_UI_INVERTSCROLLING)) {
            editor.putBoolean(PREF_UI_INVERTSCROLLING, false);
        }
        if (!settings.contains(PREF_UI_ASKONEXIT)) {
            editor.putBoolean(PREF_UI_ASKONEXIT, true);
        }
        if (!settings.contains(PREF_UI_AUTOSCROLLTOUCHPOINTER)) {
            editor.putBoolean(PREF_UI_AUTOSCROLLTOUCHPOINTER, true);
        }
        if (!settings.contains(PREF_POWER_DISCONNECTTIMEOUT)) {
            editor.putInt(PREF_POWER_DISCONNECTTIMEOUT, 5);
        }
        if (!settings.contains(PREF_SECURITY_ACCEPTALLCERTIFICATES)) {
            editor.putBoolean(PREF_SECURITY_ACCEPTALLCERTIFICATES, true);
        }
        if (!settings.contains(PREF_CONSOLE_MODE)) {
            editor.putBoolean(PREF_CONSOLE_MODE, false);
        }
        if (!settings.contains(PERF_REMOTEFX)) {
            editor.putBoolean(PERF_REMOTEFX, false);
        }
        if (!settings.contains(PERF_WALLPAPER)) {
            editor.putBoolean(PERF_WALLPAPER, false);
        }
        if (!settings.contains(PERF_FONT_SMOOTHING)) {
            editor.putBoolean(PERF_FONT_SMOOTHING, false);
        }
        if (!settings.contains(PERF_DESKTOP_COMPOSITION)) {
            editor.putBoolean(PERF_DESKTOP_COMPOSITION, false);
        }
        if (!settings.contains(PERF_WINDOW_DRAGGING)) {
            editor.putBoolean(PERF_WINDOW_DRAGGING, false);
        }
        if (!settings.contains(PERF_MENU_ANIMATION)) {
            editor.putBoolean(PERF_MENU_ANIMATION, false);
        }
        if (!settings.contains(PERF_THEMES)) {
            editor.putBoolean(PERF_THEMES, false);
        }
        if (!settings.contains(PERF_REMOTEFX_3G)) {
            editor.putBoolean(PERF_REMOTEFX_3G, false);
        }
        if (!settings.contains(PERF_WALLPAPER_3G)) {
            editor.putBoolean(PERF_WALLPAPER_3G, false);
        }
        if (!settings.contains(PERF_FONT_SMOOTHING_3G)) {
            editor.putBoolean(PERF_FONT_SMOOTHING_3G, false);
        }
        if (!settings.contains(PERF_DESKTOP_COMPOSITION_3G)) {
            editor.putBoolean(PERF_DESKTOP_COMPOSITION_3G, false);
        }
        if (!settings.contains(PERF_WINDOW_DRAGGING_3G)) {
            editor.putBoolean(PERF_WINDOW_DRAGGING_3G, false);
        }
        if (!settings.contains(PERF_MENU_ANIMATION_3G)) {
            editor.putBoolean(PERF_MENU_ANIMATION_3G, false);
        }
        if (!settings.contains(PERF_THEMES_3G)) {
            editor.putBoolean(PERF_THEMES_3G, false);
        }
        if (!settings.contains(PERF_ENABLE_3G_SETTINGS)) {
            editor.putBoolean(PERF_ENABLE_3G_SETTINGS, false);
        }
        if (!settings.contains(PERF_COLORS)) {
            editor.putInt(PERF_COLORS, 16);
        }
        if (!settings.contains(PERF_RESOLUTION)) {
            editor.putString(PERF_RESOLUTION, "automatic");
        }
        if (!settings.contains(PERF_WIDTH)) {
            editor.putInt(PERF_WIDTH, 800);
        }
        if (!settings.contains(PERF_HEIGHT)) {
            editor.putInt(PERF_HEIGHT, 1205);
        }
        if (!settings.contains(PERF_RESOLUTION_3G)) {
            editor.putString(PERF_RESOLUTION_3G, "automatic");
        }
        if (!settings.contains(PERF_WIDTH_3G)) {
            editor.putInt(PERF_WIDTH_3G, 800);
        }
        if (!settings.contains(PERF_HEIGHT_3G)) {
            editor.putInt(PERF_HEIGHT_3G, 1205);
        }
        if (!settings.contains(PERF_COLORS_3G)) {
            editor.putInt(PERF_COLORS_3G, 16);
        }
        if (!settings.contains(PERF_SECURITY)) {
            editor.putInt(PERF_SECURITY, 0);
        }
        editor.commit();
    }

    public static void setHideStatusBar(boolean hide) {
        settings.edit().putBoolean(PREF_UI_HIDESTATUSBAR, hide).commit();
    }

    public static boolean getHideStatusBar() {
        return settings.getBoolean(PREF_UI_HIDESTATUSBAR, false);
    }

    public static void setHideZoomControls(boolean hide) {
        settings.edit().putBoolean(PREF_UI_HIDEZOOMCONTROLS, hide).commit();
    }

    public static boolean getHideZoomControls() {
        return settings.getBoolean(PREF_UI_HIDEZOOMCONTROLS, true);
    }

    public static void setSwapMouseButtons(boolean swap) {
        settings.edit().putBoolean(PREF_UI_SWAPMOUSEBUTTONS, swap).commit();
    }

    public static boolean getSwapMouseButtons() {
        return settings.getBoolean(PREF_UI_SWAPMOUSEBUTTONS, false);
    }

    public static void setInvertScrolling(boolean invert) {
        settings.edit().putBoolean(PREF_UI_INVERTSCROLLING, invert).commit();
    }

    public static boolean getInvertScrolling() {
        return settings.getBoolean(PREF_UI_INVERTSCROLLING, false);
    }

    public static void setAskOnExit(boolean ask) {
        settings.edit().putBoolean(PREF_UI_ASKONEXIT, ask).commit();
    }

    public static boolean getAskOnExit() {
        return settings.getBoolean(PREF_UI_ASKONEXIT, true);
    }

    public static void setAutoScrollTouchPointer(boolean scroll) {
        settings.edit().putBoolean(PREF_UI_AUTOSCROLLTOUCHPOINTER, scroll).commit();
    }

    public static boolean getAutoScrollTouchPointer() {
        return settings.getBoolean(PREF_UI_AUTOSCROLLTOUCHPOINTER, true);
    }

    public static void setAcceptAllCertificates(boolean accept) {
        settings.edit().putBoolean(PREF_SECURITY_ACCEPTALLCERTIFICATES, accept).commit();
    }

    public static boolean getAcceptAllCertificates() {
        return settings.getBoolean(PREF_SECURITY_ACCEPTALLCERTIFICATES, false);
    }

    public static void setDisconnectTimeout(int timeoutMinutes) {
        settings.edit().putInt(PREF_POWER_DISCONNECTTIMEOUT, timeoutMinutes).commit();
    }

    public static int getDisconnectTimeout() {
        return settings.getInt(PREF_POWER_DISCONNECTTIMEOUT, 5);
    }

    public static void setConsoleMode(boolean console_mode) {
        settings.edit().putBoolean(PREF_CONSOLE_MODE, console_mode).commit();
    }

    public static boolean getConsoleMode() {
        return settings.getBoolean(PREF_CONSOLE_MODE, false);
    }

    public static void setPerf_remotefx_3G(boolean perf_remotefx) {
        settings.edit().putBoolean(PERF_REMOTEFX_3G, perf_remotefx).commit();
    }

    public static boolean getPerf_remotefx_3G() {
        return settings.getBoolean(PERF_REMOTEFX_3G, false);
    }

    public static void setPerf_wallpaper_3G(boolean perf_wallpaper) {
        settings.edit().putBoolean(PERF_WALLPAPER_3G, perf_wallpaper).commit();
    }

    public static boolean getPerf_wallpaper_3G() {
        return settings.getBoolean(PERF_WALLPAPER_3G, false);
    }

    public static void setPerf_font_smoothing_3G(boolean perf_font_smoothing) {
        settings.edit().putBoolean(PERF_FONT_SMOOTHING_3G, perf_font_smoothing).commit();
    }

    public static boolean getPerf_font_smoothing_3G() {
        return settings.getBoolean(PERF_FONT_SMOOTHING_3G, false);
    }

    public static void setPerf_desktop_composition_3G(boolean perf_desktop_composition) {
        settings.edit().putBoolean(PERF_DESKTOP_COMPOSITION_3G, perf_desktop_composition).commit();
    }

    public static boolean getPerf_desktop_composition_3G() {
        return settings.getBoolean(PERF_DESKTOP_COMPOSITION_3G, false);
    }

    public static void setPerf_window_dragging_3G(boolean perf_window_dragging) {
        settings.edit().putBoolean(PERF_WINDOW_DRAGGING_3G, perf_window_dragging).commit();
    }

    public static boolean getPerf_window_dragging_3G() {
        return settings.getBoolean(PERF_WINDOW_DRAGGING_3G, false);
    }

    public static void setPerf_menu_animation_3G(boolean perf_menu_animation) {
        settings.edit().putBoolean(PERF_MENU_ANIMATION_3G, perf_menu_animation).commit();
    }

    public static boolean getPerf_menu_animation_3G() {
        return settings.getBoolean(PERF_MENU_ANIMATION_3G, false);
    }

    public static void setPerfThemes_3G(boolean perf_themes) {
        settings.edit().putBoolean(PERF_THEMES_3G, perf_themes).commit();
    }

    public static boolean getPerfThemes_3G() {
        return settings.getBoolean(PERF_THEMES_3G, false);
    }

    public static void setPerf_remotefx(boolean perf_remotefx) {
        settings.edit().putBoolean(PERF_REMOTEFX, perf_remotefx).commit();
    }

    public static boolean getPerf_remotefx() {
        return settings.getBoolean(PERF_REMOTEFX, false);
    }

    public static void setPerf_wallpaper(boolean perf_wallpaper) {
        settings.edit().putBoolean(PERF_WALLPAPER, perf_wallpaper).commit();
    }

    public static boolean getPerf_wallpaper() {
        return settings.getBoolean(PERF_WALLPAPER, false);
    }

    public static void setPerf_font_smoothing(boolean perf_font_smoothing) {
        settings.edit().putBoolean(PERF_FONT_SMOOTHING, perf_font_smoothing).commit();
    }

    public static boolean getPerf_font_smoothing() {
        return settings.getBoolean(PERF_FONT_SMOOTHING, false);
    }

    public static void setPerf_desktop_composition(boolean perf_desktop_composition) {
        settings.edit().putBoolean(PERF_DESKTOP_COMPOSITION, perf_desktop_composition).commit();
    }

    public static boolean getPerf_desktop_composition() {
        return settings.getBoolean(PERF_DESKTOP_COMPOSITION, false);
    }

    public static void setPerf_window_dragging(boolean perf_window_dragging) {
        settings.edit().putBoolean(PERF_WINDOW_DRAGGING, perf_window_dragging).commit();
    }

    public static boolean getPerf_window_dragging() {
        return settings.getBoolean(PERF_WINDOW_DRAGGING, false);
    }

    public static void setPerf_menu_animation(boolean perf_menu_animation) {
        settings.edit().putBoolean(PERF_MENU_ANIMATION, perf_menu_animation).commit();
    }

    public static boolean getPerf_menu_animation() {
        return settings.getBoolean(PERF_MENU_ANIMATION, false);
    }

    public static void setPerfThemes(boolean perf_themes) {
        settings.edit().putBoolean(PERF_THEMES, perf_themes).commit();
    }

    public static boolean getPerfThemes() {
        return settings.getBoolean(PERF_THEMES, false);
    }

    public static void setEnabled_3g_setting(boolean Enabled_3g_setting) {
        settings.edit().putBoolean(PERF_ENABLE_3G_SETTINGS, Enabled_3g_setting).commit();
    }

    public static boolean getEnabled_3g_setting() {
        return settings.getBoolean(PERF_ENABLE_3G_SETTINGS, false);
    }

    public static void setColors(int colors) {
        settings.edit().putInt(PERF_COLORS, colors).commit();
    }

    public static int getColors() {
        return settings.getInt(PERF_COLORS, 16);
    }

    public static void setWidth(int width) {
        settings.edit().putInt(PERF_WIDTH, width).commit();
    }

    public static int getWidth() {
        return settings.getInt(PERF_WIDTH, 800);
    }

    public static void setHeight(int height) {
        settings.edit().putInt(PERF_HEIGHT, height).commit();
    }

    public static int getHeight() {
        return settings.getInt(PERF_HEIGHT, 1205);
    }

    public static void setWidth_3G(int width) {
        settings.edit().putInt(PERF_WIDTH_3G, width).commit();
    }

    public static int getWidth_3G() {
        return settings.getInt(PERF_WIDTH_3G, 800);
    }

    public static void setHeight_3G(int height) {
        settings.edit().putInt(PERF_HEIGHT_3G, height).commit();
    }

    public static int getHeight_3G() {
        return settings.getInt(PERF_HEIGHT_3G, 1205);
    }

    public static void setColors_3g(int colors) {
        settings.edit().putInt(PERF_COLORS_3G, colors).commit();
    }

    public static int getColors_3g() {
        return settings.getInt(PERF_COLORS_3G, 16);
    }

    public static void setResolution_3G(String resolution) {
        settings.edit().putString(PERF_RESOLUTION_3G, resolution).commit();
    }

    public static String getResolution_3G() {
        return settings.getString(PERF_RESOLUTION_3G, "custom");
    }

    public static void setResolution(String resolution) {
        settings.edit().putString(PERF_RESOLUTION, resolution).commit();
    }

    public static String getResolution() {
        return settings.getString(PERF_RESOLUTION, "custom");
    }

    public static void setsecurity(int security) {
        settings.edit().putInt(PERF_SECURITY, security).commit();
    }

    public static int getsecurity() {
        return settings.getInt(PERF_SECURITY, 0);
    }
}
