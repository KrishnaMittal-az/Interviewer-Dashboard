package com.example.scheduler.dto;

import lombok.Builder;
import lombok.Value;

import java.util.List;

@Value
@Builder
public class CursorPage<T> {
    List<T> items;
    String nextCursor;
    boolean hasNext;
}

