package com.example.scheduler.service;

import com.example.scheduler.domain.model.InterviewSlot;
import com.example.scheduler.domain.repository.InterviewSlotPort;
import com.example.scheduler.dto.CursorPage;
import com.example.scheduler.dto.SlotResponse;
import com.example.scheduler.util.CursorUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SlotQueryService {

    private final InterviewSlotPort slotPort;

    public CursorPage<SlotResponse> listOpenSlots(LocalDateTime from,
                                                 LocalDateTime to,
                                                 Long interviewerId,
                                                 String cursor,
                                                 int limit) {
        CursorUtil.DecodedCursor decoded;
        try {
            decoded = CursorUtil.decode(cursor);
        } catch (IllegalArgumentException e) {
            throw new com.example.scheduler.exception.BadRequestException("Invalid cursor");
        }
        LocalDateTime cursorTs = decoded != null ? LocalDateTime.parse(decoded.startTs()) : from;
        Long cursorId = decoded != null ? decoded.slotId() : 0L;
        List<InterviewSlot> slots = slotPort.findOpenPage(from, to, interviewerId, cursorTs, cursorId, limit + 1);
        boolean hasNext = slots.size() > limit;
        List<InterviewSlot> page = hasNext ? slots.subList(0, limit) : slots;
        String nextCursor = null;
        if (hasNext) {
            InterviewSlot last = page.get(page.size() - 1);
            nextCursor = CursorUtil.encode(last.getId(), last.getStartTs().toString());
        }
        List<SlotResponse> responses = page.stream()
                .map(s -> SlotResponse.builder()
                        .id(s.getId())
                        .interviewerId(s.getInterviewerId())
                        .startTs(s.getStartTs())
                        .endTs(s.getEndTs())
                        .capacity(s.getCapacity())
                        .status(s.getStatus().name())
                        .build())
                .collect(Collectors.toList());
        return CursorPage.<SlotResponse>builder()
                .items(responses)
                .nextCursor(nextCursor)
                .hasNext(hasNext)
                .build();
    }
}

