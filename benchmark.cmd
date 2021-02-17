@echo off
setlocal

set SAMPLE_DURATION=30
echo Sample duration: %SAMPLE_DURATION% sec

call mvn -q compile
echo .

for /l %%i in (1,1,5) do (
  call mvn -q exec:java -Dexec.mainClass=net.example.jaxrs.benchmark.BenchmarkSpec -Dexec.args=%SAMPLE_DURATION%
  call mvn -q exec:java -Dexec.mainClass=net.example.jaxrs.benchmark.BenchmarkEnahnced -Dexec.args=%SAMPLE_DURATION%
)
endlocal
