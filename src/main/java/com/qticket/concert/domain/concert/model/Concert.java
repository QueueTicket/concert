package com.qticket.concert.domain.concert.model;

import com.qticket.common.BaseEntity;
import com.qticket.concert.presentation.concert.dto.requset.PriceRequest;
import com.qticket.concert.presentation.concert.dto.requset.UpdateConcertRequest;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Concert extends BaseEntity {

  @Id @GeneratedValue private UUID id;

  @Column(nullable = false)
  private String concertTitle;

  @Column(nullable = false)
  private LocalDateTime concertStartTime;

  @Column(nullable = false)
  private Integer playTime;

  private String description;

  @OneToMany(mappedBy = "concert", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<Price> prices = new ArrayList<>();

  public void update(UpdateConcertRequest request) {
    if (request.getConcertTitle() != null) {
      this.concertTitle = request.getConcertTitle();
    }

    if (request.getConcertStartTime() != null) {
      this.concertStartTime = request.getConcertStartTime();
    }

    if (request.getDescription() != null) {
      this.description = request.getDescription();
    }

    if(request.getPlaytime() != null) {
      this.playTime = request.getPlaytime();
    }

    if (request.getPrices() != null) {
      for (PriceRequest priceRequest : request.getPrices()) {
        for (Price price : prices) {
          price.updatePrice(priceRequest);
        }
      }
    }
  }
}
