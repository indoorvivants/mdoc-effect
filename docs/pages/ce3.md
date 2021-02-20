---
title: Cats Effect 3 
mdoc: true
mdoc-group: cats-effect-3
---



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
