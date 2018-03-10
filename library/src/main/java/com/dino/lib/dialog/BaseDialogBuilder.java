package com.dino.lib.dialog;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

/**
 * Created by dineshkumar.m on 11/03/18.
 */

public abstract class BaseDialogBuilder<T extends BaseDialogBuilder<T>> {
    protected final static String ARG_REQUEST_CODE = "request_code";
    protected final static String ARG_CANCELABLE_ON_TOUCH_OUTSIDE = "cancelable_oto";
    private final static String DEFAULT_TAG = "simple_dialog";
    protected String mTag = DEFAULT_TAG;
    protected int mPriority;
    private final static int DEFAULT_REQUEST_CODE = -42;
    protected int mRequestCode = DEFAULT_REQUEST_CODE;
    protected static String ARG_USE_DARK_THEME = "usedarktheme";
    protected static String ARG_USE_LIGHT_THEME = "uselighttheme";
    protected final Context mContext;
    protected final FragmentManager mFragmentManager;
    protected final Class<? extends BaseDialogFragment> mClass;
    protected Fragment mTargetFragment;
    protected boolean mCancelable = true;
    protected boolean mCancelableOnTouchOutside = true;
    protected boolean mUseDarkTheme = false;
    protected boolean mUseLightTheme = false;

    public BaseDialogBuilder(Context context, FragmentManager fragmentManager, Class<? extends BaseDialogFragment> clazz) {
        mFragmentManager = fragmentManager;
        mContext = context.getApplicationContext();
        mClass = clazz;
    }

    protected abstract T self();

    protected abstract BaseDialogFragment build();

    public T setCancelable(boolean cancelable) {
        mCancelable = cancelable;
        return self();
    }

    public T setCancelableOnTouchOutside(boolean cancelable) {
        mCancelableOnTouchOutside = cancelable;
        if (cancelable) {
            mCancelable = cancelable;
        }
        return self();
    }

    public T setTargetFragment(Fragment fragment, int requestCode) {
        mTargetFragment = fragment;
        mRequestCode = requestCode;
        return self();
    }

    public T setRequestCode(int requestCode) {
        mRequestCode = requestCode;
        return self();
    }

    public T setTag(String tag) {
        mTag = tag;
        return self();
    }

    public String getTag(){
        return mTag;
    }
    public T setPriority(int priority) {
        mPriority = priority;
        return self();
    }

    public int getPriority(){
        return mPriority;
    }


    public T useDarkTheme() {
        mUseDarkTheme = true;
        return self();
    }

    public T useLightTheme() {
        mUseLightTheme = true;
        return self();
    }

}
