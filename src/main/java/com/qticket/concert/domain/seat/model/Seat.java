package com.qticket.concert.domain.seat.model;

import com.qticket.common.BaseEntity;
import com.qticket.concert.domain.seat.model.SeatGrade;
import com.qticket.concert.domain.venue.Venue;
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
public class Seat extends BaseEntity implements Serializable {
  @Id
  @GeneratedValue
  private UUID id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "venue_id")
  private Venue venue;

  @Column(nullable = false)
  private Integer seatNumber;

  @Column(nullable = false)
  @Enumerated(EnumType.STRING)
  private SeatGrade seatGrade;

  public void addSeat(Venue venue){
    venue.getSeats().add(this);
  }

  public void updateSeat(SeatGrade seatGrade, Integer seatNumber){
    if(seatGrade != null){
      this.seatGrade = seatGrade;
    }
    if(seatNumber != null){
      this.seatNumber = seatNumber;
    }
  }


}
