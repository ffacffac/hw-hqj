package com.hw.ourlife.component;

import android.app.Activity;

import com.hw.ourlife.fragment.OurLifeFragment;
import com.hw.ourlife.module.FragmentModule;

import dagger.Component;

/**
 * @author huangqj
 *         Created by huangqj on 2019-04-10.
 */
@Component(modules = FragmentModule.class)
public interface FragmentComponent {

    /**
     * 获取Activity实例
     *
     * @return Activity
     */
    Activity getActivity();

    /**
     * 注入OurLifeFragment所需的依赖
     *
     * @param ourLifeFragment OurLifeFragment
     */
    void inject(OurLifeFragment ourLifeFragment);

    // /**
    //  * 注入KnowledgeHierarchyFragment所需的依赖
    //  *
    //  * @param knowledgeHierarchyFragment KnowledgeHierarchyFragment
    //  */
    // void inject(KnowledgeHierarchyFragment knowledgeHierarchyFragment);
    //
    // /**
    //  * 注入KnowledgeHierarchyListFragment所需的依赖
    //  *
    //  * @param knowledgeHierarchyListFragment KnowledgeHierarchyListFragment
    //  */
    // void inject(KnowledgeHierarchyListFragment knowledgeHierarchyListFragment);
}
