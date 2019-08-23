package com.dino.lib.dialog

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.dino.lib.dialog.PriorityMode.LOW

/**
 * Created by dineshkumar.m on 11/03/18.
 */

abstract class BaseDialogBuilder<T : BaseDialogBuilder<T>>(
  context: Context,
  protected val mFragmentManager: FragmentManager,
  protected val mClass: Class<out BaseDialogFragment>
) {
  protected var mTag = DEFAULT_TAG
  protected var mPriority: PriorityMode = LOW
  protected var mRequestCode = DEFAULT_REQUEST_CODE
  protected val mContext: Context = context.applicationContext
  protected var mTargetFragment: Fragment? = null
  protected var mCancelable = true
  protected var mCancelableOnTouchOutside = true
  protected var mUseDarkTheme = false
  protected var mUseLightTheme = false

  protected abstract fun self(): T

  abstract fun build(): BaseDialogFragment

  open fun setCancelable(cancelable: Boolean): T {
    mCancelable = cancelable
    return self()
  }

  open fun setCancelableOnTouchOutside(cancelable: Boolean): T {
    mCancelableOnTouchOutside = cancelable
    if (cancelable) {
      mCancelable = cancelable
    }
    return self()
  }

  fun setTargetFragment(
    fragment: Fragment,
    requestCode: Int
  ): T {
    mTargetFragment = fragment
    mRequestCode = requestCode
    return self()
  }

  fun setRequestCode(requestCode: Int): T {
    mRequestCode = requestCode
    return self()
  }

  open fun setTag(tag: String): T {
    mTag = tag
    return self()
  }

  open fun getTag(): String? {
    return mTag
  }

  open fun setPriority(priority: PriorityMode): T {
    mPriority = priority
    return self()
  }

  open fun getPriority(): PriorityMode {
    return mPriority
  }

  fun useDarkTheme(): T {
    mUseDarkTheme = true
    return self()
  }

  fun useLightTheme(): T {
    mUseLightTheme = true
    return self()
  }

  companion object {
    const val ARG_REQUEST_CODE = "request_code"
    const val ARG_CANCELABLE_ON_TOUCH_OUTSIDE = "cancelable_oto"
    private const val DEFAULT_TAG = "simple_dialog"
    private const val DEFAULT_REQUEST_CODE = -42
    const val ARG_USE_DARK_THEME = "usedarktheme"
    const val ARG_USE_LIGHT_THEME = "uselighttheme"
  }

}
