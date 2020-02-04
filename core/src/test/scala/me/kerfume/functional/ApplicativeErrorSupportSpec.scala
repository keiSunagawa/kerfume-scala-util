package me.kerfume.functional

import me.kerfume.testkit.FreeSpecBase

import scala.util.Try

class ApplicativeErrorSupportSpec extends FreeSpecBase {
  import cats.instances.try_._
  import me.kerfume.functional.ApplicativeErrorSupport._

  "OptionOps" - {
    "toError raise throwable when None" in {
      val x: Option[Int] = None

      val err = new RuntimeException("error with OptionOps test")
      val actual: Try[Int] = x.toError(err)

      assert(actual.isFailure)
      assert(actual.failed.get == err)
    }
    "toError conv to success F[_] when Some" in {
      val x: Option[Int] = Some(3)

      val err = new RuntimeException("error with OptionOps test")
      val actual: Try[Int] = x.toError(err)

      assert(actual.isSuccess)
      assert(actual.get == 3)
    }
  }
  "EitherOps" - {
    "toError raise throwable when Left" in {
      val err = new RuntimeException("error with EitherOps test")

      val x: Either[RuntimeException, Int] = Left(err)

      val actual: Try[Int] = x.toError

      assert(actual.isFailure)
      assert(actual.failed.get == err)
    }
    "toError conv to success F[_] when Right" in {
      val x: Either[RuntimeException, Int] = Right(3)

      val actual: Try[Int] = x.toError

      assert(actual.isSuccess)
      assert(actual.get == 3)
    }
  }
}
