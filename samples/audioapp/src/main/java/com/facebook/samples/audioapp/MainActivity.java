/*
 * This file provided by Facebook is for non-commercial testing and evaluation
 * purposes only. Facebook reserves all rights not expressly granted.
 *
 * Copyright (c) 2015-present, Facebook, Inc.
 * Copyright (c) 2018-present, Pablo Delgado Kraemer
 *
 * This source code is licensed under the MIT license found in the
 * LICENSE file in the root directory of this source tree.
 */
package com.facebook.samples.audioapp;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

public class MainActivity extends Activity {

  private static final int PERMISSION_RESULT_READ_EXTERNAL_STORAGE = 1;
  private static final int COLUMN_COUNT = 3;

  private DraweeRecyclerViewAdapter mAdapter;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    // Reading audio files requires the READ_EXTERNAL_STORAGE permission.
    if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
        != PackageManager.PERMISSION_GRANTED) {
      ActivityCompat.requestPermissions(this,
        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
        PERMISSION_RESULT_READ_EXTERNAL_STORAGE);
    } else {
      setupViews();
    }
  }

  @Override
  public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[],
      @NonNull int[] grantResults) {
    switch (requestCode) {
      case PERMISSION_RESULT_READ_EXTERNAL_STORAGE: {
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
          setupViews();
        } else {
          finish();
        }
      }
    }
  }

  private void setupViews() {
    Context context = getApplicationContext();
    RecyclerView recyclerView = findViewById(R.id.recycler);
    RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, COLUMN_COUNT);

    mAdapter = new DraweeRecyclerViewAdapter(context);
    recyclerView.setLayoutManager(layoutManager);
    recyclerView.setAdapter(mAdapter);
  }
}
