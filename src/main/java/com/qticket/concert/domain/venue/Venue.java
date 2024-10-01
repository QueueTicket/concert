package com.qticket.concert.domain.venue;

import com.qticket.common.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@NoArgsConstructor @AllArgsConstructor
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

}
