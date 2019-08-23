package com.dino.lib.dialog.util

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.Checkable
import android.widget.LinearLayout
import java.util.HashSet

class CheckableLinearLayout(
  context: Context,
  attrs: AttributeSet
) : LinearLayout(context, attrs), Checkable {

  private val mCheckableSet = HashSet<Checkable>()
  private var mChecked: Boolean = false

  override fun onFinishInflate() {
    super.onFinishInflate()
    // find checkable items
    val childCount = childCount
    for (i in 0 until childCount) {
      val v = getChildAt(i)
      if (v is Checkable) {
        mCheckableSet.add(v as Checkable)
      }
    }
  }

  override fun isChecked(): Boolean {
    return mChecked
  }

  override fun setChecked(checked: Boolean) {
    if (checked == this.mChecked) {
      return
    }
    this.mChecked = checked
    for (checkable in mCheckableSet) {
      checkable.isChecked = checked
    }
    refreshDrawableState()
  }

  override fun toggle() {
    isChecked = !mChecked
  }

  public override fun onCreateDrawableState(extraSpace: Int): IntArray {
    val drawableState = super.onCreateDrawableState(extraSpace + 1)
    if (isChecked) {
      View.mergeDrawableStates(drawableState, CHECKED_STATE_SET)
    }
    return drawableState
  }

  companion object {
    private val CHECKED_STATE_SET = intArrayOf(android.R.attr.state_checked)
  }
}