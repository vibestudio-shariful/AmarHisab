
package com.amarhisab

import android.app.Application
import com.amarhisab.data.AppDatabase

class AmarHisabApp : Application() {
    val database by lazy { AppDatabase.getDatabase(this) }
}
