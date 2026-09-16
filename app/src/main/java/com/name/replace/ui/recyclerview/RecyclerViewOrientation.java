package com.name.replace.ui.recyclerview;

/**
 * Created by oliviergoutay on 1/23/15.
 */
public enum RecyclerViewOrientation {

    VERTICAL(0),
    HORIZONTAL(1),
    GRID(2);

    private int mValue;

    RecyclerViewOrientation(int value) {
        this.mValue = value;
    }

    public static RecyclerViewOrientation getFromInt(int value) {
        for (RecyclerViewOrientation orientation : RecyclerViewOrientation.values()) {
            if (orientation.mValue == value) {
                return orientation;
            }
        }
        return VERTICAL;
    }

}
