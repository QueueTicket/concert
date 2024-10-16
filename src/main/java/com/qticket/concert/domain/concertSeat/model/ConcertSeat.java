package com.qticket.concert.domain.concertSeat.model;

import com.qticket.common.BaseEntity;
import com.qticket.concert.domain.concert.model.Price;
import com.qticket.concert.domain.seat.model.Seat;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.io.Serializable;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLRestriction;

@Entity
@Getter
@Builder
@NoArgsConstructor @AllArgsConstructor
@SQLRestriction("is_delete is false")
public class ConcertSeat extends BaseEntity implements Serializable {
  @Id
  @GeneratedValue
  private UUID id;
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "price_id")
  private Price price;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "seat_id")
  private Seat seat;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private SeatStatus status;

  public void changeStatus(SeatStatus status) {
    this.status = status;
  }

  public void updateConcertSeat(SeatStatus status, Price price) {
    if (status != null) {
      this.status = status;
    }

    if(price != null) {
      this.price = price;
    }
  }
}
