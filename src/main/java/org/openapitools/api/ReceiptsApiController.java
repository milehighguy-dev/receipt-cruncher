package org.openapitools.api;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import org.openapitools.model.Receipt;
import org.openapitools.model.ReceiptsIdPointsGet200Response;
import org.openapitools.model.ReceiptsProcessPost200Response;
import org.openapitools.service.ReceiptsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.NativeWebRequest;
import javax.validation.constraints.*;
import javax.validation.Valid;
import java.util.Optional;

@Controller
@RequestMapping("/receipts")
public class ReceiptsApiController implements ReceiptsApi {

    ReceiptsService receiptsService;

    private final NativeWebRequest request;

    @Autowired
    public ReceiptsApiController(NativeWebRequest request, ReceiptsService receiptsService) {
        this.request = request;
        this.receiptsService = receiptsService;
    }


    @Override
    public Optional<NativeWebRequest> getRequest() {
        return Optional.ofNullable(request);
    }

    @Override
    @RequestMapping(
            method = RequestMethod.GET,
            value = "/{id}/points",
            produces = { "application/json" }
    )
    public ResponseEntity<ReceiptsIdPointsGet200Response> receiptsIdPointsGet(
            @Pattern(regexp = "^\\S+$")
            @Parameter(name = "id", description = "The ID of the receipt.", required = true, in = ParameterIn.PATH)
            @PathVariable("id") String id
    ) {

        Receipt savedReceipt = receiptsService.getReceiptById(id).orElse(null);
        if (savedReceipt != null) {

            Long score = savedReceipt.getScore();
            ReceiptsIdPointsGet200Response response = new ReceiptsIdPointsGet200Response();
            response.setPoints(score);
            return ResponseEntity.ok(response);

        } else {
            return ResponseEntity.notFound().build();
        }
    }


    @Override
    @RequestMapping(
            method = RequestMethod.POST,
            value = "/process",
            produces = { "application/json" },
            consumes = { "application/json" }
    )
    public ResponseEntity<ReceiptsProcessPost200Response> receiptsProcessPost(
            @Parameter(name = "Receipt", description = "", required = true)
            @Valid @RequestBody Receipt receipt
    ) {
        Receipt savedReceipt = receiptsService.processReceipt(receipt);

        if (savedReceipt != null && savedReceipt.getId() != null) {
            ReceiptsProcessPost200Response response =
                    new ReceiptsProcessPost200Response(savedReceipt.getId().toString());

            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

}
