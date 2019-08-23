package com.dino.lib.dialog.list

interface MultiChoiceListDialogListener {
  fun onListItemsSelected(
    values: Array<CharSequence?>?,
    selectedPositions: IntArray?,
    requestCode: Int
  )
}
