package com.android.ql.lf.carapp.ui.views;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialog;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.ql.lf.carapp.R;
import com.android.ql.lf.carapp.data.NewSpecificationBean;
import com.android.ql.lf.carapp.data.SpecificationBean;
import com.android.ql.lf.carapp.utils.GlideManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author liufeng
 * @date 2017/12/4
 */

public class BottomGoodsParamDialog extends BottomSheetDialog {

    private TextView tv_price;
    private TextView tv_release_count;
    private TextView tv_goods_name;

    private EditText tv_goods_num;

    private LinearLayout llContainer;
    private ImageView iv_goods_pic;

    private String selectPic;
    private String key = "";
    private String mPrice = "0.00";

    private String startCount = "1";
    private String endCount = "1";

    private TextView tv_delete_count;
    private TextView tv_add_count;
    private TextView tv_confirm;

    private ProgressBar pb_select_spe;

    private HashMap<String, String> selectSpe = new HashMap<>();


    private OnGoodsConfirmClickListener onGoodsConfirmClickListener;
    private OnGoodsSpeSelectListener onGoodsSpeSelectListener;

    private ArrayList<NewSpecificationBean> mSpecificationList = null;
    private ArrayList<MyFlexboxLayout> flexboxLayouts = new ArrayList<>();

    public void setOnGoodsConfirmClickListener(OnGoodsConfirmClickListener onGoodsConfirmClickListener) {
        this.onGoodsConfirmClickListener = onGoodsConfirmClickListener;
    }

    public void setOnGoodsSpeSelectListener(OnGoodsSpeSelectListener onGoodsSpeSelectListener) {
        this.onGoodsSpeSelectListener = onGoodsSpeSelectListener;
    }

