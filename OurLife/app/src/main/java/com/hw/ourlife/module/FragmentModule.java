package com.hw.ourlife.module;

import android.app.Activity;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;

import dagger.Module;
import dagger.Provides;

/**
 * @author huangqj
 *         Created by huangqj on 2019-04-10.
 */
@Module
public class FragmentModule {

    private Fragment fragment;
    private DialogFragment dialogFragment;

    public FragmentModule(Fragment fragment) {
        this.fragment = fragment;
        this.dialogFragment = null;
    }

    public FragmentModule(DialogFragment dialogFragment) {
        this.dialogFragment = dialogFragment;
        this.fragment = null;
    }

    @Provides
    Activity provideActivity() {
        if (fragment == null) {
            return dialogFragment.getActivity();
        } else {
            return fragment.getActivity();
        }
    }

}
