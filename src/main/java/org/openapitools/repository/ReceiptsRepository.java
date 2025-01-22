package org.openapitools.repository;

import org.openapitools.model.Receipt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ReceiptsRepository extends JpaRepository<Receipt, UUID> {

}
