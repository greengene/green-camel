<h3>Hard-coded Camel route URIs considered harmful?</h3>

<strong>The big picture: </strong>Green Camel is an extension of the Apache Camel integration framework. It allows you to write routes as if they were methods, and it can also help you extend the native Camel Java DSL.

<em>So what is it all about?</em>

I don't know about you, but one of my major grievances with developing large-scale Camel-based applications, is the constant need to text search those URI strings that define the starting point of routes. Each time I want to look at the actual implementation of an inner route, I have to pop up the text search box and then start scrolling through the search results. It's such a distraction and a massive time vampire overall.

The more I try to make my code properly encapsulated, i.e. the more I break down big routes into smaller routes - the more I have to text search those URIs all day long, as I skip back and forth between route implementations.

So, what is wrong with hard-coded route URIs?

Well, what isn't? :)

Let's list here the major problems:

1. First, as I have already mentioned - it forces us to use text search in order to navigate between routes when we want to look at the inner implementation of a route that is invoked from within the route we are currently working on.

```java
public class MyRouteBuilder extends RouteBuilder
{
    public void configure()
    {
        from("direct:sayHi")
        .to("direct:sayBye");
        // where is the implementation of "direct:sayBye"?
        // I have no idea, so let's run a text search...
        // in the meantime, while the CPU crunches the codebase,
        // go grab a cup of coffee.
    }
}
```

2. What if we want to rename the URI of a route? We will have to look it up all over the application codebase and replace all its occurrences. It might be risky to simply run an automatic text replacement, so maybe we want to replace them one by one.
Wait, what about those <a href="http://camel.apache.org/how-to-use-a-dynamic-uri-in-to.html" target="_blank" rel="noopener">dynamic route URIs</a>? Good luck with finding all of them!
This shaky process definitely does not encourage a continuous improvement of naming. And it may come back to <a href="http://www.itiseezee.com/?p=83" target="_blank" rel="noopener">bite you later</a>.

3. How do we know that the route we just created is not a duplicate of another existing route? That is, how do we know that no two routes in our application share the same URI? Well, we don't know in compile time.... Unless we do a text search. Or until we try to deploy the application and Camel blows up.

Is there a better way?

Welcome to <strong><span style="color: #008000;"><a style="color: #008000;" href="https://github.com/greengene/green-camel" target="_blank" rel="noopener">Green Camel</a></span></strong>. Green Camel is an extension of Apache Camel that lets us create routes without hard-coding their URIs.

Let's see a simple example:

Native Camel:

```java
public class NativeGreetingRouteBuilder extends RouteBuilder
{
    public void configure()
    {
        from("direct:saySomething")
        .to("direct:sayHi_1")
        .to("direct:sayHi_");
    }
}
```

Green Camel:

```java
public class GreenGreetingRouteBuilder extends DefaultRouteBuilder
{
    @Autowired
    HiWorldRouteBuilder hiWorldRouteBuilder;

    public String saySomething()
    {
        return configureRoute(
                new Route()
                {
                    @Override
                    public void configure()
                    {
                        fromHere()
                        .to(hiWorldRouteBuilder.sayHi_1())
                        .to(hiWorldRouteBuilder.sayHi_2())
                        .returnHere()
                        ;
                    }
                }
          );
    }
}
```

Let's walk through the basic features:

<em><strong>fromHere()</strong></em>

This is the starting point of every route that wants to be managed by the Green Camel extension. <code>fromHere()</code> looks up the call stack and tries to figure out the immediate <code>RouteBuilder</code> method that defines this route. Green Camel will use this method's canonical name as (more or less) the URI of this route. Bid adieu to those dreadful route URIs!

<em><strong>returnHere()</strong></em>

