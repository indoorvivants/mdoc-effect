package mdoc.effect

import subatomic.builders._
import subatomic.builders.librarysite._

object Build extends LibrarySite.App {
  def config = LibrarySite(
    contentRoot = os.pwd / "docs" / "pages",
    name = "Mdoc helper for Cats Effect"
  )
}
