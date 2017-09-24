package slackbot.plugins

import java.util.function.Consumer
import slackbot.plugins.SlackBotPlugin

public class HaberSmurfBanner : SlackBotPlugin {
    override fun Initialize(configJson: String) {

    }

    override val MinTickTimer: Long
        get() = 5000

    override fun ProcessMessage(msg: String): Boolean {
        return false
    }

    override fun Tick(logger: Consumer<String>) {
        logger.accept("hai")
    }

}