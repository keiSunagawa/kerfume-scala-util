package me.kerfume.functional

import cats._
import cats.data.NonEmptyList

trait NelSupport {
  protected def whenNel0[A, B, F[_]: MonoidK, G[_]: Applicative](
      xs: List[A]
  )(f: NonEmptyList[A] => G[F[B]]): G[F[B]] = {
    NonEmptyList.fromList(xs) match {
      case Some(nel) => f(nel)
      case None =>
        implicitly[Applicative[G]].pure(implicitly[MonoidK[F]].empty[B])
    }
  }
  protected def whenNel0_[A, G[_]: Applicative](
      xs: List[A]
  )(f: NonEmptyList[A] => G[Unit]): G[Unit] = {
    NonEmptyList.fromList(xs) match {
      case Some(nel) => f(nel)
      case None      => implicitly[Applicative[G]].pure(())
    }
  }

  implicit class ListOps[A](xs: List[A]) {
    def whenNel[B, F[_]: MonoidK, G[_]: Applicative](
        f: NonEmptyList[A] => G[F[B]]
    ): G[F[B]] = whenNel0(xs)(f)
    def whenNel_[G[_]: Applicative](f: NonEmptyList[A] => G[Unit]): G[Unit] =
      whenNel0_(xs)(f)
  }
}
object NelSupport extends NelSupport
