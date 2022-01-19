import com.justai.jaicf.template.playCitiesBot
import com.justai.jaicf.test.BotTest
import org.junit.jupiter.api.Test

class MainBotTest : BotTest(playCitiesBot) {

    @Test
    fun `should activate by query`(){
        query("пока") goesToState "/bye"
    }
}