sbt-kind
========
sbt-kind is a [sbt][sbt] plugin that loads built [Docker][docker] images for your project into a [kind][kind] cluster 
for local development.

Requirements
------------
* [sbt][sbt]
* [kind][kind]
* [Docker][docker] _**(soft dependency)**_
* [sbt-docker][sbt-docker] plugin _**(recommended)**_

Setup
-----
Add sbt-kind as a dependency in `project/plugins.sbt`:

```scala
addSbtPlugin("sbt-kind" % "sbt-kind" % "0.1.0")
```

Enable the plugin: 

```scala
lazy val `project-module` = (project in file("."))
  .enablePlugins(KindPlugin)
```

Useful Keys & Command
---------------------
The below settings will attempt to use your username as the cluster as well as upload all docker images made by 
the current build of the project. 

```scala
import scala.sys.process._

val commonKindSettings = Seq(
    kind / clusterName      := ("whoami" !!).strip,
    kind / dockerImageNames := (docker / imageNames).value.map(_.toString()),
)
```

```scala
addCommandAlias("publishKind", "clean; test; docker; kind; ")
```

```bash
$ sbt publishKind
```

[docker]: https://www.docker.com/
[sbt]: http://www.scala-sbt.org/
[kind]: https://kind.sigs.k8s.io/
[sbt-docker]: https://github.com/marcuslonnberg/sbt-docker