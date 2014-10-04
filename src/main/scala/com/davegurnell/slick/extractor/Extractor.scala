package com.davegurnell.slick.extractor

import scala.collection.immutable.Queue

sealed trait Extractor[A, B] {
  type Accumulator

  final def apply(input: Seq[A]): Seq[B] = {
    if(input.isEmpty) {
      Queue.empty[B]
    } else {
      var result = Queue.empty[B]

      val accum0 = init(input.head)
      val accum1 = input.tail.foldLeft(accum0) { (accum, item) =>
        if(mustEmit(item, accum)) {
          result = result :+ emit(accum)
          init(item)
        } else {
          accumulate(item, accum)
        }
      }

      result :+ emit(accum1)
    }
  }

  def init(input: A): Accumulator

  def accumulate(input: A, accum: Accumulator): Accumulator

  def mustEmit(input: A, accum: Accumulator): Boolean

  def emit(accum: Accumulator): B
}

final class SingleExtractor[A](val distinct: Boolean) extends Extractor[A, A] {
  type Accumulator = A

  def init(input: A): Accumulator =
    input

  def accumulate(input: A, accum: Accumulator): Accumulator =
    input

  def mustEmit(input: A, accum: Accumulator): Boolean =
    if(distinct) input != accum else true

  def emit(accum: Accumulator): A =
    accum
}

final class SeqExtractor[A](val grouped: Boolean) extends Extractor[A, Seq[A]] {
  type Accumulator = Seq[A]

  def init(input: A): Accumulator =
    Queue(input)

  def accumulate(input: A, accum: Accumulator): Accumulator =
    accum :+ input

  def mustEmit(input: A, accum: Accumulator): Boolean =
    if(grouped) input != accum.last else false

  def emit(accum: Accumulator): Seq[A] =
    accum
}

trait TupleExtractor[A, B] extends Extractor[A, B]
