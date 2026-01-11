## Automatic Interview Scheduling – Design

### 1. Overview
- Interviewers configure weekly availability + weekly interview capacity.
- System pre-generates 14 days of slots from weekly availability.
- Candidates browse available slots (cursor pagination), book exactly one, and can reschedule.
- Hard concurrency guarantees with DB locking to prevent double-booking and respect weekly caps.

### 2. Architecture (Clean)
Controller (REST, no business rules) → Application Services (use-cases) → Domain (models, ports) → Infrastructure (JPA adapters, MySQL, Flyway). Domain is framework-free; adapters map entities ↔ domain using MapStruct.

### 3. Flows
Slot generation (per interviewer):
```
weekly availability -> iterate days (14d) -> build slots if missing (uniq start/end)
```
Booking (pessimistic lock):
```
lock slot FOR UPDATE -> validate open/future/capacity
check candidate already booked -> check interviewer weekly cap (count bookings in week)
insert booking -> decrement slot capacity, mark BOOKED if 0 -> commit
```
Reschedule:
```
lock booking + old slot + new slot -> validate new slot open/capacity
weekly cap check -> increment old slot cap, decrement new slot cap -> set booking RESCHEDULED -> commit
```

### 4. API (v1)
- `POST /api/v1/interviewers` create interviewer.
- `PUT /api/v1/interviewers/{id}/availability` set weekly availability.
- `PUT /api/v1/interviewers/{id}/capacity/{capacity}` update weekly capacity.
- `POST /api/v1/interviewers/{id}/slots/generate?days=14` generate next slots.
- `GET /api/v1/slots?from&to&interviewerId&cursor&limit` list open slots (cursor).
- `POST /api/v1/bookings/slot/{slotId}` body `{candidateId}` book.
- `PATCH /api/v1/bookings/{bookingId}/reschedule` body `{newSlotId}` reschedule.
- `POST /api/v1/candidates` register candidate.

Error model via `GlobalExceptionHandler` → JSON `{timestamp,status,error,message}` with 400/404/409/500.

### 5. DB Schema (MySQL, Flyway)
- `interviewer(id, name, weekly_capacity, created_at)`
- `weekly_availability(id, interviewer_id, day_of_week, start_time, end_time, slot_duration_minutes)`
- `interview_slot(id, interviewer_id, start_ts, end_ts, capacity, status, version, uniq_slot)`
- `candidate(id, name, email unique)`
- `interview_booking(id, candidate_id, slot_id, status, booked_at, version, uniq_candidate_slot)`
Indexes: `idx_slot_status_start(status,start_ts)`, `idx_booking_slot(slot_id)`.

### 6. Concurrency & Race Handling
- **Pessimistic locking** (`@Lock(PESSIMISTIC_WRITE)`) on slot + booking rows during book/reschedule.
- Unique constraints prevent duplicate slot/candidate pairs.
- Weekly cap enforced in locked transaction (`countActiveBookingsForWeek`).
- Slots carry capacity and status; decremented atomically to block double booking.

### 7. Pagination
- Cursor pagination: cursor = base64(`startTs|slotId`), query `(start_ts,id) > cursor` ORDER BY start_ts,id LIMIT N+1.
- Offset risks: slow on large data, unstable under inserts; included only in docs, not implemented.

### 8. Trade-offs
- Pessimistic locking chosen for clarity and predictable behavior under contention; optimistic would need retry loop.
- Pre-generation of slots avoids runtime cost at booking time; could be on-demand to reduce storage.
- Capacity per-slot set to 1; weekly cap governs total per interviewer.

### 9. Testing (plan)
- Unit: week range calc, cursor encoding/decoding.
- Service (H2): book/reschedule respects capacity, weekly limit, candidate single booking, concurrency conflict.
- Controller: MockMvc contract + error codes.

### 10. UI (bonus)
- Thymeleaf page `/` lists slots via API, books with debounced search and button-level debouncing on search.

