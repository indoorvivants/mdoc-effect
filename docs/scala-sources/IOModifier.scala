package mdoc.effect

import mdoc._
import cats.effect._
import cats.syntax.all._
import java.io.{PrintStream, ByteArrayOutputStream}
import java.nio.charset.StandardCharsets

import cats.effect.unsafe.implicits.global

class IOModifier extends mdoc.PostModifier {
  override val name = "io"

  val runs = scala.collection.mutable.ListBuffer[String]()

  override def process(ctx: PostModifierContext) = {
    runs.append(ctx.originalCode.text)

    ctx.lastValue match {
      case io: IO[Any] =>
        val ps = new java.io.ByteArrayOutputStream()
        val result = captureStdOut(io.void).unsafeRunSync()
        val add =
          if (result.nonEmpty)
            s"\n<div class='terminal'><pre><code>$result</code></pre>"
          else ""
        "```scala\n" + ctx.outputCode + "\n```" + add

      case _ => ""
    }
  }

  private def printStream(
      out: ByteArrayOutputStream
  ): Resource[IO, PrintStream] =
    Resource.make(IO(new PrintStream(out)))(ps => IO(ps.close()))

  private def replace(
      ps: PrintStream,
      get: () => PrintStream,
      set: PrintStream => Unit
  ): IO[PrintStream] =
    for {
      out <- IO(get())
      _ <- IO(set(ps))
    } yield out

  private def restore(ps: PrintStream, set: PrintStream => Unit): IO[Unit] =
    for {
      _ <- IO(set(ps))
    } yield ()

  private def replaceStandardOut(ps: PrintStream): Resource[IO, Unit] =
    Resource
      .make(replace(ps, () => System.out, System.setOut))(
        restore(_, System.setOut)
      )
      .map(_ => ())

  private def extractMessage(out: ByteArrayOutputStream): IO[String] =
    IO(new String(out.toByteArray(), StandardCharsets.UTF_8))

  private def captureStdOut(io: => IO[Unit]): IO[String] = {
    val test = for {
      out <- Resource.eval(IO(new ByteArrayOutputStream()))
      ps <- printStream(out)
      _ <- replaceStandardOut(ps)
    } yield out

    test.use(out => io.as(out)).flatMap(extractMessage)
  }

}
