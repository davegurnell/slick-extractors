package com.davegurnell.slick.extractor

import org.scalatest._
import org.scalatest.matchers._
import scala.language.existentials

class ExtractorSpec extends FlatSpec with Matchers {
  "single extractor" should "extract all values" in {
    val input = Seq(1, 1, 1, 2, 2, 2)

    val extractor = Extract.single[Int]

    extractor(input) should equal(input)
  }

  "distinct extractor" should "extract distinct values" in {
    val input = Seq(1, 1, 1, 2, 2, 2)

    val extractor = Extract.distinct[Int]

    extractor(input) should equal(Seq(1, 2))
  }

  "all extractor" should "extract everything as a single sequence" in {
    val input = Seq(1, 1, 1, 2, 2, 2)

    val extractor = Extract.all[Int]

    extractor(input) should equal(Seq(
      Seq(1, 1, 1, 2, 2, 2)
    ))
  }

  "grouped extractor" should "extract sequences of distinct values" in {
    val input = Seq(1, 1, 1, 2, 2, 2)

    val extractor = Extract.grouped[Int]

    extractor(input) should equal(Seq(
      Seq(1, 1, 1),
      Seq(2, 2, 2)
    ))
  }

  "tuple extractor of single extractors" should "extract individual tuples" in {
    val input = Seq(
      (1, "a"),
      (1, "a"),
      (1, "a"),
      (2, "b"),
      (2, "b"),
      (2, "b")
    )

    val extractor = Extract.tuple(
      Extract.single[Int],
      Extract.single[String]
    )

    extractor(input) should equal(input)
  }

  "tuple extractor of distinct/all extractors" should "extract one tuple per distinct value" in {
    val input = Seq(
      (1, "a", true),
      (1, "a", true),
      (1, "a", false),
      (2, "b", false),
      (2, "b", true),
      (2, "b", true)
    )

    val extractor = Extract.tuple(
      Extract.distinct[Int],
      Extract.all[String],
      Extract.all[Boolean]
    )

    extractor(input) should equal(Seq(
      (1, Seq("a", "a", "a"), Seq(true, true, false)),
      (2, Seq("b", "b", "b"), Seq(false, true, true))
    ))
  }

  "tuple extractor of distinct/grouped extractors" should "extract one value per unique combination" in {
    val input = Seq(
      (1, "a", true),
      (1, "a", true),
      (1, "a", false),
      (2, "b", false),
      (2, "b", true),
      (2, "b", true)
    )

    val extractor = Extract.tuple(
      Extract.distinct[Int],
      Extract.grouped[String],
      Extract.grouped[Boolean]
    )

    extractor(input) should equal(Seq(
      (1, Seq("a", "a"), Seq(true, true)),
      (1, Seq("a"), Seq(false)),
      (2, Seq("b"), Seq(false)),
      (2, Seq("b", "b"), Seq(true, true))
    ))
  }

}
