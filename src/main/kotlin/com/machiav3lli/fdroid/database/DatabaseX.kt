package com.machiav3lli.fdroid.database

import android.content.Context
import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.AutoMigrationSpec
import androidx.sqlite.db.SupportSQLiteDatabase
import com.machiav3lli.fdroid.database.dao.CategoryDao
import com.machiav3lli.fdroid.database.dao.CategoryTempDao
import com.machiav3lli.fdroid.database.dao.DownloadedDao
import com.machiav3lli.fdroid.database.dao.ExodusInfoDao
import com.machiav3lli.fdroid.database.dao.ExtrasDao
import com.machiav3lli.fdroid.database.dao.InstalledDao
import com.machiav3lli.fdroid.database.dao.ProductDao
import com.machiav3lli.fdroid.database.dao.ProductTempDao
import com.machiav3lli.fdroid.database.dao.ReleaseDao
import com.machiav3lli.fdroid.database.dao.RepositoryDao
import com.machiav3lli.fdroid.database.dao.TrackerDao
import com.machiav3lli.fdroid.database.entity.Category
import com.machiav3lli.fdroid.database.entity.CategoryTemp
import com.machiav3lli.fdroid.database.entity.Downloaded
import com.machiav3lli.fdroid.database.entity.ExodusInfo
import com.machiav3lli.fdroid.database.entity.Extras
import com.machiav3lli.fdroid.database.entity.Installed
import com.machiav3lli.fdroid.database.entity.Product
import com.machiav3lli.fdroid.database.entity.ProductTemp
import com.machiav3lli.fdroid.database.entity.Release
import com.machiav3lli.fdroid.database.entity.Repository
import com.machiav3lli.fdroid.database.entity.Repository.Companion.addedReposV10
import com.machiav3lli.fdroid.database.entity.Repository.Companion.addedReposV11
import com.machiav3lli.fdroid.database.entity.Repository.Companion.addedReposV12
import com.machiav3lli.fdroid.database.entity.Repository.Companion.addedReposV14
import com.machiav3lli.fdroid.database.entity.Repository.Companion.addedReposV15
import com.machiav3lli.fdroid.database.entity.Repository.Companion.addedReposV17
import com.machiav3lli.fdroid.database.entity.Repository.Companion.addedReposV18
import com.machiav3lli.fdroid.database.entity.Repository.Companion.addedReposV19
import com.machiav3lli.fdroid.database.entity.Repository.Companion.addedReposV9
import com.machiav3lli.fdroid.database.entity.Repository.Companion.defaultRepositories
import com.machiav3lli.fdroid.database.entity.Tracker
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

@Database(
    entities = [
        Repository::class,
        Product::class,
        Release::class,
        ProductTemp::class,
        Category::class,
        CategoryTemp::class,
        Installed::class,
        Extras::class,
        ExodusInfo::class,
        Tracker::class,
        Downloaded::class,
    ],
    version = 19,
    exportSchema = true,
    autoMigrations = [
        AutoMigration(
            from = 8,
            to = 9,
            spec = DatabaseX.Companion.MigrationSpec8to9::class
        ),
        AutoMigration(
            from = 9,
            to = 10,
            spec = DatabaseX.Companion.MigrationSpec9to10::class
        ),
        AutoMigration(
            from = 10,
            to = 11,
            spec = DatabaseX.Companion.MigrationSpec10to11::class
        ),
        AutoMigration(
            from = 11,
            to = 12,
            spec = DatabaseX.Companion.MigrationSpec11to12::class
        ),
        AutoMigration(
            from = 12,
            to = 13,
        ),
        AutoMigration(
            from = 13,
            to = 14,
            spec = DatabaseX.Companion.MigrationSpec13to14::class
        ),
        AutoMigration(
            from = 14,
            to = 15,
            spec = DatabaseX.Companion.MigrationSpec14to15::class
        ),
        AutoMigration(
            from = 15,
            to = 16,
        ),
        AutoMigration(
            from = 16,
            to = 17,
            spec = DatabaseX.Companion.MigrationSpec16to17::class
        ),
        AutoMigration(
            from = 17,
            to = 18,
            spec = DatabaseX.Companion.MigrationSpec17to18::class
        ),
        AutoMigration(
            from = 18,
            to = 19,
            spec = DatabaseX.Companion.MigrationSpec18to19::class
        ),
    ]
)
@TypeConverters(Converters::class)
abstract class DatabaseX : RoomDatabase() {
    abstract fun getRepositoryDao(): RepositoryDao
    abstract fun getProductDao(): ProductDao
    abstract fun getReleaseDao(): ReleaseDao
    abstract fun getProductTempDao(): ProductTempDao
    abstract fun getCategoryDao(): CategoryDao
    abstract fun getCategoryTempDao(): CategoryTempDao
    abstract fun getInstalledDao(): InstalledDao
    abstract fun getExtrasDao(): ExtrasDao
    abstract fun getExodusInfoDao(): ExodusInfoDao
    abstract fun getTrackerDao(): TrackerDao
    abstract fun getDownloadedDao(): DownloadedDao

