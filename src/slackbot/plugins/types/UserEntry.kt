package slackbot.plugins.types

import com.google.gson.Gson
import slackapi.types.User
import java.io.File
import java.util.*
import java.util.concurrent.locks.ReentrantLock
import kotlin.collections.ArrayList
import com.google.gson.reflect.TypeToken



// privremeno se cuva sve u ogromnom JSONu, nekad kasnije kada nadjem neku kvalitetniju SQLite lib za kotlin migrirati cu na to
public class UserEntry(public var user : User?, public val tstamp : Long = Date().time) {
    public constructor() : this(null) {}

    companion object {
        private val ENTRY_FILE = "user_entries.json"

        private val lock = ReentrantLock()

        fun log(s : String) {
            System.out.println(s)
        }

        private fun LoadUsers() : List<UserEntry> {
            lock.lock()
            var res = ArrayList<UserEntry>()
            val resType = object : TypeToken<ArrayList<UserEntry>>() {}.type
            try {
                val gson = Gson()
                val file = File(ENTRY_FILE)
                if ( file.exists() ) {
                    res = gson.fromJson(file.readText(), resType)
                } else {
                    file.createNewFile()
                }
            } catch ( ex : Exception ) {
                ex.printStackTrace()
            }
            lock.unlock()
            return res
        }

        private fun SaveUsers( usrs : List<UserEntry> ) {
            lock.lock()
            try {
                val gson = Gson()
                val file = File(ENTRY_FILE)

                file.writeText(gson.toJson(usrs))
            } catch ( ex : Exception ) {
                ex.printStackTrace()
            }
            lock.unlock()
        }


        fun RegisterUserEntry( usr : User ) {
            var users = LoadUsers()
            users += UserEntry(usr)
            SaveUsers(users)
        }

        fun GetEntriesForUser( username : String ): List<UserEntry> {
            return LoadUsers().filter { it.user!!.Name.equals(username, true) }
        }
    }
}