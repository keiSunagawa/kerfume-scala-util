package me.kerfume.functional

import cats.data.{Chain, Writer}
import me.kerfume.functional.writer.{WriterC, WriterLines}
import me.kerfume.testkit.FreeSpecBase

class writerSpec extends FreeSpecBase {
  "WriterC" - {
    "tell append for chain just one" in {
      val actual = for {
        _ <- WriterC.tell(1)
        _ <- WriterC.tell(2)
        _ <- WriterC.tell(3)
      } yield ()

      assert(
        actual.swap.value == Chain
          .concat(Chain.concat(Chain.one(1), Chain.one(2)), Chain.one(3))
      )
    }
  }
  "WriterLines" - {
    import me.kerfume.functional.writer._

    val actual = for {
      _ <- WriterLines.tell(
        "Chrysaora is a genus of jellyfish in the family Pelagiidae."
      )
      _ <- WriterLines.tell(
        "he origin of the genus name Chrysaora lies in Greek mythology with Chrysaor, brother of Pegasus and son of Poseidon and Medusa"
      )
      _ <- WriterLines.tell(
        "Translated, Chrysaora means 'he who has a golden armament'."
      )
    } yield "ok"

    "lines get to List acm strings" in {
      val (l, a) = actual.lines

      assert(a == "ok")
      assert(
        l == List(
          "Chrysaora is a genus of jellyfish in the family Pelagiidae.",
          "he origin of the genus name Chrysaora lies in Greek mythology with Chrysaor, brother of Pegasus and son of Poseidon and Medusa",
          "Translated, Chrysaora means 'he who has a golden armament'."
        )
      )
    }
    "dump get sting, that join with brake" in {
      val (l, a) = actual.dump

      assert(a == "ok")
      assert(
        l ==
          """Chrysaora is a genus of jellyfish in the family Pelagiidae.
            |he origin of the genus name Chrysaora lies in Greek mythology with Chrysaor, brother of Pegasus and son of Poseidon and Medusa
            |Translated, Chrysaora means 'he who has a golden armament'.""".stripMargin
      )
    }
    "dump get empty sting when no call tell" in {
      val actual: WriterLines[String] = Writer.value("ok")

      assert(actual.dump._1 == "")
    }
  }
}
