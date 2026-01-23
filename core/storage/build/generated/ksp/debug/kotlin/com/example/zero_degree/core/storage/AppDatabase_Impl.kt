package com.example.zero_degree.core.storage

import androidx.room.InvalidationTracker
import androidx.room.RoomOpenDelegate
import androidx.room.migration.AutoMigrationSpec
import androidx.room.migration.Migration
import androidx.room.util.TableInfo
import androidx.room.util.TableInfo.Companion.read
import androidx.room.util.dropFtsSyncTriggers
import androidx.sqlite.SQLiteConnection
import androidx.sqlite.execSQL
import com.example.zero_degree.core.storage.dao.BarDao
import com.example.zero_degree.core.storage.dao.BarDao_Impl
import com.example.zero_degree.core.storage.dao.BookingDao
import com.example.zero_degree.core.storage.dao.BookingDao_Impl
import com.example.zero_degree.core.storage.dao.DrinkDao
import com.example.zero_degree.core.storage.dao.DrinkDao_Impl
import com.example.zero_degree.core.storage.dao.EventDao
import com.example.zero_degree.core.storage.dao.EventDao_Impl
import com.example.zero_degree.core.storage.dao.ReviewDao
import com.example.zero_degree.core.storage.dao.ReviewDao_Impl
import kotlin.Lazy
import kotlin.String
import kotlin.Suppress
import kotlin.collections.List
import kotlin.collections.Map
import kotlin.collections.MutableList
import kotlin.collections.MutableMap
import kotlin.collections.MutableSet
import kotlin.collections.Set
import kotlin.collections.mutableListOf
import kotlin.collections.mutableMapOf
import kotlin.collections.mutableSetOf
import kotlin.reflect.KClass

@Suppress(names = ["UNCHECKED_CAST", "DEPRECATION", "REDUNDANT_PROJECTION", "REMOVAL"])
public class AppDatabase_Impl : AppDatabase() {
  private val _barDao: Lazy<BarDao> = lazy {
    BarDao_Impl(this)
  }

  private val _drinkDao: Lazy<DrinkDao> = lazy {
    DrinkDao_Impl(this)
  }

  private val _eventDao: Lazy<EventDao> = lazy {
    EventDao_Impl(this)
  }

  private val _bookingDao: Lazy<BookingDao> = lazy {
    BookingDao_Impl(this)
  }

  private val _reviewDao: Lazy<ReviewDao> = lazy {
    ReviewDao_Impl(this)
  }

