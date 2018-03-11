package com.dino.lib.dialog.list;

import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import com.dino.lib.dialog.BaseDialogBuilder;
import com.dino.lib.dialog.BaseDialogFragment;
import com.dino.lib.dialog.R;
import java.util.Arrays;

/**
 * Created by dineshkumar.m on 11/03/18.
 */

public class PriorityListDialog extends BaseDialogFragment {

    private CharSequence mTitle;
    private CharSequence[] mItems;
    @ChoiceMode
    private int mMode;
    private int[] mCheckedItems = new int[]{};
    private CharSequence mCancelButtonText;
    private CharSequence mConfirmButtonText;
    private String mTag;
    private ListDialogListener mListDialogListener;
    private MultiChoiceListDialogListener mMultiChoiceListDialogListener;

    public static SimpleListDialogBuilder createBuilder(Context context, FragmentManager fragmentManager) {
        return new SimpleListDialogBuilder(context, fragmentManager);
    }

    private static int[] asIntArray(SparseBooleanArray checkedItems) {
        int checked = 0;
        // compute number of items
        for (int i = 0; i < checkedItems.size(); i++) {
            int key = checkedItems.keyAt(i);
            if (checkedItems.get(key)) {
                ++checked;
            }
        }

        int[] array = new int[checked];
        //add indexes that are checked
        for (int i = 0, j = 0; i < checkedItems.size(); i++) {
            int key = checkedItems.keyAt(i);
            if (checkedItems.get(key)) {
                array[j++] = key;
            }
        }
        Arrays.sort(array);
        return array;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (getArguments() == null) {
            throw new IllegalArgumentException(
                    "use SimpleListDialogBuilder to construct this dialog");
        }
    }

