/*
 * Designed and developed by "옴마야" Team 2023.
 *
 * Licensed under the MIT.
 * Please see full license: https://github.com/mash-up-kr/WeQuiz-Android/blob/main/LICENSE
 */

package team.ommaya.wequiz.android.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.DataStoreFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import team.ommaya.wequiz.android.preference.UserPreference
import team.ommaya.wequiz.android.preference.UserPreferenceSerializer
import java.io.File
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataStoreModule {

    @Provides
    @Singleton
    fun provideUserPreferencesDataStore(
        @ApplicationContext context: Context,
    ): DataStore<UserPreference> {
        return DataStoreFactory.create(
            serializer = UserPreferenceSerializer(),
        ) {
            File("${context.cacheDir.path}/${UserPreference.localPath}")
        }
    }
}
