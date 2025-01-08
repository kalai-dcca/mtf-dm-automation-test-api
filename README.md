# Set up

## Installation

Please install the project dependencies and required JDK 21

## Executing tests

Make sure to provide the values for the following environment variables

- PenvName: we have added multiple environments in maven profile, so for any env execution use -Pdev

If you have to provide all three env values:

```shell
mvn clean test -Pdev
```

more configs we can do with it:

```shell
mvn test -Dsurefire.includeJUnit5Engines=cucumber -Dcucumber.plugin=pretty -Dcucumber.features=path/to/example.feature:10 
```


## API Steps

follow the example 'src/test.demoApi/resources/feature/demoApi.feature' to write scenarios. 


more steps: 