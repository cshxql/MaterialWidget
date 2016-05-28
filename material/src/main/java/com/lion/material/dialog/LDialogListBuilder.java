package com.lion.material.dialog;

import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.lion.material.R;
import com.lion.material.dialog.LAlertDialog.Builder;

public class LDialogListBuilder extends Builder {
	private Context mContext;
	private ListView mListView;
	private OnClickListener mListener;
	private LDialogController mController;
	private int mSelectedIndex = -1;
	private boolean mIsChooseMode;
	private ListItemAdapter mAdapter;

	public LDialogListBuilder(Context context, int selectedIndex,
			boolean isChooseMode) {
		super(context);
		this.mContext = context;
		View v = View.inflate(mContext, R.layout.material_dialog_listview, null);
		mListView = (ListView) v.findViewById(R.id.dialog_listview);
		this.setView(v);
		this.mSelectedIndex = selectedIndex;
		this.mIsChooseMode = isChooseMode;
	}

	public Builder setItems(String[] items, OnClickListener listener) {
		this.mListener = listener;
		mAdapter = new ListItemAdapter(mContext, items, mSelectedIndex,
				mIsChooseMode);
		mListView.setAdapter(mAdapter);
		return this;
	}

	public void setSelectedIndex(int index) {
		this.mSelectedIndex = index;
		if (mAdapter != null) {
			mAdapter.setSelectedIndex(index);
		}

	}

	/**
	 * 创建dialog
	 * 
	 * @return
	 */
	public LAlertDialog create() {
		LAlertDialog dialog = super.create();
		mController = dialog.getController();
		return dialog;
	}

	/**
	 * 创建并显示dialog
	 * 
	 * @return
	 */
	public LAlertDialog show() {
		LAlertDialog dialog = create();
		dialog.show();
		return dialog;
	}

	public class ListItemAdapter extends BaseAdapter {
		private Context mContext;
		private String[] items;
		private int selectedIndex = -1;
		private boolean isChooseMode;

		public ListItemAdapter(Context context, String[] items,
				int selectedIndex, boolean isChooseMode) {
			this.mContext = context;
			this.items = items;
			this.selectedIndex = selectedIndex;
			this.isChooseMode = isChooseMode;
		}

		@Override
		public int getCount() {
			return items.length;
		}

		@Override
		public Object getItem(int arg0) {
			return null;
		}

		@Override
		public long getItemId(int arg0) {
			return 0;
		}

		@Override
		public View getView(final int position, View convertView, ViewGroup arg2) {

			ViewHolder viewHolder = null;
			if (convertView == null) {
				viewHolder = new ViewHolder();
				convertView = View.inflate(mContext,
						R.layout.material_dialog_listview_adapter, null);
				viewHolder.mTextView = (TextView) convertView
						.findViewById(R.id.dialog_listview_adapter_content);
				viewHolder.mIconImageView = (ImageView) convertView
						.findViewById(R.id.dialog_listview_adapter_checkbox);
				if (isChooseMode) {
					viewHolder.mIconImageView.setVisibility(View.VISIBLE);
				}
				convertView.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolder) convertView.getTag();
			}
			viewHolder.mIconImageView.setSelected(selectedIndex == position);
			viewHolder.mTextView.setText(items[position]);
			convertView.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View arg0) {
					if (mListener != null) {
						DialogInterface dialogInterface = mController == null ? null
								: mController.getDialogInterface();
						mListener.onClick(dialogInterface, position);
						if (dialogInterface != null) {
							dialogInterface.dismiss();
						}
					}
				}
			});

			return convertView;
		}

		private class ViewHolder {
			TextView mTextView;
			ImageView mIconImageView;
		}

		public void setSelectedIndex(int index) {
			this.selectedIndex = index;
			notifyDataSetChanged();
		}
	}

}
