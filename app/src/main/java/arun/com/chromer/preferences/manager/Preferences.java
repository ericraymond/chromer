package arun.com.chromer.preferences.manager;

import android.content.ComponentName;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;

import java.util.List;

import arun.com.chromer.R;
import arun.com.chromer.customtabs.CustomTabs;
import arun.com.chromer.shared.Constants;
import arun.com.chromer.util.Util;

/**
 * Created by Arun on 05/01/2016.
 */
public class Preferences {
    public static final String TOOLBAR_COLOR = "toolbar_color";
    public static final String WEB_HEADS_COLOR = "webhead_color";
    public static final String ANIMATION_TYPE = "animation_preference";
    public static final String ANIMATION_SPEED = "animation_speed_preference";
    public static final String DYNAMIC_COLOR = "dynamic_color";
    public static final String WEB_HEAD_CLOSE_ON_OPEN = "webhead_close_onclick_pref";
    public static final String PREFERRED_ACTION = "preferred_action_preference";
    public static final String WEB_HEAD_ENABLED = "webhead_enabled_pref";
    public static final String WEB_HEAD_SPAWN_LOCATION = "webhead_spawn_preference";
    public static final String WEB_HEAD_SIZE = "webhead_size_preference";
    private static final String WEB_HEAD_FAVICON = "webhead_favicons_pref";
    public static final String BOTTOM_BAR_ENABLED = "bottombar_enabled_pref";
    public static final int PREFERRED_ACTION_BROWSER = 1;
    public static final int PREFERRED_ACTION_FAV_SHARE = 2;
    public static final int ANIMATION_MEDIUM = 1;
    public static final int ANIMATION_SHORT = 2;
    public static final String TOOLBAR_COLOR_PREF = "toolbar_color_pref";
    public static final String WARM_UP = "warm_up_preference";
    public static final String PRE_FETCH = "pre_fetch_preference";
    public static final String WIFI_PREFETCH = "wifi_preference";
    public static final String PRE_FETCH_NOTIFICATION = "pre_fetch_notification_preference";
    public static final String BLACKLIST_DUMMY = "blacklist_preference_dummy";
    public static final String MERGE_TABS_AND_APPS = "merge_tabs_and_apps_preference";
    private static final String BLACKLIST = "blacklist_preference";
    private static final String PREFERRED_PACKAGE = "preferred_package";
    private static final String FIRST_RUN = "firstrun_2";
    private static final String USER_KNOWS_BOTTOM_BAR = "user_learnt_bottom_bar";
    private static final String SECONDARY_PREF = "secondary_preference";
    private static final String FAV_SHARE_PREF = "fav_share_preference";
    private static final String CLEAN_DATABASE = "clean_database";
    private static final String DYNAMIC_COLOR_APP = "dynamic_color_app";
    private static final String DYNAMIC_COLOR_WEB = "dynamic_color_web";
    public static final String AGGRESSIVE_LOADING = "aggressive_loading";

