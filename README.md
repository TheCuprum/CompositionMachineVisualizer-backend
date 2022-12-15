# CompositionMachineVisualizer-backend

## Basic Usage

This project can be used as a basic simulator of Composition Machine, with the ability of loading internal or external classes as part of the machine.

### CLI Commands

Basic usage is `java -jar composition-machine-visualizer-jar-with-dependencies.jar -Q <QuiverInitializer> -R <RuleSet> [optional_arguments]`:

```
Required options: Q, R

usage:
 -cb, --callbacks <callback.A,callback.B>   Callbacks during execution
 -cp, --custom-classpath <folder>           Custom path to load classes from
 -H, --halt-predicate <halt predicate>      Halt predicate class name
 -help                                      This is a help
 -n, --machine-name <name>                  Machine name
 -o, --dot-output <folder>                  DOT file output directory
 -Q, --quiver-name <quiver initializer>     Quiver initializer class name
 -R, --rule-name <rule set>                 Rule set class name
 -t, --step-time <step>                     CM step time
 ```

 For example, `java -jar composition-machine-visualizer-jar-with-dependencies.jar -Q compositionmachine.examples.ExampleQuiverInitializer -R compositionmachine.examples.ExampleRules2 -H compositionmachine.machine.predicates.LoopPredicate -cp custom` will use `compositionmachine.examples.ExampleQuiverInitializer` as custom quiver initializer, `compositionmachine.examples.ExampleRules2` as execution rules, `compositionmachine.machine.predicates.LoopPredicate` as execution halt predicate, with classes customly loaded from path `./custom` and program's class-path. Other augument values will use its default value.

### Integrated Visualizer

Use `java -cp composition-machine-visualizer-jar-with-dependencies.jar compositionmachine.server.Main` to launch the integrated visualizer, which will automatically start a webserver and open the visualizer web app.

### Quiver Initializers

Quiver initializer provides inital quivers where composition machine will execute on. The integrated quiver initializer class(es) is(are):

- `compositionmachine.examples.ExampleQuiverInitializer`

You can use custom classes by importing this project as external library.

### Rule Sets

Rule set describes how the quiver will chage its state. The integrated rule set class(es) is(are):

- `compositionmachine.examples.ExampleRules1`
- `compositionmachine.examples.ExampleRules2`

You can use custom classes by importing this project as external library.

### Halt Predicates

Halt predicate describes when the quiver should halt. The integrated halt predicte class(es) is(are):

- `compositionmachine.machine.predicates.NullPredicate`
- `compositionmachine.machine.predicates.UnchangePredicate`
- `compositionmachine.machine.predicates.LoopPredicate`

You can use custom classes by importing this project as external library.

### Callbacks

Callback will be called at some specific execution stages. The integrated callback class(es) is(are):

- `compositionmachine.machine.callbacks.EmptyCallback`
- `compositionmachine.machine.callbacks.PrintBlockCallback`
- `compositionmachine.machine.callbacks.SaveDotCallback`

You can use custom classes by importing this project as external library.

## Developing & Use as External Library

Run `mvn clean package` to get build jar and executable jar.

Run `mvn clean install` to install the build to local maven repository. To include this in other projects, add:

```xml
<dependency>
    <groupId>composition-machine-visualizer</groupId>
    <artifactId>composition-machine-visualizer</artifactId>
    <version>1.0-SNAPSHOT</version>
</dependency>
```

to `pom.xml`, or add:

```xml
<dependency>
    <groupId>composition-machine-visualizer</groupId>
    <artifactId>composition-machine-visualizer</artifactId>
    <version>1.0-SNAPSHOT</version>
    <classifier>jar-with-dependencies</classifier>
</dependency>
```
if a executable dependency is needed.

## References

Arellanes, D. (2021). Composition Machines: Programming Self-Organising Software Models for the Emergence of Sequential Program Spaces. ArXiv e-prints, arXiv:2108.05401.

### Libraries

- junit (https://mvnrepository.com/artifact/junit/junit)
- jgrapht-core (https://mvnrepository.com/artifact/org.jgrapht/jgrapht-core)
- jgrapht-io (https://mvnrepository.com/artifact/org.jgrapht/jgrapht-io)
- guava (https://mvnrepository.com/artifact/com.google.guava/guava)
- commons-cli (https://mvnrepository.com/artifact/commons-cli/commons-cli)