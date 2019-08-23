package com.dino.sample

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.dino.lib.dialog.PriorityDialog
import com.dino.lib.dialog.PriorityDialogManager
import com.dino.lib.dialog.PriorityMode

class MainActivity : AppCompatActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)

    PriorityDialogManager.INSTANCE.showDialog(
        PriorityDialog.createBuilder(this, supportFragmentManager).setTitle(
            getString(R.string.title)
        ).setPriority(PriorityMode.MEDIUM).setMessage(getString(R.string.sd_message)).setPositiveButton(
            getString(R.string.yes)
        ).setNegativeButton(getString(R.string.cancel))
    )

  }
}
