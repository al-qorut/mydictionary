package smk.adzikro.mydictionary.di

import android.app.Application
import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import smk.adzikro.mydictionary.data.local.KamusData
import smk.adzikro.mydictionary.data.models.KamusDao
import smk.adzikro.mydictionary.data.repositories.Repo
import smk.adzikro.mydictionary.data.repositories.RepoImpl
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideDatabase(app: Application): KamusData = KamusData.getKamusData(app)!!

    @Provides
    @Singleton
    fun provideDao(db: KamusData) = db.kamusDao()

    @Provides
    @Singleton
    fun provideRepo(dao: KamusDao,
                    app: Application): Repo = RepoImpl(dao, context = app)

}