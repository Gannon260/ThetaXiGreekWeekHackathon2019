package com.example.peterlee.thetaxigreekweek2019;

import android.graphics.Color;

public enum Cuisine {

    AMERICAN("American", "#F44336"),
    ASIAN("Asian", "#673AB7"),
    EUROPEAN("European", "#4CAF50"),
    LATIN("Latin", "#FF9800"),
    MIDDLE_EASTERN("Middle Eastern", "#FF5722");

    private final String cuisineName;
    private final String cuisineColor;

    Cuisine(String name, String color) {
        cuisineName = name;
        cuisineColor = color;
    }

    public String getName() {
        return cuisineName;
    }

    public int getColor() {
        return Color.parseColor(cuisineColor);
    }

}