/*
 * Designed and developed by "옴마야" Team 2023.
 *
 * Licensed under the MIT.
 * Please see full license: https://github.com/mash-up-kr/WeQuiz-Android/blob/main/LICENSE
 */

package team.ommaya.wequiz.android.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import team.ommaya.wequiz.android.domain.repository.FirebaseAuthRepository
import team.ommaya.wequiz.android.repository.FirebaseAuthRepositoryImpl
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    @Singleton
    abstract fun bindFirebaseAuthRepository(
        firebaseAuthRepositoryImpl: FirebaseAuthRepositoryImpl,
    ): FirebaseAuthRepository
}
