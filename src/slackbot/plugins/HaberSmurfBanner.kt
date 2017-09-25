package slackbot.plugins

import com.google.gson.Gson
import org.json.JSONObject
import slackapi.SlackConfig
import slackapi.SlackContext
import slackapi.types.User
import java.util.function.Consumer
import slackbot.plugins.SlackBotPlugin
import slackbot.plugins.types.UserEntry
import java.text.SimpleDateFormat
import java.util.*
import java.util.regex.Pattern

public class HaberSmurfBanner : SlackBotPlugin() {
    private var logger : Consumer<String>? = null
    private var slackContext : SlackContext? = null

    override fun Initialize(logger: Consumer<String>, configJson: String) {
        this.logger = logger

        val sjson = JSONObject(configJson).getJSONObject("slack")
        val scfg = SlackConfig(sjson.getString("token"), sjson.getString("username"))
        this.slackContext = SlackContext(scfg)
    }

    override val MinTickTimer: Long
        get() = 50000

    override fun ProcessMessage(msg: String, senderId: String, ts: String, channelId: String): Boolean {
        val m = Pattern.compile("!uhistory *@*(.*)").matcher(msg)
        while (m.find()) {
            val user = m.group(1).trim()

            logger!!.accept("Printing history for $user")

            val report = StringBuilder()
            var found = false
            report.append("Korisnik ").append(user).append(" je imao sljedece izmjene na profilu:\n")
            UserEntry.GetEntriesForUser(user).forEach {
                found = true

                report.append( SimpleDateFormat("dd.MM.yyyy 'u' HH:mm:ss").format(Date(it.tstamp))).append(":\n```\n")
                        .append(it.user.toString()).append("\n```\n")
            }
            if ( found ) {
                slackContext!!.ChatApi.PostMessage(senderId, report.toString(), false)
            } else {
                slackContext!!.ChatApi.PostMessage(senderId, "Korisnik nije imao nikakvih promjena!", false)
            }

            //konzumiraj poruku da se ne triggeruje nesta drugo pored ovoga
            return true
        }

        return false
    }

    override fun Tick() { }

    override fun NotifyUserJoined(usr: User) {
        UserEntry.RegisterUserEntry(usr)
    }

    override fun NotifyUserUpdate(usr: User) {
        UserEntry.RegisterUserEntry(usr)
    }

}