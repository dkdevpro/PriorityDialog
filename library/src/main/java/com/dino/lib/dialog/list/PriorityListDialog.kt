package com.dino.lib.dialog.list

import android.content.Context
import android.content.res.Resources
import android.os.Bundle
import android.text.TextUtils
import android.util.SparseBooleanArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListAdapter
import android.widget.ListView
import android.widget.TextView
import androidx.annotation.IntDef
import com.dino.lib.dialog.BaseDialogBuilder
import com.dino.lib.dialog.BaseDialogFragment
import com.dino.lib.dialog.PriorityMode
import com.dino.lib.dialog.PriorityMode.LOW
import com.dino.lib.dialog.R
import java.util.Arrays

/**
 * Created by dineshkumar.m on 11/03/18.
 */

class PriorityListDialog : BaseDialogFragment() {

  private var title: CharSequence? = null
  private var items: Array<CharSequence>? = null
  @ChoiceMode @get:ChoiceMode private var mode: Int = 0
  private var checkedItems: IntArray? = intArrayOf()
  private var negativeButtonText: CharSequence? = null
  private var positiveButtonText: CharSequence? = null
  private var mTag: String? = null
  private var mListDialogListener: ListDialogListener? = null
  private var mMultiChoiceListDialogListener: MultiChoiceListDialogListener? = null

  override val customTag: String?
    get() = null

  override val priority: PriorityMode = LOW


  override fun onActivityCreated(savedInstanceState: Bundle?) {
    super.onActivityCreated(savedInstanceState)
    if (arguments == null) {
      throw IllegalArgumentException(
          "use SimpleListDialogBuilder to construct this dialog"
      )
    }
  }

  private fun prepareAdapter(itemLayoutId: Int): ListAdapter {
    return object : ArrayAdapter<Any>(
        activity, itemLayoutId, R.id.pdl_text, items!!
    ) {

      /**
       * Overriding default implementation because it ignores current light/dark theme.
       */
      override fun getView(
        position: Int,
        convertView: View?,
        parent: ViewGroup
      ): View {
        var convertView = convertView
        if (convertView == null) {
          convertView = LayoutInflater.from(parent.context)
              .inflate(itemLayoutId, parent, false)
        }
        val t = convertView!!.findViewById<View>(R.id.pdl_text) as TextView
        if (t != null) {
          t.text = getItem(position) as CharSequence
        }
        return convertView
      }
    }
  }

  private fun buildMultiChoice(builder: Builder) {
    builder.setItems(prepareAdapter(R.layout.view_list_item_multichoice), checkedItems,
        AbsListView.CHOICE_MODE_MULTIPLE, AdapterView.OnItemClickListener { parent, _, _, _ ->
      val checkedPositions = (parent as ListView).checkedItemPositions
      checkedItems = asIntArray(checkedPositions)
    })
  }

  private fun buildSingleChoice(builder: Builder) {
    builder.setItems(prepareAdapter(R.layout.view_list_item_singlechoice), checkedItems,
        AbsListView.CHOICE_MODE_SINGLE, AdapterView.OnItemClickListener { parent, _, _, _ ->
      val checkedPositions = (parent as ListView).checkedItemPositions
      checkedItems = asIntArray(checkedPositions)
    })
  }

  private fun buildNormalChoice(builder: Builder) {
    builder.setItems(prepareAdapter(R.layout.view_list_item), -1,
        AdapterView.OnItemClickListener { _, _, position, id ->
          /*for (IListDialogListener listener : getSingleDialogListeners()) {
                                listener.onListItemSelected(getItems()[position], position, mRequestCode);
                            }*/
          dismiss()
          if (mListDialogListener != null) {
            mListDialogListener!!.onListItemSelected(items!![position], position, mRequestCode)
          }
        })
  }

