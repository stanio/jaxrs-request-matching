#### `net.example.jaxrs`

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

_Implementation note_

Currently `PathInfo` and the `RequestMatching` implementations maintain some
`ThreadLocal` resources.  It could be made so `RequestMatching` instances are
maintained in a `ThreadLocal`, instead.  The `RequestMatching` instance could
maintain its own (non-thread-safe) `Map` of `(PathInfo, Matcher)` pairs, so
this package-private detail gets removed from the `PathInfo` implementation.


#### `net.example.jaxrs.rest`

Sample resource classes:

-   `FooResource`
-   `BarResource` / `BarEnhanced`
    
    The latter is not compatible to use in combination with `FooResource` and
    the spec-compliant matching algorithm.
    
-   `UnmatchedResource`


#### `net.example.jaxrs.benchmark`

Sample benchmark.
