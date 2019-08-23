package com.dino.lib.dialog

import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import android.text.Html
import android.text.SpannedString
import android.text.TextUtils
import android.view.View
import com.dino.lib.dialog.PriorityMode.LOW

/**
 * Created by dineshkumar.m on 11/03/18.
 */

class PriorityDialog : BaseDialogFragment() {

  private var title: CharSequence? = null
  private var message: CharSequence? = null
  private var positiveButtonText: CharSequence? = null
  private var negativeButtonText: CharSequence? = null
  private var neutralButtonText: CharSequence? = null
  override var priority: PriorityMode = LOW
  override var customTag: String? = null
  private var mOnDismissListener: DialogInterface.OnDismissListener? = null
  private var mOnCancelListener: DialogInterface.OnCancelListener? = null
  private var mOnShowListener: DialogInterface.OnShowListener? = null
  private var mView: View? = null
  private var mPositiveButtonListener: View.OnClickListener? = null
  private var mNegativeButtonListener: View.OnClickListener? = null
  private var mNeutralButtonListener: View.OnClickListener? = null
  override fun onShow(dialog: DialogInterface) {
    super.onShow(dialog)
    if (mOnShowListener != null) {
      mOnShowListener!!.onShow(dialog)
    }
  }

  override fun onCancel(dialog: DialogInterface?) {
    super.onCancel(dialog)
    if (mOnCancelListener != null) {
      mOnCancelListener!!.onCancel(dialog)
    }
  }

  override fun onDismiss(dialog: DialogInterface?) {
    super.onDismiss(dialog)
    if (mOnDismissListener != null) {
      mOnDismissListener!!.onDismiss(dialog)
    }
  }

  override fun build(builder: Builder): Builder? {
    val title = title
    if (!TextUtils.isEmpty(title)) {
      builder.setTitle(title)
    }

    val message = message
    if (!TextUtils.isEmpty(message)) {
      builder.setMessage(message)
    }

    val positiveButtonText = positiveButtonText
    if (!TextUtils.isEmpty(positiveButtonText)) {
      builder.setPositiveButton(positiveButtonText, View.OnClickListener { view ->
        dismiss()
        if (mPositiveButtonListener != null) {
          mPositiveButtonListener!!.onClick(view)
        }
      })
    }

    val negativeButtonText = negativeButtonText
    if (!TextUtils.isEmpty(negativeButtonText)) {
      builder.setNegativeButton(negativeButtonText, View.OnClickListener { view ->
        dismiss()
        if (mNegativeButtonListener != null) {
          mNegativeButtonListener!!.onClick(view)
        }
      })
    }

    val neutralButtonText = neutralButtonText
    if (!TextUtils.isEmpty(neutralButtonText)) {
      builder.setNeutralButton(neutralButtonText, View.OnClickListener { view ->
        dismiss()
        if (mNeutralButtonListener != null) {
          mNeutralButtonListener!!.onClick(view)
        }
      })
    }
    builder.setView(mView)

    return builder
  }