  override fun build(builder: Builder): Builder? {
    val title = title
    if (!TextUtils.isEmpty(title)) {
      builder.setTitle(title)
    }

    if (!TextUtils.isEmpty(negativeButtonText)) {
      builder.setNegativeButton(negativeButtonText, View.OnClickListener { dismiss() })
    }

    if (mode != AbsListView.CHOICE_MODE_NONE) {
      var positiveButtonClickListener: View.OnClickListener? = null
      when (mode) {
        AbsListView.CHOICE_MODE_MULTIPLE -> positiveButtonClickListener = View.OnClickListener {
          val checkedPositions = checkedItems
          val items = items
          val checkedValues = checkedPositions?.size?.let { it1 -> arrayOfNulls<CharSequence>(it1) }
          var i = 0
          if (checkedPositions != null) {
            for (checkedPosition in checkedPositions) {
              if (checkedPosition >= 0 && checkedPosition < items!!.size) {
                checkedValues?.set(i++, items[checkedPosition])
              }
            }
          }
          dismiss()
          if (mMultiChoiceListDialogListener != null) {
            mMultiChoiceListDialogListener!!.onListItemsSelected(
                checkedValues, checkedPositions, mRequestCode
            )
          }
        }
        AbsListView.CHOICE_MODE_SINGLE -> positiveButtonClickListener = View.OnClickListener {
          var selectedPosition = -1
          val checkedPositions = checkedItems
          val items = items
          if (checkedPositions != null) {
            for (i in checkedPositions) {
              if (i >= 0 && i < items!!.size) {
                //1st valid value
                selectedPosition = i
                break
              }
            }
          }

          dismiss()
          if (selectedPosition != -1) {
            if (mListDialogListener != null) {
              mListDialogListener!!.onListItemSelected(
                  items!![selectedPosition], selectedPosition, mRequestCode
              )
            }
          } else {
            //for (ISimpleDialogCancelListener listener : getCancelListeners()) {
            //    listener.onCancelled(mRequestCode);
            //}
          }
        }
      }

      var positiveButton = positiveButtonText
      if (TextUtils.isEmpty(positiveButtonText)) {
        //we always need confirm button when CHOICE_MODE_SINGLE or CHOICE_MODE_MULTIPLE
        positiveButton = getString(android.R.string.ok)
      }
      builder.setPositiveButton(positiveButton, positiveButtonClickListener)
    }

    // prepare list and its item click listener
    val items = items
    if (items != null && items.size > 0) {
      when (mode) {
        AbsListView.CHOICE_MODE_MULTIPLE -> buildMultiChoice(builder)
        AbsListView.CHOICE_MODE_SINGLE -> buildSingleChoice(builder)
        AbsListView.CHOICE_MODE_NONE -> buildNormalChoice(builder)
      }
    }

    return builder
  }

  @IntDef(
      AbsListView.CHOICE_MODE_MULTIPLE, AbsListView.CHOICE_MODE_SINGLE,
      AbsListView.CHOICE_MODE_NONE
  )
  annotation class ChoiceMode