    public BottomGoodsParamDialog(@NonNull Context context) {
        super(context);
        View contentView = View.inflate(context, R.layout.layout_goods_info_bootom_params_layout, null);
        setContentView(contentView);
        getWindow().findViewById(R.id.design_bottom_sheet).setBackgroundColor(Color.TRANSPARENT);
        int height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 400.0f, context.getResources().getDisplayMetrics());
        contentView.getLayoutParams().height = height;
        BottomSheetBehavior<View> behavior = BottomSheetBehavior.from(((View) contentView.getParent()));
        behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        behavior.setPeekHeight(height);
        tv_price = contentView.findViewById(R.id.mTvBottomParamPrice);
        pb_select_spe = contentView.findViewById(R.id.mPbSpeProgress);
        tv_release_count = contentView.findViewById(R.id.mTvBottomParamReleaseCount);
        tv_goods_name = contentView.findViewById(R.id.mTvBottomParamName);
        tv_goods_num = contentView.findViewById(R.id.mTvBottomParamCount);
        llContainer = contentView.findViewById(R.id.mLlBottomParamRuleContainer);
        iv_goods_pic = contentView.findViewById(R.id.mIvGoodsPic);
        tv_delete_count = contentView.findViewById(R.id.mTvBottomParamDelete);
        tv_add_count = contentView.findViewById(R.id.mTvBottomParamAdd);
        tv_confirm = contentView.findViewById(R.id.mTvBottomParamConfirm);
        tv_goods_num.setText("1");
        tv_goods_num.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!TextUtils.isEmpty(s.toString()) && s.toString().startsWith("0")) {
                    tv_goods_num.setText("1");
                    tv_goods_num.setSelection(tv_goods_num.getText().toString().length());
                }
            }
        });
        contentView.findViewById(R.id.mTvBottomParamClose).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        tv_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if (onGoodsConfirmClickListener != null) {
                        StringBuilder stringBuilder = new StringBuilder();
                        for (MyFlexboxLayout item : flexboxLayouts) {
                            if (item != null) {
                                String selectName = item.getSelectName();
                                if (TextUtils.isEmpty(selectName)) {
                                    Toast.makeText(getContext(), "请选择  " + item.getTitle() + "  规格", Toast.LENGTH_SHORT).show();
                                    return;
                                } else {
                                    stringBuilder.append(selectName).append(",");
                                }
                            }
                        }
                        if (stringBuilder.length() > 0) {
                            stringBuilder.deleteCharAt(stringBuilder.length() - 1);
                        }
                        if (TextUtils.isEmpty(tv_goods_num.getText().toString())){
                            tv_goods_num.setText("1");
                        }
                        if (Integer.parseInt(tv_goods_num.getText().toString()) < Integer.parseInt(startCount) || Integer.parseInt(tv_goods_num.getText().toString()) > Integer.parseInt(endCount)) {
                            Toast.makeText(getContext(), "请输入 " + startCount + " - " + endCount + " 商品数量", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        onGoodsConfirmClickListener.onGoodsConfirmClick(stringBuilder.toString(), selectPic, tv_goods_num.getText().toString(), key, mPrice);
                        dismiss();
                    }
                } catch (Exception e) {
                    Toast.makeText(getContext(), "商品规格选择错误", Toast.LENGTH_SHORT).show();
                }
            }
        });
        tv_delete_count.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int num = Integer.parseInt(tv_goods_num.getText().toString());
                if (num <= 1) {
                    tv_goods_num.setText("1");
                } else {
                    tv_goods_num.setText(String.valueOf(--num));
                }
            }
        });

        tv_add_count.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int num = Integer.parseInt(tv_goods_num.getText().toString());
                tv_goods_num.setText(String.valueOf(++num));
            }
        });
    }

   /* public void bindDataToView(String price, String releaseCount, String goodsName, String defaultPicPath, ArrayList<SpecificationBean> items) {
        tv_price.setText(price);
        this.mPrice = price.replace("￥", "");
        tv_release_count.setText(releaseCount);
        tv_goods_name.setText(goodsName);
        selectPic = defaultPicPath;
        GlideManager.loadRoundImage(getContext(), defaultPicPath, iv_goods_pic, 15);
        if (items != null && !items.isEmpty()) {
            mSpecificationList = items;
            llContainer.removeAllViews();
            flexboxLayouts.clear();
            for (final SpecificationBean item : mSpecificationList) {
                MyFlexboxLayout myFlexboxLayout = new MyFlexboxLayout(getContext());
                flexboxLayouts.add(myFlexboxLayout);
                myFlexboxLayout.setTitle(item.getName());
                //设置默认key
                if (item.getKey() != null && item.getStatus() != null && item.getKey().size() == item.getStatus().size()) {
                    for (int i = 0; i < item.getStatus().size(); i++) {
                        if ("1".equals(item.getStatus().get(i))) {
                            key = item.getKey().get(i);
                            String defaultPicSelect = item.getPic().get(i);
                            if (TextUtils.isEmpty(defaultPicSelect)){
                                defaultPicSelect = selectPic;
                            }
                            GlideManager.loadRoundImage(getContext(), defaultPicSelect, iv_goods_pic, 15);
                            break;
                        }
                    }
                }
                myFlexboxLayout.addItems(item.getItem(), item.getStatus());
                myFlexboxLayout.setOnItemClickListener(new MyFlexboxLayout.OnItemClickListener() {
                    @Override
                    public void onItemClick(int index) {
                        ArrayList<String> pic = item.getPic();
                        ArrayList<String> price1 = item.getPrice();
                        if (price1 != null && !price1.isEmpty() && price1.size() > index) {
                            tv_price.setText(String.format("￥%s", price1.get(index)));
                            mPrice = price1.get(index);
                        }
                        ArrayList<String> repertory = item.getRepertory();
                        if (repertory != null && !repertory.isEmpty() && repertory.size() > index) {
                            tv_release_count.setText(String.format("库存%s件", repertory.get(index)));
                        }
                        if (pic != null && !pic.isEmpty() && pic.size() > index) {
                            String path = pic.get(index);
                            if (!TextUtils.isEmpty(path)) {
                                selectPic = path;
                                GlideManager.loadRoundImage(getContext(), path, iv_goods_pic, 15);
                            }
                        }
                        if (item.getKey() != null && item.getKey().size() > index) {
                            key = item.getKey().get(index);
                        }
                    }
                });
                llContainer.addView(myFlexboxLayout);
            }
        }
    }*/

    public void bindNewDataToView(String price, String releaseCount, String goodsName, String defaultPicPath, ArrayList<NewSpecificationBean> items) {
        tv_price.setText(price);
        this.mPrice = price.replace("￥", "");
        tv_release_count.setText(releaseCount);
        tv_goods_name.setText(goodsName);
        selectPic = defaultPicPath;
        GlideManager.loadRoundImage(getContext(), defaultPicPath, iv_goods_pic, 15);
        if (items != null && !items.isEmpty()) {
            mSpecificationList = items;
            llContainer.removeAllViews();
            flexboxLayouts.clear();
            for (final NewSpecificationBean item : mSpecificationList) {
                final MyFlexboxLayout myFlexboxLayout = new MyFlexboxLayout(getContext());
                flexboxLayouts.add(myFlexboxLayout);
                myFlexboxLayout.setTitle(item.getTitle());
                selectSpe.put(item.getTitle(), null);
                myFlexboxLayout.addItems(item.getItem());
                myFlexboxLayout.setOnItemClickListener(new MyFlexboxLayout.OnItemClickListener() {
                    @Override
                    public void onItemClick(int index) {
                        final String id = item.getItem_id().get(index);
                        if (!selectSpe.containsValue(id) || selectSpe.get(item.getTitle()) == null) {
                            selectSpe.put(item.getTitle(), id);
                        }
                        String selectName = myFlexboxLayout.getSelectName();
                        if (!TextUtils.isEmpty(selectName) && selectName.contains("-")) {
                            String[] splitName = selectName.split("-");
                            startCount = splitName[0];
                            endCount = splitName[1];
                        }
                        Set<Map.Entry<String, String>> entries = selectSpe.entrySet();
                        for (Map.Entry<String, String> entry : entries) {
                            if (entry.getValue() == null) {
                                return;
                            }
                        }
                        if (onGoodsSpeSelectListener != null) {
                            onGoodsSpeSelectListener.onGoodsSpeSelect(selectSpe);
                        }
                    }

                    @Override
                    public void onUnSelectItemClick(int index) {
                        final String id = item.getItem_id().get(index);
                        if (selectSpe.containsValue(id) || selectSpe.get(item.getTitle()) != null) {
                            selectSpe.put(item.getTitle(), null);
                        }
                    }
                });
                llContainer.addView(myFlexboxLayout);
            }
        }
    }

    public void reBindData(String price, String releaseCount,String releaseWithInfoCount, String defaultPicPath) {
        tv_price.setText(price);
        this.mPrice = price.replace("￥", "");
        tv_release_count.setText(releaseWithInfoCount);
        setConfirmBt(!(Integer.parseInt(releaseCount) <= 0));
        selectPic = defaultPicPath;
        GlideManager.loadRoundImage(getContext(), defaultPicPath, iv_goods_pic, 15);
    }

    public void dismissProgress(){
        pb_select_spe.setVisibility(View.GONE);
    }

    public void showProgress(){
        pb_select_spe.setVisibility(View.VISIBLE);
    }


    public void setConfirmBt(boolean isEnable) {
        tv_confirm.setEnabled(isEnable);
    }


    public interface OnGoodsConfirmClickListener {
        public void onGoodsConfirmClick(String specification, String picPath, String num, String key, String mPrice);
    }

    public interface OnGoodsSpeSelectListener {
        void onGoodsSpeSelect(HashMap<String, String> speMap);
    }

}
