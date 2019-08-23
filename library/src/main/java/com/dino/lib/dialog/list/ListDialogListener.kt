package com.dino.lib.dialog.list

interface ListDialogListener {
  fun onListItemSelected(
    value: CharSequence,
    position: Int,
    requestCode: Int
  )
}
