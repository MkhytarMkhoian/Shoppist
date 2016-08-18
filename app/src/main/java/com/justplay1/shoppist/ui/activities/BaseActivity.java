package com.justplay1.shoppist.ui.activities;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.database.ContentObserver;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.LoaderManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.justplay1.shoppist.R;
import com.justplay1.shoppist.models.BaseModel;
import com.justplay1.shoppist.settings.preferences.ShoppistPreferences;
import com.justplay1.shoppist.ui.views.CustomProgressDialog;
import com.justplay1.shoppist.ui.views.EmptyView;
import com.justplay1.shoppist.ui.views.actionmode.ActionModeListener;
import com.justplay1.shoppist.ui.views.actionmode.ShoppingListActionModeCallback;
import com.justplay1.shoppist.ui.views.animboxes.BaseSelectItemsManager;
import com.justplay1.shoppist.ui.views.recyclerview.ShoppistRecyclerView;
import com.justplay1.shoppist.ui.views.recyclerview.holders.BaseItemHolder;
import com.justplay1.shoppist.utils.ActivityUtils;
import com.justplay1.shoppist.utils.ColorThemeUpdater;
import com.justplay1.shoppist.utils.ShoppistUtils;


/**
 * Created by Mkhitar on 05.03.2015.
 */
public abstract class BaseActivity<T extends BaseModel> extends AppCompatActivity implements ActionModeListener,
        View.OnClickListener, Toolbar.OnMenuItemClickListener, ShoppistRecyclerView.OnItemClickListener,
        ColorThemeUpdater {

    protected static final String SAVED_STATE_EXPANDABLE_ITEM_MANAGER = "RecyclerViewExpandableItemManager";

    protected Toolbar mToolbar;
    protected FloatingActionButton mAddBtn;
    protected BaseSelectItemsManager<T> mSelectItemsManager;
    protected EmptyView mEmptyView;
    protected ChangeObserver mChangeObserver;
    protected CustomProgressDialog mProgressDialog;
    protected ShoppistRecyclerView mRecyclerView;
    protected LinearLayoutManager mLayoutManager;
    protected RecyclerView.Adapter mWrappedAdapter;

    protected abstract String getActivityClassName();

    protected abstract ShoppingListActionModeCallback getActionModeCallback();

    protected abstract void deleteItem();

    protected abstract void initAdapter();

    protected abstract int getMainCursorLoaderId();

    protected abstract void loadData();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityUtils.setStatusBarColor(this);
        mChangeObserver = new ChangeObserver();
    }

    protected void initFrame(Bundle savedInstanceState) {
        mProgressDialog = new CustomProgressDialog(this);
        mProgressDialog.setCancelable(false);
        mProgressDialog.setMessage(getString(R.string.please_wait));

        mEmptyView = (EmptyView) findViewById(android.R.id.empty);
        mEmptyView.setProgressBarColor(ShoppistPreferences.getColorPrimary());

        mAddBtn = (FloatingActionButton) findViewById(R.id.add_button);
        mAddBtn.setOnClickListener(this);
        mAddBtn.setBackgroundTintList(ColorStateList.valueOf(ShoppistPreferences.getColorPrimary()));

        initRecyclerView(savedInstanceState);

        mRecyclerView.setRecyclerListener(new RecyclerView.RecyclerListener() {
            @Override
            public void onViewRecycled(RecyclerView.ViewHolder holder) {
                mSelectItemsManager.onMovedToScrapHeap(holder);
            }
        });
    }

    protected void initRecyclerView(Bundle savedInstanceState) {
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView = (ShoppistRecyclerView) findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setEmptyView(mEmptyView);
    }

    @Override
    public boolean onItemLongClick(BaseItemHolder holder, int position, long id) {
        mSelectItemsManager.onLongClick(holder.itemView);
        return true;
    }

    protected void share(String textToSend, String header) {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, textToSend);
        sendIntent.putExtra(Intent.EXTRA_SUBJECT, header);
        sendIntent.setType("text/plain");
        startActivity(Intent.createChooser(sendIntent, getString(R.string.send_to)));

