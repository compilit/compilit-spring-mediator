# solidcoding-spring-mediator
An implementation of the Mediator/CQERS pattern using Spring.

This implementation was inspired by <a href="https://github.com/jkratz55/spring-mediatR">Spring MediatR</a> and <a href=https://github.com/jbogard/MediatR>MediatR for .NET</a>.

I do not claim do have a better implementation. Only the initial configuration is a bit easier.

# Installation
Get this dependency with the latest version.

```xml
<dependency>
    <artifactId>solidcoding-spring-mediator</artifactId>
    <groupId>org.solidcoding</groupId>
</dependency>
```

Then add an extra ComponentScan to your project by annotating any managed bean class with @ComponentScan(CQERS_MEDIATOR_PACKAGE)
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
The Mediator pattern is about making your application loosely coupled. The CQERS pattern is meant to make the application robust and predictable. Combining them is quite a popular approach.
The idea is that a Mediator is in between all requests (requests can be read or write requests), so that is no direct interaction with resources.

In the org.solidcoding.mediator.api package you'll find all interfaces which you can use to write your own Commands, Queries, Events and their respective handlers.

```java
public class TestQuery implements Query<String> {
  
  private final String someData;
  
  public TestQuery(String someData) {
    this.someData = someData;
  }
  
  public String getData() {
    return someData;
  }
}

@Component
public class TestQueryHandler implements QueryHandler<TestQuery, String> {

  @Override
  public String handle(TestQuery query) {
    return query.getData();
  }
  
}
```