package com.git.toastclient.setting;

import com.git.toastclient.setting.builder.SettingBuilder;
import com.git.toastclient.setting.builder.numerical.DoubleSettingBuilder;
import com.git.toastclient.setting.builder.numerical.FloatSettingBuilder;
import com.git.toastclient.setting.builder.numerical.IntegerSettingBuilder;
import com.git.toastclient.setting.builder.numerical.NumericalSettingBuilder;
import com.git.toastclient.setting.builder.primitive.BooleanSettingBuilder;
import com.git.toastclient.setting.builder.primitive.EnumSettingBuilder;
import com.git.toastclient.setting.builder.primitive.StringSettingBuilder;
import com.google.common.base.Converter;

import java.util.function.BiConsumer;
import java.util.function.Predicate;

public class Settings {
    public static FloatSettingBuilder floatBuilder() {
        return new FloatSettingBuilder();
    }

    public static DoubleSettingBuilder doubleBuilder() {
        return new DoubleSettingBuilder();
    }

    public static IntegerSettingBuilder integerBuilder() {
        return new IntegerSettingBuilder();
    }

    public static BooleanSettingBuilder booleanBuilder() {
        return new BooleanSettingBuilder();
    }

    public static StringSettingBuilder stringBuilder() {
        return new StringSettingBuilder();
    }

    public static EnumSettingBuilder enumBuilder(Class<? extends Enum> clazz) {
        return new EnumSettingBuilder(clazz);
    }

    public static Setting<Float> f(String name, float value) {
        return floatBuilder(name).withValue(value).build();
    }

    public static Setting<Double> d(String name, double value) {
        return doubleBuilder(name).withValue(value).build();
    }

    public static Setting<Integer> i(String name, int value) {
        return integerBuilder(name).withValue(value).build();
    }

    public static Setting<Boolean> b(String name, boolean value) {
        return booleanBuilder(name).withValue(value).build();
    }

    public static Setting<Boolean> b(String name) {
        return booleanBuilder(name).withValue(true).build();
    }

    public static Setting<String> s(String name, String value) {
        return stringBuilder(name).withValue(value).build();
    }

    public static <T extends Enum> Setting<T> e(String name, Enum value) {
        return enumBuilder(value.getClass()).withName(name).withValue(value).build();
    }

    public static NumericalSettingBuilder<Float> floatBuilder(String name) {
        return new FloatSettingBuilder().withName(name);
    }

    public static NumericalSettingBuilder<Double> doubleBuilder(String name) {
        return new DoubleSettingBuilder().withName(name);
    }

    public static NumericalSettingBuilder<Integer> integerBuilder(String name) {
        return new IntegerSettingBuilder().withName(name);
    }

    public static BooleanSettingBuilder booleanBuilder(String name) {
        return new BooleanSettingBuilder().withName(name);
    }

    public static StringSettingBuilder stringBuilder(String name) {
        return (StringSettingBuilder) new StringSettingBuilder().withName(name);
    }

    public static <T> SettingBuilder<T> custom(String name, T initialValue, Converter converter, Predicate<T> restriction, BiConsumer<T, T> consumer, Predicate<T> visibilityPredicate) {
        return new SettingBuilder<T>() {
            @Override
            public Setting<String> build() {
                return new Setting<T>(initialValue, predicate(), consumer, name, visibilityPredicate()) {
                    @Override
                    public Converter converter() {
                        return converter;
                    }
                };
            }
        }.withName(name).withValue(initialValue).withConsumer(consumer).withVisibility(visibilityPredicate).withRestriction(restriction);
    }

    public static <T> SettingBuilder<T> custom(String name, T initialValue, Converter converter, Predicate<T> restriction, BiConsumer<T, T> consumer, boolean hidden) {
        return custom(name, initialValue, converter, restriction, consumer, t -> !hidden);
    }

    public static <T> SettingBuilder<T> custom(String name, T initialValue, Converter converter, Predicate<T> restriction, boolean hidden) {
        return custom(name, initialValue, converter, restriction, (t, t2) -> {
        }, hidden);
    }

    public static <T> SettingBuilder<T> custom(String name, T initialValue, Converter converter, boolean hidden) {
        return custom(name, initialValue, converter, input -> true, (t, t2) -> {
        }, hidden);
    }

    public static <T> SettingBuilder<T> custom(String name, T initialValue, Converter converter) {
        return custom(name, initialValue, converter, input -> true, (t, t2) -> {
        }, false);
    }

}
