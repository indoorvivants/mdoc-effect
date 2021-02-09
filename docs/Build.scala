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

import java.time.LocalDate

import subatomic._
import subatomic.builders._
import subatomic.builders.librarysite._

object Build extends LibrarySite.App {
  override def extra(site: Site[LibrarySite.Doc]) = {
    site
      .addCopyOf(SiteRoot / "CNAME", os.pwd / "docs" / "assets" / "CNAME")
  }

  val currentYear = LocalDate.now().getYear()

  def config = LibrarySite(
    contentRoot = os.pwd / "docs" / "pages",
    name = "Mdoc helper for Cats Effect",
    githubUrl = Some("https://github.com/indoorvivants/mdoc-effect"),
    assetsFilter = _.baseName != "CNAME",
    copyright = Some(s"© 2021-$currentYear Anton Sviridov"),
    assetsRoot = Some(os.pwd / "docs" / "assets"),
    highlightJS = HighlightJS.default.copy(theme = "monokai-sublime")
  )
}