  class SimpleDialogBuilder(
    context: Context,
    fragmentManager: androidx.fragment.app.FragmentManager,
    clazz: Class<out PriorityDialog>
  ) : BaseDialogBuilder<SimpleDialogBuilder>(context, fragmentManager, clazz) {

    private var mTitle: CharSequence? = null
    private var mMessage: CharSequence? = null
    private var mPositiveButtonText: CharSequence? = null
    private var mNegativeButtonText: CharSequence? = null
    private var mNeutralButtonText: CharSequence? = null
    private var priority: PriorityMode = LOW
    private var tag: String = ""
    private var mOnDismissListener: DialogInterface.OnDismissListener? = null
    private var mOnCancelListener: DialogInterface.OnCancelListener? = null
    private var mOnShowListener: DialogInterface.OnShowListener? = null
    private var mIsTouchOutsideCancel: Boolean = false
    private var mView: View? = null
    private val mIsCancelable: Boolean = false
    private var mPositiveButtonListener: View.OnClickListener? = null
    private var mNegativeButtonListener: View.OnClickListener? = null
    private var mNeutralButtonListener: View.OnClickListener? = null

    override fun self(): SimpleDialogBuilder {
      return this
    }

    fun setTitle(titleResourceId: Int): SimpleDialogBuilder {
      mTitle = mContext.getString(titleResourceId)
      return this
    }

    fun setTitle(title: CharSequence): SimpleDialogBuilder {
      mTitle = title
      return this
    }

    fun setMessage(messageResourceId: Int): SimpleDialogBuilder {
      mMessage = mContext.getText(messageResourceId)
      return this
    }

    fun setMessage(
      resourceId: Int,
      vararg formatArgs: Any
    ): SimpleDialogBuilder {
      mMessage = Html.fromHtml(
          String.format(Html.toHtml(SpannedString(mContext.getText(resourceId))), *formatArgs)
      )
      return this
    }

    fun setMessage(message: CharSequence): SimpleDialogBuilder {
      mMessage = message
      return this
    }

    fun setPositiveButton(
      text: CharSequence,
      listener: View.OnClickListener? = null
    ): SimpleDialogBuilder {
      mPositiveButtonText = text
      mPositiveButtonListener = listener
      return this
    }

    fun setNegativeButton(
      text: CharSequence,
      listener: View.OnClickListener? = null
    ): SimpleDialogBuilder {
      mNegativeButtonText = text
      mNegativeButtonListener = listener
      return this
    }

    fun setNeutralButton(
      text: CharSequence,
      listener: View.OnClickListener
    ): SimpleDialogBuilder {
      mNeutralButtonText = text
      mNeutralButtonListener = listener
      return this
    }

    fun setPositiveButton(
      textResourceId: Int,
      listener: View.OnClickListener
    ): SimpleDialogBuilder {
      mPositiveButtonText = mContext.getString(textResourceId)
      mPositiveButtonListener = listener
      return this
    }

    fun setNegativeButton(
      textResourceId: Int,
      listener: View.OnClickListener
    ): SimpleDialogBuilder {
      mNegativeButtonText = mContext.getString(textResourceId)
      mNegativeButtonListener = listener
      return this
    }

    fun setNeutralButton(
      textResourceId: Int,
      listener: View.OnClickListener
    ): SimpleDialogBuilder {
      mNeutralButtonText = mContext.getString(textResourceId)
      mNeutralButtonListener = listener
      return this
    }

    override fun setCancelable(cancelable: Boolean): SimpleDialogBuilder {
      return super.setCancelable(cancelable)
    }

    override fun setCancelableOnTouchOutside(cancelable: Boolean): SimpleDialogBuilder {
      return super.setCancelableOnTouchOutside(cancelable)
    }

    fun setOnDismissListener(onDismissListener: DialogInterface.OnDismissListener): SimpleDialogBuilder {
      mOnDismissListener = onDismissListener
      return this
    }

    fun setOnCancelListener(onCancelListener: DialogInterface.OnCancelListener): SimpleDialogBuilder {
      mOnCancelListener = onCancelListener
      return this
    }

    fun setOnShowListener(onShowListener: DialogInterface.OnShowListener): SimpleDialogBuilder {
      mOnShowListener = onShowListener
      return this
    }

    override fun setPriority(priority: PriorityMode): SimpleDialogBuilder {
      this.priority = priority
      super.setPriority(priority)
      return this
    }

    override fun getPriority(): PriorityMode {
      return priority
    }

    override fun setTag(tag: String): SimpleDialogBuilder {
      this.tag = tag
      super.setTag(this.tag)
      return this
    }

    override fun getTag(): String? {
      return tag
    }

    fun setView(view: View): SimpleDialogBuilder {
      mView = view
      return this
    }

    fun setTouchOutsideCancel(touchOutsideCancel: Boolean): SimpleDialogBuilder {
      mIsTouchOutsideCancel = touchOutsideCancel
      return this
    }

    override fun build(): BaseDialogFragment {
      return showAllowingStateLoss()
    }

    private fun create(): PriorityDialog {
      val args = Bundle()

      val fragment = androidx.fragment.app.Fragment.instantiate(mContext, mClass.name, args) as PriorityDialog

      args.putBoolean(ARG_CANCELABLE_ON_TOUCH_OUTSIDE, mCancelableOnTouchOutside)

      args.putBoolean(ARG_USE_DARK_THEME, mUseDarkTheme)

      args.putBoolean(ARG_USE_LIGHT_THEME, mUseLightTheme)

      fragment.setTargetFragment(mTargetFragment, mRequestCode)
      fragment.isCancelable = mCancelable

      return fragment
    }

    fun show(): PriorityDialog {
      val fragment = create()
      initFragment(fragment)
      fragment.show(mFragmentManager, tag)
      return fragment
    }

    fun showAllowingStateLoss(): PriorityDialog {
      val fragment = create()
      initFragment(fragment)
      fragment.showAllowingStateLoss(mFragmentManager, tag)
      return fragment
    }

    private fun initFragment(fragment: PriorityDialog) {
      fragment.title = this.mTitle
      fragment.message = this.mMessage
      fragment.positiveButtonText = this.mPositiveButtonText
      fragment.negativeButtonText = this.mNegativeButtonText
      fragment.neutralButtonText = this.mNeutralButtonText
      fragment.priority = this.priority
      fragment.customTag = this.tag
      fragment.mOnDismissListener = this.mOnDismissListener
      fragment.mView = this.mView
      fragment.mOnCancelListener = this.mOnCancelListener
      fragment.mOnShowListener = this.mOnShowListener
      fragment.mPositiveButtonListener = this.mPositiveButtonListener
      fragment.mNegativeButtonListener = this.mNegativeButtonListener
      fragment.mNeutralButtonListener = this.mNeutralButtonListener
    }

  }

  companion object {

    fun createBuilder(
      context: Context,
      fragmentManager: androidx.fragment.app.FragmentManager
    ): SimpleDialogBuilder {
      return SimpleDialogBuilder(context, fragmentManager, PriorityDialog::class.java)
    }
  }
}
