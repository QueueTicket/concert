package com.qticket.concert.domain.venue;

import com.qticket.common.BaseEntity;
import com.qticket.concert.domain.seat.model.Seat;
import com.qticket.concert.presentation.venue.dto.request.UpdateVenueRequest;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
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
public class Venue extends BaseEntity {
  @Id
  @GeneratedValue
  private UUID id;
  @Column(nullable = false)
  private String venueName;
  @Column(nullable = false)
  private String venueAddress;
  @Column(nullable = false)
  private Integer seatCapacity;

  @OneToMany(mappedBy = "venue", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<Seat> seats = new ArrayList<>();

  public void update(UpdateVenueRequest request) {
    if(request.getVenueName() != null){
      venueName = request.getVenueName();
    }
    if(request.getVenueAddress() != null){
      venueAddress = request.getVenueAddress();
    }
    if(request.getSeatCapacity() != null){
      seatCapacity = request.getSeatCapacity();
    }
  }
}
