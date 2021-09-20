# JAICF Telephony Showcase

This bot demonstrates using JAICF-Kotlin for voice communications via JAICP with inbound and outbound telephony
campaigns.

### Telephony analytics via JAICP

JAICP provides analytics API used in this scenario. The state below demonstrates usage of analytics. Whenever client is
transferred to operator and call is finished, we set call result (or this dialog session result) to "Operator helped
with result".

```kotlin
state("transfer") {
    activators {
        event("transfer")
    }
    action {
        jaicpAnalytics.setSessionResult("Operator helped with result")
        reactions.go("/finish")
    }
}
```

### Barge-In

**Barge-In** is speech synthesis interruption. Interruption can be enabled for any answer in scenario.

```kotlin
state("start") {
    activators {
        regex("/start")
    }
    action {
        reactions.say(
            text = "Hello! My name is Jessy and i'm a voice assistant for your preferred bank. How can I help you?",
            bargeIn = true
        )
        reactions.startNewSession()
    }
}
```

The example above allows interruption for welcome phrase. Interruption will be successful only if bot know what to
answer for client message. Otherwise, speech synthesis will continue. This behaviour can be altered
with `reactions.bargeIn` properties.

### Sentiment Analysis

Sentiment Analysis is a common part of call campaigns to provide a better conversational experience. Chat-bot calls can
be annoying to some part of people, so developers can rely on sentiment analysis to end calls or transfer calls to
operators whenever client is angry.

The example below demonstrates usage of `SentimentAnalyzer` based on `Stanford CoreNLP` library. Whenever bot cannot
understand the message and sentiment is below 0 (this means client is angry or very angry), bot will transfer call to
operator.

```kotlin
fallback {
    jaicpAnalytics.setMessageLabel("fallback", "Unrecognized")
    val sentiment = SentimentAnalyzer.findSentiment(request.input)
    if (sentiment != null && sentiment.intValue < 0) {
        reactions.say("I'll redirect your call to our operator.")
        reactions.transferCall(TelephonySwitchReply("+79818305614", continueCall = true, continueRecording = true))
    } else {
        reactions.sayRandom(
            "Sorry, I didn't get that...",
            "Sorry, could you repeat please?"
        )
    }
}
```

## Evaluate showcase scenario locally

1. Create JAICF project in JAICP Application Console
2. Put your JAICP Access Token in `src/main/resources/application.yml`
3. Import intents and entities from `caila` directory
4. Create SIP Connection and Telephony Channel
5. Run `Bot.kt` file