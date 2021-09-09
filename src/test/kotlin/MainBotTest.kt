import com.justai.jaicf.template.templateBot
import com.justai.jaicf.test.BotTest
import org.junit.jupiter.api.Test

class MainBotTest : BotTest(templateBot) {

    @Test
    fun `should activate by query`(){
        query("пока") goesToState "/bye"
    }
}