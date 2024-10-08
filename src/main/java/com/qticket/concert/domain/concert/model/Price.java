package com.qticket.concert.domain.concert.model;

import com.qticket.common.BaseEntity;
import com.qticket.concert.domain.seat.model.SeatGrade;
import com.qticket.concert.presentation.concert.dto.requset.PriceRequest;
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
public class Price extends BaseEntity implements Serializable {
  @Id
  @GeneratedValue
  private UUID id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "concert_id")
  private Concert concert;
  @Column(nullable = false)
  private Integer price;

  @Enumerated(EnumType.STRING)
  private SeatGrade seatGrade;

  public void addConcert(Concert concert) {
    this.concert = concert;
    concert.getPrices().add(this);
  }

  public void updatePrice(PriceRequest request){
    price = request.getPrice();
    seatGrade = request.getSeatGrade();
}
}
