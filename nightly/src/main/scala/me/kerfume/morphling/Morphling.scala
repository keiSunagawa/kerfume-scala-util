package me.kerfume.morphling

import scala.language.experimental.macros

class morphling extends scala.annotation.StaticAnnotation {
  def macroTransform(annottees: Any*): Any = macro MorphlingMacro.impl
}

import scala.reflect.macros.whitebox

class MorphlingMacro(val c: whitebox.Context) {
  import c.universe._

  def impl(annottees: Tree*) = {
    val outputs: List[Tree] = annottees.toList match {
      case (cd @ ClassDef(_, cName, _, _)) :: tail =>
        val mod0: ModuleDef = tail match {
          case (md @ ModuleDef(_, mName, mTemp)) :: Nil
              if cName.decodedName.toString == mName.decodedName.toString =>
            md

          case _ => c.abort(c.enclosingPosition, "Expected a companion object")
        }

        val members = {
          val q"$_ trait $_[..$_] extends { ..$_ } with ..$_ { $_ => ..$cstats }" =
            cd

          cstats.collect {
            case q"$_ val $name: $tpt = $rhs" if rhs.isEmpty =>
              name -> tpt
          }
        }
        println(members)

        val q"$mods object $tname extends { ..$earlydefns } with ..$parents { $self => ..$body }" =
          mod0

        val behaviorDefs = {
          members.map {
            case (name, tpe) =>
              q"def $name(a: $tpe): ${cd.name}"
          }
        }
        println(behaviorDefs)
        val fooDef = q"def foo: Int = 4"
        val mTempBody1 = fooDef +: body
        val mod1 =
          q"$mods object $tname extends { ..$earlydefns } with ..$parents { $self => ..$mTempBody1 }"

        cd :: mod1 :: Nil

      case _ => c.abort(c.enclosingPosition, "Must annotate a class or trait")
    }

    q"..$outputs"
  }
}
