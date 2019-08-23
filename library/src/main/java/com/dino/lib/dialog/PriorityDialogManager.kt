package com.dino.lib.dialog

import android.text.TextUtils

/**
 * Created by dinesh3.kumar on 11/03/18.
 */

class PriorityDialogManager : IPriorityDialogManager {

  private var dialogFragment: BaseDialogFragment? = null

  override val isShowing: Boolean
    get() = (dialogFragment != null && dialogFragment!!.dialog != null && dialogFragment!!.dialog.isShowing)

  override val tag: String?
    get() = if (dialogFragment != null) dialogFragment!!.customTag else null

  private object Holder {
    val INSTANCE = PriorityDialogManager()
  }

  fun showDialog(builder: BaseDialogBuilder<*>?) {
    synchronized(this) {
      if (builder == null) return

      if (isShowing) {
        if (dialogFragment!!.priority.mode > builder.getPriority().mode) return

        if (dialogFragment!!.priority.mode < builder.getPriority().mode || !TextUtils.isEmpty(
                builder.getTag()
            ) && builder.getTag().equals(tag!!, ignoreCase = true)
        ) {
          dismissDialog(tag)
        }
      }

      if (dialogFragment == null || !isShowing) {
        dialogFragment = builder.build()
      }
    }
  }

  private fun dismissDialog(tag: String?) {
    synchronized(this) {
      if (!TextUtils.isEmpty(tag) && !tag!!.equals(tag, ignoreCase = true)) return

      if (dialogFragment != null && isShowing) {
        dialogFragment!!.dismissAllowingStateLoss()
      }
    }
  }

  fun dismissDialog() {
    synchronized(this) {
      if (dialogFragment != null && isShowing) {
        dialogFragment!!.dismissAllowingStateLoss()
      }
    }
  }

  companion object {
    val INSTANCE: PriorityDialogManager
      get() = Holder.INSTANCE
  }

}