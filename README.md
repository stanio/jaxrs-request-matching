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


#### `net.example.jaxrs.rest`

Sample resource classes:

-   `FooResource`
-   `BarResource` / `BarEnhanced`
    
    The latter is not compatible to use in combination with `FooResource` and
    the spec-compliant matching algorithm.
    
-   `UnmatchedResource`


#### `net.example.jaxrs.benchmark`

Sample benchmark.