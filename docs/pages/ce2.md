---
title: Cats Effect 2 
mdoc: true
mdoc-group: cats-effect-2
---



## Usage

Just use `io-silent` and make sure the last value the codeblock returns is
of type `IO[_]`, for example:

````md
```scala mdoc:io-silent
import cats.effect._
import scala.concurrent._
import scala.concurrent.duration._
import cats.syntax.all._

implicit val tm = IO.timer(ExecutionContext.global)
implicit val cs = IO.contextShift(ExecutionContext.global)

def go(i: Int): IO[Unit] = i match {
  case 0 => IO.unit
  case n => IO.sleep(5.millis) *> IO(println(s"tick $i")) >> go(n - 1)
}

go(10)
```
````

Which will be executed, the output captured and rendered separately:


```scala mdoc:io-silent
import cats.effect._
import scala.concurrent._
import scala.concurrent.duration._
import cats.syntax.all._

implicit val tm = IO.timer(ExecutionContext.global)
implicit val cs = IO.contextShift(ExecutionContext.global)

def printIO(s: Any) = IO(System.out.println(s))

def go(i: Int): IO[Unit] = i match {
  case 0 => IO.unit
  case n => IO.sleep(5.millis) *> printIO(s"tick $i") >> go(n - 1)
}

go(10)
```
