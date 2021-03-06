package com.softdesign.devintensive.ui.fragments;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;

import com.softdesign.devintensive.R;
import com.softdesign.devintensive.ui.callbacks.MainActivityCallback;
import com.softdesign.devintensive.ui.callbacks.UserListActivityCallback;
import com.softdesign.devintensive.utils.Const;

public class DialogsFragment extends DialogFragment {
    private static final String TAG = Const.TAG_PREFIX + "DialogsFragment";

    private MainActivityCallback mCallback;
    private UserListActivityCallback mULCallback;

    public static DialogsFragment newInstance(int type) {
        DialogsFragment dialogsFragment = new DialogsFragment();
        Bundle args = new Bundle();
        args.putInt(Const.DIALOG_FRAGMENT_KEY, type);
        dialogsFragment.setArguments(args);
        return dialogsFragment;
    }

    public static DialogsFragment newInstance(int type, String content) {
        DialogsFragment dialogsFragment = new DialogsFragment();
        Bundle args = new Bundle();
        args.putInt(Const.DIALOG_FRAGMENT_KEY, type);
        args.putString(Const.DIALOG_CONTENT_KEY, content);
        dialogsFragment.setArguments(args);
        return dialogsFragment;
    }

    @SuppressWarnings("deprecation")
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof MainActivityCallback) {
            mCallback = (MainActivityCallback) activity;
        }
        if (activity instanceof UserListActivityCallback) {
            mULCallback = (UserListActivityCallback) activity;
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        int type = getArguments().getInt(Const.DIALOG_FRAGMENT_KEY);
        switch (type) {
            case Const.DIALOG_LOAD_PROFILE_PHOTO:
                if (mCallback != null) return loadPhotoDialog();
                else
                    throw new IllegalStateException("Parent activity must implement MainActivityCallback");
            case Const.DIALOG_SHOW_ERROR:
                return errorAlertDialog(getArguments().getString(Const.DIALOG_CONTENT_KEY));
            case Const.DIALOG_SHOW_ERROR_RETURN_TO_MAIN:
                return errorAlertDialogWithAction(getArguments().getString(Const.DIALOG_CONTENT_KEY));
            default:
                return errorAlertDialog(getString(R.string.error));
        }
    }

    //region Dialogs
    private Dialog loadPhotoDialog() {
        return new AlertDialog.Builder(getActivity())
                .setTitle(getString(R.string.header_profile_placeHolder_loadPhotoDialog_title))
                .setItems(R.array.profile_placeHolder_loadPhotoDialog, (dialog, chosenItem) -> {
                    switch (chosenItem) {
                        case 0:
                            mCallback.loadPhotoFromCamera();
                            break;
                        case 1:
                            mCallback.loadPhotoFromGallery();
                            break;
                        case 2:
                            dialog.cancel();
                            break;
                    }
                }).create();
    }

    public Dialog errorAlertDialog(String error) {
        return new AlertDialog.Builder(getActivity())
                .setMessage(error)
                .setCancelable(true)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton(android.R.string.ok, (dialog, id) -> {
                    dialog.cancel();
                }).create();
    }

    public Dialog errorAlertDialogWithAction(String error) {
        return new AlertDialog.Builder(getActivity())
                .setMessage(error)
                .setCancelable(true)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton(android.R.string.ok, (dialog, id) -> {
                    if (mULCallback != null) mULCallback.startMainActivity();
                }).create();
    }
    //endregion
}
