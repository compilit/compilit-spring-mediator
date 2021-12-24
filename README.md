# solidcoding-spring-mediator

An implementation of the Mediator/CQERS pattern using Spring.

This implementation was inspired by <a href="https://github.com/jkratz55/spring-mediatR">Spring MediatR</a>
and <a href=https://github.com/jbogard/MediatR>MediatR for .NET</a>.

I do not claim do have a better implementation. Only the initial configuration is a bit easier/less verbose.

# Installation

Get this dependency with the latest version.

```xml

<dependency>
    <artifactId>solidcoding-spring-mediator</artifactId>
    <groupId>org.solidcoding</groupId>
</dependency>
```

Then add an extra ManagedBeanScan to your project by annotating any managed bean class with @ComponentScan(
CQERS_MEDIATOR_PACKAGE)

```Java

@ComponentScan(CQERS_MEDIATOR_PACKAGE)
@SpringBootApplication
public class Launcher {

  public static void main(String[] args) {
    SpringApplication.run(Launcher.class, args);
  }

}
```

Now all you need to do is register QueryHandler, CommandHandler and/or EventHandler implementations.

# Usage

The Mediator pattern is about making your application loosely
coupled. <a href="https://www.solidcoding.org/definitions/cqers/">CQRS</a> (or in this case CQERS) is meant to make the
application robust and predictable by separating reading operations from writing operations. Combining them is quite a
popular approach. The idea is that a Mediator is in between all requests (requests can be read or write requests), so
that is no direct interaction with resources. This means that there are only 3 specific dependencies which connect your
api layer to the domain layer: the CommandDispatcher, the QueryDispatcher and the EventEmitter. Why 3 instead of just
1 'Mediator' class? Because that would introduce the 'Service Locator antipattern' and defeat the purpose of this
library. By having a separate interface for Commands, Queries and Events, CQ(E)RS is enforced.

In the org.solidcoding.mediator.api package you'll find all interfaces which you can use to write your own Commands,
Queries, Events and their respective handlers.

All components which a user of the API can to interact with:

### Command-related
- <b>Command:</b> a writing operation which is handled by a single handler. It provides a return value option to return an Id
  of a created entity for example. Or you could return a "Result". This return value should never be filled by a reading
  operation.
- <b>CommandHandler:</b> the handler for a specific Command.
- <b>CommandDispatcher:</b> the main interactor for dispatching Commands.

### Query-related
- <b>Query:</b> a reading operation which is handled by a single handler.
- <b>QueryHandler:</b> the handler for a specific Query.
- <b>QueryDispatcher:</b> the main interactor for dispatching Queries.

### Event-related
- <b>Event:</b> something that has happened which other operations can subscribe to. Can be handled by multiple EventHandlers.
- <b>EventHandler:</b> the handler for a specific Event.
- <b>EventEmitter:</b> the main interactor for emitting Events.

Here is an example:

```java
import org.solidcoding.mediator.api.QueryDispatcher;

public class TestQuery implements Query<String> {

  private final String someData;

  public TestQuery(String someData) {
    this.someData = someData;
  }

  public String getData() {
    return someData;
  }
}

@ManagedBean
public class TestQueryHandler implements QueryHandler<TestQuery, String> {

  //This class could interact with other systems/clients/hibernate etc.
  @Override
  public String handle(TestQuery query) {
    return query.getData();
  }

}

@RestController
public class ExampleController {

  private final QueryDispatcher queryDispatcher;

  public ExampleController(QueryDispatcher queryDispatcher) {
    this.queryDispatcher = queryDispatcher;
  }

  @GetMapping("/some-example")
  public ResponseEntity<String> interact() {
    return queryDispatcher.dispatch(new TestQuery());
  }

}
```