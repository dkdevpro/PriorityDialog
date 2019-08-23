package com.dino.lib.dialog

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.text.TextUtils
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import android.widget.AdapterView
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.ListAdapter
import android.widget.ListView
import android.widget.ScrollView
import android.widget.TextView
import androidx.annotation.StyleRes
import java.util.ArrayList
import java.util.Collections

/**
 * Created by dineshkumar.m on 11/03/18.
 */

abstract class BaseDialogFragment : androidx.fragment.app.DialogFragment(), DialogInterface.OnShowListener {

  protected var mRequestCode: Int = 0

  abstract val customTag: String?

  abstract val priority: PriorityMode

  /**
   * This method resolves the current theme declared in the manifest
   */
  //Reading attr value from current theme
  //Passing the resource ID to TypedArray to get the attribute value
  //Resource not found , so sticking to light theme
  private val isActivityThemeLight: Boolean
    get() {
      return try {
        val `val` = TypedValue()
        activity?.theme?.resolveAttribute(R.attr.isLightTheme, `val`, true)
        val styledAttributes =
          activity?.obtainStyledAttributes(`val`.data, intArrayOf(R.attr.isLightTheme))
        val lightTheme = styledAttributes?.getBoolean(0, false) ?: false
        styledAttributes?.recycle()

        lightTheme
      } catch (e: RuntimeException) {
        true
      }

    }

  override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
    val theme = resolveTheme()
    val dialog = Dialog(activity, theme)

