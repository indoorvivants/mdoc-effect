---
title: Mdoc cats-effect modifier
scala-mdoc: true
---



```scala mdoc:invisible
import cats.effect._
```

Howdee-doo-dee-doo

```scala mdoc:io
IO.println("25") *> 
IO.println("25") *> 
IO.println("25") *> 
IO.println("25")
```

boom-badi boom boom

```scala mdoc:io
import scala.concurrent.duration._

def go(i: Int): IO[Unit] = i match {
  case 0 => IO.unit
  case n => IO.sleep(5.millis) *> IO.println("tick") >> go(n - 1)
}

go(10)
```
