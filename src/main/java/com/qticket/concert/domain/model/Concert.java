package com.qticket.concert.domain.model;

import com.qticket.common.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import java.time.LocalDateTime;
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

  @Id
  @GeneratedValue
  private UUID id;
  @Column(nullable = false)
  private String concertName;
  @Column(nullable = false)
  private LocalDateTime concertStartTime;
  @Column(nullable = false)
  private Integer playTime;
  private String description;

}