  protected override fun createOpenDelegate(): RoomOpenDelegate {
    val _openDelegate: RoomOpenDelegate = object : RoomOpenDelegate(2, "a210b3aa94f54d493f6e04acd4ed1744", "64238ae21c9998479445ca06c9208ebc") {
      public override fun createAllTables(connection: SQLiteConnection) {
        connection.execSQL("CREATE TABLE IF NOT EXISTS `bars` (`id` TEXT NOT NULL, `name` TEXT NOT NULL, `address` TEXT NOT NULL, `latitude` REAL NOT NULL, `longitude` REAL NOT NULL, `capacity` INTEGER NOT NULL, `imageUrl` TEXT, `description` TEXT, `phone` TEXT, PRIMARY KEY(`id`))")
        connection.execSQL("CREATE TABLE IF NOT EXISTS `drinks` (`id` TEXT NOT NULL, `name` TEXT NOT NULL, `description` TEXT, `imageUrl` TEXT, `type` TEXT, `taste` TEXT, `price` REAL NOT NULL, `available` INTEGER NOT NULL, `alcoholContent` REAL NOT NULL, PRIMARY KEY(`id`))")
        connection.execSQL("CREATE TABLE IF NOT EXISTS `events` (`id` TEXT NOT NULL, `name` TEXT NOT NULL, `description` TEXT, `date` TEXT NOT NULL, `barId` TEXT NOT NULL, `imageUrl` TEXT, PRIMARY KEY(`id`))")
        connection.execSQL("CREATE TABLE IF NOT EXISTS `bookings` (`id` TEXT NOT NULL, `barId` TEXT NOT NULL, `userId` TEXT NOT NULL, `date` TEXT NOT NULL, `time` TEXT NOT NULL, `guestsCount` INTEGER NOT NULL, `status` TEXT NOT NULL, PRIMARY KEY(`id`))")
        connection.execSQL("CREATE TABLE IF NOT EXISTS `reviews` (`id` TEXT NOT NULL, `targetId` TEXT NOT NULL, `targetType` TEXT NOT NULL, `userId` TEXT NOT NULL, `userName` TEXT, `rating` INTEGER NOT NULL, `comment` TEXT, `date` TEXT, PRIMARY KEY(`id`))")
        connection.execSQL("CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT)")
        connection.execSQL("INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, 'a210b3aa94f54d493f6e04acd4ed1744')")
      }

      public override fun dropAllTables(connection: SQLiteConnection) {
        connection.execSQL("DROP TABLE IF EXISTS `bars`")
        connection.execSQL("DROP TABLE IF EXISTS `drinks`")
        connection.execSQL("DROP TABLE IF EXISTS `events`")
        connection.execSQL("DROP TABLE IF EXISTS `bookings`")
        connection.execSQL("DROP TABLE IF EXISTS `reviews`")
      }

      public override fun onCreate(connection: SQLiteConnection) {
      }

      public override fun onOpen(connection: SQLiteConnection) {
        internalInitInvalidationTracker(connection)
      }

      public override fun onPreMigrate(connection: SQLiteConnection) {
        dropFtsSyncTriggers(connection)
      }

      public override fun onPostMigrate(connection: SQLiteConnection) {
      }

      public override fun onValidateSchema(connection: SQLiteConnection): RoomOpenDelegate.ValidationResult {
        val _columnsBars: MutableMap<String, TableInfo.Column> = mutableMapOf()
        _columnsBars.put("id", TableInfo.Column("id", "TEXT", true, 1, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsBars.put("name", TableInfo.Column("name", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsBars.put("address", TableInfo.Column("address", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsBars.put("latitude", TableInfo.Column("latitude", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsBars.put("longitude", TableInfo.Column("longitude", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsBars.put("capacity", TableInfo.Column("capacity", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsBars.put("imageUrl", TableInfo.Column("imageUrl", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsBars.put("description", TableInfo.Column("description", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsBars.put("phone", TableInfo.Column("phone", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY))
        val _foreignKeysBars: MutableSet<TableInfo.ForeignKey> = mutableSetOf()
        val _indicesBars: MutableSet<TableInfo.Index> = mutableSetOf()
        val _infoBars: TableInfo = TableInfo("bars", _columnsBars, _foreignKeysBars, _indicesBars)
        val _existingBars: TableInfo = read(connection, "bars")
        if (!_infoBars.equals(_existingBars)) {
          return RoomOpenDelegate.ValidationResult(false, """
              |bars(com.example.zero_degree.core.storage.entity.BarEntity).
              | Expected:
              |""".trimMargin() + _infoBars + """
              |
              | Found:
              |""".trimMargin() + _existingBars)
        }
        val _columnsDrinks: MutableMap<String, TableInfo.Column> = mutableMapOf()
        _columnsDrinks.put("id", TableInfo.Column("id", "TEXT", true, 1, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsDrinks.put("name", TableInfo.Column("name", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsDrinks.put("description", TableInfo.Column("description", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsDrinks.put("imageUrl", TableInfo.Column("imageUrl", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsDrinks.put("type", TableInfo.Column("type", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsDrinks.put("taste", TableInfo.Column("taste", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsDrinks.put("price", TableInfo.Column("price", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsDrinks.put("available", TableInfo.Column("available", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsDrinks.put("alcoholContent", TableInfo.Column("alcoholContent", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        val _foreignKeysDrinks: MutableSet<TableInfo.ForeignKey> = mutableSetOf()
        val _indicesDrinks: MutableSet<TableInfo.Index> = mutableSetOf()
        val _infoDrinks: TableInfo = TableInfo("drinks", _columnsDrinks, _foreignKeysDrinks, _indicesDrinks)
        val _existingDrinks: TableInfo = read(connection, "drinks")
        if (!_infoDrinks.equals(_existingDrinks)) {
          return RoomOpenDelegate.ValidationResult(false, """
              |drinks(com.example.zero_degree.core.storage.entity.DrinkEntity).
              | Expected:
              |""".trimMargin() + _infoDrinks + """
              |
              | Found:
              |""".trimMargin() + _existingDrinks)
        }
        val _columnsEvents: MutableMap<String, TableInfo.Column> = mutableMapOf()
        _columnsEvents.put("id", TableInfo.Column("id", "TEXT", true, 1, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsEvents.put("name", TableInfo.Column("name", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsEvents.put("description", TableInfo.Column("description", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsEvents.put("date", TableInfo.Column("date", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsEvents.put("barId", TableInfo.Column("barId", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsEvents.put("imageUrl", TableInfo.Column("imageUrl", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY))
        val _foreignKeysEvents: MutableSet<TableInfo.ForeignKey> = mutableSetOf()
        val _indicesEvents: MutableSet<TableInfo.Index> = mutableSetOf()
        val _infoEvents: TableInfo = TableInfo("events", _columnsEvents, _foreignKeysEvents, _indicesEvents)
        val _existingEvents: TableInfo = read(connection, "events")
        if (!_infoEvents.equals(_existingEvents)) {
          return RoomOpenDelegate.ValidationResult(false, """
              |events(com.example.zero_degree.core.storage.entity.EventEntity).
              | Expected:
              |""".trimMargin() + _infoEvents + """
              |
              | Found:
              |""".trimMargin() + _existingEvents)
        }
        val _columnsBookings: MutableMap<String, TableInfo.Column> = mutableMapOf()
        _columnsBookings.put("id", TableInfo.Column("id", "TEXT", true, 1, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsBookings.put("barId", TableInfo.Column("barId", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsBookings.put("userId", TableInfo.Column("userId", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsBookings.put("date", TableInfo.Column("date", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsBookings.put("time", TableInfo.Column("time", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsBookings.put("guestsCount", TableInfo.Column("guestsCount", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsBookings.put("status", TableInfo.Column("status", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        val _foreignKeysBookings: MutableSet<TableInfo.ForeignKey> = mutableSetOf()
        val _indicesBookings: MutableSet<TableInfo.Index> = mutableSetOf()
        val _infoBookings: TableInfo = TableInfo("bookings", _columnsBookings, _foreignKeysBookings, _indicesBookings)
        val _existingBookings: TableInfo = read(connection, "bookings")
        if (!_infoBookings.equals(_existingBookings)) {
          return RoomOpenDelegate.ValidationResult(false, """
              |bookings(com.example.zero_degree.core.storage.entity.BookingEntity).
              | Expected:
              |""".trimMargin() + _infoBookings + """
              |
              | Found:
              |""".trimMargin() + _existingBookings)
        }
        val _columnsReviews: MutableMap<String, TableInfo.Column> = mutableMapOf()
        _columnsReviews.put("id", TableInfo.Column("id", "TEXT", true, 1, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsReviews.put("targetId", TableInfo.Column("targetId", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsReviews.put("targetType", TableInfo.Column("targetType", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsReviews.put("userId", TableInfo.Column("userId", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsReviews.put("userName", TableInfo.Column("userName", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsReviews.put("rating", TableInfo.Column("rating", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsReviews.put("comment", TableInfo.Column("comment", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsReviews.put("date", TableInfo.Column("date", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY))
        val _foreignKeysReviews: MutableSet<TableInfo.ForeignKey> = mutableSetOf()
        val _indicesReviews: MutableSet<TableInfo.Index> = mutableSetOf()
        val _infoReviews: TableInfo = TableInfo("reviews", _columnsReviews, _foreignKeysReviews, _indicesReviews)
        val _existingReviews: TableInfo = read(connection, "reviews")
        if (!_infoReviews.equals(_existingReviews)) {
          return RoomOpenDelegate.ValidationResult(false, """
              |reviews(com.example.zero_degree.core.storage.entity.ReviewEntity).
              | Expected:
              |""".trimMargin() + _infoReviews + """
              |
              | Found:
              |""".trimMargin() + _existingReviews)
        }
        return RoomOpenDelegate.ValidationResult(true, null)
      }
    }
    return _openDelegate
  }

  protected override fun createInvalidationTracker(): InvalidationTracker {
    val _shadowTablesMap: MutableMap<String, String> = mutableMapOf()
    val _viewTables: MutableMap<String, Set<String>> = mutableMapOf()
    return InvalidationTracker(this, _shadowTablesMap, _viewTables, "bars", "drinks", "events", "bookings", "reviews")
  }

  public override fun clearAllTables() {
    super.performClear(false, "bars", "drinks", "events", "bookings", "reviews")
  }

  protected override fun getRequiredTypeConverterClasses(): Map<KClass<*>, List<KClass<*>>> {
    val _typeConvertersMap: MutableMap<KClass<*>, List<KClass<*>>> = mutableMapOf()
    _typeConvertersMap.put(BarDao::class, BarDao_Impl.getRequiredConverters())
    _typeConvertersMap.put(DrinkDao::class, DrinkDao_Impl.getRequiredConverters())
    _typeConvertersMap.put(EventDao::class, EventDao_Impl.getRequiredConverters())
    _typeConvertersMap.put(BookingDao::class, BookingDao_Impl.getRequiredConverters())
    _typeConvertersMap.put(ReviewDao::class, ReviewDao_Impl.getRequiredConverters())
    return _typeConvertersMap
  }

  public override fun getRequiredAutoMigrationSpecClasses(): Set<KClass<out AutoMigrationSpec>> {
    val _autoMigrationSpecsSet: MutableSet<KClass<out AutoMigrationSpec>> = mutableSetOf()
    return _autoMigrationSpecsSet
  }

  public override fun createAutoMigrations(autoMigrationSpecs: Map<KClass<out AutoMigrationSpec>, AutoMigrationSpec>): List<Migration> {
    val _autoMigrations: MutableList<Migration> = mutableListOf()
    return _autoMigrations
  }

  public override fun barDao(): BarDao = _barDao.value

  public override fun drinkDao(): DrinkDao = _drinkDao.value

  public override fun eventDao(): EventDao = _eventDao.value

  public override fun bookingDao(): BookingDao = _bookingDao.value

  public override fun reviewDao(): ReviewDao = _reviewDao.value
}
