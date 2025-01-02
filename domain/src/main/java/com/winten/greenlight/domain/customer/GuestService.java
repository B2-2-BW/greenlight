package com.winten.greenlight.domain.customer;

import com.winten.greenlight.support.error.CoreException;
import com.winten.greenlight.support.error.ErrorType;
import com.winten.greenlight.support.util.TSIDGenerator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class GuestService {
    private final TSIDGenerator tsidGenerator;
    private final GuestRepository guestRepository;

    public Mono<Guest> register(String eventId) {
        String guestId = tsidGenerator.generate();
        Guest guest = new Guest(eventId, guestId, WaitingStatus.WAITING);
        return guestRepository.save(guest)
                .flatMap(Mono::just)
                .doOnError(e -> log.error("Register error: {}", e.getMessage()));
    }

    public Mono<GuestStatus> poll(String eventId, String guestId) {
        Guest guest = new Guest(eventId, guestId, WaitingStatus.WAITING);
        return guestRepository.findRank(guest)
                .zipWith(guestRepository.size(WaitingStatus.WAITING.name()))
                .flatMap(tuple -> Mono.just(
                        GuestStatus.builder()
                                .status(WaitingStatus.WAITING)
                                .position(tuple.getT1())
                                .size(tuple.getT2())
                                .estimatedWaitingTime(calculateEstimatedWaitingTime(tuple.getT1(), tuple.getT2()))
                                .entranceTicket(EntranceTicket.empty())
                                .build()
                ))
                .switchIfEmpty(
                        isReady(eventId, guestId)
                                .flatMap(ready -> {
                                    if (ready) {
                                        return issueTicket(eventId, guestId);
                                    }
                                    else {
                                        return Mono.error(new CoreException(ErrorType.GUEST_NOT_FOUND));
                                    }
                                })
                )
                .doOnError(e -> log.error("Polling error: {}", e.getMessage()));

    }

    public Mono<GuestStatus> issueTicket(String eventId, String guestId) {
        Guest guest = new Guest(eventId, guestId, WaitingStatus.READY);
        return Mono.just(
                GuestStatus.builder()
                        .status(WaitingStatus.READY)
                        .entranceTicket(createTicket(guest))
                        .build()
        );
    }

    private Mono<Boolean> isReady(String eventId, String guestId) {
        Guest readyGuest = new Guest(eventId, guestId, WaitingStatus.READY);
        return guestRepository.findRank(readyGuest)
                .flatMap(rank -> Mono.just(true))
                .switchIfEmpty(Mono.just(false));
    }

    private long calculateEstimatedWaitingTime(long rank, long size) {
        return rank;
    }

    private EntranceTicket createTicket(Guest guest) {
        String redirectUrl = "https://www.thehyundai.com/front/bda/BDALiveBrodViewer.thd?pLiveBfmtNo=202411130001";
        return EntranceTicket.builder()
                .issuedAt(LocalDateTime.now())
                .owner(guest.guestId())
                .token(guest.tokenize())
                .redirectUrl(redirectUrl)
                .build();
    }

    public Mono<List<Boolean>> moveFirstNCustomerToEntryQueue(Integer count) {
        return guestRepository.findAll(WaitingStatus.WAITING.name(), count)
                .flatMap(waitingGuest -> {
                        var readyGuest = waitingGuest.convert(WaitingStatus.READY);
                        return guestRepository.save(readyGuest)
                                .flatMap(__ -> guestRepository.remove(waitingGuest))
                                .doOnNext(success -> log.info("Customer moved to ready queue: {}", success));
                        }
                )
                .collectList()
                .doOnNext(list -> log.info("Moved {} customers to ready queue", list.size()))
                .onErrorResume(Mono::error);
    }
}