package com.lion.material.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;

import com.lion.material.R;

public class LAlertDialog extends Dialog implements DialogInterface {

    public interface VisibleCallback {
        public void onclick();
    }

    private LDialogController control;
    private static final int default_theme = 0;

    // private static final int default_theme = R.style.customDialogStyle;

    protected LAlertDialog(Context context) {
        this(context, default_theme);
        this.getWindow().setBackgroundDrawableResource(
                R.drawable.material_dialog_bg);
    }

    protected LAlertDialog(Context context, int theme) {
        super(context, theme);
        control = new LDialogController(context, this, getWindow());
    }

    protected LAlertDialog(Context context, boolean cancelable,
                           OnCancelListener cancelListener) {
        super(context, default_theme);
        setCancelable(cancelable);
        setOnCancelListener(cancelListener);
        control = new LDialogController(context, this, getWindow());
    }

    public void setPositiveButtonEnable(boolean enable) {
        control.setPositiveButtonEnable(enable);
    }

    public LDialogController getController() {
        return control;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        control.installContent();
    }

    public static class Builder {
        private final LDialogController.DialogParams P;

        public Builder(Context context) {
            P = new LDialogController.DialogParams(context);
        }

        /**
         * 根据resource id来设置title
         *
         * @param titleId
         * @return
         */
        public Builder setTitle(int titleId) {
            P.mTitle = P.mContext.getText(titleId);
            return this;
        }

        /**
         * 给定字符串来设置dialog的title
         *
         * @param title
         * @return
         */
        public Builder setTitle(CharSequence title) {
            P.mTitle = title;
            return this;
        }

        /**
         * 根据resource id来设置message
         *
         * @param messageId
         * @return
         */
        public Builder setMessage(int messageId) {
            P.mMessage = P.mContext.getText(messageId);
            return this;
        }

        /**
         * 给定字符串来设置dialog的message
         *
         * @param message
         * @return
         */
        public Builder setMessage(CharSequence message) {
            P.mMessage = message;
            return this;
        }

        public Builder showCloseButton(boolean show) {
            P.visibleCallback = show;
            return this;
        }

        /**
         * 根据resource id来设置title左边图标
         *
         * @param iconId
         * @return
         */
        public Builder setIcon(int iconId) {
            P.mIconId = iconId;
            return this;
        }

        /**
         * 给定drawable来设置dialog的title左边图标
         *
         * @param icon
         * @return
         */
        public Builder setIcon(Drawable icon) {
            P.mIcon = icon;
            return this;
        }

        /**
         * 设置message信息是否居中，默认居左
         *
         * @param value
         * @return
         */
        public Builder setCenterMsg(boolean value) {
            P.centerMsg = value;
            return this;
        }

        /**
         * 设置dialog弹出的是否为警告信息， 是则改变title文字的颜色为红色。默认为蓝色
         *
         * @param value
         * @return
         */
        // public Builder setWarnTitle(boolean value){
        // P.warnTitle = value;
        // return this;
        // }

        /**
         * 为左边的按钮设置一个事件监听，当按钮被按下时可以执行某些动作
         *
         * @param textId   按钮中显示的文字的resource id
         * @param listener 事件监听器
         * @return
         */
        public Builder setPositiveButton(int textId,
                                         final OnClickListener listener) {
            P.mPositiveButtonText = P.mContext.getText(textId);
            P.mPositiveButtonListener = listener;
            return this;
        }

        /**
         * 为确定的按钮设置一个事件监听，当按钮被按下时可以执行某些动作
         *
         * @param text     按钮中显示的文字字符串
         * @param listener 事件监听器
         * @return
         */
        public Builder setPositiveButton(CharSequence text,
                                         final OnClickListener listener) {
            P.mPositiveButtonText = text;
            P.mPositiveButtonListener = listener;
            return this;
        }

        /**
         * @param value false点击确定不dismiss，默认为true
         * @return
         */
        public Builder setClickPositiveBtnDismiss(boolean value) {
            P.clickPositiveBtnDismiss = value;
            return this;
        }

        /**
         * 为右边的按钮设置一个事件监听，当按钮被按下时可以执行某些动作
         *
         * @param textId   按钮中显示的文字的resource id
         * @param listener 事件监听器
         * @return
         */
        public Builder setNegativeButton(int textId,
                                         final OnClickListener listener) {
            P.mNegativeButtonText = P.mContext.getText(textId);
            P.mNegativeButtonListener = listener;
            return this;
        }

        /**
         * 为右边的按钮设置一个事件监听，当按钮被按下时可以执行某些动作
         *
         * @param text     按钮中显示的文字字符串
         * @param listener 事件监听器
         * @return
         */
        public Builder setNegativeButton(CharSequence text,
                                         final OnClickListener listener) {
            P.mNegativeButtonText = text;
            P.mNegativeButtonListener = listener;
            return this;
        }

        public Builder setCancelable(boolean cancelable) {
            P.mCancelable = cancelable;
            return this;
        }

        public Builder setOnCancelListener(OnCancelListener onCancelListener) {
            P.mOnCancelListener = onCancelListener;
            return this;
        }

        /**
         * 为内容区域设置自定义的视图
         *
         * @param view
         * @return
         */
        public Builder setView(View view) {
            P.mView = view;
            return this;
        }

        /**
         * 创建dialog
         *
         * @return
         */
        public LAlertDialog create() {
            final LAlertDialog dialog = new LAlertDialog(P.mContext);
            P.apply(dialog.control);
            dialog.setCancelable(P.mCancelable);
            if (P.mCancelable) {
                dialog.setCanceledOnTouchOutside(true);
            }
            dialog.setOnCancelListener(P.mOnCancelListener);
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
    }

}
