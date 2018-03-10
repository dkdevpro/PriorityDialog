package com.dino.lib.dialog.list;

public interface MultiChoiceListDialogListener {
    void onListItemsSelected(CharSequence[] values, int[] selectedPositions, int requestCode);
}
