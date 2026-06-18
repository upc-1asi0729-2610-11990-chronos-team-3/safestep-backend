package com.safestep.platform.gamification.interfaces.rest;

import com.safestep.platform.shared.application.services.CurrentUserService;
import com.safestep.platform.gamification.application.queryservices.GamificationQueryService;
import com.safestep.platform.gamification.domain.model.queries.*;
import com.safestep.platform.gamification.interfaces.rest.transform.GamificationResourceAssembler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/v1/gamification", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Gamification", description = "XP, coins, missions, badges and leaderboard endpoints")
public class GamificationController {

    private final GamificationQueryService gamificationQueryService;
    private final CurrentUserService currentUserService;

    public GamificationController(GamificationQueryService gamificationQueryService,
            CurrentUserService currentUserService) {
        this.gamificationQueryService = gamificationQueryService;
        this.currentUserService = currentUserService;
    }

    @GetMapping("/summary/me")
    @Operation(summary = "Get current user gamification summary")
    public ResponseEntity<?> getMySummary() {
        return ResponseEntity.ok(GamificationResourceAssembler
                .toResource(gamificationQueryService.handle(new GetSummaryQuery(currentUserService.username()))));
    }

    @GetMapping("/missions")
    @Operation(summary = "Get available missions")
    public ResponseEntity<?> getMissions() {
        var username = currentUserService.username();
        return ResponseEntity.ok(gamificationQueryService.handle(new GetMissionsQuery()).stream()
                .map(m -> GamificationResourceAssembler.toResource(m,
                        gamificationQueryService.handle(new GetMissionProgressQuery(username, m.getExternalId()))))
                .toList());
    }

    @GetMapping("/badges/me")
    @Operation(summary = "Get current user badges")
    public ResponseEntity<?> getMyBadges() {
        var username = currentUserService.username();
        var unlocked = gamificationQueryService.handle(new GetUnlockedBadgeIdsQuery(username));
        return ResponseEntity.ok(gamificationQueryService.handle(new GetBadgesQuery(username)).stream()
                .map(b -> GamificationResourceAssembler.toResource(b, unlocked.contains(b.getExternalId()))).toList());
    }

    @GetMapping("/leaderboard")
    @Operation(summary = "Get SafeStep leaderboard")
    public ResponseEntity<?> getLeaderboard() {
        var players = gamificationQueryService.handle(new GetLeaderboardQuery());
        var resources = java.util.stream.IntStream.range(0, players.size())
                .mapToObj(index -> GamificationResourceAssembler.toResource(players.get(index), index + 1)).toList();
        return ResponseEntity.ok(resources);
    }

    @GetMapping("/coin-transactions/me")
    @Operation(summary = "Get current user coin transactions")
    public ResponseEntity<?> getMyCoinTransactions() {
        return ResponseEntity
                .ok(gamificationQueryService.handle(new GetCoinTransactionsQuery(currentUserService.username())).stream()
                        .map(GamificationResourceAssembler::toResource).toList());
    }
}
