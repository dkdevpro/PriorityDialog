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
        .showDialog(PriorityDialog.createBuilder(this, getSupportFragmentManager())
            .setTitle("Title")
            .setMessage("Simple Priority Dialog")
            .setPositiveButton("OK",
                new View.OnClickListener() {
                  @Override
                  public void onClick(View view) {
                    Toast.makeText(MainActivity.this, "Simple priority dialog", Toast.LENGTH_SHORT).show();
                  }
                })
            .setNegativeButton("Cancel",
                new View.OnClickListener() {
                  @Override
                  public void onClick(View view) {
                    finish();
                  }
                }));

  }
}
