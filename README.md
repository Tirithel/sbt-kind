sbt-kind
========
sbt-kind is a [sbt][sbt] plugin that loads built [Docker][docker] images for your project into a [kind][kind] cluster for local development.

Requirements
------------
* sbt
* Docker
* kind

Setup
-----

Clone the project and run publishLocal:

```text
sbt publishLocal
```

Add sbt-kind as a dependency in `project/plugins.sbt`:

```text
addSbtPlugin("sbt-kind" % "sbt-kind" % "0.1.0-SNAPSHOT")
```

[docker]: https://www.docker.com/
[sbt]: http://www.scala-sbt.org/
[kind]: https://kind.sigs.k8s.io/