    companion object {
        @Volatile
        private var INSTANCE: DatabaseX? = null

        fun getInstance(context: Context): DatabaseX {
            synchronized(this) {
                if (INSTANCE == null) {
                    INSTANCE = Room
                        .databaseBuilder(
                            context.applicationContext,
                            DatabaseX::class.java,
                            "main_database.db"
                        )
                        .fallbackToDestructiveMigration()
                        .build()
                    INSTANCE?.let { instance ->
                        GlobalScope.launch(Dispatchers.IO) {
                            if (instance.getRepositoryDao()
                                    .getCount() == 0
                            ) defaultRepositories.forEach {
                                instance.getRepositoryDao().put(it)
                            }
                        }
                    }
                }
                return INSTANCE!!
            }
        }

        class MigrationSpec8to9 : AutoMigrationSpec {
            override fun onPostMigrate(db: SupportSQLiteDatabase) {
                super.onPostMigrate(db)
                onPostMigrate(8)
            }
        }

        class MigrationSpec9to10 : AutoMigrationSpec {
            override fun onPostMigrate(db: SupportSQLiteDatabase) {
                super.onPostMigrate(db)
                onPostMigrate(9)
            }
        }

        class MigrationSpec10to11 : AutoMigrationSpec {
            override fun onPostMigrate(db: SupportSQLiteDatabase) {
                super.onPostMigrate(db)
                onPostMigrate(10)
            }
        }

        class MigrationSpec11to12 : AutoMigrationSpec {
            override fun onPostMigrate(db: SupportSQLiteDatabase) {
                super.onPostMigrate(db)
                onPostMigrate(11)
            }
        }

        class MigrationSpec13to14 : AutoMigrationSpec {
            override fun onPostMigrate(db: SupportSQLiteDatabase) {
                super.onPostMigrate(db)
                onPostMigrate(13)
            }
        }

        class MigrationSpec14to15 : AutoMigrationSpec {
            override fun onPostMigrate(db: SupportSQLiteDatabase) {
                super.onPostMigrate(db)
                onPostMigrate(14)
            }
        }

        class MigrationSpec16to17 : AutoMigrationSpec {
            override fun onPostMigrate(db: SupportSQLiteDatabase) {
                super.onPostMigrate(db)
                onPostMigrate(16)
            }
        }

        class MigrationSpec17to18 : AutoMigrationSpec {
            override fun onPostMigrate(db: SupportSQLiteDatabase) {
                super.onPostMigrate(db)
                onPostMigrate(17)
            }
        }

        class MigrationSpec18to19 : AutoMigrationSpec {
            override fun onPostMigrate(db: SupportSQLiteDatabase) {
                super.onPostMigrate(db)
                onPostMigrate(18)
            }
        }

        fun onPostMigrate(from: Int) {
            val preRepos = mutableListOf<Repository>()
            if (from == 8) preRepos.addAll(addedReposV9)
            if (from == 9) preRepos.addAll(addedReposV10)
            if (from == 10) preRepos.addAll(addedReposV11)
            if (from == 11) preRepos.addAll(addedReposV12)
            if (from == 13) preRepos.addAll(addedReposV14)
            if (from == 14) preRepos.addAll(addedReposV15)
            if (from == 16) preRepos.addAll(addedReposV17)
            if (from == 17) preRepos.addAll(addedReposV18)
            if (from == 18) preRepos.addAll(addedReposV19)
            GlobalScope.launch(Dispatchers.IO) {
                preRepos.forEach {
                    INSTANCE?.getRepositoryDao()?.put(it)
                    if (from == 18) INSTANCE?.getDownloadedDao()?.emptyTable()
                }
            }
        }
    }

    fun cleanUp(vararg pairs: Pair<Long, Boolean>) {
        runInTransaction {
            pairs.forEach { pair ->
                val id = pair.first
                getProductDao().deleteById(id)
                getCategoryDao().deleteById(id)
                if (pair.second) getRepositoryDao().deleteById(id)
            }
        }
    }

    fun cleanUp(pairs: Set<Pair<Long, Boolean>>) = cleanUp(*pairs.toTypedArray())

    fun finishTemporary(repository: Repository, success: Boolean) {
        runInTransaction {
            if (success) {
                getProductDao().deleteById(repository.id)
                getCategoryDao().deleteById(repository.id)
                getProductDao().insert(*(getProductTempDao().getAll()))
                getCategoryDao().insert(*(getCategoryTempDao().getAll()))
                getRepositoryDao().put(repository)
            }
            getProductTempDao().emptyTable()
            getCategoryTempDao().emptyTable()
        }
    }
}
