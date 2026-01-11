package com.example.scheduler.util;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

public final class CursorUtil {
    private CursorUtil() {}

    public static String encode(long slotId, String startTs) {
        String raw = startTs + "|" + slotId;
        return Base64.getUrlEncoder().encodeToString(raw.getBytes(StandardCharsets.UTF_8));
    }

    public static DecodedCursor decode(String cursor) {
        if (cursor == null || cursor.isBlank()) {
            return null;
        }
        String decoded = new String(Base64.getUrlDecoder().decode(cursor), StandardCharsets.UTF_8);
        String[] parts = decoded.split("\\|");
        if (parts.length != 2) {
            throw new IllegalArgumentException("Invalid cursor");
        }
        return new DecodedCursor(parts[0], Long.parseLong(parts[1]));
    }

    public record DecodedCursor(String startTs, long slotId) { }
}

