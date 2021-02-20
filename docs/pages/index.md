---
title: Home 
mdoc: true
mdoc-group: cats-effect-3
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



## Limitations

1. You can't mix modifiers (i.e. `mdoc:io-silent:passthrough`)
   
   This is a limitation of mdoc, which handles built-in modifiers separately
   from user provided ones. I hope to fix this upstream

2. You can't have a mixture of some variables being rendered, and some
   not. Again, I believe this is a limitation in mdoc - you only get a hold
   of raw original code, or fully rendered one.
