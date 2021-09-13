import com.justai.jaicf.template.scenario.TelephonyScenario
import com.justai.jaicf.test.ScenarioTest
import org.junit.jupiter.api.Test

class MainScenarioTest : ScenarioTest(TelephonyScenario) {

    @Test
    fun `should activate by query`(){
        query("/start") goesToState "/start"
    }

    @Test
    fun `should activate by intent`(){
        intent("Bye") goesToState "/bye"
    }

    @Test
    fun `should activate by any other intent`(){
        intent("AnyOtherIntent") goesToState "/smalltalk"
    }
}