package com.dino.lib.dialog.time

import com.dino.lib.dialog.BaseDialogFragment
import com.dino.lib.dialog.PriorityMode
import com.dino.lib.dialog.PriorityMode.LOW

/**
 * Created by dineshkumar.m on 11/03/18.
 */

class PriorityTimePickerDialog : BaseDialogFragment() {

  override val customTag: String?
    get() = null

  override val priority: PriorityMode = LOW

  override fun build(initialBuilder: Builder): Builder? {
    return null
  }
}
