package com.dino.lib.dialog.progress;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import com.dino.lib.dialog.BaseDialogBuilder;
import com.dino.lib.dialog.BaseDialogFragment;
import com.dino.lib.dialog.R;

/**
 * Created by dineshkumar.m on 11/03/18.
 */

public class PriorityProgressDialog extends BaseDialogFragment {

    private CharSequence mTitle;
    private CharSequence mMessage;
    private boolean mIsIndeterminate = false;
    private int mPriority;
    private String mTag;
    private  boolean mIsTouchOutsideCancel=true;
    private boolean mIsCircleProgress;
    private  DialogInterface.OnDismissListener mOnDismissListener;
    private  DialogInterface.OnCancelListener mOnCancelListener;
    private  DialogInterface.OnShowListener mOnShowListener;

    public static ProgressDialogBuilder createBuilder(Context context, FragmentManager fragmentManager) {
        return new ProgressDialogBuilder(context, fragmentManager);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        if(mIsCircleProgress){
            //View view = getActivity().getLayoutInflater().inflate(R.layout.ui_progress_dialog, null);
            //
            Dialog dialog = new Dialog(getActivity(), R.style.ProgressDialogPrimary);
            //dialog.setContentView(view);`
            dialog.setCanceledOnTouchOutside(mIsTouchOutsideCancel);

            return dialog;
        }else {
            if (mMessage == null) {
                mMessage = getString(R.string.dialog_loading);
            }

            ProgressDialog progressDialog = new ProgressDialog(getActivity());
            progressDialog.setMessage(mMessage);
            if (mTitle != null)
                progressDialog.setTitle(mTitle);

            progressDialog.setIndeterminate(mIsIndeterminate);

            return progressDialog;
        }
    }


    @Override
    protected String getCustomTag() {
        return mTag;
    }

    @Override
    protected int getPriority() {
        return mPriority;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (getArguments() == null) {
            throw new IllegalArgumentException("use ProgressDialogBuilder to construct this dialog");
        }
    }

    @Override
    protected Builder build(Builder initialBuilder) {
        return null;
    }


    public static class ProgressDialogBuilder extends BaseDialogBuilder<ProgressDialogBuilder> {

        private CharSequence mTitle;
        private CharSequence mMessage;
        private boolean mIsCircleProgress;
        private  DialogInterface.OnDismissListener mOnDismissListener;
        private  DialogInterface.OnCancelListener mOnCancelListener;
        private  DialogInterface.OnShowListener mOnShowListener;
        protected ProgressDialogBuilder(Context context, FragmentManager fragmentManager) {
            super(context, fragmentManager, PriorityProgressDialog.class);
        }

        @Override
        protected ProgressDialogBuilder self() {
            return this;
        }

        public ProgressDialogBuilder setTitle(int titleResourceId) {
            mTitle = mContext.getString(titleResourceId);
            return this;
        }


        public ProgressDialogBuilder setTitle(CharSequence title) {
            mTitle = title;
            return this;
        }

        public ProgressDialogBuilder setMessage(int messageResourceId) {
            mMessage = mContext.getString(messageResourceId);
            return this;
        }

        public ProgressDialogBuilder setMessage(CharSequence message) {
            mMessage = message;
            return this;
        }

        public ProgressDialogBuilder setPriority(int priority) {
            mPriority = priority;
            super.setPriority(priority);
            return this;
        }
        public int getPriority() {
            return mPriority;
        }

        public ProgressDialogBuilder setTag(String tag) {
            mTag = tag;
            super.setTag(mTag);
            return this;
        }

        public String getTag() {
            return mTag;
        }

        public ProgressDialogBuilder setCircleProgress(boolean isCircle) {
            mIsCircleProgress = isCircle;
            return this;
        }

        public ProgressDialogBuilder setTouchOutsideCancel(boolean touchOutsideCancel) {
            mCancelableOnTouchOutside = touchOutsideCancel;
            return this;
        }
        @Override
        public ProgressDialogBuilder setCancelable(boolean cancelable) {
            mCancelable = cancelable;
            return this;
        }

        public ProgressDialogBuilder setOnDismissListener(DialogInterface.OnDismissListener onDismissListener) {
            mOnDismissListener = onDismissListener;
            return this;
        }
        public ProgressDialogBuilder setOnCancelListener(DialogInterface.OnCancelListener onCancelListener) {
            mOnCancelListener = onCancelListener;
            return this;
        }

        public ProgressDialogBuilder setOnShowListener(DialogInterface.OnShowListener onShowListener) {
            mOnShowListener = onShowListener;
            return this;
        }


        @Override
        protected BaseDialogFragment build() {
            return showAllowingStateLoss();
        }
        public PriorityProgressDialog showAllowingStateLoss() {
            PriorityProgressDialog fragment = create();
            initFragment(fragment);
            fragment.showAllowingStateLoss(mFragmentManager, mTag);
            return fragment;
        }

        private PriorityProgressDialog create() {
            final Bundle args = new Bundle();

            final PriorityProgressDialog
                fragment = (PriorityProgressDialog) Fragment.instantiate(mContext, mClass.getName(), args);

            args.putBoolean(ARG_CANCELABLE_ON_TOUCH_OUTSIDE, mCancelableOnTouchOutside);

            args.putBoolean(ARG_USE_DARK_THEME, mUseDarkTheme);

            args.putBoolean(ARG_USE_LIGHT_THEME, mUseLightTheme);

            if (mTargetFragment != null) {
                fragment.setTargetFragment(mTargetFragment, mRequestCode);
            } else {
                args.putInt(ARG_REQUEST_CODE, mRequestCode);
            }
            fragment.setCancelable(mCancelable);

            return fragment;
        }

        private void initFragment(PriorityProgressDialog fragment){
            fragment.mTitle = this.mTitle;
            fragment.mMessage = this.mMessage;
            fragment.mTag = this.mTag;
            fragment.mPriority = this.mPriority;
            fragment.mIsTouchOutsideCancel = this.mCancelableOnTouchOutside;
            fragment.mIsCircleProgress = this.mIsCircleProgress;
            fragment.mPriority = this.mPriority;
            fragment.mOnDismissListener = this.mOnDismissListener;
            fragment.mOnCancelListener = this.mOnCancelListener;
            fragment.mOnShowListener = this.mOnShowListener;

        }
    }
}
