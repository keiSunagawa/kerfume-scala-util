package me.kerfume.value

import cats.Order

trait Atom0 {
  type Value
  val value: Value
}

trait Atom[A] extends Atom0 {
  type Value = A
}

object Atom {
  implicit def atomOrder[A, X <: Atom[A]](implicit order: Order[A]): Order[X] =
    Order.from { (x, y) =>
      Order[A].compare(x.value, y.value)
    }
}
