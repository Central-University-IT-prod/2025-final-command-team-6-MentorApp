package com.prodmobile.template

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import com.prodmobile.template.core.models.AccountInfo
import com.prodmobile.template.core.theme.MobileTemplateTheme
import com.prodmobile.template.data.accounts.account_info.AccountInfoDataSource
import com.prodmobile.template.feature.app.AppScreen
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.compose.KoinContext

class MainActivity : ComponentActivity() {
    private val accountInfoLocalDataSource by inject<AccountInfoDataSource>()
    private var accountInfo by mutableStateOf<AccountInfo?>(null)
    private var isLoading by mutableStateOf<Boolean?>(true)

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)

        splashScreen.setKeepOnScreenCondition { isLoading == true }

        lifecycleScope.launch {
            accountInfo = accountInfoLocalDataSource.getAccountInfo()
            delay(300)
            isLoading = false
        }

        enableEdgeToEdge()
        setContent {
            KoinContext {
                MobileTemplateTheme {
                    Surface {
                        AppScreen(accountInfo)
                    }
                }
            }
        }
    }
}
