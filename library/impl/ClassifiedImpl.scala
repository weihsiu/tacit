package tacit.library

import language.experimental.captureChecking
import caps.unsafe.unsafeAssumePure
import caps.*

import scala.util.Try

private[library] final class ClassifiedImpl[+T](private val value: Try[T]) extends Classified[T]:
  def map[B](op: T ->{any.rd} B): Classified[B] =
    ClassifiedImpl(value.map(op).unsafeAssumePure)
  def flatMap[B](op: T ->{any.rd} Classified[B]): Classified[B] =
    ClassifiedImpl(value.flatMap(v => ClassifiedImpl.unwrap(op(v))).unsafeAssumePure)
  override def toString: String = "Classified(***)"

private[library] object ClassifiedImpl:
  def wrap[T](value: ->{any.rd} T): Classified[T] = ClassifiedImpl(Try(value).unsafeAssumePure)
  /** Wrap an already-evaluated result. Used by impure library sinks (e.g. an
   *  HTTP POST of classified data) that must run an effectful computation and
   *  then re-classify its outcome. The effect happens *before* wrapping, so it
   *  cannot go through `wrap`'s pure-function parameter. */
  def fromTry[T](value: Try[T]): Classified[T] = ClassifiedImpl(value.unsafeAssumePure)
  def unwrap[T](c: Classified[T]): Try[T] = c.asInstanceOf[ClassifiedImpl[T]].value
