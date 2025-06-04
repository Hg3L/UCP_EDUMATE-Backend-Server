package vn.base.edumate.vision;

import java.text.Normalizer;
import java.util.regex.Pattern;

public final class TextNormalizer {
    private TextNormalizer() {
    }

    public static String normalizeText(String input) {
        if (input == null) return "";

        String normalized = input.toLowerCase();

        normalized = Normalizer.normalize(normalized, Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        normalized = pattern.matcher(normalized).replaceAll("");

        normalized = normalized.replaceAll("[^a-z0-9\\s]", "");

        normalized = normalized.trim().replaceAll("\\s+", " ");

        return normalized;
    }

    public static String normalizeForSbert(String input) {
        if (input == null) return "";
        return input.trim().replaceAll("\\s+", " ");
    }

}
