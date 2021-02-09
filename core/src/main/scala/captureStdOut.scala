/*
 * Copyright 2021 Anton Sviridov
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package mdoc.effect
import cats.effect._
import java.io._
import java.nio.charset.StandardCharsets

object captureStdOut {

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
      _   <- IO(set(ps))
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

  def apply(io: => IO[Unit]): IO[String] = {
    val test = for {
      out <- CECompat.resource(IO(new ByteArrayOutputStream()))
      ps  <- printStream(out)
      _   <- replaceStandardOut(ps)
    } yield out

    test.use(out => io.as(out)).flatMap(extractMessage)
  }

}
