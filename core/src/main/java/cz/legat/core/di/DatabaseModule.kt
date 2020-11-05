package cz.legat.core.di

import android.content.Context
import androidx.room.Room
import cz.legat.core.persistence.AppDatabase
import cz.legat.core.persistence.OverviewDao
import cz.legat.core.persistence.SavedBookDao
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
    fun provideOverviewBooksDao(db: AppDatabase): OverviewDao {
        return db.overviewBooksDao()
    }
}