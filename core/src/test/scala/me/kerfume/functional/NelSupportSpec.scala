package me.kerfume.functional

import me.kerfume.testkit.FreeSpecBase

class NelSupportSpec extends FreeSpecBase {
  import me.kerfume.functional.NelSupport._
  import cats.instances.either._
  import cats.syntax.either._
  import cats.instances.list._

  "whenNel" - {
    "underlying has element" - {
      val underlying = List(1, 2, 3)

      "return G wrapped value with callback result" in {
        val actual = underlying.whenNel { nel =>
          nel.map(_ + 1).toList.asRight[String]
        }
        assert(actual == Right(List(2, 3, 4)))
      }

      "return empty if return empty value by callback" in {
        val actual = underlying.whenNel { _ =>
          "fail".asLeft[List[Int]]
        }
        assert(actual == Left("fail"))
      }
    }
    "underlying is empty" - {
      val underlying = List.empty[Int]

      "return G wrapped Nil" in {
        val actual = underlying.whenNel { nel =>
          nel.map(_ + 1).toList.asRight[String]
        }
        assert(actual == Right(Nil))
      }

      "return G wrapped Nil even if return left value by callback" in {
        val actual = underlying.whenNel { _ =>
          "fail".asLeft[List[Int]]
        }
        assert(actual == Right(Nil))
      }
    }
  }
  "whenNel_" - {
    "underlying has element" - {
      val underlying = List(1, 2, 3)

      "return G context in equal to callback value with Right" in {
        val actual = underlying.whenNel_ { _ =>
          ().asRight[String]
        }
        assert(actual == Right(()))
      }

      "return G context in equal to callback value with Left" in {
        val actual = underlying.whenNel_ { _ =>
          "fail".asLeft[Unit]
        }
        assert(actual == Left("fail"))
      }
    }
    "underlying is empty" - {
      val underlying = List.empty[Int]

      "return G sucees value with callback succee context" in {
        val actual = underlying.whenNel_ { _ =>
          ().asRight[String]
        }
        assert(actual == Right(()))
      }

      "return G succee value with callback fail context" in {
        val actual = underlying.whenNel_ { _ =>
          "fail".asLeft[Unit]
        }
        assert(actual == Right(()))
      }
    }
  }
}
