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

import android.content.Context;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;

public class DraweeRecyclerViewAdapter extends
  RecyclerView.Adapter<DraweeRecyclerViewAdapter.DraweeViewHolder> {

  private static final String LOCAL_FILE_URI_PREFIX = "file://";

  public static class DraweeViewHolder extends RecyclerView.ViewHolder {
    public SimpleDraweeView mDraweeView;

    public DraweeViewHolder(SquareRelativeLayout layout) {
      super(layout);
      mDraweeView = layout.findViewById(R.id.drawee);
    }
  }

  private String[] mAudioIds;

  public DraweeRecyclerViewAdapter(Context context) {
    final String sqlSelection = MediaStore.Audio.Media.IS_MUSIC + " != 0";
    final String[] sqlProjection = {MediaStore.Audio.Media.DATA};
    final String sqlSortString = "RANDOM()";

    Cursor cursor = context.getContentResolver().query(MediaStore.Audio.Media
      .EXTERNAL_CONTENT_URI, sqlProjection, sqlSelection, null, sqlSortString);
    if (cursor == null) {
      mAudioIds = new String[0];
    } else {
      if (!cursor.moveToFirst()) {
        mAudioIds = new String[0];
      } else {
        mAudioIds = new String[cursor.getCount()];
        int i = 0;
        do {
          mAudioIds[i] = cursor.getString(0);
          i++;
        } while (cursor.moveToNext());
      }
      cursor.close();
    }
  }

  @NonNull
  @Override
  public DraweeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    SquareRelativeLayout layout = (SquareRelativeLayout) LayoutInflater.from(parent.getContext())
      .inflate(R.layout.recyclerview_item, parent, false);
    DraweeViewHolder viewHolder = new DraweeViewHolder(layout);
    return viewHolder;
  }

  @Override
  public void onBindViewHolder(@NonNull DraweeViewHolder holder, int position) {
    Context context = holder.mDraweeView.getContext();
    final Drawable placeholder = ContextCompat.getDrawable(context,
      R.drawable.ic_loading_failed_24px);
    final String audioPath = LOCAL_FILE_URI_PREFIX + mAudioIds[position];
    DraweeController controller = Fresco.newDraweeControllerBuilder()
      .setUri(audioPath)
      .setCallerContext("AudioApp-DraweeRecyclerViewAdapter").build();
    holder.mDraweeView.setController(controller);
    GenericDraweeHierarchy hierarchy = holder.mDraweeView.getHierarchy();
    hierarchy.setFailureImage(placeholder);
  }

  @Override
  public int getItemCount() {
    return mAudioIds.length;
  }
}
