package com.justplay1.shoppist.navigation;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;

import com.justplay1.shoppist.R;
import com.justplay1.shoppist.models.CategoryViewModel;
import com.justplay1.shoppist.models.ListItemViewModel;
import com.justplay1.shoppist.models.ListViewModel;
import com.justplay1.shoppist.view.activities.AddElementActivity;
import com.justplay1.shoppist.view.activities.CategoriesActivity;
import com.justplay1.shoppist.view.activities.CurrencyActivity;
import com.justplay1.shoppist.view.activities.GoodsActivity;
import com.justplay1.shoppist.view.activities.ListItemActivity;
import com.justplay1.shoppist.view.activities.NotificationActivity;
import com.justplay1.shoppist.view.activities.SearchActivity;
import com.justplay1.shoppist.view.activities.SettingsActivity;
import com.justplay1.shoppist.view.activities.SignInActivity;
import com.justplay1.shoppist.view.activities.UnitsActivity;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Class used to navigate through the application.
 */
@Singleton
public class Navigator {

    @Inject
    public Navigator() {
        //empty
    }

    public void navigateToCategoriesScreen(Activity activity) {
        if (activity != null) {
            Intent intentToLaunch = CategoriesActivity.getCallingIntent(activity);
            activity.startActivity(intentToLaunch);
            startActivityAnimation(activity);
        }
    }

    public void navigateToCurrencyScreen(Activity activity) {
        if (activity != null) {
            Intent intentToLaunch = CurrencyActivity.getCallingIntent(activity);
            activity.startActivity(intentToLaunch);
            startActivityAnimation(activity);
        }
    }

    public void navigateToGoodsScreen(Activity activity) {
        if (activity != null) {
            Intent intentToLaunch = GoodsActivity.getCallingIntent(activity);
            activity.startActivity(intentToLaunch);
            startActivityAnimation(activity);
        }
    }

    public void navigateToUnitsScreen(Activity activity) {
        if (activity != null) {
            Intent intentToLaunch = UnitsActivity.getCallingIntent(activity);
            activity.startActivity(intentToLaunch);
            startActivityAnimation(activity);
        }
    }

    public void navigateToListItemsScreen(Activity activity, String parentId) {
        if (activity != null) {
            Intent intentToLaunch = ListItemActivity.getCallingIntent(activity, parentId);
            activity.startActivity(intentToLaunch);
            startActivityAnimation(activity);
        }
    }

    public void navigateToNotificationsScreen(Activity activity) {
        if (activity != null) {
            Intent intentToLaunch = NotificationActivity.getCallingIntent(activity);
            activity.startActivity(intentToLaunch);
            startActivityAnimation(activity);
        }
    }

    public void navigateToSettingScreen(Activity activity, int settingId) {
        if (activity != null) {
            Intent intentToLaunch = SettingsActivity.getCallingIntent(activity, settingId);
            activity.startActivity(intentToLaunch);
            startActivityAnimation(activity);
        }
    }

    public void navigateToSignInScreen(Activity activity) {
        if (activity != null) {
            Intent intentToLaunch = SignInActivity.getCallingIntent(activity);
            activity.startActivity(intentToLaunch);
            startActivityAnimation(activity);
        }
    }

    public void navigateToAddCategoryScreen(Activity activity, CategoryViewModel category) {
        if (activity != null) {
            Intent intentToLaunch = AddElementActivity.getCallingIntent(activity,
                    AddElementActivity.AddElementType.CATEGORY, category, null, null);
            activity.startActivity(intentToLaunch);
            startActivityAnimation(activity);
        }
    }

    public void navigateToAddListScreen(Activity activity, ListViewModel list) {
        if (activity != null) {
            Intent intentToLaunch = AddElementActivity.getCallingIntent(activity,
                    AddElementActivity.AddElementType.LIST, null, list, null);
            activity.startActivity(intentToLaunch);
            startActivityAnimation(activity);
        }
    }

    public void navigateToAddListItemScreen(Activity activity, ListViewModel list, ListItemViewModel listItem) {
        if (activity != null) {
            Intent intentToLaunch = AddElementActivity.getCallingIntent(activity,
                    AddElementActivity.AddElementType.LIST_ITEM, null, list, listItem);
            activity.startActivity(intentToLaunch);
            startActivityAnimation(activity);
        }
    }

    public void navigateToSearchScreen(Activity activity, int contextType, String parentListId) {
        if (activity != null) {
            Intent intentToLaunch = SearchActivity.getCallingIntent(activity, contextType, parentListId);
            startActivityWithFadeAnim(activity, intentToLaunch);
        }
    }

    private void startActivityWithFadeAnim(Activity activity, Intent intent) {
        activity.startActivity(intent);
        activity.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }

    private static void startActivityAnimation(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            activity.overridePendingTransition(R.anim.activity_open_enter_v21, R.anim.activity_open_exit_v21);
        } else {
            activity.overridePendingTransition(R.anim.activity_open_enter, R.anim.activity_open_exit);
        }
    }
}
