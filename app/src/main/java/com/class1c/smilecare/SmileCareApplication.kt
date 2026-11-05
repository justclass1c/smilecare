package com.class1c.smilecare

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

/**
 * Hilt's composition root. The generated `Hilt_SmileCareApplication` builds the
 * singleton component that every `@Inject`/`@HiltViewModel` uses.
 * It is referenced from AndroidManifest.xml (`android:name=".SmileCareApplication"`).
 */
@HiltAndroidApp
class SmileCareApplication : Application()
