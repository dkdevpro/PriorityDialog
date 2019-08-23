package com.dino.lib.dialog.progress

import android.app.Dialog
import android.app.ProgressDialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.dino.lib.dialog.BaseDialogBuilder
import com.dino.lib.dialog.BaseDialogFragment
import com.dino.lib.dialog.PriorityMode
import com.dino.lib.dialog.PriorityMode.LOW
import com.dino.lib.dialog.R

/**
 * Created by dineshkumar.m on 11/03/18.
 */

class PriorityProgressDialog : BaseDialogFragment() {

  private var mTitle: CharSequence? = null
  private var mMessage: CharSequence? = null
  private val mIsIndeterminate = false
  override var priority: PriorityMode = LOW
    private set
  override var customTag: String? = null
    private set
  private var mIsTouchOutsideCancel = true
  private var mIsCircleProgress: Boolean = false
  private var mOnDismissListener: DialogInterface.OnDismissListener? = null
  private var mOnCancelListener: DialogInterface.OnCancelListener? = null
  private var mOnShowListener: DialogInterface.OnShowListener? = null

  override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

    if (mIsCircleProgress) {
      //View view = getActivity().getLayoutInflater().inflate(R.layout.ui_progress_dialog, null);
      //
      val dialog = Dialog(activity, R.style.ProgressDialogPrimary)
      //dialog.setContentView(view);`
      dialog.setCanceledOnTouchOutside(mIsTouchOutsideCancel)

      return dialog
    } else {
      if (mMessage == null) {
        mMessage = getString(R.string.dialog_loading)
      }

      val progressDialog = ProgressDialog(activity)
      progressDialog.setMessage(mMessage)
      if (mTitle != null) progressDialog.setTitle(mTitle)

      progressDialog.isIndeterminate = mIsIndeterminate

      return progressDialog
    }
  }

  override fun onActivityCreated(savedInstanceState: Bundle?) {
    super.onActivityCreated(savedInstanceState)
    if (arguments == null) {
      throw IllegalArgumentException("use ProgressDialogBuilder to construct this dialog")
    }
  }

  override fun build(initialBuilder: Builder): Builder? {
    return null
  }

  class ProgressDialogBuilder(
    context: Context,
    fragmentManager: FragmentManager
  ) : BaseDialogBuilder<ProgressDialogBuilder>(
      context, fragmentManager, PriorityProgressDialog::class.java
  ) {

    private var mTitle: CharSequence? = null
    private var mMessage: CharSequence? = null
    private var mIsCircleProgress: Boolean = false
    private var mOnDismissListener: DialogInterface.OnDismissListener? = null
    private var mOnCancelListener: DialogInterface.OnCancelListener? = null
    private var mOnShowListener: DialogInterface.OnShowListener? = null

    override fun self(): ProgressDialogBuilder {
      return this
    }

    fun setTitle(titleResourceId: Int): ProgressDialogBuilder {
      mTitle = mContext.getString(titleResourceId)
      return this
    }

    fun setTitle(title: CharSequence): ProgressDialogBuilder {
      mTitle = title
      return this
    }

    fun setMessage(messageResourceId: Int): ProgressDialogBuilder {
      mMessage = mContext.getString(messageResourceId)
      return this
    }

    fun setMessage(message: CharSequence): ProgressDialogBuilder {
      mMessage = message
      return this
    }

    override fun setPriority(priority: PriorityMode): ProgressDialogBuilder {
      mPriority = priority
      super.setPriority(priority)
      return this
    }

    override fun getPriority(): PriorityMode {
      return mPriority
    }

    override fun setTag(tag: String): ProgressDialogBuilder {
      mTag = tag
      super.setTag(mTag)
      return this
    }

    override fun getTag(): String {
      return mTag
    }

    fun setCircleProgress(isCircle: Boolean): ProgressDialogBuilder {
      mIsCircleProgress = isCircle
      return this
    }

    fun setTouchOutsideCancel(touchOutsideCancel: Boolean): ProgressDialogBuilder {
      mCancelableOnTouchOutside = touchOutsideCancel
      return this
    }

    override fun setCancelable(cancelable: Boolean): ProgressDialogBuilder {
      mCancelable = cancelable
      return this
    }

    fun setOnDismissListener(onDismissListener: DialogInterface.OnDismissListener): ProgressDialogBuilder {
      mOnDismissListener = onDismissListener
      return this
    }

    fun setOnCancelListener(onCancelListener: DialogInterface.OnCancelListener): ProgressDialogBuilder {
      mOnCancelListener = onCancelListener
      return this
    }

    fun setOnShowListener(onShowListener: DialogInterface.OnShowListener): ProgressDialogBuilder {
      mOnShowListener = onShowListener
      return this
    }

    override fun build(): BaseDialogFragment {
      return showAllowingStateLoss()
    }

    private fun showAllowingStateLoss(): PriorityProgressDialog {
      val fragment = create()
      initFragment(fragment)
      fragment.showAllowingStateLoss(mFragmentManager, mTag)
      return fragment
    }

    private fun create(): PriorityProgressDialog {
      val args = Bundle()

      val fragment = Fragment.instantiate(mContext, mClass.name, args) as PriorityProgressDialog

      args.putBoolean(ARG_CANCELABLE_ON_TOUCH_OUTSIDE, mCancelableOnTouchOutside)

      args.putBoolean(ARG_USE_DARK_THEME, mUseDarkTheme)

      args.putBoolean(ARG_USE_LIGHT_THEME, mUseLightTheme)

      if (mTargetFragment != null) {
        fragment.setTargetFragment(mTargetFragment, mRequestCode)
      } else {
        args.putInt(ARG_REQUEST_CODE, mRequestCode)
      }
      fragment.isCancelable = mCancelable

      return fragment
    }

    private fun initFragment(fragment: PriorityProgressDialog) {
      fragment.mTitle = this.mTitle
      fragment.mMessage = this.mMessage
      fragment.customTag = this.mTag
      fragment.priority = this.mPriority
      fragment.mIsTouchOutsideCancel = this.mCancelableOnTouchOutside
      fragment.mIsCircleProgress = this.mIsCircleProgress
      fragment.priority = this.mPriority
      fragment.mOnDismissListener = this.mOnDismissListener
      fragment.mOnCancelListener = this.mOnCancelListener
      fragment.mOnShowListener = this.mOnShowListener

    }
  }

  companion object {

    fun createBuilder(
      context: Context,
      fragmentManager: FragmentManager
    ): ProgressDialogBuilder {
      return ProgressDialogBuilder(context, fragmentManager)
    }
  }
}
