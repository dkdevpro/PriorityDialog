package com.dino.lib.dialog;

import android.text.TextUtils;

/**
 * Created by dinesh3.kumar on 11/03/18.
 */

public class PriorityDialogManager implements IPriorityDialogManager {

  private BaseDialogFragment dialogFragment;
  private static class Holder{
    private static final PriorityDialogManager INSTANCE = new PriorityDialogManager();
  }
  public static PriorityDialogManager getNewInstance() {
    return Holder.INSTANCE;
  }

  @Override
  public boolean isShowing() {
    return (dialogFragment != null && dialogFragment.getDialog()!=null
        && dialogFragment.getDialog().isShowing());
  }

  @Override
  public String getTag() {
    return dialogFragment != null ? dialogFragment.getCustomTag() : null;
  }

  public void showDialog(BaseDialogBuilder builder){
    synchronized (this) {
      if (builder == null)
        return;

      if(isShowing()){
        if(dialogFragment.getPriority() > builder.getPriority())
          return;

        if( dialogFragment.getPriority() < builder.getPriority() || (!TextUtils.isEmpty(builder.getTag()) && builder.getTag().equalsIgnoreCase(getTag()))){
          dismissDialog(getTag());
        }
      }

      if(dialogFragment == null || !isShowing()){
        dialogFragment = builder.build();
      }
    }
  }

  public void dismissDialog(String tag){
    synchronized (this) {
      if(!TextUtils.isEmpty(tag) && !tag.equalsIgnoreCase(getTag()))
        return;

      if (dialogFragment != null && isShowing()) {
        dialogFragment.dismissAllowingStateLoss();
      }
    }
  }
  public void dismissDialog(){
    synchronized (this) {
      if (dialogFragment != null && isShowing()) {
        dialogFragment.dismissAllowingStateLoss();
      }
    }
  }

}