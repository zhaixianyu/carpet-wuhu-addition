package com.zxy.carpet_wh_addition.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;

public class ConfigHelper {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().serializeNulls().disableHtmlEscaping().create();

    public static <T> T loadConfig(Path path, T defaultValue) {
        if (!Files.exists(path)) {
            saveConfig(path, defaultValue);
        }

        try {
            BufferedReader br = Files.newBufferedReader(path);
            return GSON.fromJson(br, (Type) defaultValue.getClass());
        } catch (IOException e) {
            e.printStackTrace();
        }

        return defaultValue;
    }

    public static <T> void saveConfig(Path path, T value) {
        try {
            BufferedWriter bw = Files.newBufferedWriter(path);
            GSON.toJson(value, bw);
            bw.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
