package com.git.toastclient.setting.builder.primitive;

import com.git.toastclient.setting.Setting;
import com.git.toastclient.setting.builder.SettingBuilder;
import com.git.toastclient.setting.impl.StringSetting;

/**
 * Created by 086 on 13/10/2018.
 */
public class StringSettingBuilder extends SettingBuilder<String> {
    @Override
    public Setting<String> build() {
        return new StringSetting(initialValue, predicate(), consumer(), name, visibilityPredicate());
    }
}
