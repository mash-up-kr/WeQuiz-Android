/*
 * Designed and developed by "옴마야" Team 2023.
 *
 * Licensed under the MIT.
 * Please see full license: https://github.com/mash-up-kr/WeQuiz-Android/blob/main/LICENSE
 */

package team.ommaya.wequiz.android.data.client

import com.fasterxml.jackson.databind.DeserializationFeature
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.engine.cio.endpoint
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.serialization.jackson.jackson
import team.ommaya.wequiz.android.data.BuildConfig
import java.util.Locale
import java.util.TimeZone

private const val MaxTimeoutMillis = 3000L
private const val MaxRetryCount = 3

const val TmpToken = "AIE-54Ve5T1TA3ahiwsjfLWjNeFHvQxd4F9E05mRqSqV5OuoZPba-BSrd74JwpBvxobRpO1pocbDLRx0K5sU0Ahb5Q_Uj_53cMNfqf0v_wS3huRVQtQCPvf6-R_vwttoU7qbtN3YOKhW8Seb73kHVThfjsQoqo1hPw"

private val KtorClient =
    HttpClient(engineFactory = CIO) {
        engine {
            endpoint {
                connectTimeout = MaxTimeoutMillis
                connectAttempts = MaxRetryCount
            }
        }
        defaultRequest {
            url("http://wequiz-server-env.eba-c2jfzm3b.eu-north-1.elasticbeanstalk.com/api/v1/")
            contentType(ContentType.Application.Json)
        }
        install(ContentNegotiation) {
            jackson {
                disable(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES)
                disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
                setTimeZone(TimeZone.getTimeZone("Asia/Seoul"))
                setLocale(Locale.KOREA)
            }
        }
        install(Logging) {
            logger = object : Logger {
                override fun log(message: String) {
                    if (BuildConfig.DEBUG) {
                        println(message)
                    }
                }
            }
            level = LogLevel.ALL
        }
    }

@Suppress("unused")
@Module
@InstallIn(SingletonComponent::class)
object HttpClientProvider {
    @Provides
    fun provideKtorClient(): HttpClient = KtorClient
}
