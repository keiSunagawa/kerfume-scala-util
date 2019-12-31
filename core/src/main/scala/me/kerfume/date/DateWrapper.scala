package me.kerfume.date

import java.time.LocalDate

import cats.kernel.Order
import me.kerfume.value.Atom

trait DateWrapper[Repr <: DateWrapper[Repr]] extends Atom[LocalDate] {
  import me.kerfume.java8.implicits._
  import Atom._

  val value: LocalDate

  private[this] def repr: Repr = reprFrom(value)

  def reprFrom(dt: LocalDate): Repr

  def plusDays(i: Long): Repr = reprFrom(value.plusDays(i))
  def plusMonth(i: Long): Repr = reprFrom(value.plusMonths(i))
  def plusYear(i: Long): Repr = reprFrom(value.plusYears(i))

  def minusDays(i: Long): Repr = reprFrom(value.minusDays(i))
  def minusMonth(i: Long): Repr = reprFrom(value.minusMonths(i))
  def minusYear(i: Long): Repr = reprFrom(value.minusYears(i))

  def ===(rhs: Repr): Boolean =
    Order[Repr].eqv(repr, rhs)
  def <(rhs: Repr): Boolean =
    Order[Repr].lt(repr, rhs)
  def >(rhs: Repr): Boolean =
    Order[Repr].gt(repr, rhs)
  def <=(rhs: Repr): Boolean =
    Order[Repr].lteqv(repr, rhs)
  def >=(rhs: Repr): Boolean =
    Order[Repr].gteqv(repr, rhs)

  def to(rhs: Repr): List[Repr] = {
    Iterator
      .iterate(repr) { _.plusDays(1) }
      .takeWhile(_ <= rhs)
      .toList
  }
}
