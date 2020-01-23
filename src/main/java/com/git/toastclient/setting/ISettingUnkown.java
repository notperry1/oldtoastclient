package com.git.toastclient.setting;

public interface ISettingUnkown {
    String getName();

    /**
     * @return The Class of the internal value (used for .set's output)
     */
    Class getValueClass();

    /**
     * @return The value in a format that setValueFromString can accept (if possible)
     */
    String getValueAsString();

    /**
     * @return Whether or not this setting should be displayed to the user
     */
    boolean isVisible();

    /**
     * Convert & set for .set & other "generic" setter cases.
     * Will throw if unconvertable.
     */
    void setValueFromString(String value);
}