  class SimpleListDialogBuilder(
    context: Context,
    fragmentManager: androidx.fragment.app.FragmentManager
  ) : BaseDialogBuilder<SimpleListDialogBuilder>(
      context, fragmentManager, PriorityListDialog::class.java
  ) {

    private var title: CharSequence? = null

    private var items: Array<CharSequence>? = null

    @ChoiceMode private var mode: Int = 0
    private var checkedItems: IntArray? = null

    private var cancelButtonText: CharSequence? = null
    private var confirmButtonText: CharSequence? = null
    private var iListDialogListener: ListDialogListener? = null
    private var iMultiChoiceListDialogListener: MultiChoiceListDialogListener? = null

    private val resources: Resources
      get() = mContext.resources

    override fun self(): SimpleListDialogBuilder {
      return this
    }

    fun setTitle(title: CharSequence): SimpleListDialogBuilder {
      this.title = title
      return this
    }

    fun setTitle(titleResID: Int): SimpleListDialogBuilder {
      this.title = resources.getString(titleResID)
      return this
    }

    fun setCheckedItems(positions: IntArray): SimpleListDialogBuilder {
      this.checkedItems = positions
      return this
    }

    fun setSelectedItem(position: Int): SimpleListDialogBuilder {
      this.checkedItems = intArrayOf(position)
      return this
    }

    fun setChoiceMode(@ChoiceMode choiceMode: Int): SimpleListDialogBuilder {
      this.mode = choiceMode
      return this
    }

    fun setItems(items: Array<CharSequence>): SimpleListDialogBuilder {
      this.items = items
      return this
    }

    /*fun setItems(itemsArrayResID: Int): SimpleListDialogBuilder {
      this.items = resources.getStringArray(itemsArrayResID)
      return this
    }*/

    fun setConfirmButtonText(text: CharSequence): SimpleListDialogBuilder {
      this.confirmButtonText = text
      return this
    }

    fun setConfirmButtonText(confirmBttTextResID: Int): SimpleListDialogBuilder {
      this.confirmButtonText = resources.getString(confirmBttTextResID)
      return this
    }

    fun setCancelButtonText(text: CharSequence): SimpleListDialogBuilder {
      this.cancelButtonText = text
      return this
    }

    fun setCancelButtonText(cancelBttTextResID: Int): SimpleListDialogBuilder {
      this.cancelButtonText = resources.getString(cancelBttTextResID)
      return this
    }

    override fun setTag(tag: String): SimpleListDialogBuilder {
      this.mTag = tag
      return this
    }

    fun setOnListItemSelected(listener: ListDialogListener): SimpleListDialogBuilder {
      this.iListDialogListener = listener
      return this
    }

    fun setOnListItemsSelected(listener: MultiChoiceListDialogListener): SimpleListDialogBuilder {
      this.iMultiChoiceListDialogListener = listener
      return this
    }

    override fun build(): PriorityListDialog {
      return showAllowingStateLoss()
    }

    private fun create(): PriorityListDialog {

      val args = Bundle()

      val fragment = androidx.fragment.app.Fragment.instantiate(mContext, mClass.name, args) as PriorityListDialog
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

    fun show(): PriorityListDialog {
      val fragment = create()
      initFragment(fragment)
      fragment.show(mFragmentManager, mTag)
      return fragment
    }

    private fun showAllowingStateLoss(): PriorityListDialog {
      val fragment = create()
      initFragment(fragment)
      fragment.showAllowingStateLoss(mFragmentManager, mTag)
      return fragment
    }

    private fun initFragment(fragment: PriorityListDialog) {

      fragment.title = this.title
      fragment.items = this.items
      fragment.mode = this.mode
      fragment.checkedItems = this.checkedItems
      fragment.negativeButtonText = this.cancelButtonText
      fragment.positiveButtonText = this.confirmButtonText
      fragment.mListDialogListener = this.iListDialogListener
      fragment.mMultiChoiceListDialogListener = this.iMultiChoiceListDialogListener
      fragment.mTag = this.mTag
    }
  }

  companion object {

    fun createBuilder(
      context: Context,
      fragmentManager: androidx.fragment.app.FragmentManager
    ): SimpleListDialogBuilder {
      return SimpleListDialogBuilder(context, fragmentManager)
    }

    private fun asIntArray(checkedItems: SparseBooleanArray): IntArray {
      var checked = 0
      // compute number of items
      for (i in 0 until checkedItems.size()) {
        val key = checkedItems.keyAt(i)
        if (checkedItems.get(key)) {
          ++checked
        }
      }

      val array = IntArray(checked)
      //add indexes that are checked
      var i = 0
      var j = 0
      while (i < checkedItems.size()) {
        val key = checkedItems.keyAt(i)
        if (checkedItems.get(key)) {
          array[j++] = key
        }
        i++
      }
      Arrays.sort(array)
      return array
    }
  }
}
