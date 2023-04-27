package net.cps.client.utils;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public enum Theme {
    LIGHT("light"),
    DARK("dark");
    
    private final String name;
    
    Theme (String name) {
        this.name = name;
    }
    
    public String getName () {
        return name;
    }
    
    public static @Nullable Theme getTheme (String name) {
        for (Theme theme : Theme.values()) {
            if (theme.getName().equals(name)) {
                return theme;
            }
        }
        return null;
    }
    
    public static String fromTheme (@NotNull Theme theme) {
        return theme.getName();
    }
    
    public static @NotNull Theme fromString (@NotNull String name) {
        Theme theme = getTheme(name.trim().toLowerCase());
        if (theme == null) {
            throw new IllegalArgumentException("No theme with name " + name + " found.");
        }
        return theme;
    }
}
