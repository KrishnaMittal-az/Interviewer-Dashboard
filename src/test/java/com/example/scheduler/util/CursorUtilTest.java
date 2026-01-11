package com.example.scheduler.util;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CursorUtilTest {

    @Test
    void encodeDecodeRoundTrip() {
        String cursor = CursorUtil.encode(5L, "2025-01-01T10:00");
        CursorUtil.DecodedCursor decoded = CursorUtil.decode(cursor);
        assertThat(decoded.slotId()).isEqualTo(5L);
        assertThat(decoded.startTs()).isEqualTo("2025-01-01T10:00");
    }

    @Test
    void invalidCursorThrows() {
        assertThrows(IllegalArgumentException.class, () -> CursorUtil.decode("bad"));
    }
}

