Slick Extractors
================

Copyright 2014 Dave Gurnell. Licensed under the [Apache License 2.0].

Based on open source code from [SQLest], Copyright 2014 [JHC Systems Limited].

[Apache License 2.0]: http://www.apache.org/licenses/LICENSE-2.0.html
[SQLest]: https://github.com/jhc-systems/sqlest
[JHC Systems Limited]: https://github.com/jhc-systems

About
-----

Slick Extractors allows users to iterate over the results of a Slick query, grouping results together in sensible ways according to the join structure of the query.

Slick Extractors is a port of the *extractors library* from *SQLest* by JHC Systems.

This code is currently in an alpha state and feature limited. Suggestions and contributions welcome.

Synopsis
--------

The extractor library isn't as powerful as SQLest extractors, but it does provide enough primitives to satisfy common use cases:

~~~ scala
import com.davegurnell.slick.extractor.Extract

// Suppose we have the results of a Slick query:
val results: Seq[(Int, String)] = Seq(
  (1, "a"),
  (1, "a"),
  (1, "b"),
  (2, "b"),
  (2, "c"),
  (2, "c")
)

// A "single" extractor accumulates every row from the results:
val single = Extract.single[(Int, String)]

single(results)
// == results

// A "distinct" extractor only accumulates results when the row content changes:
val distinct = Extract.distinct[(Int, String)]

distinct(results)
// == Seq(
//   (1, "a"),
//   (1, "b"),
//   (2, "b"),
//   (2, "c")
// )

// An "all" extractor accumulates all rows as a single result:
val all = Extract.all[(Int, String)]

all(results)
// == Seq(results)

// A "grouped" extractor groups identical rows together as individual results:
val grouped = Extract.grouped[(Int, String)]

grouped(results)
// == Seq(
//   Seq((1, "a"), (1, "a")),
//   Seq((1, "b")),
//   Seq((2, "b")),
//   Seq((2, "c"), (2, "c"))
// )

// Finally, "tupled" extractors allow us to combine other extractors:
val tupled = Extract.tupled(
  Extract.distinct[Int],
  Extract.all[String]
)

tupled(results)
// == Seq(
//   (1, Seq("a", "a", "b")),
//   (2, Seq("b", "c", "c"))
// )
~~~

For further examples see the [unit tests].

[unit tests]: https://github.com/davegurnell/slick-extractors/blob/develop/src/test/scala/com/davegurnell/slick/extractor

Acknowledgements
----------------

Thanks to the following for their contributions to SQLest and the design of the extractors library:

- [Brendan Maginnis](https://github.com/brendanator) - author of SQLest, which inspired Slick Extractors
- [Dean Chapman](https://github.com/p14n) - author of SQLer, which inspired SQLest