This is an optional way to conclude a route workflow. It returns <code>void</code> so no additional route endpoints may be chained following it (unless it's called within an inner route). It saves the route result (whatever is found on the <code>InMessage</code> body) as an exchange property whose name is the (automatically created) route URI. <em>Why would we want to do that?</em> Well, let's think for a moment. In an ordinary Camel application it's common to see code such as:

```java
exchange.setProperty("propertyName", propertyValue);
```

Hmmm... do you feel the code smell?

What are the problems with such properties?

1. First, when we set a property like that, how do we know that it isn't already defined somewhere else within this route workflow? Well, we have no idea if it has already been defined somewhere in our codebase or dependencies, and if it is already being used somewhere within the workflow of the specific route we are currently working on (we may reuse routes that somebody else wrote, or we may not remember what we did a few weeks ago). To get ourselves better covered, we will have to text search the property name in our codebase. If we happened to find it, we would have to spend some time considering whether this property may be unintentionally affected by our route, i.e. if there may be a collision.
Even if we verified that our property name is unique within the scope of the route we are currently working on, there still might a problem. Routes are meant to be reused, but this mechanism of saving objects on the global scope of the route exchange, prevents us from truly reusing routes, since we cannot easily guarantee that they do not override each other's exchange properties.

One solution might be to enforce a naming convention that would guarantee unique property names. For example by using a standard name prefix for each package, similarly to the common workaround to C’s lack of namespaces. This may create astonishingly long property names polluting our codebase. We may mask them by using some like a global dictionary, although when we finally realize we need it, it might be too late.

Singleton mutable variables might be harmful and such problems are common when we are forced to use only one global namespace.

2. Second, like in the problem of renaming route URIs - how can we safely rename properties? Well, we will have to text search those property names, and replace them one by one, wherever is needed.

That might be time consuming and risky, especially if we have to modify code that was written by somebody else. In short - it's a recipe for never improving the naming of our properties and gradual deterioration of the code quality.

For example, let's say we work in a team that develops an application for a Honda dealership, and we have a property called "vehicle" that is used across the application (however, different developers may use it to store different semantics). A year later, the business grows and takes over the nearby Toyota dealership. Now we want to add another vehicle property to our code, but in order to prevent a collision with the "vehicle" property, we might want to replace "vehicle" (when it is used in the context of the code developed by us) with "Honda", and add a new property called "Toyota". However we realize that renaming the existing "vehicle" property might be too difficult, since our code is now mixed with other developers' code - so we simply set the new property to be "vehicle_Toyota". Not the best way to promote code consistency!

3. Third, global property names are an easy way to create spaghetti code. Even if you make sure that properties never override each other - still, when it comes to <code>exchange</code> properties, there is no hierarchy of scopes. So a property you set in some low-level corner of your workflow, may be written or read in a higher-level area of the workflow. Ever seen a spaghetti monster? With Camel you may very well be raising one in your own backyard!

So how does <code>returnHere()</code> save the day? Usually, we keep within a property the result of some calculation (or some route), so we could refer to it at some later point. If we wrap - with its own route - every calculation that needs to save its result in a property, and conclude the route with <code>returnHere()</code> - the result (i.e. the <code>InMessage</code> body) will be saved in a property whose name is the route URI. So we can later look it up by the route URI. Since route URIs are automatically synced when we rename their corresponding methods, you never need to hard-code property names.

```java
@Component
public class GreetingRouteBuilder extends DefaultRouteBuilder
{
    @Autowired
    HiWorldRouteBuilder hiWorldRouteBuilder;

    public Variable someResult = new Variable();

    public String saySomething()
    {
        return configureRoute(
                new Route()
                {
                    @Override
                    public void configure()
                    {
                        fromHere()
                        .to(hiWorldRouteBuilder.sayHi_1())
                        .to(hiWorldRouteBuilder.sayHi_2())
                        .returnHere()
                        ;
                    }
                }
          );
    }

    public String analyzeIt()
    {
        return configureRoute(
                new Route()
                {
                    Variable someResult = new Variable();

                    @Override
                    public void configure()
                    {
                        fromHere()
                        .to(saySomething()).as(someResult)
                        .to(/*some more processing that changes the body...*/)
                        .bean(SpeechAnalyzer.class, "analyze")
                        ;
                    }
                }
          );
    }
}

//now you can refer to the save point from a bean like this:

@Component
public class SpeechAnalyzer
{
    @Autowired
    GreetingRouteBuilder greetingRouteBuilder;

    public void analyze(Exchange exchange)
    {
        String saySomething = exchange.getProperty(greetingRouteBuilder.someResult, String.class);

        exchange.getOut().setBody(TextToSpeech.convert(saySomething));
    }
}
```

And there is more to it than that... Green Camel can also extend the native Camel Java DSL. Check out the <a href="https://github.com/greengene/green-camel/tree/master/dynamic.dsl.demo" target="_blank" rel="noopener">demo project</a> for some examples.