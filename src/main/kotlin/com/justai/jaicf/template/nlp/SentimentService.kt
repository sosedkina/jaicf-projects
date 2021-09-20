package com.justai.jaicf.template.nlp

import edu.stanford.nlp.ling.CoreAnnotations
import edu.stanford.nlp.pipeline.StanfordCoreNLP
import edu.stanford.nlp.sentiment.SentimentCoreAnnotations
import java.util.*

enum class Sentiment(val value: String, val intValue: Int) {
    VERY_NEGATIVE("Very negative", -2),
    NEGATIVE("Negative", -1),
    NEUTRAL("Neutral", 0),
    POSITIVE("Positive", 1),
    VERY_POSITIVE("Very positive", 2);

    companion object {
        fun valueOfString(value: String) = values().find { it.value == value }
    }
}

object SentimentAnalyzer {
    private val props = Properties().apply { setProperty("annotators", "tokenize, ssplit, parse, sentiment") }
    private val nlp = StanfordCoreNLP(props)

    fun findSentiment(request: String): Sentiment? {
        val annotation = nlp.process(request);
        for (sentence in annotation.get(CoreAnnotations.SentencesAnnotation::class.java)) {
            val sentimentType = sentence.get(SentimentCoreAnnotations.SentimentClass::class.java);
            return Sentiment.valueOfString(sentimentType)
        }
        return null
    }
}