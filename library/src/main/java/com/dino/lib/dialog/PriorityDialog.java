package com.dino.lib.dialog;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.Html;
import android.text.SpannedString;
import android.text.TextUtils;
import android.view.View;

/**
 * Created by dineshkumar.m on 11/03/18.
 */

public class PriorityDialog extends BaseDialogFragment{

    private CharSequence mTitle;
    private CharSequence mMessage;
    private CharSequence mPositiveButtonText;
    private CharSequence mNegativeButtonText;
    private CharSequence mNeutralButtonText;
    private int mPriority;
    private String mTag;
    private  DialogInterface.OnDismissListener mOnDismissListener;
    private  DialogInterface.OnCancelListener mOnCancelListener;
    private  DialogInterface.OnShowListener mOnShowListener;
    private  View mView;
    private View.OnClickListener mPositiveButtonListener;
    private View.OnClickListener mNegativeButtonListener;
    private View.OnClickListener mNeutralButtonListener;

    public static SimpleDialogBuilder createBuilder(Context context, FragmentManager fragmentManager) {
        return new SimpleDialogBuilder(context, fragmentManager, PriorityDialog.class);
    }
    @Override
    public void onShow(DialogInterface dialog) {
        super.onShow(dialog);
        if(mOnShowListener != null){
            mOnShowListener.onShow(dialog);
        }
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        super.onCancel(dialog);
        if(mOnCancelListener != null){
            mOnCancelListener.onCancel(dialog);
        }
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        if(mOnDismissListener != null){
            mOnDismissListener.onDismiss(dialog);
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    @Override
    protected BaseDialogFragment.Builder build(BaseDialogFragment.Builder builder) {
        final CharSequence title = getTitle();
        if (!TextUtils.isEmpty(title)) {
            builder.setTitle(title);
        }

        final CharSequence message = getMessage();
        if (!TextUtils.isEmpty(message)) {
            builder.setMessage(message);
        }

        final CharSequence positiveButtonText = getPositiveButtonText();
        if (!TextUtils.isEmpty(positiveButtonText)) {
            builder.setPositiveButton(positiveButtonText, new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dismiss();
                    if(mPositiveButtonListener != null){
                        mPositiveButtonListener.onClick(view);
                    }
                }
            });
        }

        final CharSequence negativeButtonText = getNegativeButtonText();
        if (!TextUtils.isEmpty(negativeButtonText)) {
            builder.setNegativeButton(negativeButtonText, new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dismiss();
                    if(mNegativeButtonListener != null){
                        mNegativeButtonListener.onClick(view);
                    }
                }
            });
        }

        final CharSequence neutralButtonText = getNeutralButtonText();
        if (!TextUtils.isEmpty(neutralButtonText)) {
            builder.setNeutralButton(neutralButtonText, new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dismiss();
                if(mNeutralButtonListener != null){
                        mNeutralButtonListener.onClick(view);
                    }
                }
            });
        }
        builder.setView(mView);

        return builder;
    }

    protected CharSequence getMessage() {
        return mMessage;
    }

    protected CharSequence getTitle() {
        return mTitle;
    }

    protected CharSequence getPositiveButtonText() {
        return mPositiveButtonText;
    }

    protected CharSequence getNegativeButtonText() {
        return mNegativeButtonText;
    }

    protected CharSequence getNeutralButtonText() {
        return mNeutralButtonText;
    }


    @Override
    protected int getPriority() {
        return mPriority;
    }
    @Override
    protected String getCustomTag() {
        return mTag;
    }

    public static class SimpleDialogBuilder extends BaseDialogBuilder<SimpleDialogBuilder> {

        private CharSequence mTitle;
        private CharSequence mMessage;
        private CharSequence mPositiveButtonText;
        private CharSequence mNegativeButtonText;
        private CharSequence mNeutralButtonText;
        private int mPriority;
        private String mTag;
        private  DialogInterface.OnDismissListener mOnDismissListener;
        private  DialogInterface.OnCancelListener mOnCancelListener;
        private  DialogInterface.OnShowListener mOnShowListener;
        private  boolean mIsTouchOutsideCancel;
        private  View mView;
        private boolean mIsCancelable;
        private View.OnClickListener mPositiveButtonListener;
        private View.OnClickListener mNegativeButtonListener;
        private View.OnClickListener mNeutralButtonListener;

        protected SimpleDialogBuilder(Context context, FragmentManager fragmentManager, Class<? extends PriorityDialog> clazz) {
            super(context, fragmentManager, clazz);
        }

        @Override
        protected SimpleDialogBuilder self() {
            return this;
        }

        public SimpleDialogBuilder setTitle(int titleResourceId) {
            mTitle = mContext.getString(titleResourceId);
            return this;
        }


        public SimpleDialogBuilder setTitle(CharSequence title) {
            mTitle = title;
            return this;
        }

        public SimpleDialogBuilder setMessage(int messageResourceId) {
            mMessage = mContext.getText(messageResourceId);
            return this;
        }

        public SimpleDialogBuilder setMessage(int resourceId, Object... formatArgs) {
            mMessage = Html.fromHtml(String.format(Html.toHtml(new SpannedString(mContext.getText(resourceId))), formatArgs));
            return this;
        }

        public SimpleDialogBuilder setMessage(CharSequence message) {
            mMessage = message;
            return this;
        }
        public SimpleDialogBuilder setPositiveButton(CharSequence text,final View.OnClickListener listener) {
            mPositiveButtonText = text;
            mPositiveButtonListener = listener;
            return this;
        }

        public SimpleDialogBuilder setNegativeButton(CharSequence text,final View.OnClickListener listener) {
            mNegativeButtonText = text;
            mNegativeButtonListener = listener;
            return this;
        }

        public SimpleDialogBuilder setNeutralButton(CharSequence text,final View.OnClickListener listener) {
            mNeutralButtonText = text;
            mNeutralButtonListener = listener;
            return this;
        }

        public SimpleDialogBuilder setPositiveButton(int textResourceId,final View.OnClickListener listener) {
            mPositiveButtonText = mContext.getString(textResourceId);
            mPositiveButtonListener = listener;
            return this;
        }


        public SimpleDialogBuilder setNegativeButton(int textResourceId,final View.OnClickListener listener) {
            mNegativeButtonText = mContext.getString(textResourceId);
            mNegativeButtonListener = listener;
            return this;
        }

        public SimpleDialogBuilder setNeutralButton(int textResourceId,final View.OnClickListener listener) {
            mNeutralButtonText = mContext.getString(textResourceId);
            mNeutralButtonListener = listener;
            return this;
        }


        @Override
        public SimpleDialogBuilder setCancelable(boolean cancelable) {
            return super.setCancelable(cancelable);
        }

        @Override
        public SimpleDialogBuilder setCancelableOnTouchOutside(boolean cancelable) {
            return super.setCancelableOnTouchOutside(cancelable);
        }

        public SimpleDialogBuilder setOnDismissListener(DialogInterface.OnDismissListener onDismissListener) {
            mOnDismissListener = onDismissListener;
            return this;
        }
        public SimpleDialogBuilder setOnCancelListener(DialogInterface.OnCancelListener onCancelListener) {
            mOnCancelListener = onCancelListener;
            return this;
        }

        public SimpleDialogBuilder setOnShowListener(DialogInterface.OnShowListener onShowListener) {
            mOnShowListener = onShowListener;
            return this;
        }

        public SimpleDialogBuilder setPriority(int priority) {
            mPriority = priority;
            super.setPriority(priority);
            return this;
        }
        public int getPriority() {
            return mPriority;
        }

        public SimpleDialogBuilder setTag(String tag) {
            mTag = tag;
            super.setTag(mTag);
            return this;
        }

        public String getTag() {
            return mTag;
        }

        public SimpleDialogBuilder setView(View view) {
            mView = view;
            return this;
        }

        public SimpleDialogBuilder setTouchOutsideCancel(boolean touchOutsideCancel) {
            mIsTouchOutsideCancel = touchOutsideCancel;
            return this;
        }
        public BaseDialogFragment build() {
            return showAllowingStateLoss();
        }

        private PriorityDialog create() {
            final Bundle args = new Bundle();

            final PriorityDialog
                fragment = (PriorityDialog) Fragment.instantiate(mContext, mClass.getName(), args);

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

        public PriorityDialog show() {
            PriorityDialog fragment = create();
            initFragment(fragment);
            fragment.show(mFragmentManager, mTag);
            return fragment;
        }

        public PriorityDialog showAllowingStateLoss() {
            PriorityDialog fragment = create();
            initFragment(fragment);
            fragment.showAllowingStateLoss(mFragmentManager, mTag);
            return fragment;
        }
        private void initFragment(PriorityDialog fragment){
            fragment.mTitle = this.mTitle;
            fragment.mMessage = this.mMessage;
            fragment.mPositiveButtonText = this.mPositiveButtonText;
            fragment.mNegativeButtonText = this.mNegativeButtonText;
            fragment.mNeutralButtonText = this.mNeutralButtonText;
            fragment.mPriority = this.mPriority;
            fragment.mTag = this.mTag;
            fragment.mOnDismissListener = this.mOnDismissListener;
            fragment.mView = this.mView;
            fragment.mOnCancelListener = this.mOnCancelListener;
            fragment.mOnShowListener = this.mOnShowListener;
            fragment.mPositiveButtonListener = this.mPositiveButtonListener;
            fragment.mNegativeButtonListener = this.mNegativeButtonListener;
            fragment.mNeutralButtonListener = this.mNeutralButtonListener;
        }

    }
}
