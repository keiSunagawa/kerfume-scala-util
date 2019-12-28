package me.kerfume

import me.kerfume.testkit.FreeSpecBase

class FooSpec extends FreeSpecBase {
  "Foo called foo" - {
    "run return foo" in {
      assert(Foo.run == "foo")
    }
  }
}
