package com.davegurnell.slick.extractor

object Extract extends TupleExtractors {
  def single[A]   = new SingleExtractor[A](false)
  def distinct[A] = new SingleExtractor[A](true)
  def all[A]      = new SeqExtractor[A](false)
  def grouped[A]  = new SeqExtractor[A](true)
}