    //Bundle args = getArguments();
    /*if (args != null) {
            dialog.setCanceledOnTouchOutside(
                    args.getBoolean(BaseDialogBuilder.ARG_CANCELABLE_ON_TOUCH_OUTSIDE));
        }*/
    dialog.setOnShowListener(this)
    dialog.setOnDismissListener(this)
    dialog.setOnCancelListener(this)
    return dialog
  }

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    val builder = Builder(activity, inflater, container)
    return if (build(builder) != null) build(builder)!!.create() else super.onCreateView(
        inflater, container, savedInstanceState
    )
  }

  override fun onActivityCreated(savedInstanceState: Bundle?) {
    super.onActivityCreated(savedInstanceState)
    val targetFragment = targetFragment
    if (targetFragment != null) {
      mRequestCode = targetRequestCode
    } else {
      val args = arguments
      if (args != null) {
        mRequestCode = args.getInt(BaseDialogBuilder.ARG_REQUEST_CODE, 0)
      }
    }
  }

  protected abstract fun build(initialBuilder: Builder): Builder?

  override fun onDestroyView() {
    // bug in the compatibility library
    if (dialog != null && retainInstance) {
      dialog.setDismissMessage(null)
    }
    super.onDestroyView()
  }

  fun showAllowingStateLoss(
    manager: androidx.fragment.app.FragmentManager,
    tag: String
  ) {
    val ft = manager.beginTransaction()
    ft.add(this, tag)
    ft.commitAllowingStateLoss()
  }

  override fun onShow(dialog: DialogInterface) {
    if (view != null) {
      val vMessageScrollView = view!!.findViewById<View>(R.id.pdl_message_scrollview) as ScrollView
      val vListView = view!!.findViewById<View>(R.id.pdl_list) as ListView
      val vCustomViewNoScrollView = view!!.findViewById<View>(R.id.pdl_custom) as FrameLayout
      var customViewNoScrollViewScrollable = false
      if (vCustomViewNoScrollView.childCount > 0) {
        val firstChild = vCustomViewNoScrollView.getChildAt(0)
        if (firstChild is ViewGroup) {
          customViewNoScrollViewScrollable = isScrollable(firstChild)
        }
      }
      val listViewScrollable = isScrollable(vListView)
      val messageScrollable = isScrollable(vMessageScrollView)
      val scrollable = listViewScrollable || messageScrollable || customViewNoScrollViewScrollable
      modifyButtonsBasedOnScrollableContent(scrollable)
    }
  }

  protected fun <T> getDialogListeners(listenerInterface: Class<T>): List<T> {
    val targetFragment = targetFragment
    val listeners = ArrayList<T>(2)
    if (targetFragment != null && listenerInterface.isAssignableFrom(targetFragment.javaClass)) {
      listeners.add(targetFragment as T)
    }
    if (activity != null && listenerInterface.isAssignableFrom(activity!!.javaClass)) {
      listeners.add(activity as T)
    }
    return Collections.unmodifiableList(listeners)
  }

  private fun modifyButtonsBasedOnScrollableContent(scrollable: Boolean) {
    if (view == null) {
      return
    }
    val vButtonDivider = view!!.findViewById<View>(R.id.pdl_button_divider)
    val vButtonsBottomSpace = view!!.findViewById<View>(R.id.pdl_buttons_bottom_space)
    val vDefaultButtons = view!!.findViewById<View>(R.id.pdl_buttons_default)
    val vStackedButtons = view!!.findViewById<View>(R.id.pdl_buttons_stacked)
    if (vDefaultButtons.visibility == View.GONE && vStackedButtons.visibility == View.GONE) {
      // no buttons
      vButtonDivider.visibility = View.GONE
      vButtonsBottomSpace.visibility = View.GONE
    } else if (scrollable) {
      vButtonDivider.visibility = View.VISIBLE
      vButtonsBottomSpace.visibility = View.GONE
    } else {
      vButtonDivider.visibility = View.GONE
      vButtonsBottomSpace.visibility = View.VISIBLE
    }
  }

  private fun isScrollable(listView: ViewGroup): Boolean {
    var totalHeight = 0
    for (i in 0 until listView.childCount) {
      totalHeight += listView.getChildAt(i)
          .measuredHeight
    }
    return listView.measuredHeight < totalHeight
  }

  /**
   * Resolves the theme to be used for the dialog.
   *
   * @return The theme.
   */
  @StyleRes private fun resolveTheme(): Int {
    // First check if getTheme() returns some usable theme.
    val theme = theme
    if (theme != 0) {
      return theme
    }

    // Get the light/dark attribute from the Activity's Theme.
    var useLightTheme = isActivityThemeLight

    // Now check if developer overrides the Activity's Theme with an argument.
    val args = arguments
    if (args != null) {
      if (args.getBoolean(BaseDialogBuilder.ARG_USE_DARK_THEME)) {
        // Developer is explicitly using the dark theme.
        useLightTheme = false
      } else if (args.getBoolean(BaseDialogBuilder.ARG_USE_LIGHT_THEME)) {
        // Developer is explicitly using the light theme.
        useLightTheme = true
      }
    }

    return if (useLightTheme) R.style.AppTheme_Dialog else R.style.AppTheme_Dialog
  }

  /**
   * Custom dialog builder
   */
  protected class Builder(
    private val mContext: Context?,
    private val layoutInflater: LayoutInflater?,
    private val mContainer: ViewGroup?
  ) {

    private var mTitle: CharSequence? = null

    private var mPositiveButtonText: CharSequence? = null

    private var mPositiveButtonListener: View.OnClickListener? = null

    private var mNegativeButtonText: CharSequence? = null

    private var mNegativeButtonListener: View.OnClickListener? = null

    private var mNeutralButtonText: CharSequence? = null

    private var mNeutralButtonListener: View.OnClickListener? = null

    private var mMessage: CharSequence? = null

    private var mCustomView: View? = null

    private var mListAdapter: ListAdapter? = null

    private var mListCheckedItemIdx: Int = 0

    private var mChoiceMode: Int = 0

    private var mListCheckedItemMultipleIds: IntArray? = null

    private var mOnItemClickListener: AdapterView.OnItemClickListener? = null

    fun setTitle(titleId: Int): Builder {
      this.mTitle = mContext?.getText(titleId)
      return this
    }

    fun setTitle(title: CharSequence?): Builder {
      this.mTitle = title
      return this
    }

    fun setPositiveButton(
      textId: Int,
      listener: View.OnClickListener? = null
    ): Builder {
      mPositiveButtonText = mContext?.getText(textId)
      mPositiveButtonListener = listener
      return this
    }

    fun setPositiveButton(
      text: CharSequence?,
      listener: View.OnClickListener? = null
    ): Builder {
      mPositiveButtonText = text
      mPositiveButtonListener = listener
      return this
    }

    fun setNegativeButton(
      textId: Int,
      listener: View.OnClickListener
    ): Builder {
      mNegativeButtonText = mContext?.getText(textId)
      mNegativeButtonListener = listener
      return this
    }

    fun setNegativeButton(
      text: CharSequence?,
      listener: View.OnClickListener
    ): Builder {
      mNegativeButtonText = text
      mNegativeButtonListener = listener
      return this
    }

    fun setNeutralButton(
      textId: Int,
      listener: View.OnClickListener
    ): Builder {
      mNeutralButtonText = mContext?.getText(textId)
      mNeutralButtonListener = listener
      return this
    }

    fun setNeutralButton(
      text: CharSequence?,
      listener: View.OnClickListener
    ): Builder {
      mNeutralButtonText = text
      mNeutralButtonListener = listener
      return this
    }

    fun setMessage(messageId: Int): Builder {
      mMessage = mContext?.getText(messageId)
      return this
    }

    fun setMessage(message: CharSequence?): Builder {
      mMessage = message
      return this
    }

    fun setItems(
      listAdapter: ListAdapter,
      checkedItemIds: IntArray?,
      choiceMode: Int,
      listener: AdapterView.OnItemClickListener
    ): Builder {
      mListAdapter = listAdapter
      mListCheckedItemMultipleIds = checkedItemIds
      mOnItemClickListener = listener
      mChoiceMode = choiceMode
      mListCheckedItemIdx = -1
      return this
    }

    /**
     * Set list
     *
     * @param checkedItemIdx Item check by default, -1 if no item should be checked
     */
    fun setItems(
      listAdapter: ListAdapter,
      checkedItemIdx: Int,
      listener: AdapterView.OnItemClickListener
    ): Builder {
      mListAdapter = listAdapter
      mOnItemClickListener = listener
      mListCheckedItemIdx = checkedItemIdx
      mChoiceMode = AbsListView.CHOICE_MODE_NONE
      return this
    }

    fun setView(view: View?): Builder {
      mCustomView = view
      return this
    }

    fun create(): View {

      val content = layoutInflater?.inflate(R.layout.view_dialog, mContainer, false) as LinearLayout
      val vTitle = content.findViewById<TextView>(R.id.pdl_title)
      val vMessage = content.findViewById<TextView>(R.id.pdl_message)
      val vCustomView = content.findViewById<FrameLayout>(R.id.pdl_custom)
      val vPositiveButton = content.findViewById<TextView>(R.id.pdl_button_positive)
      val vNegativeButton = content.findViewById<TextView>(R.id.pdl_button_negative)
      val vNeutralButton = content.findViewById<TextView>(R.id.pdl_button_neutral)
      val vPositiveButtonStacked = content.findViewById<TextView>(R.id.pdl_button_positive_stacked)
      val vNegativeButtonStacked = content.findViewById<TextView>(R.id.pdl_button_negative_stacked)
      val vNeutralButtonStacked = content.findViewById<TextView>(R.id.pdl_button_neutral_stacked)
      val vButtonsDefault = content.findViewById<View>(R.id.pdl_buttons_default)
      val vButtonsStacked = content.findViewById<View>(R.id.pdl_buttons_stacked)
      val vList = content.findViewById<ListView>(R.id.pdl_list)


      set(vTitle, mTitle)
      set(vMessage, mMessage)
      setPaddingOfTitleAndMessage(vTitle, vMessage)

      if (mCustomView != null) {
        vCustomView.addView(mCustomView)
      }
      if (mListAdapter != null) {
        vList.adapter = mListAdapter
        vList.onItemClickListener = mOnItemClickListener
        if (mListCheckedItemIdx != -1) {
          vList.setSelection(mListCheckedItemIdx)
        }
        if (mListCheckedItemMultipleIds != null) {
          vList.choiceMode = mChoiceMode
          for (i in mListCheckedItemMultipleIds!!) {
            vList.setItemChecked(i, true)
          }
        }
      }

      if (shouldStackButtons()) {
        set(vPositiveButtonStacked, mPositiveButtonText, mPositiveButtonListener)
        set(vNegativeButtonStacked, mNegativeButtonText, mNegativeButtonListener)
        set(vNeutralButtonStacked, mNeutralButtonText, mNeutralButtonListener)
        vButtonsDefault.visibility = View.GONE
        vButtonsStacked.visibility = View.VISIBLE
      } else {
        set(vPositiveButton, mPositiveButtonText, mPositiveButtonListener)
        set(vNegativeButton, mNegativeButtonText, mNegativeButtonListener)
        set(vNeutralButton, mNeutralButtonText, mNeutralButtonListener)
        vButtonsDefault.visibility = View.VISIBLE
        vButtonsStacked.visibility = View.GONE
      }
      if (TextUtils.isEmpty(mPositiveButtonText) && TextUtils.isEmpty(
              mNegativeButtonText
          ) && TextUtils.isEmpty(mNeutralButtonText)
      ) {
        vButtonsDefault.visibility = View.GONE
      }

      return content
    }

    /**
     * Padding is different if there is only title, only message or both.
     */
    private fun setPaddingOfTitleAndMessage(
      vTitle: TextView,
      vMessage: TextView
    ) {
      val grid6 = mContext!!.resources.getDimensionPixelSize(R.dimen.grid_6)
      val grid4 = mContext.resources.getDimensionPixelSize(R.dimen.grid_4)
      if (!TextUtils.isEmpty(mTitle) && !TextUtils.isEmpty(mMessage)) {
        vTitle.setPadding(grid6, grid6, grid6, grid4)
        vMessage.setPadding(grid6, 0, grid6, grid4)
      } else if (TextUtils.isEmpty(mTitle)) {
        vMessage.setPadding(grid6, grid4, grid6, grid4)
      } else if (TextUtils.isEmpty(mMessage)) {
        vTitle.setPadding(grid6, grid6, grid6, grid4)
      }
    }

    private fun shouldStackButtons(): Boolean {
      return (shouldStackButton(mPositiveButtonText) || shouldStackButton(
          mNegativeButtonText
      ) || shouldStackButton(mNeutralButtonText))
    }

    private fun shouldStackButton(text: CharSequence?): Boolean {
      val MAX_BUTTON_CHARS = 12 // based on observation, could be done better with measuring widths
      return text != null && text.length > MAX_BUTTON_CHARS
    }

    private operator fun set(
      button: TextView,
      text: CharSequence?,
      listener: View.OnClickListener?
    ) {
      set(button, text)
      if (listener != null) {
        button.setOnClickListener(listener)
      }
    }

    private operator fun set(
      textView: TextView,
      text: CharSequence?
    ) {
      if (text != null) {
        textView.text = text
      } else {
        textView.visibility = View.GONE
      }
    }
  }
}
