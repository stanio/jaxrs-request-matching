#!/bin/bash

SAMPLE_DURATION=30
echo "Sample duration: $SAMPLE_DURATION sec"

mvn -q compile
echo .

for i in {1..5}; do
  mvn -q exec:java -Dexec.mainClass=net.example.jaxrs.benchmark.BenchmarkSpec -Dexec.args=$SAMPLE_DURATION
  mvn -q exec:java -Dexec.mainClass=net.example.jaxrs.benchmark.BenchmarkEnahnced -Dexec.args=$SAMPLE_DURATION
done
