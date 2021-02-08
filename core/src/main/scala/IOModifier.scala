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

import mdoc._
import cats.effect._
import cats.syntax.all._

class IOSilentModifier extends mdoc.PostModifier {
  override val name = "io-silent"

  val runs = scala.collection.mutable.ListBuffer[String]()

  override def process(ctx: PostModifierContext) = {
    runs.append(ctx.originalCode.text)

    ctx.lastValue match {
      case io: IO[Any] =>
        val result = CECompat.unsafeRun(captureStdOut(io.void))
        val add =
          if (result.nonEmpty)
            s"\n<div class='terminal'><pre><code class = 'nohighlight'>$result</code></pre></div>\n"
          else ""
        "```scala\n" + ctx.originalCode.text + "\n```\n" + add

      case _ => ""
    }
  }

}
