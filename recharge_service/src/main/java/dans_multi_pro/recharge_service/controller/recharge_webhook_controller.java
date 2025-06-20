package dans_multi_pro.recharge_service.controller;

import dans_multi_pro.recharge_service.common.ApiResponse;
import dans_multi_pro.recharge_service.model.dto.ThirdPartyWebhookDto;
import dans_multi_pro.recharge_service.service.RechargeWebhookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/recharge-webhook")
public class recharge_webhook_controller {

    @Autowired
    private RechargeWebhookService rechargeWebhookService;

    @PostMapping(value = "/update")
    public ResponseEntity<ApiResponse<Boolean>> createRequest(
            @RequestBody ThirdPartyWebhookDto data,
            @RequestHeader("x-third-party-key") String thirdPartyKeyHeader
    ) {
        if(rechargeWebhookService.validateThirdPartyKey(thirdPartyKeyHeader)){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized access");
        }
        rechargeWebhookService.updateData(data);
        return ResponseEntity.ok(ApiResponse.success(true));
    }
}