//        Resources resources = getResources();
//
//        Intent emailIntent = new Intent();
//        emailIntent.setAction(Intent.ACTION_SEND);
//        // Native email client doesn't currently support HTML, but it doesn't hurt to try in case they fix it
//        emailIntent.putExtra(Intent.EXTRA_TEXT, textToSend);
//        emailIntent.putExtra(Intent.EXTRA_SUBJECT, header);
//        emailIntent.setType("message/rfc822");
//
//        PackageManager pm = getPackageManager();
//        Intent sendIntent = new Intent(Intent.ACTION_SEND);
//        sendIntent.setType("text/plain");
//
//        Intent openInChooser = Intent.createChooser(emailIntent, getString(R.string.send_to));
//
//        List<ResolveInfo> resInfo = pm.queryIntentActivities(sendIntent, 0);
//        List<LabeledIntent> intentList = new ArrayList<>();
//        for (int i = 0; i < resInfo.size(); i++) {
//            // Extract the label, append it, and repackage it in a LabeledIntent
//            ResolveInfo ri = resInfo.get(i);
//            String packageName = ri.activityInfo.packageName;
//            if(packageName.contains("android.email")) {
//                emailIntent.setPackage(packageName);
//            } else if(packageName.contains("twitter") || packageName.contains("facebook")
//                    || packageName.contains("mms") || packageName.contains("android.gm")
//                    || packageName.contains("com.google.android.apps.plus") || packageName.contains("hangouts")
//                    || packageName.contains("com.linkedin") || packageName.contains("com.whatsapp")
//                    || packageName.contains("sms") || packageName.contains("mail")) {
//
//                Intent intent = new Intent();
//                intent.setComponent(new ComponentName(packageName, ri.activityInfo.name));
//                intent.setAction(Intent.ACTION_SEND);
//                intent.putExtra(Intent.EXTRA_TEXT, textToSend);
//                intent.putExtra(Intent.EXTRA_SUBJECT, header);
//                intent.setType("text/plain");
//                if(packageName.contains("twitter")) {
//                    intent.putExtra(Intent.EXTRA_TEXT, textToSend);
//                } else if(packageName.contains("facebook")) {
//                    // Warning: Facebook IGNORES our text. They say "These fields are intended for users to express themselves. Pre-filling these fields erodes the authenticity of the user voice."
//                    // One workaround is to use the Facebook SDK to post, but that doesn't allow the user to choose how they want to share. We can also make a custom landing page, and the link
//                    // will show the <meta content ="..."> text from that page with our link in Facebook.
//                    intent.putExtra(Intent.EXTRA_TEXT, textToSend);
//                } else if(packageName.contains("mms")) {
//                    intent.putExtra(Intent.EXTRA_TEXT, textToSend);
//                } else if(packageName.contains("android.gm")) {
//                    intent.putExtra(Intent.EXTRA_TEXT, textToSend);
//                    intent.putExtra(Intent.EXTRA_SUBJECT, header);
//                    intent.setType("message/rfc822");
//                }
//
//                intentList.add(new LabeledIntent(intent, packageName, ri.loadLabel(pm), ri.icon));
//            }
//        }
//
//        // convert intentList to array
//        LabeledIntent[] extraIntents = intentList.toArray( new LabeledIntent[ intentList.size() ]);
//
//        openInChooser.putExtra(Intent.EXTRA_INITIAL_INTENTS, extraIntents);
//        startActivity(openInChooser);
    }

    @Override
    public void onDestroyActionMode(ActionMode actionMode) {
        mSelectItemsManager.unCheckAllItems(true);
        mSelectItemsManager.release();
    }

    @Override
    public void onPrepareActionMode(ActionMode actionMode, Menu menu) {
        MenuItem edit = menu.findItem(R.id.action_edit);
        if (edit != null) {
            boolean editFlag = true;
            if (mSelectItemsManager.getCheckedCount() != 1) {
                editFlag = false;
            }
            edit.setVisible(editFlag);
        }

        MenuItem checkAll = menu.findItem(R.id.menu_check_all);
        if (mSelectItemsManager.isAllItemsChecked()) {
            checkAll.setEnabled(false);
        } else {
            checkAll.setEnabled(true);
        }
    }

    @Override
    public void updateTheme() {
        if (ShoppistPreferences.isNeedUpdateTheme(getActivityClassName())) {
            ShoppistPreferences.deleteUpdateItem(getActivityClassName());

            mToolbar.setBackgroundColor(ShoppistPreferences.getColorPrimary());
            mAddBtn.setBackgroundTintList(ColorStateList.valueOf(ShoppistPreferences.getColorPrimary()));
            mEmptyView.setProgressBarColor(ShoppistPreferences.getColorPrimary());
            ActivityUtils.setStatusBarColor(this);
        }
    }

    protected void showDeleteDialog(String message) {
        if (ShoppistPreferences.isNeedShowConfirmDeleteDialog()) {
            showConfirmDeleteDialog(message, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(final DialogInterface dialog, int which) {
                    switch (which) {
                        case Dialog.BUTTON_POSITIVE:
                            deleteItem();
                            dialog.dismiss();
                        case Dialog.BUTTON_NEGATIVE:
                            dialog.dismiss();
                            break;
                    }
                }
            });
        } else {
            deleteItem();
        }
    }

    protected void showConfirmDeleteDialog(String message, DialogInterface.OnClickListener listener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(message);
        builder.setPositiveButton(R.string.action_delete, listener);
        builder.setNegativeButton(R.string.cancel, listener);
        AlertDialog dialog = builder.create();
        dialog.show();
        dialog.getButton(Dialog.BUTTON_POSITIVE).setTextColor(ShoppistPreferences.getColorPrimary());
        dialog.getButton(Dialog.BUTTON_NEGATIVE).setTextColor(ShoppistPreferences.getColorPrimary());
    }

    @Override
    protected void onResume() {
        super.onResume();
        ShoppistUtils.setKeepScreenOn(getWindow(), ShoppistPreferences.isLockScreen());
        updateTheme();
    }

    @Override
    public void onBackPressed() {
        if (mSelectItemsManager.isActionModeShowing()) {
            mSelectItemsManager.closeActionMode();
        } else {
            finishActivity();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && data != null) {
            if (data.getStringExtra(ActivityUtils.OLD_ID) != null) {
                String oldId = data.getStringExtra(ActivityUtils.OLD_ID);
                String newId = data.getStringExtra(ActivityUtils.NEW_ID);

                boolean checked = mSelectItemsManager.isItemChecked(oldId);
                mSelectItemsManager.deleteItemFromChecked(oldId);
                mSelectItemsManager.addToChecked(newId, checked);

                data.removeExtra(ActivityUtils.NEW_ID);
                data.removeExtra(ActivityUtils.OLD_ID);
            }
        }
    }

    private void finishActivity() {
        ActivityUtils.finishActivity(this);
    }

    public class ChangeObserver extends ContentObserver {
        public ChangeObserver() {
            super(new Handler());
        }

        @Override
        public boolean deliverSelfNotifications() {
            return true;
        }

        @Override
        public void onChange(boolean selfChange) {
            loadData();
        }
    }
}
