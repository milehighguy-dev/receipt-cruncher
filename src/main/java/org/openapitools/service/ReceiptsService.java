package org.openapitools.service;

import org.openapitools.model.Item;
import org.openapitools.model.Receipt;
import org.openapitools.repository.ReceiptsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class ReceiptsService {

    ReceiptsRepository receiptsRepository;

    @Autowired
    ReceiptsService(ReceiptsRepository receiptsRepository) {
        this.receiptsRepository = receiptsRepository;
    }

    public Receipt processReceipt(Receipt receipt) {

        // needed for JPA relationships
        for (Item item : receipt.getItems()) {
            item.setReceipt(receipt);
        }

        // generate score based on ruleset
        ReceiptsScorer.score(receipt);

        return receiptsRepository.save(receipt);
    }

    public Optional<Receipt> getReceiptById(String id) {

        return receiptsRepository.findById(UUID.fromString(id));

    }
}
