package io.hexlet.util;

public class SlugUtils {
    public static String generateSlug(String title) {
        return title.toLowerCase()
                .replaceAll("[^a-zа-яё0-9\\s-]", "")  // убрать спецсимволы
                .trim()
                .replaceAll("\\s+", "-")              // пробелы → дефисы
                .replaceAll("-+", "-");               // множественные дефисы
    }
}
