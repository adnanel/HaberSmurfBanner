import org.json.JSONObject
import slackapi.SlackContext
import utility.log
import zamgerapi.ZamgerContext

public class HaberSmurfBanner(jsonConfig : String) : HaberPlugin(jsonConfig) {
    override fun ProcessMessage(msg: String): Boolean {
        return false
    }

    override val MinTickTimer: Long = 5000


    override fun Tick() {

        log("tick!")
    }
}