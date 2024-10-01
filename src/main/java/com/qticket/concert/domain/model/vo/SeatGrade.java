package com.qticket.concert.domain.model.vo;

import jakarta.persistence.Enumerated;

public enum SeatGrade {
  R("R석"),
  S("S석"),
  A("A석"),
  B("B석");

  final String name;

  SeatGrade(String name) {
    this.name = name;
  }
}