    private ListAdapter prepareAdapter(final int itemLayoutId) {
        return new ArrayAdapter<Object>(getActivity(),
                itemLayoutId,
                R.id.pdl_text,
                getItems()) {

            /**
             * Overriding default implementation because it ignores current light/dark theme.
             */
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                if (convertView == null) {
                    convertView = LayoutInflater.from(parent.getContext()).inflate(itemLayoutId, parent, false);
                }
                TextView t = (TextView)convertView.findViewById(R.id.pdl_text);
                if (t != null) {
                    t.setText((CharSequence)getItem(position));
                }
                return convertView;
            }
        };
    }

    private void buildMultiChoice(Builder builder) {
        builder.setItems(
                prepareAdapter(R.layout.view_list_item_multichoice),
                getCheckedItems(), AbsListView.CHOICE_MODE_MULTIPLE,
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        SparseBooleanArray checkedPositions = ((ListView)parent).getCheckedItemPositions();
                        setCheckedItems(asIntArray(checkedPositions));
                    }
                });
    }

    private void buildSingleChoice(Builder builder) {
        builder.setItems(
                prepareAdapter(R.layout.view_list_item_singlechoice),
                getCheckedItems(),
                AbsListView.CHOICE_MODE_SINGLE, new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        SparseBooleanArray checkedPositions = ((ListView)parent).getCheckedItemPositions();
                        setCheckedItems(asIntArray(checkedPositions));
                    }
                });
    }

    private void buildNormalChoice(Builder builder) {
        builder.setItems(
                prepareAdapter(R.layout.view_list_item), -1,
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        /*for (IListDialogListener listener : getSingleDialogListeners()) {
                            listener.onListItemSelected(getItems()[position], position, mRequestCode);
                        }*/
                        dismiss();
                        if(mListDialogListener != null){
                            mListDialogListener.onListItemSelected(getItems()[position], position, mRequestCode);
                        }
                    }
                });
    }

    @Override
    protected Builder build(Builder builder) {
        final CharSequence title = getTitle();
        if (!TextUtils.isEmpty(title)) {
            builder.setTitle(title);
        }

        if (!TextUtils.isEmpty(getNegativeButtonText())) {
            builder.setNegativeButton(getNegativeButtonText(), new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    dismiss();
                }
            });
        }

        if (getMode() != AbsListView.CHOICE_MODE_NONE) {
            View.OnClickListener positiveButtonClickListener = null;
            switch (getMode()) {
                case AbsListView.CHOICE_MODE_MULTIPLE:
                    positiveButtonClickListener = new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            final int[] checkedPositions = getCheckedItems();
                            final CharSequence[] items = getItems();
                            final CharSequence[] checkedValues = new CharSequence[checkedPositions.length];
                            int i = 0;
                            for (int checkedPosition : checkedPositions) {
                                if (checkedPosition >= 0 && checkedPosition < items.length) {
                                    checkedValues[i++] = items[checkedPosition];
                                }
                            }
                            dismiss();
                            if(mMultiChoiceListDialogListener != null){
                                mMultiChoiceListDialogListener.onListItemsSelected(checkedValues, checkedPositions, mRequestCode);
                            }
                        }
                    };
                    break;
                case AbsListView.CHOICE_MODE_SINGLE:
                    positiveButtonClickListener = new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            int selectedPosition = -1;
                            final int[] checkedPositions = getCheckedItems();
                            final CharSequence[] items = getItems();
                            for (int i : checkedPositions) {
                                if (i >= 0 && i < items.length) {
                                    //1st valid value
                                    selectedPosition = i;
                                    break;
                                }
                            }

                            dismiss();
                            if (selectedPosition != -1) {
                                if(mListDialogListener != null){
                                    mListDialogListener.onListItemSelected(items[selectedPosition], selectedPosition, mRequestCode);
                                }
                            } else {
                                //for (ISimpleDialogCancelListener listener : getCancelListeners()) {
                                //    listener.onCancelled(mRequestCode);
                                //}
                            }

                        }
                    };
                    break;
            }

            CharSequence positiveButton = getPositiveButtonText();
            if (TextUtils.isEmpty(getPositiveButtonText())) {
                //we always need confirm button when CHOICE_MODE_SINGLE or CHOICE_MODE_MULTIPLE
                positiveButton = getString(android.R.string.ok);
            }
            builder.setPositiveButton(positiveButton, positiveButtonClickListener);
        }

        // prepare list and its item click listener
        final CharSequence[] items = getItems();
        if (items != null && items.length > 0) {
            @ChoiceMode
            final int mode = getMode();
            switch (mode) {
                case AbsListView.CHOICE_MODE_MULTIPLE:
                    buildMultiChoice(builder);
                    break;
                case AbsListView.CHOICE_MODE_SINGLE:
                    buildSingleChoice(builder);
                    break;
                case AbsListView.CHOICE_MODE_NONE:
                    buildNormalChoice(builder);
                    break;
            }
        }

        return builder;
    }

    @Override
    protected String getCustomTag() {
        return null;
    }

    @Override
    protected int getPriority() {
        return 0;
    }

    private CharSequence getTitle() {
        return mTitle;
    }

    @SuppressWarnings("ResourceType")
    @ChoiceMode
    private int getMode() {
        return mMode;
    }

    private CharSequence[] getItems() {
        return mItems;
    }

    @NonNull
    private int[] getCheckedItems() {
        return mCheckedItems;
    }

    private void setCheckedItems(int[] checkedItems) {
        mCheckedItems = checkedItems;
    }

    private CharSequence getPositiveButtonText() {
        return mConfirmButtonText;
    }

    private CharSequence getNegativeButtonText() {
        return mCancelButtonText;
    }

    @IntDef({AbsListView.CHOICE_MODE_MULTIPLE, AbsListView.CHOICE_MODE_SINGLE, AbsListView.CHOICE_MODE_NONE})
    public @interface ChoiceMode {
    }

    public static class SimpleListDialogBuilder extends BaseDialogBuilder<SimpleListDialogBuilder> {

        private CharSequence title;

        private CharSequence[] items;

        @ChoiceMode
        private int mode;
        private int[] checkedItems;

        private CharSequence cancelButtonText;
        private CharSequence confirmButtonText;
        private ListDialogListener iListDialogListener;
        private MultiChoiceListDialogListener iMultiChoiceListDialogListener;


        public SimpleListDialogBuilder(Context context, FragmentManager fragmentManager) {
            super(context, fragmentManager, PriorityListDialog.class);
        }

        @Override
        protected SimpleListDialogBuilder self() {
            return this;
        }

        private Resources getResources() {
            return mContext.getResources();
        }

        public SimpleListDialogBuilder setTitle(CharSequence title) {
            this.title = title;
            return this;
        }

        public SimpleListDialogBuilder setTitle(int titleResID) {
            this.title = getResources().getString(titleResID);
            return this;
        }

        public SimpleListDialogBuilder setCheckedItems(int[] positions) {
            this.checkedItems = positions;
            return this;
        }


        public SimpleListDialogBuilder setSelectedItem(int position) {
            this.checkedItems = new int[]{position};
            return this;
        }

        public SimpleListDialogBuilder setChoiceMode(@ChoiceMode int choiceMode) {
            this.mode = choiceMode;
            return this;
        }

        public SimpleListDialogBuilder setItems(CharSequence[] items) {
            this.items = items;
            return this;
        }

        public SimpleListDialogBuilder setItems(int itemsArrayResID) {
            this.items = getResources().getStringArray(itemsArrayResID);
            return this;
        }

        public SimpleListDialogBuilder setConfirmButtonText(CharSequence text) {
            this.confirmButtonText = text;
            return this;
        }

        public SimpleListDialogBuilder setConfirmButtonText(int confirmBttTextResID) {
            this.confirmButtonText = getResources().getString(confirmBttTextResID);
            return this;
        }

        public SimpleListDialogBuilder setCancelButtonText(CharSequence text) {
            this.cancelButtonText = text;
            return this;
        }

        public SimpleListDialogBuilder setCancelButtonText(int cancelBttTextResID) {
            this.cancelButtonText = getResources().getString(cancelBttTextResID);
            return this;
        }

        public SimpleListDialogBuilder setTag(String tag) {
            this.mTag = tag;
            return this;
        }

        public SimpleListDialogBuilder setOnListItemSelected(ListDialogListener listener) {
            this.iListDialogListener = listener;
            return this;
        }
        public SimpleListDialogBuilder setOnListItemsSelected(MultiChoiceListDialogListener listener) {
            this.iMultiChoiceListDialogListener = listener;
            return this;
        }

        @Override
        public PriorityListDialog build() {
            return showAllowingStateLoss();
        }

        private PriorityListDialog create() {

            final Bundle args = new Bundle();

            final PriorityListDialog
                fragment = (PriorityListDialog) Fragment.instantiate(mContext, mClass.getName(), args);
            args.putBoolean(ARG_CANCELABLE_ON_TOUCH_OUTSIDE, mCancelableOnTouchOutside);
            args.putBoolean(ARG_USE_DARK_THEME, mUseDarkTheme);
            args.putBoolean(ARG_USE_LIGHT_THEME, mUseLightTheme);

            if (mTargetFragment != null) {
                fragment.setTargetFragment(mTargetFragment, mRequestCode);
            } else {
                args.putInt(ARG_REQUEST_CODE, mRequestCode);
            }
            fragment.setCancelable(mCancelable);

            return fragment;
        }

        public PriorityListDialog show() {
            PriorityListDialog fragment = create();
            initFragment(fragment);
            fragment.show(mFragmentManager, mTag);
            return fragment;
        }

        public PriorityListDialog showAllowingStateLoss() {
            PriorityListDialog fragment = create();
            initFragment(fragment);
            fragment.showAllowingStateLoss(mFragmentManager, mTag);
            return fragment;
        }
        private void initFragment(PriorityListDialog fragment){

            fragment.mTitle = this.title;
            fragment.mItems = this.items;
            fragment.mMode = this.mode;
            fragment.mCheckedItems = this.checkedItems;
            fragment.mCancelButtonText = this.cancelButtonText;
            fragment.mConfirmButtonText = this.confirmButtonText;
            fragment.mListDialogListener = this.iListDialogListener;
            fragment.mMultiChoiceListDialogListener = this.iMultiChoiceListDialogListener;
            fragment.mTag = this.mTag;
        }
    }
}
