package cz.legat.prectito.di

import android.content.Context
import androidx.room.Room
import cz.legat.prectito.persistence.AppDatabase
import cz.legat.prectito.persistence.HomeBooksDao
import cz.legat.prectito.persistence.SavedBookDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext

@Module
@InstallIn(ApplicationComponent::class)
object DatabaseModule {

    @Provides
    fun provideAppDb(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java, "prectito-db"
        ).build()
    }

    @Provides
    fun provideSavedBookDao(db: AppDatabase): SavedBookDao {
        return db.savedBookDao()
    }

    @Provides
    fun provideHomeBooksDao(db: AppDatabase): HomeBooksDao {
        return db.homeBooksDao()
    }
}