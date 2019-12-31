package me.kerfume.date

import java.time.LocalDate

import me.kerfume.testkit.FreeSpecBase

class DateWrapperSpec extends FreeSpecBase {
  case class TestDate(value: LocalDate) extends DateWrapper[TestDate] {
    override def reprFrom(dt: LocalDate): TestDate = TestDate(dt)
  }

  "lhs === rhs" in {
    assert(
      TestDate(LocalDate.of(2017, 5, 12)) === TestDate(
        LocalDate.of(2017, 5, 12)
      )
    )
  }
  "lhs < rhs" in {
    val lhs = TestDate(LocalDate.of(2017, 5, 12))
    assert(lhs < lhs.plusDays(1))
  }
  "lhs <= rhs" in {
    val lhs = TestDate(LocalDate.of(2017, 5, 12))
    assert(lhs <= lhs.plusDays(1))
    assert(lhs <= TestDate(LocalDate.of(2017, 5, 12)))
  }
  "lhs > rhs" in {
    val lhs = TestDate(LocalDate.of(2017, 5, 12))
    assert(lhs > lhs.minusDays(1))
  }
  "lhs >= rhs" in {
    val lhs = TestDate(LocalDate.of(2017, 5, 12))
    assert(lhs >= lhs.minusDays(1))
    assert(lhs >= TestDate(LocalDate.of(2017, 5, 12)))
  }

  "to" - {
    val fromDate = TestDate(LocalDate.of(2017, 5, 12))
    val toDate = TestDate(LocalDate.of(2017, 5, 15))

    val actual = fromDate to toDate
    "2017/5/12 ~ 2017/5/15" in {
      val expect = List(
        TestDate(LocalDate.of(2017, 5, 12)),
        TestDate(LocalDate.of(2017, 5, 13)),
        TestDate(LocalDate.of(2017, 5, 14)),
        TestDate(LocalDate.of(2017, 5, 15))
      )

      assert(actual == expect)
    }
  }
}
