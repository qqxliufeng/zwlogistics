package com.android.ql.lf.carapp.utils;


import com.android.ql.lf.carapp.ui.fragments.BaseFragment;

import java.util.ArrayList;

/**
 * Created by lf on 18.4.21.
 *
 * @author lf on 18.4.21
 */

public class ActivityQueueUtils {

    private ArrayList<BaseFragment> fragmentArrayList = new ArrayList<>();

    public void addItem(BaseFragment fragment) {
        if (!fragmentArrayList.contains(fragment)) {
            fragmentArrayList.add(0, fragment);
            if (fragmentArrayList.size() >= 4) {
                BaseFragment lastFragment = fragmentArrayList.get(fragmentArrayList.size() - 1);
                lastFragment.finish();
                fragmentArrayList.remove(lastFragment);
            }
        }
    }

    public void removeItem(BaseFragment fragment) {
        if (fragmentArrayList.contains(fragment)) {
            fragmentArrayList.remove(fragment);
        }
    }

    public void clear() {
        fragmentArrayList.clear();
        fragmentArrayList = null;
    }

}
