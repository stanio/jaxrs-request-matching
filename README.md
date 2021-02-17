# JAX-RS Request Matching experiment

Related to eclipse-ee4j/jaxrs-api#904, this is a small experiment trying to compare
practical performance between spec-compliant reference implementation and a suggested
"enhanced" implementation.  See:

-   [_JAX-RS 2.1 specification – § 3.7.2 Request Matching_](https://download.oracle.com/otn-pub/jcp/jaxrs-2_1-final-eval-spec/jaxrs-2_1-final-spec.pdf)

[eclipse-ee4j/jaxrs-api#904]: https://github.com/eclipse-ee4j/jaxrs-api/issues/904 "[spec] Matching Requests to Resource Methods – Fails to match some straightforward case"


## `net.example.jaxrs`

These build a registry of available resource classes and methods:

-   `PathInfo`
    -   `ResourceInfo`
    -   `MethodInfo`
-   `Introspector`

Matching algorithm implementations:

-   `RequestMatching`
    -   `RequestMatchingSpec`
    -   `RequestMatchingEnhanced`
-   `MatchInfo` –  The result from `RequestMatching.find(path)`.

### Implementation note

Currently `PathInfo` and the `RequestMatching` implementations maintain some
`ThreadLocal` resources.  It could be made so `RequestMatching` instances are
maintained in a `ThreadLocal`, instead.  The `RequestMatching` instance could
maintain its own (non-thread-safe) `Map` of `(PathInfo, Matcher)` pairs, so
this package-private detail gets removed from the `PathInfo` implementation.


## `net.example.jaxrs.rest`

Sample resource classes:

-   `FooResource`
-   `BarResource` / `BarEnhanced`
    
    The latter is not compatible to use in combination with `FooResource` and
    the spec-compliant matching algorithm.
    
-   `UnmatchedResource`


## `net.example.jaxrs.benchmark`

Sample benchmark.