    private static SharedPreferences preferences(Context context) {
        return context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE);
    }

    public static boolean isFirstRun(Context context) {
        if (preferences(context).getBoolean(FIRST_RUN, true)) {
            preferences(context).edit().putBoolean(FIRST_RUN, false).apply();
            return true;
        }
        return false;
    }

    public static boolean dummyBottomBar(Context context) {
        // TODO Fix this and make learnt flag persistable
        return !preferences(context).getBoolean(USER_KNOWS_BOTTOM_BAR, false);
    }

    public static boolean isColoredToolbar(Context context) {
        return PreferenceManager
                .getDefaultSharedPreferences(context)
                .getBoolean(TOOLBAR_COLOR_PREF, true);
    }

    public static int toolbarColor(Context context) {
        return PreferenceManager
                .getDefaultSharedPreferences(context)
                .getInt(TOOLBAR_COLOR,
                        ContextCompat.getColor(context, R.color.colorPrimary));
    }

    public static void toolbarColor(Context context, int selectedColor) {
        PreferenceManager
                .getDefaultSharedPreferences(context)
                .edit()
                .putInt(TOOLBAR_COLOR, selectedColor).apply();
    }

    public static int webHeadColor(Context context) {
        return PreferenceManager
                .getDefaultSharedPreferences(context)
                .getInt(WEB_HEADS_COLOR,
                        ContextCompat.getColor(context, R.color.web_head_bg));
    }

    public static void webHeadColor(Context context, int selectedColor) {
        PreferenceManager
                .getDefaultSharedPreferences(context)
                .edit()
                .putInt(WEB_HEADS_COLOR, selectedColor).apply();
    }

    public static boolean isAnimationEnabled(Context context) {
        return animationType(context) != 0;
    }

    public static int animationType(Context context) {
        return Integer.parseInt(PreferenceManager
                .getDefaultSharedPreferences(context)
                .getString(ANIMATION_TYPE, "1"));
    }

    public static int animationSpeed(Context context) {
        return Integer.parseInt(PreferenceManager
                .getDefaultSharedPreferences(context)
                .getString(ANIMATION_SPEED, "1"));
    }

    public static int preferredAction(Context context) {
        return Integer.parseInt(PreferenceManager
                .getDefaultSharedPreferences(context)
                .getString(PREFERRED_ACTION, "1"));
    }

    @Nullable
    public static String customTabApp(Context context) {
        String packageName = PreferenceManager
                .getDefaultSharedPreferences(context)
                .getString(PREFERRED_PACKAGE, null);

        if (packageName != null && Util.isPackageInstalled(context, packageName))
            return packageName;
        else {
            packageName = getDefaultCustomTabApp(context);
            // update the new custom tab package
            customTabApp(context, packageName);
        }
        return packageName;
    }

    @Nullable
    private static String getDefaultCustomTabApp(Context context) {
        if (Util.isPackageInstalled(context, Constants.CHROME_PACKAGE) &&
                CustomTabs.isPackageSupportCustomTabs(context, Constants.CHROME_PACKAGE))
            return Constants.CHROME_PACKAGE;

        List<String> supportingPackages = CustomTabs.getCustomTabSupportingPackages(context);
        if (supportingPackages.size() > 0) {
            return supportingPackages.get(0);
        } else
            return null;
    }

    public static void customTabApp(Context context, String string) {
        PreferenceManager
                .getDefaultSharedPreferences(context)
                .edit()
                .putString(PREFERRED_PACKAGE, string).apply();
    }

    @Nullable
    public static String secondaryBrowserComponent(Context context) {
        return PreferenceManager
                .getDefaultSharedPreferences(context)
                .getString(SECONDARY_PREF, null);
    }

    public static void secondaryBrowserComponent(Context context, String flattenChomponentString) {
        PreferenceManager
                .getDefaultSharedPreferences(context)
                .edit()
                .putString(SECONDARY_PREF, flattenChomponentString).apply();
    }

    @Nullable
    public static String secondaryBrowserPackage(Context context) {
        String flatString = secondaryBrowserComponent(context);
        if (flatString == null) {
            return null;
        }

        ComponentName cN = ComponentName.unflattenFromString(flatString);
        if (cN == null) return null;

        return cN.getPackageName();
    }

    @Nullable
    public static String favShareComponent(Context context) {
        return PreferenceManager
                .getDefaultSharedPreferences(context)
                .getString(FAV_SHARE_PREF, null);
    }

    public static void favShareComponent(Context context, String string) {
        PreferenceManager
                .getDefaultSharedPreferences(context)
                .edit()
                .putString(FAV_SHARE_PREF, string).apply();
    }

    @Nullable
    public static String favSharePackage(Context context) {
        String flatString = favShareComponent(context);
        if (flatString == null) {
            return null;
        }

        ComponentName cN = ComponentName.unflattenFromString(flatString);
        if (cN == null) return null;

        return cN.getPackageName();
    }

    public static boolean warmUp(Context context) {
        return PreferenceManager
                .getDefaultSharedPreferences(context).getBoolean(WARM_UP, false);
    }

    public static void warmUp(Context context, boolean preference) {
        PreferenceManager
                .getDefaultSharedPreferences(context)
                .edit()
                .putBoolean(WARM_UP, preference).apply();
    }

    public static boolean preFetch(Context context) {
        return PreferenceManager
                .getDefaultSharedPreferences(context)
                .getBoolean(PRE_FETCH, false);
    }

    public static void preFetch(Context context, boolean preference) {
        PreferenceManager
                .getDefaultSharedPreferences(context)
                .edit().putBoolean(PRE_FETCH, preference).apply();
    }

    public static boolean wifiOnlyPrefetch(Context context) {
        return PreferenceManager
                .getDefaultSharedPreferences(context)
                .getBoolean(WIFI_PREFETCH, false);
    }

    public static void wifiOnlyPrefetch(Context context, boolean preference) {
        PreferenceManager
                .getDefaultSharedPreferences(context)
                .edit().putBoolean(WIFI_PREFETCH, preference).apply();
    }

    public static boolean preFetchNotification(Context context) {
        return PreferenceManager
                .getDefaultSharedPreferences(context)
                .getBoolean(PRE_FETCH_NOTIFICATION, true);
    }

    public static void preFetchNotification(Context context, boolean preference) {
        PreferenceManager
                .getDefaultSharedPreferences(context)
                .edit().putBoolean(PRE_FETCH_NOTIFICATION, preference).apply();
    }

    public static boolean dynamicToolbar(Context context) {
        return PreferenceManager
                .getDefaultSharedPreferences(context)
                .getBoolean(DYNAMIC_COLOR, false);
    }

    @SuppressWarnings("unused")
    public static void dynamicToolbar(Context context, boolean preference) {
        PreferenceManager
                .getDefaultSharedPreferences(context)
                .edit()
                .putBoolean(DYNAMIC_COLOR, preference).apply();
    }

    public static boolean shouldCleanDB(Context context) {
        if (preferences(context).getBoolean(CLEAN_DATABASE, true)) {
            preferences(context).edit().putBoolean(CLEAN_DATABASE, false).apply();
            return true;
        }
        return false;
    }

    public static boolean dynamicToolbarOnApp(Context context) {
        return preferences(context).getBoolean(DYNAMIC_COLOR_APP, false);
    }

    private static void dynamicToolbarOnApp(Context context, boolean preference) {
        preferences(context).edit().putBoolean(DYNAMIC_COLOR_APP, preference).apply();
    }

    public static boolean dynamicToolbarOnWeb(Context context) {
        return preferences(context).getBoolean(DYNAMIC_COLOR_WEB, false);
    }

    private static void dynamicToolbarOnWeb(Context context, boolean preference) {
        preferences(context).edit().putBoolean(DYNAMIC_COLOR_WEB, preference).apply();
    }

    public static boolean aggressiveLoading(Context context) {
        return Util.isLollipopAbove()
                && webHeads(context)
                && PreferenceManager
                .getDefaultSharedPreferences(context).getBoolean(AGGRESSIVE_LOADING, false);
    }

    public static void aggressiveLoading(Context context, boolean preference) {
        PreferenceManager
                .getDefaultSharedPreferences(context)
                .edit()
                .putBoolean(AGGRESSIVE_LOADING, preference).apply();
    }

    private static void dynamicToolbarOptions(Context context, boolean app, boolean web) {
        dynamicToolbarOnApp(context, app);
        dynamicToolbarOnWeb(context, web);
        dynamicToolbar(context, app || web);
    }

    @Nullable
    public static Integer[] dynamicToolbarSelections(Context context) {
        if (dynamicToolbarOnApp(context) && dynamicToolbarOnWeb(context))
            return new Integer[]{0, 1};
        else if (dynamicToolbarOnApp(context))
            return new Integer[]{0};
        else if (dynamicToolbarOnWeb(context))
            return new Integer[]{1};
        else return null;
    }

    public static void updateAppAndWeb(Context context, Integer[] which) {
        switch (which.length) {
            case 0:
                Preferences.dynamicToolbarOptions(context, false, false);
                break;
            case 1:
                if (which[0] == 0) {
                    Preferences.dynamicToolbarOptions(context, true, false);
                } else if (which[0] == 1) {
                    Preferences.dynamicToolbarOptions(context, false, true);
                }
                break;
            case 2:
                Preferences.dynamicToolbarOptions(context, true, true);
                break;
        }
    }

    @NonNull
    public static CharSequence dynamicColorSummary(Context context) {
        if (dynamicToolbarOnApp(context) && dynamicToolbarOnWeb(context)) {
            return context.getString(R.string.dynamic_summary_appweb);
        } else if (dynamicToolbarOnApp(context)) {
            return context.getString(R.string.dynamic_summary_app);
        } else if (dynamicToolbarOnWeb(context)) {
            return context.getString(R.string.dynamic_summary_web);
        } else
            return context.getString(R.string.no_option_selected);
    }

    public static boolean webHeads(Context context) {
        return PreferenceManager
                .getDefaultSharedPreferences(context)
                .getBoolean(WEB_HEAD_ENABLED, false);
    }

    @SuppressWarnings("unused")
    public static void webHeads(Context context, boolean preference) {
        PreferenceManager
                .getDefaultSharedPreferences(context)
                .edit()
                .putBoolean(WEB_HEAD_ENABLED, preference).apply();
    }

    public static boolean favicons(Context context) {
        return PreferenceManager
                .getDefaultSharedPreferences(context)
                .getBoolean(WEB_HEAD_FAVICON, true);
    }

    @SuppressWarnings("unused")
    public static void favicons(Context context, boolean preference) {
        PreferenceManager
                .getDefaultSharedPreferences(context)
                .edit()
                .putBoolean(WEB_HEAD_FAVICON, preference).apply();
    }

    public static int webHeadsSpawnLocation(Context context) {
        return Integer.parseInt(PreferenceManager
                .getDefaultSharedPreferences(context)
                .getString(WEB_HEAD_SPAWN_LOCATION, "1"));
    }

    public static int webHeadsSize(Context context) {
        return Integer.parseInt(PreferenceManager
                .getDefaultSharedPreferences(context)
                .getString(WEB_HEAD_SIZE, "1"));
    }

    public static boolean webHeadsCloseOnOpen(Context context) {
        return PreferenceManager
                .getDefaultSharedPreferences(context)
                .getBoolean(WEB_HEAD_CLOSE_ON_OPEN, false);
    }

    @SuppressWarnings("unused")
    public static void webHeadsCloseOnOpen(Context context, boolean preference) {
        PreferenceManager
                .getDefaultSharedPreferences(context)
                .edit()
                .putBoolean(WEB_HEAD_CLOSE_ON_OPEN, preference).apply();
    }

    public static boolean blacklist(Context context) {
        return PreferenceManager
                .getDefaultSharedPreferences(context)
                .getBoolean(BLACKLIST, false);
    }

    public static void blacklist(Context context, boolean preference) {
        PreferenceManager
                .getDefaultSharedPreferences(context)
                .edit().putBoolean(BLACKLIST, preference).apply();
    }

    public static boolean mergeTabs(Context context) {
        return Util.isLollipopAbove() &&
                PreferenceManager
                        .getDefaultSharedPreferences(context)
                        .getBoolean(MERGE_TABS_AND_APPS, false);
    }

    public static void mergeTabs(Context context, boolean preference) {
        PreferenceManager
                .getDefaultSharedPreferences(context)
                .edit()
                .putBoolean(MERGE_TABS_AND_APPS, preference).apply();
    }

    public static boolean bottomBar(Context context) {
        return PreferenceManager
                .getDefaultSharedPreferences(context)
                .getBoolean(BOTTOM_BAR_ENABLED, false);
    }

    @SuppressWarnings("unused")
    public static void bottomBar(Context context, boolean preference) {
        PreferenceManager
                .getDefaultSharedPreferences(context)
                .edit()
                .putBoolean(BOTTOM_BAR_ENABLED, preference).apply();
    }
}
