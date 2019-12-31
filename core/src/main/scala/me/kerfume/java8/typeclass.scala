package me.kerfume.java8

import java.time.LocalDate

import cats.Order

trait LocalDateInstance {
  implicit val localDateOrder: Order[LocalDate] = Order.from(_ compareTo _)
}
