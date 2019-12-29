package me.kerfume.bipath

import me.kerfume.testkit.FreeSpecBase

class BiPathSpec extends FreeSpecBase {
  import BiPathSpec._
  import TestEnum._

  "create" - {
    val actual: BiPath[TestEnum, Int] = BiPath.create {
      case Foo     => 1
      case Bar     => 2
      case Piyo(n) => n
    }

    "leftIn convert Enum to right literal value" in {
      assert(actual.leftIn(Foo) == 1)
      assert(actual.leftIn(Bar) == 2)
      assert(actual.leftIn(Piyo(3)) == 3)
    }
    "rightIn convert right value to optional Enum with Singlton value" in {
      assert(actual.rightIn(1) == Some(Foo))
      assert(actual.rightIn(2) == Some(Bar))
      assert(actual.rightIn(3) == None)
    }
    "completion" - {
      "rightIn support user completion matching" in {
        val complitionActual = actual.completion {
          case 3 => Piyo(3)
        }

        assert(complitionActual.rightIn(3) == Some(Piyo(3)))
      }
    }
  }
}

object BiPathSpec {
  sealed trait TestEnum
  object TestEnum {
    case object Foo extends TestEnum
    case object Bar extends TestEnum
    case class Piyo(x: Int) extends TestEnum
  }
}
