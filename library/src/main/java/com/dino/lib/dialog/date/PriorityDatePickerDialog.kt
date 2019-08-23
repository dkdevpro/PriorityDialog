package com.dino.lib.dialog.date

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import android.text.TextUtils
import android.text.format.DateFormat
import android.view.View
import android.widget.DatePicker
import com.dino.lib.dialog.BaseDialogBuilder
import com.dino.lib.dialog.BaseDialogFragment
import com.dino.lib.dialog.PriorityMode
import com.dino.lib.dialog.PriorityMode.LOW
import com.dino.lib.dialog.progress.PriorityProgressDialog
import java.util.Calendar
import java.util.Date
import java.util.TimeZone

/**
 * Created by dineshkumar.m on 11/03/18.
 */

class PriorityDatePickerDialog : BaseDialogFragment() {

  override val customTag: String?
    get() = null

  override val priority: PriorityMode = LOW

  private var mDatePicker: DatePicker? = null
  private lateinit var mCalendar: Calendar

  private val title: CharSequence?
    get() = arguments!!.getCharSequence(ARG_TITLE)

  private val positiveButtonText: CharSequence?
    get() = arguments!!.getCharSequence(ARG_POSITIVE_BUTTON)

  private val negativeButtonText: CharSequence?
    get() = arguments!!.getCharSequence(ARG_NEGATIVE_BUTTON)

  val date: Date
    get() {
      mCalendar.set(Calendar.YEAR, mDatePicker!!.year)
      mCalendar.set(Calendar.MONTH, mDatePicker!!.month)
      mCalendar.set(Calendar.DAY_OF_MONTH, mDatePicker!!.dayOfMonth)
      return mCalendar.time
    }

  override fun build(builder: Builder): Builder? {
    val title = title
    if (!TextUtils.isEmpty(title)) {
      builder.setTitle(title)
    }

    val positiveButtonText = positiveButtonText
    if (!TextUtils.isEmpty(positiveButtonText)) {
      builder.setPositiveButton(positiveButtonText, View.OnClickListener { dismiss() })
    }

    val negativeButtonText = negativeButtonText
    if (!TextUtils.isEmpty(negativeButtonText)) {
      builder.setNegativeButton(negativeButtonText, View.OnClickListener { dismiss() })
    }
    //mDatePicker = (DatePicker) builder.getLayoutInflater().inflate(R.layout.pdl_datepicker, null);
    builder.setView(mDatePicker)

    val zone = TimeZone.getTimeZone(arguments!!.getString(ARG_ZONE))
    mCalendar = Calendar.getInstance(zone)
    mCalendar.timeInMillis = arguments!!.getLong(ARG_DATE, System.currentTimeMillis())
    mDatePicker!!.updateDate(
        mCalendar.get(Calendar.YEAR), mCalendar.get(Calendar.MONTH),
        mCalendar.get(Calendar.DAY_OF_MONTH)
    )
    return builder
  }

  class SimpleDialogBuilder(
    context: Context,
    fragmentManager: androidx.fragment.app.FragmentManager,
    clazz: Class<out PriorityDatePickerDialog>
  ) : BaseDialogBuilder<SimpleDialogBuilder>(context, fragmentManager, clazz) {
    internal var mDate = Date()
    internal var mTimeZone: String? = null

    private var mTitle: CharSequence? = null
    private var mPositiveButtonText: CharSequence? = null
    private var mNegativeButtonText: CharSequence? = null

    private val mShowDefaultButton = true
    private var m24h: Boolean = false

    init {
      m24h = DateFormat.is24HourFormat(context)
    }

    fun setTitle(titleResourceId: Int): SimpleDialogBuilder {
      mTitle = mContext.getString(titleResourceId)
      return this
    }

    fun setTitle(title: CharSequence): SimpleDialogBuilder {
      mTitle = title
      return this
    }

    fun setPositiveButtonText(textResourceId: Int): SimpleDialogBuilder {
      mPositiveButtonText = mContext.getString(textResourceId)
      return this
    }

    fun setPositiveButtonText(text: CharSequence): SimpleDialogBuilder {
      mPositiveButtonText = text
      return this
    }

    fun setNegativeButtonText(textResourceId: Int): SimpleDialogBuilder {
      mNegativeButtonText = mContext.getString(textResourceId)
      return this
    }

    fun setNegativeButtonText(text: CharSequence): SimpleDialogBuilder {
      mNegativeButtonText = text
      return this
    }

    fun setDate(date: Date): SimpleDialogBuilder {
      mDate = date
      return this
    }

    fun setTimeZone(zone: String): SimpleDialogBuilder {
      mTimeZone = zone
      return this
    }

    fun set24hour(state: Boolean): SimpleDialogBuilder {
      m24h = state
      return this
    }

    override fun self(): SimpleDialogBuilder {
      return this
    }

    override fun build(): BaseDialogFragment {
      return create()
    }

    private fun create(): PriorityProgressDialog {
      val args = Bundle()

      val fragment = androidx.fragment.app.Fragment.instantiate(mContext, mClass.name, args) as PriorityProgressDialog

      args.putBoolean(ARG_CANCELABLE_ON_TOUCH_OUTSIDE, mCancelableOnTouchOutside)

      args.putBoolean(ARG_USE_DARK_THEME, mUseDarkTheme)

      args.putBoolean(BaseDialogBuilder.ARG_USE_LIGHT_THEME, mUseLightTheme)

      fragment.setTargetFragment(mTargetFragment, mRequestCode)
      fragment.isCancelable = mCancelable

      return fragment
    }
  }

  companion object {
    protected val ARG_ZONE = "zone"
    protected val ARG_TITLE = "title"
    protected val ARG_POSITIVE_BUTTON = "positive_button"
    protected val ARG_NEGATIVE_BUTTON = "negative_button"
    protected val ARG_DATE = "date"
    protected val ARG_24H = "24h"

    fun createBuilder(
      context: Context,
      fragmentManager: androidx.fragment.app.FragmentManager
    ): SimpleDialogBuilder {
      return SimpleDialogBuilder(context, fragmentManager, PriorityDatePickerDialog::class.java)
    }
  }

}
