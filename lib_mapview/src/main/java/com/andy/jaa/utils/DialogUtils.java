package com.andy.jaa.utils;

import android.app.Dialog;
import android.content.Context;
import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.andy.jaa.R;

/**
 * Created by quanxi on 2018/8/24.
 */

public class DialogUtils {
    private int creenWidth,creenHeight;
    private Context mContext;
    private Dialog mDialog;

    private DialogUtils(){}

    public DialogUtils(Context context){
        this.mContext = context;
        Resources resources = context.getResources();
        DisplayMetrics dm = resources.getDisplayMetrics();
        creenHeight = dm.heightPixels;
        creenWidth = dm.widthPixels;
        initDialog();
    }

    public Dialog getDialog(){
        return mDialog;
    }

    public void showDialog() {
        mDialog.show();
    }

    private Dialog initDialog() {
        mDialog = new Dialog(mContext, R.style.dialog_bottom_full);
        mDialog.setCanceledOnTouchOutside(false);
        mDialog.setCancelable(false);
        Window window = mDialog.getWindow();
        window.setGravity(Gravity.CENTER);
        WindowManager.LayoutParams params = mDialog.getWindow().getAttributes();//获取dialog信息
        params.height = (int) (creenWidth*0.3) ;
        params.width = (int) (creenWidth*0.8);
        mDialog.getWindow().setAttributes(params);//设置大小
        window.setWindowAnimations(R.style.share_animation);
        View view = View.inflate(mContext, R.layout.layout_bottom_dialog, null);

        window.setContentView(view);
//        window.setLayout((int) (creenWidth*0.8), WindowManager.LayoutParams.WRAP_CONTENT);//设置横向全屏
        return mDialog;
    }
}
