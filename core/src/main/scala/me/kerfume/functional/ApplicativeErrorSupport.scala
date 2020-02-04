package me.kerfume.functional

import cats.ApplicativeError

trait ApplicativeErrorSupport {
  implicit class OptionOps[A](fa: Option[A]) {
    def toError[F[_], E <: Throwable](
        err: E
    )(implicit e: ApplicativeError[F, Throwable]): F[A] =
      e.fromOption(fa, err)
  }
  implicit class EitherOps[E <: Throwable, A](fa: Either[E, A]) {
    def toError[F[_]](implicit e: ApplicativeError[F, Throwable]): F[A] =
      e.fromEither(fa)
  }
}
object ApplicativeErrorSupport extends ApplicativeErrorSupport
