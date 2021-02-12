---
title: Home 
scala-mdoc: true
---

A very simple modifier to write documentation about Cats Effect libraries.


## Installation

This library is published to Maven Central.

Add the following dependency to your documentation project (the one on which
`MdocPlugin` is enabled)

Cats Effect 3:

```scala
libraryDependencies += "com.indoorvivants" %% "mdoc-effect-ce3" % "@VERSION@"
```

Cats Effect 2:

```scala
libraryDependencies += "com.indoorvivants" %% "mdoc-effect-ce2" % "@VERSION@"
```


## Usage

Just use `io-silent` and make sure the last value the codeblock returns is
of type `IO[_]`, for example:

````md
```scala mdoc:io-silent
import cats.effect._
import scala.concurrent.duration._

def go(i: Int): IO[Unit] = i match {
  case 0 => IO.unit
  case n => IO.sleep(5.millis) *> IO.println(s"tick $i") >> go(n - 1)
}

go(10)
```
````

Which will be executed, the output captured and rendered separately:


```scala mdoc:io-silent
import cats.effect._
import scala.concurrent.duration._

def go(i: Int): IO[Unit] = i match {
  case 0 => IO.unit
  case n => IO.sleep(5.millis) *> IO.println(s"tick $i") >> go(n - 1)
}

go(10)
```

## Limitations

1. You can't mix modifiers (i.e. `mdoc:io-silent:passthrough`)
   
   This is a limitation of mdoc, which handles built-in modifiers separately
   from user provided ones. I hope to fix this upstream

2. You can't have a mixture of some variables being rendered, and some
   not. Again, I believe this is a limitation in mdoc - you only get a hold
   of raw original code, or fully rendered one.
