package com.example.zero_degree.core.storage.dao

import androidx.room.EntityInsertAdapter
import androidx.room.RoomDatabase
import androidx.room.util.getColumnIndexOrThrow
import androidx.room.util.performSuspending
import androidx.sqlite.SQLiteStatement
import com.example.zero_degree.core.storage.entity.BookingEntity
import kotlin.Int
import kotlin.String
import kotlin.Suppress
import kotlin.Unit
import kotlin.collections.List
import kotlin.collections.MutableList
import kotlin.collections.mutableListOf
import kotlin.reflect.KClass

@Suppress(names = ["UNCHECKED_CAST", "DEPRECATION", "REDUNDANT_PROJECTION", "REMOVAL"])
public class BookingDao_Impl(
  __db: RoomDatabase,
) : BookingDao {
  private val __db: RoomDatabase

  private val __insertAdapterOfBookingEntity: EntityInsertAdapter<BookingEntity>
  init {
    this.__db = __db
    this.__insertAdapterOfBookingEntity = object : EntityInsertAdapter<BookingEntity>() {
      protected override fun createQuery(): String = "INSERT OR REPLACE INTO `bookings` (`id`,`barId`,`userId`,`date`,`time`,`guestsCount`,`status`) VALUES (?,?,?,?,?,?,?)"

      protected override fun bind(statement: SQLiteStatement, entity: BookingEntity) {
        statement.bindText(1, entity.id)
        statement.bindText(2, entity.barId)
        statement.bindText(3, entity.userId)
        statement.bindText(4, entity.date)
        statement.bindText(5, entity.time)
        statement.bindLong(6, entity.guestsCount.toLong())
        statement.bindText(7, entity.status)
      }
    }
  }

  public override suspend fun insertBooking(booking: BookingEntity): Unit = performSuspending(__db, false, true) { _connection ->
    __insertAdapterOfBookingEntity.insert(_connection, booking)
  }

  public override suspend fun insertBookings(bookings: List<BookingEntity>): Unit = performSuspending(__db, false, true) { _connection ->
    __insertAdapterOfBookingEntity.insert(_connection, bookings)
  }

  public override suspend fun getAllBookings(): List<BookingEntity> {
    val _sql: String = "SELECT * FROM bookings"
    return performSuspending(__db, true, false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfBarId: Int = getColumnIndexOrThrow(_stmt, "barId")
        val _columnIndexOfUserId: Int = getColumnIndexOrThrow(_stmt, "userId")
        val _columnIndexOfDate: Int = getColumnIndexOrThrow(_stmt, "date")
        val _columnIndexOfTime: Int = getColumnIndexOrThrow(_stmt, "time")
        val _columnIndexOfGuestsCount: Int = getColumnIndexOrThrow(_stmt, "guestsCount")
        val _columnIndexOfStatus: Int = getColumnIndexOrThrow(_stmt, "status")
        val _result: MutableList<BookingEntity> = mutableListOf()
        while (_stmt.step()) {
          val _item: BookingEntity
          val _tmpId: String
          _tmpId = _stmt.getText(_columnIndexOfId)
          val _tmpBarId: String
          _tmpBarId = _stmt.getText(_columnIndexOfBarId)
          val _tmpUserId: String
          _tmpUserId = _stmt.getText(_columnIndexOfUserId)
          val _tmpDate: String
          _tmpDate = _stmt.getText(_columnIndexOfDate)
          val _tmpTime: String
          _tmpTime = _stmt.getText(_columnIndexOfTime)
          val _tmpGuestsCount: Int
          _tmpGuestsCount = _stmt.getLong(_columnIndexOfGuestsCount).toInt()
          val _tmpStatus: String
          _tmpStatus = _stmt.getText(_columnIndexOfStatus)
          _item = BookingEntity(_tmpId,_tmpBarId,_tmpUserId,_tmpDate,_tmpTime,_tmpGuestsCount,_tmpStatus)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun getBookingById(id: String): BookingEntity? {
    val _sql: String = "SELECT * FROM bookings WHERE id = ?"
    return performSuspending(__db, true, false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindText(_argIndex, id)
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfBarId: Int = getColumnIndexOrThrow(_stmt, "barId")
        val _columnIndexOfUserId: Int = getColumnIndexOrThrow(_stmt, "userId")
        val _columnIndexOfDate: Int = getColumnIndexOrThrow(_stmt, "date")
        val _columnIndexOfTime: Int = getColumnIndexOrThrow(_stmt, "time")
        val _columnIndexOfGuestsCount: Int = getColumnIndexOrThrow(_stmt, "guestsCount")
        val _columnIndexOfStatus: Int = getColumnIndexOrThrow(_stmt, "status")
        val _result: BookingEntity?
        if (_stmt.step()) {
          val _tmpId: String
          _tmpId = _stmt.getText(_columnIndexOfId)
          val _tmpBarId: String
          _tmpBarId = _stmt.getText(_columnIndexOfBarId)
          val _tmpUserId: String
          _tmpUserId = _stmt.getText(_columnIndexOfUserId)
          val _tmpDate: String
          _tmpDate = _stmt.getText(_columnIndexOfDate)
          val _tmpTime: String
          _tmpTime = _stmt.getText(_columnIndexOfTime)
          val _tmpGuestsCount: Int
          _tmpGuestsCount = _stmt.getLong(_columnIndexOfGuestsCount).toInt()
          val _tmpStatus: String
          _tmpStatus = _stmt.getText(_columnIndexOfStatus)
          _result = BookingEntity(_tmpId,_tmpBarId,_tmpUserId,_tmpDate,_tmpTime,_tmpGuestsCount,_tmpStatus)
        } else {
          _result = null
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun getBookingsByUserId(userId: String): List<BookingEntity> {
    val _sql: String = "SELECT * FROM bookings WHERE userId = ?"
    return performSuspending(__db, true, false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindText(_argIndex, userId)
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfBarId: Int = getColumnIndexOrThrow(_stmt, "barId")
        val _columnIndexOfUserId: Int = getColumnIndexOrThrow(_stmt, "userId")
        val _columnIndexOfDate: Int = getColumnIndexOrThrow(_stmt, "date")
        val _columnIndexOfTime: Int = getColumnIndexOrThrow(_stmt, "time")
        val _columnIndexOfGuestsCount: Int = getColumnIndexOrThrow(_stmt, "guestsCount")
        val _columnIndexOfStatus: Int = getColumnIndexOrThrow(_stmt, "status")
        val _result: MutableList<BookingEntity> = mutableListOf()
        while (_stmt.step()) {
          val _item: BookingEntity
          val _tmpId: String
          _tmpId = _stmt.getText(_columnIndexOfId)
          val _tmpBarId: String
          _tmpBarId = _stmt.getText(_columnIndexOfBarId)
          val _tmpUserId: String
          _tmpUserId = _stmt.getText(_columnIndexOfUserId)
          val _tmpDate: String
          _tmpDate = _stmt.getText(_columnIndexOfDate)
          val _tmpTime: String
          _tmpTime = _stmt.getText(_columnIndexOfTime)
          val _tmpGuestsCount: Int
          _tmpGuestsCount = _stmt.getLong(_columnIndexOfGuestsCount).toInt()
          val _tmpStatus: String
          _tmpStatus = _stmt.getText(_columnIndexOfStatus)
          _item = BookingEntity(_tmpId,_tmpBarId,_tmpUserId,_tmpDate,_tmpTime,_tmpGuestsCount,_tmpStatus)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun deleteAllBookings() {
    val _sql: String = "DELETE FROM bookings"
    return performSuspending(__db, false, true) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        _stmt.step()
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun deleteBooking(id: String) {
    val _sql: String = "DELETE FROM bookings WHERE id = ?"
    return performSuspending(__db, false, true) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindText(_argIndex, id)
        _stmt.step()
      } finally {
        _stmt.close()
      }
    }
  }

  public companion object {
    public fun getRequiredConverters(): List<KClass<*>> = emptyList()
  }
}
