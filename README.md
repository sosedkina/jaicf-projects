# JAICP Telephony Showcase



## Hot Start

1. Put your JAICP Api token in `src/main/resources/application.yml`
2. Run `TemplateBot.kt` file

![Run Kotlin File](https://github.com/Denire/jaicf-template-for-jaicp-developers/raw/master/static/Run%20Kotlin%20File.png)

## Project Structure

```
src
├── main
│   ├── kotlin
│   │   └── com
│   │       └── justai
│   │           └── jaicf
│   │               └── template
│   │                   ├── TemplateBot.kt // File with main function to run
│   │                   ├── configuration 
│   │                   │   └── MainConfiguration.kt // yaml configuration mappings
│   │                   ├── extensions
│   │                   │   └── BotEngineRunners.kt // utilitary extensions
│   │                   ├── http
│   │                   │   └── Http.kt // preconfigured HttpClent
│   │                   ├── scenario
│   │                   │   └── MainScenario.kt // scenario example
│   │                   └── serializers
│   │                       ├── Jackson.kt // Jackson-serializer
│   │                       └── Kotlinx.kt // KotlinX-serializer
│   └── resources
│       ├── application-local.example.yml // local configuration example
│       └── application.yml // default configuration
└── test
    └── kotlin
        └── MainScenarioTest.kt // Tests example
```
