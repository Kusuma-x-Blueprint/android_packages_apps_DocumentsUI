
/*
 * Copyright (C) 2017 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.android.documentsui.inspector;

import android.annotation.StringRes;
import android.content.Context;
import android.content.res.Resources;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.documentsui.R;
import com.android.documentsui.inspector.InspectorController.TableDisplay;

import java.util.HashMap;
import java.util.Map;

/**
 * Organizes and Displays the basic details about a file
 */
public class TableView extends LinearLayout implements TableDisplay {

    private final LayoutInflater mInflater;

    private final Map<String, KeyValueRow> mRows = new HashMap<>();
    private final Resources mRes;
    private @Nullable TextView mTitle;

    public TableView(Context context) {
        this(context, null);
    }

    public TableView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TableView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mRes = context.getResources();
    }

    @Override
    public void setTitle(@StringRes int title) {
        if (mTitle == null) {
            mTitle = (TextView) mInflater.inflate(R.layout.inspector_section_title, null);
            addView(mTitle);
        }
        mTitle.setText(mContext.getResources().getString(title));
    }

    protected KeyValueRow createKeyValueRow(ViewGroup parent) {
        KeyValueRow row = (KeyValueRow) mInflater.inflate(R.layout.table_key_value_row, null);
        parent.addView(row);
        return row;
    }

    /**
     * Puts or updates an value in the table view.
     */
    @Override
    public void put(@StringRes int keyId, CharSequence value) {
        put(mRes.getString(keyId), value);
    }

    /**
     * Puts or updates an value in the table view.
     */
    protected KeyValueRow put(String key, CharSequence value) {
        KeyValueRow row = mRows.get(key);

        if (row == null) {
            row = createKeyValueRow(this);
            row.setKey(key);
            mRows.put(key, row);
        } else {
            row.removeOnClickListener();
        }

        row.setValue(value);
        return row;
    }

    @Override
    public void put(@StringRes int keyId, CharSequence value, OnClickListener callback) {
        put(keyId, value);
        mRows.get(mRes.getString(keyId)).setOnClickListener(callback);
    }

    @Override
    public boolean isEmpty() {
        return mRows.isEmpty();
    }

    @Override
    public void setVisible(boolean visible) {
        setVisibility(visible ? View.VISIBLE : View.GONE);
    }
}
