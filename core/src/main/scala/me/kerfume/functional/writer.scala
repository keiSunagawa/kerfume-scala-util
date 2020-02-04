package me.kerfume.functional

import cats.data.{Writer, Chain}

object writer {
  type WriterC[L, A] = Writer[Chain[L], A]

  object WriterC {
    def tell[L](l: L): WriterC[L, Unit] = Writer.tell(Chain.one(l))
  }

  type WriterLines[A] = WriterC[String, A]

  object WriterLines {
    def tell(line: String): WriterLines[Unit] = WriterC.tell(line)

    def lines[A](w: WriterLines[A]): (List[String], A) = {
      w.mapWritten(_.toList).run
    }

    def dump[A](w: WriterLines[A]): (String, A) = {
      val buf = new StringBuilder()
      val (c, r) = w.run

      c.initLast match {
        case Some((init, last)) =>
          init.map { buf.append(_).append("\n") }
          buf.append(last)
        case None =>
      }

      (buf.toString, r)
    }
  }

  implicit class WriterLinesOps[A](w: WriterLines[A]) {
    def lines: (List[String], A) = WriterLines.lines(w)
    def dump: (String, A) = WriterLines.dump(w)
  }
}
