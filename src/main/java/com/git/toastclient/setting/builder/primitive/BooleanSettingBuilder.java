package com.git.toastclient.setting.builder.primitive;

import com.git.toastclient.setting.Setting;
import com.git.toastclient.setting.builder.SettingBuilder;
import com.git.toastclient.setting.impl.BooleanSetting;

/**
 * Created by 086 on 13/10/2018.
 */
public class BooleanSettingBuilder extends SettingBuilder<Boolean> {
    @Override
    public Setting<Boolean> build() {
        return new BooleanSetting(initialValue, predicate(), consumer(), name, visibilityPredicate());
    }

    @Override
    public SettingBuilder<Boolean> withName(String name) {
        return (BooleanSettingBuilder) super.withName(name);
    }
}
