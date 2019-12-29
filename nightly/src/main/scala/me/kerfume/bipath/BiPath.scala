package me.kerfume.bipath

trait BiPath[X, Y] { self =>
  val leftIn: X => Y
  val rightIn: Y => Option[X]

  def completion[XX <: X](f: PartialFunction[Y, XX]): BiPath[X, Y] =
    new BiPath[X, Y] {
      val leftIn: X => Y = self.leftIn
      val rightIn: Y => Option[X] = { y =>
        f.lift(y).orElse(rightIn(y))
      }
    }
}

import scala.language.experimental.macros
import scala.reflect.macros.blackbox

object BiPath {
  def create[X, Y](f: X => Y): BiPath[X, Y] =
    macro BiPathMacros.createImpl[X, Y]

  def apply[X, Y](lin: X => Y, rin: Y => Option[X]): BiPath[X, Y] =
    new BiPath[X, Y] {
      val leftIn: X => Y = lin
      val rightIn: Y => Option[X] = rin
    }
}

class BiPathMacros(val c: blackbox.Context) {
  import c.universe._
  import cats.instances.list._
  import cats.instances.either._
  import cats.syntax.traverse._
  import cats.syntax.either._

  def createImpl[X: c.WeakTypeTag, Y: c.WeakTypeTag](f: Tree) = {
    val symbol = weakTypeOf[X].typeSymbol.asClass
    val children = symbol.knownDirectSubclasses.toList

    val objects = children.filter(_.isModuleClass).map(_.fullName)

    val xTpe = weakTypeOf[X]
    val yTpe = weakTypeOf[Y]

    val leftIn = for {
      expr <- f match {
        case q"(..$_) => $expr" => expr.asRight
        case _                  => "args is required pattern match syntax.".asLeft
      }
      matches <- expr match {
        case q"$_ match { case ..$ms }" => ms.asRight
        case _                          => "args is required pattern match syntax.".asLeft
      }
      revMatches <- matches
        .traverse[({ type T[A] = Either[String, A] })#T, Option[Tree]] {
          case cq"$a => $b " if objects.contains(a.symbol.fullName) =>
            if (b.collect { case l: Literal => l }.nonEmpty) {
              Some(cq"$b => Some($a): Option[$xTpe]").asRight
            } else {
              "rhs is required literal syntax.".asLeft
            }
          case _ => None.asRight
        }
      withNonePattern = revMatches.flatten :+ cq"_ => None"
    } yield q"(x: $yTpe) => x match { case ..$withNonePattern }"

    leftIn match {
      case Right(res) =>
        q"me.kerfume.bipath.BiPath.apply[$xTpe, $yTpe]($f, $res)"
      case Left(emsg) => c.abort(c.enclosingPosition, emsg)
    }
  }
}

object Test {}
