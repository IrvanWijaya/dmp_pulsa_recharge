package dans_multi_pro.recharge_service.controller;

import dans_multi_pro.recharge_service.common.ApiResponse;
import dans_multi_pro.recharge_service.common.aop.annotation.RequireRole;
import dans_multi_pro.recharge_service.config.TokenProvider;
import dans_multi_pro.recharge_service.constant.UserRoleEnum;
import dans_multi_pro.recharge_service.model.dto.RechargeRequestDto;
import dans_multi_pro.recharge_service.model.entity.RechargeRequest;
import dans_multi_pro.recharge_service.service.RechargeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/recharge")
public class recharge_controller {

    @Autowired
    private RechargeService rechargeService;

    @Autowired
    private TokenProvider tokenProvider;

    @RequireRole(UserRoleEnum.USER)
    @GetMapping(value = "/recharge-id/{id}")
    public ResponseEntity<ApiResponse<RechargeRequest>> getRechargeRequest(
            @PathVariable("id") long id,
            @RequestHeader("Authorization") String authHeader
    ) {
        long userId = tokenProvider.getUserIdFromToken(authHeader);
        return ResponseEntity.ok(ApiResponse.success(rechargeService.getStatus(id, userId)));
    }

    @RequireRole(UserRoleEnum.USER)
    @GetMapping(value = "/histories/me")
    public ResponseEntity<ApiResponse<Page<RechargeRequest>>> getHistories(
            @RequestHeader("Authorization") String authHeader,
            Pageable pageable
    ) {
        long userId = tokenProvider.getUserIdFromToken(authHeader);
        return ResponseEntity.ok(ApiResponse.success(rechargeService.getHistories(userId, pageable)));
    }

    @RequireRole(UserRoleEnum.USER)
    @PostMapping(value = "/request")
    public ResponseEntity<ApiResponse<RechargeRequest>> createRequest(
            @RequestBody RechargeRequestDto loginUserDto,
            @RequestHeader("Authorization") String authHeader
    ) {
        long userId = tokenProvider.getUserIdFromToken(authHeader);
        return ResponseEntity.ok(ApiResponse.success(rechargeService.recharge(loginUserDto, userId)));
    }
}
