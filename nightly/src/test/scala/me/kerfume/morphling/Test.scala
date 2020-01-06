package me.kerfume.morphling

import me.kerfume.testkit.FreeSpecBase

class Test extends FreeSpecBase {

  "hoge" - {
    println(A.foo + A.b)
  }
}
@morphling trait A {
  val foo: Int
}
object A {
  def b: Int = 4
}
