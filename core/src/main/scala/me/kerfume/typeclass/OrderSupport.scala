package me.kerfume.typeclass

import cats.Order

object OrderSupport {
  implicit class OrderOps[A: Order](val value: A) {
    def ===(rhs: A): Boolean =
      Order[A].eqv(value, rhs)
    def <(rhs: A): Boolean =
      Order[A].lt(value, rhs)
    def >(rhs: A): Boolean =
      Order[A].gt(value, rhs)
    def <=(rhs: A): Boolean =
      Order[A].lteqv(value, rhs)
    def >=(rhs: A): Boolean =
      Order[A].gteqv(value, rhs)
  }
}
