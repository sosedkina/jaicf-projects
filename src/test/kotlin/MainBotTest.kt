import com.justai.jaicf.template.TelephonyBot
import com.justai.jaicf.test.BotTest
import org.junit.jupiter.api.Test

class MainBotTest : BotTest(TelephonyBot) {

    @Test
    fun `should activate by query`(){
        query("пока") goesToState "/bye"
    }
}