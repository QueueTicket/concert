package com.qticket.concert.infrastructure.repository.price;

import com.qticket.concert.domain.concert.model.Price;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PriceRepository extends JpaRepository<Price, UUID> {

  @Query("select p from Price p where p.id IN :priceIds")
  List<Price> findPriceByPriceIds(@Param("priceIds") List<UUID> priceIds);
}
