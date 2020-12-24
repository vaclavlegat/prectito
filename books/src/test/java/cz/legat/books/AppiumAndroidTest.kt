package cz.legat.books

import io.appium.java_client.MobileBy
import io.appium.java_client.MobileElement
import io.appium.java_client.android.AndroidDriver
import io.appium.java_client.android.connection.ConnectionState
import io.appium.java_client.pagefactory.AndroidFindBy
import io.appium.java_client.pagefactory.AppiumFieldDecorator
import io.appium.java_client.remote.MobileCapabilityType
import org.junit.Assert
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.openqa.selenium.By
import org.openqa.selenium.remote.DesiredCapabilities
import org.openqa.selenium.support.PageFactory
import org.openqa.selenium.support.ui.WebDriverWait
import java.net.URL
import java.util.concurrent.TimeUnit


class AppiumAndroidTest {
    private var driver: AndroidDriver<MobileElement>? = null
    private var wait: WebDriverWait? = null

    @Before
    fun setUp() {
        val capabilities = DesiredCapabilities()
        capabilities.setCapability("platformName", "Android")
        capabilities.setCapability("deviceName", "Android Device")
        capabilities.setCapability(MobileCapabilityType.PLATFORM_VERSION, "11.0")
        capabilities.setCapability("appPackage", "cz.legat.prectito")
        capabilities.setCapability(
            "appActivity",
            "cz.legat.prectito.ui.SplashActivity"
        )
        capabilities.setCapability("autoGrantPermissions", "true")
        capabilities.setCapability("noReset", "false")
        driver = AndroidDriver<MobileElement>(URL("http:/0.0.0.0:4723/wd/hub"), capabilities)

        driver!!.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS)
        wait = WebDriverWait(driver, 5)

    }

    @Test
    fun test() {
        // Appium provide these masks for network connection:
        // AIRPLANE_MODE(1), ALL_NETWORK_ON(6), DATA_ONLY(4), WIFI_ONLY(2), NONE(0);
        val connectionState = ConnectionState(2)
        driver!!.connection = connectionState
        assertTrue(driver!!.connection.isWiFiEnabled)
        Assert.assertEquals(connectionState.bitMask, driver!!.connection.bitMask)


        val btns = driver!!.findElementsById("pt_more_btn")
        println(btns.size)
        btns[0]?.click()

    }
}