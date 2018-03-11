package com.dino.sample;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import com.dino.lib.dialog.IPriorityDialogManager;
import com.dino.lib.dialog.PriorityDialog;
import com.dino.lib.dialog.PriorityDialogManager;

public class MainActivity extends AppCompatActivity {

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    PriorityDialogManager.getNewInstance()
        .showDialog(
            PriorityDialog.createBuilder(this, getSupportFragmentManager())
            .setTitle(getString(R.string.title))
            .setPriority(3)
            .setMessage(getString(R.string.sd_message))
            .setPositiveButton(getString(R.string.yes),null)
            .setNegativeButton(getString(R.string.cancel),null)
        );

  }
}
