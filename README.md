# ♕ BYU CS 240 Chess

This project demonstrates mastery of proper software design, client/server architecture, networking using HTTP and WebSocket, database persistence, unit testing, serialization, and security.

## 10k Architecture Overview

The application implements a multiplayer chess server and a command line chess client.

[![Sequence Diagram](10k-architecture.png)](https://sequencediagram.org/index.html#initialData=C4S2BsFMAIGEAtIGckCh0AcCGAnUBjEbAO2DnBElIEZVs8RCSzYKrgAmO3AorU6AGVIOAG4jUAEyzAsAIyxIYAERnzFkdKgrFIuaKlaUa0ALQA+ISPE4AXNABWAexDFoAcywBbTcLEizS1VZBSVbbVc9HGgnADNYiN19QzZSDkCrfztHFzdPH1Q-Gwzg9TDEqJj4iuSjdmoMopF7LywAaxgvJ3FC6wCLaFLQyHCdSriEseSm6NMBurT7AFcMaWAYOSdcSRTjTka+7NaO6C6emZK1YdHI-Qma6N6ss3nU4Gpl1ZkNrZwdhfeByy9hwyBA7mIT2KAyGGhuSWi9wuc0sAI49nyMG6ElQQA)

## IntelliJ Support

Open the project directory in IntelliJ in order to develop, run, and debug your code using an IDE.

## Maven Support

You can use the following commands to build, test, package, and run your code.

| Command                    | Description                                     |
| -------------------------- | ----------------------------------------------- |
| `mvn compile`              | Builds the code                                 |
| `mvn package`              | Run the tests and build an Uber jar file        |
| `mvn package -DskipTests`  | Build an Uber jar file                          |
| `mvn install`              | Installs the packages into the local repository |
| `mvn test`                 | Run all the tests                               |
| `mvn -pl shared tests`     | Run all the shared tests                        |
| `mvn -pl client exec:java` | Build and run the client `Main`                 |
| `mvn -pl server exec:java` | Build and run the server `Main`                 |

These commands are configured by the `pom.xml` (Project Object Model) files. There is a POM file in the root of the project, and one in each of the modules. The root POM defines any global dependencies and references the module POM files.

### Running the program using Java

Once you have compiled your project into an uber jar, you can execute it with the following command.

```sh
java -jar client/target/client-jar-with-dependencies.jar

♕ 240 Chess Client: chess.ChessPiece@7852e922
```
### Link to Sequence Diagram from Web API
[squence diagram](https://sequencediagram.org/index.html?presentationMode=readOnly#initialData=IYYwLg9gTgBAwgGwJYFMB2YBQAHYUxIhK4YwDKKUAbpTngUSWOZVYSnfoccKQCLAwwAIIgQKAM4TMAE0HAARsAkoYMhZkwBzKBACu2GAGI0wKgE8YAJRRakEsFEFIIaYwHcAFkjAdEqUgBaAD4WakoALhgAbQAFAHkyABUAXRgAej0VKAAdNABvLMpTAFsUABoYXCl3aBlKlBLgJAQAX0wKcNgQsLZxKKhbe18oAAoiqFKKquUJWqh6mEbmhABKDtZ2GB6BIVFxKSitFDAAVWzx7Kn13ZExSQlt0PUosgBRABk3uCSYACoYAAzXQlGATGBeSiqCZTTDABDMExmSxGLw+aFXYBlGDwwbAGSWFAAD2G0nU2xCt32DwGEHcmCp9ykFOCnT6KCi4IgCgAVihwBtqFtAiE2ZEYGBPLp3EsoLpYDCsapiaTBTRuiF-OgwFEACwABgAzDB8jAAERlKTAY5mqJmt5y6BRJQyGCDACOekkYDNMHamBQCBUYOyMDQEGYgP0aBkDPk1OZItCbPYUTQegQCE0KfETxgjIOEiiIDxvnOlEuxSVlWqczqDSaLRu8aZjx6LxgAEkAHLvKy-RVlGuzeaLZYtLvdpLxEO0HOqHYtwvF0soYR6SWVyZK5t7Vt5js9vsDzFDnEbzxJCAAa3Qk+n58lcb3hZZvVTj8vN-QauFPTFUBRMAF5XreaBqpQb5ahgUQAEz6vqeT5MBkqgeg7ToLGmGaDo+iGEY0C8McMAfBAdhuKi3i+Jg0HMP+rDinEiSpBkKhSC4aBIYO0y1qO7QAXm85RAgZFIGgW5TOUvF1Os855gWNIwMcZwXNxu53K+7YKBEFAIPyzAAsCECguCkKDLO25lHCCLGIRaDEZR6IWWGEZAtGsbkkmCmHGGGZZt5baips-S+Zmv65kmAFRJK0qyvKFmwgJSa0VE0QGgAjGkpoWg81ooLa5oOvKnKmBe0BIAAXigMi+v6gbBuCUZ6DGsgKCyAW0vSAUskJzncnyApyUFQohSAxm4IMozSQsw41HUAASyieOs8KInZDlor4MxzQsMAlPYTRgCAnjhQuw3qtFUp0nF0B7QdgjHdV211gs0hJcEKUxBlWXmpaEh5QV9qOoBYKlZK5VVTVfoBkGqhUPCSCutNsZDcEHUwCWKCCGuF4STuz4aQ8B7aZOx4JdWn5oW4PYPihJ3dZFwUcpT37gUN53inTVMQRqH3INqcEIUhXOs3VLUBi12HaLoBjGDoKB3qRWj6Iim1+PzQTJgxIPRHwnxvEkbxpOkbESBxeQi2BPNPL1InKxuoyW+gslM086PKeum5O2g6kJm2wQdu8Xw-P8QIgp+EKeFCLNW6tcuDHejlbZ7X5gS5kbua17VLop6ZhQz50fnnWbs6y2uXbFlDxc1dMQ9V1vJRrOpffqmUmr9uU2naRVOqDtdQJV1W1TDwYp5Aafw8gHltV5Oc+dKBN+z1TNRCnVMwP1+mnW7c9FmogYnDjXsgazvv7iEHZ8AfW1GaCdOR9H3vb4zXTCWRKswBIeitoCGYN5qTdUrwX1CkTQ2Epa4VlkYQYroPjDBgAAcSVI8JO6sAh0S1q-GICCDbGy0EqC2J8rbvV6spJBlpHZEOds-NGu8jgnBTpQ1Cp9F7nwDiTIO3xfih1vhHMyqgn5x2gU9VBMAxKT0RjHH8nlKR0NCv5Xey8RrM2Ls-DmgEYrXSrrdGuZUB6Q3-nzdBqVvrtxylaLuhVgYlX7oPKGGFYYwAka6J+MjaEvkUgvAuZdlERG9hvXkW9Ubo2QA4chkhRhn00uw14+sQ6GXDvgqybj0bSlGBIKJRMX7siOEqAJA0wDpJdsopRF0lJ5M3uAIphjPrRGAT9M0STJAFWiPkRpSpOx8AKulWChpdSVDNGrcsll8oRDNGaAZCgECgGvMMqYtpxkDKad2JUYyzStBgCkMWWFxaQPwtgPQUBsB6XgKuRBeTUE0SboJcuMQEjJDwQQsGqd0BIWWUqfi2sbYrwxqucJTCXloHKO8soxT2Q7w8T5D2uNvaZMTDE8gcTfgJOMnwqO5lBE2SMAco5JzRHiIRi4qh4F1Cz0hXvVR3jeqqNLlFTRMptEKmeXXFGXzG7GJbm3bKf0Abd2sX3PRdjh71ThoSqRJKZ6yPJZ1VhmlC4hX8ZUrA84pWEx8pjbG-yQUoDhRIC+JMjxvH7OUsoKzsQ0xnE02VWT5XMyaZ0tRPiyn2r4DUwBdSEINJdd03pupobgN2TLfC5hAwiRlAAKQgGJc52IjBTJmVc9BNysFxFOCxdITTCHMLAkhEATc4AQBElASoLrPldG+b4mAPIo1oH+d7coeb0EFqLcCjpfAwXCncWqve0Lj7ZuoQzBFnCQ48PDvffh4rrKIhDZma6BKp6TpSXIylijslFz8o6ulV0GXAwFeDfR9ckoAI5WlVuDSeWWKBsVPdngWXCscc4xdkqu1+xlVSleirAmDVdqq19SkTharbbqp4gckWjtRU0h+5kXWtTJd2t9q7bW5OxEq06SGJRYxKEkYAYFRiNu1M26ArbQVTuMDOsNGGsQ4gQHiAkEocM-lpbc+lN1YC4ixnRoQxC2XHoFi3Q057O6jKsde9j+JLBcfQPe4MND0YGDkL4f53FiMoE6Q2-NhboDAf1VEU4sQ+DCENia1Q7xfj4YwIR2AABecm2IJ0wffZWr+rZN23Oc4WN1J76kSx2b5nCQb47AEsIgLGbHsDHMIM4VwHgqJoO1Mmsput9aG2NuoBuTqPwyGvigKwdIJCRNk3IrLelFPIIK4Oy+yW3hhwg0qa18LKtfCM7wp+g61Akz1k16rvCJj1cChlkK7mHiudfkNqQnm+MepAT5zAQA)