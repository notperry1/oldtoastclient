package com.git.toastclient.setting.builder.numerical;

import com.git.toastclient.setting.Setting;
import com.git.toastclient.setting.builder.SettingBuilder;

import java.util.function.BiConsumer;

/**
 * Created by 086 on 13/10/2018.
 */
public abstract class NumericalSettingBuilder<T extends Number> extends SettingBuilder<T> {

    protected T min;
    protected T max;

    public NumericalSettingBuilder<T> withMinimum(T minimum) {
        predicateList.add((t -> t.doubleValue() >= minimum.doubleValue()));
        if (min == null || minimum.doubleValue() > min.doubleValue())
            min = minimum;
        return this;
    }

    public NumericalSettingBuilder<T> withMaximum(T maximum) {
        predicateList.add((t -> t.doubleValue() <= maximum.doubleValue()));
        if (max == null || maximum.doubleValue() < max.doubleValue())
            max = maximum;
        return this;
    }

    public NumericalSettingBuilder<T> withRange(T minimum, T maximum) {
        predicateList.add((t -> {
            double doubleValue = t.doubleValue();
            return doubleValue >= minimum.doubleValue() && doubleValue <= maximum.doubleValue();
        }));
        if (min == null || minimum.doubleValue() > min.doubleValue())
            min = minimum;
        if (max == null || maximum.doubleValue() < max.doubleValue())
            max = maximum;
        return this;
    }

    public NumericalSettingBuilder<T> withListener(BiConsumer<T, T> consumer) {
        this.consumer = consumer;
        return this;
    }

    @Override
    public SettingBuilder<T> withValue(T value) {
        return (NumericalSettingBuilder) super.withValue(value);
    }

    @Override
    public SettingBuilder<T> withName(String name) {
        return (NumericalSettingBuilder) super.withName(name);
    }

    public abstract Setting<T> build();

}