package com.example.instagramclone;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class TabAdapter extends FragmentStateAdapter {


    private String[] titles = {"Profile", "Users", "Share Picture"};
    // Constructor
    public TabAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int tabPosition) {

        switch (tabPosition){
            case 0:
                ProfileTab profileTab = new ProfileTab();
                // we don't need break because here the return statement does the job of break statement
                return profileTab;
            case 1:
                // do the same job as in the case 0
                return new UsersTab();
            case 2:
                return new SharePictureTab();
            default:
                return null;
        }
    }

    @Override
    // how many tabs do we have inside tab layout
    public int getItemCount() {
        return titles.length;
    }




}
