package com.example.zero_degree.core.storage.dao

import androidx.room.EntityInsertAdapter
import androidx.room.RoomDatabase
import androidx.room.util.getColumnIndexOrThrow
import androidx.room.util.performSuspending
import androidx.sqlite.SQLiteStatement
import com.example.zero_degree.core.storage.entity.DrinkEntity
import kotlin.Boolean
import kotlin.Double
import kotlin.Int
import kotlin.String
import kotlin.Suppress
import kotlin.Unit
import kotlin.collections.List
import kotlin.collections.MutableList
import kotlin.collections.mutableListOf
import kotlin.reflect.KClass

@Suppress(names = ["UNCHECKED_CAST", "DEPRECATION", "REDUNDANT_PROJECTION", "REMOVAL"])
public class DrinkDao_Impl(
  __db: RoomDatabase,
) : DrinkDao {
  private val __db: RoomDatabase

  private val __insertAdapterOfDrinkEntity: EntityInsertAdapter<DrinkEntity>
  init {
    this.__db = __db
    this.__insertAdapterOfDrinkEntity = object : EntityInsertAdapter<DrinkEntity>() {
      protected override fun createQuery(): String = "INSERT OR REPLACE INTO `drinks` (`id`,`name`,`description`,`imageUrl`,`type`,`taste`,`price`,`available`,`alcoholContent`) VALUES (?,?,?,?,?,?,?,?,?)"

      protected override fun bind(statement: SQLiteStatement, entity: DrinkEntity) {
        statement.bindText(1, entity.id)
        statement.bindText(2, entity.name)
        val _tmpDescription: String? = entity.description
        if (_tmpDescription == null) {
          statement.bindNull(3)
        } else {
          statement.bindText(3, _tmpDescription)
        }
        val _tmpImageUrl: String? = entity.imageUrl
        if (_tmpImageUrl == null) {
          statement.bindNull(4)
        } else {
          statement.bindText(4, _tmpImageUrl)
        }
        val _tmpType: String? = entity.type
        if (_tmpType == null) {
          statement.bindNull(5)
        } else {
          statement.bindText(5, _tmpType)
        }
        val _tmpTaste: String? = entity.taste
        if (_tmpTaste == null) {
          statement.bindNull(6)
        } else {
          statement.bindText(6, _tmpTaste)
        }
        statement.bindDouble(7, entity.price)
        val _tmp: Int = if (entity.available) 1 else 0
        statement.bindLong(8, _tmp.toLong())
        statement.bindDouble(9, entity.alcoholContent)
      }
    }
  }

  public override suspend fun insertDrink(drink: DrinkEntity): Unit = performSuspending(__db, false, true) { _connection ->
    __insertAdapterOfDrinkEntity.insert(_connection, drink)
  }

  public override suspend fun insertDrinks(drinks: List<DrinkEntity>): Unit = performSuspending(__db, false, true) { _connection ->
    __insertAdapterOfDrinkEntity.insert(_connection, drinks)
  }

  public override suspend fun getAllDrinks(): List<DrinkEntity> {
    val _sql: String = "SELECT * FROM drinks"
    return performSuspending(__db, true, false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfName: Int = getColumnIndexOrThrow(_stmt, "name")
        val _columnIndexOfDescription: Int = getColumnIndexOrThrow(_stmt, "description")
        val _columnIndexOfImageUrl: Int = getColumnIndexOrThrow(_stmt, "imageUrl")
        val _columnIndexOfType: Int = getColumnIndexOrThrow(_stmt, "type")
        val _columnIndexOfTaste: Int = getColumnIndexOrThrow(_stmt, "taste")
        val _columnIndexOfPrice: Int = getColumnIndexOrThrow(_stmt, "price")
        val _columnIndexOfAvailable: Int = getColumnIndexOrThrow(_stmt, "available")
        val _columnIndexOfAlcoholContent: Int = getColumnIndexOrThrow(_stmt, "alcoholContent")
        val _result: MutableList<DrinkEntity> = mutableListOf()
        while (_stmt.step()) {
          val _item: DrinkEntity
          val _tmpId: String
          _tmpId = _stmt.getText(_columnIndexOfId)
          val _tmpName: String
          _tmpName = _stmt.getText(_columnIndexOfName)
          val _tmpDescription: String?
          if (_stmt.isNull(_columnIndexOfDescription)) {
            _tmpDescription = null
          } else {
            _tmpDescription = _stmt.getText(_columnIndexOfDescription)
          }
          val _tmpImageUrl: String?
          if (_stmt.isNull(_columnIndexOfImageUrl)) {
            _tmpImageUrl = null
          } else {
            _tmpImageUrl = _stmt.getText(_columnIndexOfImageUrl)
          }
          val _tmpType: String?
          if (_stmt.isNull(_columnIndexOfType)) {
            _tmpType = null
          } else {
            _tmpType = _stmt.getText(_columnIndexOfType)
          }
          val _tmpTaste: String?
          if (_stmt.isNull(_columnIndexOfTaste)) {
            _tmpTaste = null
          } else {
            _tmpTaste = _stmt.getText(_columnIndexOfTaste)
          }
          val _tmpPrice: Double
          _tmpPrice = _stmt.getDouble(_columnIndexOfPrice)
          val _tmpAvailable: Boolean
          val _tmp: Int
          _tmp = _stmt.getLong(_columnIndexOfAvailable).toInt()
          _tmpAvailable = _tmp != 0
          val _tmpAlcoholContent: Double
          _tmpAlcoholContent = _stmt.getDouble(_columnIndexOfAlcoholContent)
          _item = DrinkEntity(_tmpId,_tmpName,_tmpDescription,_tmpImageUrl,_tmpType,_tmpTaste,_tmpPrice,_tmpAvailable,_tmpAlcoholContent)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun getDrinkById(id: String): DrinkEntity? {
    val _sql: String = "SELECT * FROM drinks WHERE id = ?"
    return performSuspending(__db, true, false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindText(_argIndex, id)
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfName: Int = getColumnIndexOrThrow(_stmt, "name")
        val _columnIndexOfDescription: Int = getColumnIndexOrThrow(_stmt, "description")
        val _columnIndexOfImageUrl: Int = getColumnIndexOrThrow(_stmt, "imageUrl")
        val _columnIndexOfType: Int = getColumnIndexOrThrow(_stmt, "type")
        val _columnIndexOfTaste: Int = getColumnIndexOrThrow(_stmt, "taste")
        val _columnIndexOfPrice: Int = getColumnIndexOrThrow(_stmt, "price")
        val _columnIndexOfAvailable: Int = getColumnIndexOrThrow(_stmt, "available")
        val _columnIndexOfAlcoholContent: Int = getColumnIndexOrThrow(_stmt, "alcoholContent")
        val _result: DrinkEntity?
        if (_stmt.step()) {
          val _tmpId: String
          _tmpId = _stmt.getText(_columnIndexOfId)
          val _tmpName: String
          _tmpName = _stmt.getText(_columnIndexOfName)
          val _tmpDescription: String?
          if (_stmt.isNull(_columnIndexOfDescription)) {
            _tmpDescription = null
          } else {
            _tmpDescription = _stmt.getText(_columnIndexOfDescription)
          }
          val _tmpImageUrl: String?
          if (_stmt.isNull(_columnIndexOfImageUrl)) {
            _tmpImageUrl = null
          } else {
            _tmpImageUrl = _stmt.getText(_columnIndexOfImageUrl)
          }
          val _tmpType: String?
          if (_stmt.isNull(_columnIndexOfType)) {
            _tmpType = null
          } else {
            _tmpType = _stmt.getText(_columnIndexOfType)
          }
          val _tmpTaste: String?
          if (_stmt.isNull(_columnIndexOfTaste)) {
            _tmpTaste = null
          } else {
            _tmpTaste = _stmt.getText(_columnIndexOfTaste)
          }
          val _tmpPrice: Double
          _tmpPrice = _stmt.getDouble(_columnIndexOfPrice)
          val _tmpAvailable: Boolean
          val _tmp: Int
          _tmp = _stmt.getLong(_columnIndexOfAvailable).toInt()
          _tmpAvailable = _tmp != 0
          val _tmpAlcoholContent: Double
          _tmpAlcoholContent = _stmt.getDouble(_columnIndexOfAlcoholContent)
          _result = DrinkEntity(_tmpId,_tmpName,_tmpDescription,_tmpImageUrl,_tmpType,_tmpTaste,_tmpPrice,_tmpAvailable,_tmpAlcoholContent)
        } else {
          _result = null
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun getDrinksByType(type: String): List<DrinkEntity> {
    val _sql: String = "SELECT * FROM drinks WHERE type = ?"
    return performSuspending(__db, true, false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindText(_argIndex, type)
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfName: Int = getColumnIndexOrThrow(_stmt, "name")
        val _columnIndexOfDescription: Int = getColumnIndexOrThrow(_stmt, "description")
        val _columnIndexOfImageUrl: Int = getColumnIndexOrThrow(_stmt, "imageUrl")
        val _columnIndexOfType: Int = getColumnIndexOrThrow(_stmt, "type")
        val _columnIndexOfTaste: Int = getColumnIndexOrThrow(_stmt, "taste")
        val _columnIndexOfPrice: Int = getColumnIndexOrThrow(_stmt, "price")
        val _columnIndexOfAvailable: Int = getColumnIndexOrThrow(_stmt, "available")
        val _columnIndexOfAlcoholContent: Int = getColumnIndexOrThrow(_stmt, "alcoholContent")
        val _result: MutableList<DrinkEntity> = mutableListOf()
        while (_stmt.step()) {
          val _item: DrinkEntity
          val _tmpId: String
          _tmpId = _stmt.getText(_columnIndexOfId)
          val _tmpName: String
          _tmpName = _stmt.getText(_columnIndexOfName)
          val _tmpDescription: String?
          if (_stmt.isNull(_columnIndexOfDescription)) {
            _tmpDescription = null
          } else {
            _tmpDescription = _stmt.getText(_columnIndexOfDescription)
          }
          val _tmpImageUrl: String?
          if (_stmt.isNull(_columnIndexOfImageUrl)) {
            _tmpImageUrl = null
          } else {
            _tmpImageUrl = _stmt.getText(_columnIndexOfImageUrl)
          }
          val _tmpType: String?
          if (_stmt.isNull(_columnIndexOfType)) {
            _tmpType = null
          } else {
            _tmpType = _stmt.getText(_columnIndexOfType)
          }
          val _tmpTaste: String?
          if (_stmt.isNull(_columnIndexOfTaste)) {
            _tmpTaste = null
          } else {
            _tmpTaste = _stmt.getText(_columnIndexOfTaste)
          }
          val _tmpPrice: Double
          _tmpPrice = _stmt.getDouble(_columnIndexOfPrice)
          val _tmpAvailable: Boolean
          val _tmp: Int
          _tmp = _stmt.getLong(_columnIndexOfAvailable).toInt()
          _tmpAvailable = _tmp != 0
          val _tmpAlcoholContent: Double
          _tmpAlcoholContent = _stmt.getDouble(_columnIndexOfAlcoholContent)
          _item = DrinkEntity(_tmpId,_tmpName,_tmpDescription,_tmpImageUrl,_tmpType,_tmpTaste,_tmpPrice,_tmpAvailable,_tmpAlcoholContent)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun getAvailableDrinks(): List<DrinkEntity> {
    val _sql: String = "SELECT * FROM drinks WHERE available = 1"
    return performSuspending(__db, true, false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfName: Int = getColumnIndexOrThrow(_stmt, "name")
        val _columnIndexOfDescription: Int = getColumnIndexOrThrow(_stmt, "description")
        val _columnIndexOfImageUrl: Int = getColumnIndexOrThrow(_stmt, "imageUrl")
        val _columnIndexOfType: Int = getColumnIndexOrThrow(_stmt, "type")
        val _columnIndexOfTaste: Int = getColumnIndexOrThrow(_stmt, "taste")
        val _columnIndexOfPrice: Int = getColumnIndexOrThrow(_stmt, "price")
        val _columnIndexOfAvailable: Int = getColumnIndexOrThrow(_stmt, "available")
        val _columnIndexOfAlcoholContent: Int = getColumnIndexOrThrow(_stmt, "alcoholContent")
        val _result: MutableList<DrinkEntity> = mutableListOf()
        while (_stmt.step()) {
          val _item: DrinkEntity
          val _tmpId: String
          _tmpId = _stmt.getText(_columnIndexOfId)
          val _tmpName: String
          _tmpName = _stmt.getText(_columnIndexOfName)
          val _tmpDescription: String?
          if (_stmt.isNull(_columnIndexOfDescription)) {
            _tmpDescription = null
          } else {
            _tmpDescription = _stmt.getText(_columnIndexOfDescription)
          }
          val _tmpImageUrl: String?
          if (_stmt.isNull(_columnIndexOfImageUrl)) {
            _tmpImageUrl = null
          } else {
            _tmpImageUrl = _stmt.getText(_columnIndexOfImageUrl)
          }
          val _tmpType: String?
          if (_stmt.isNull(_columnIndexOfType)) {
            _tmpType = null
          } else {
            _tmpType = _stmt.getText(_columnIndexOfType)
          }
          val _tmpTaste: String?
          if (_stmt.isNull(_columnIndexOfTaste)) {
            _tmpTaste = null
          } else {
            _tmpTaste = _stmt.getText(_columnIndexOfTaste)
          }
          val _tmpPrice: Double
          _tmpPrice = _stmt.getDouble(_columnIndexOfPrice)
          val _tmpAvailable: Boolean
          val _tmp: Int
          _tmp = _stmt.getLong(_columnIndexOfAvailable).toInt()
          _tmpAvailable = _tmp != 0
          val _tmpAlcoholContent: Double
          _tmpAlcoholContent = _stmt.getDouble(_columnIndexOfAlcoholContent)
          _item = DrinkEntity(_tmpId,_tmpName,_tmpDescription,_tmpImageUrl,_tmpType,_tmpTaste,_tmpPrice,_tmpAvailable,_tmpAlcoholContent)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun deleteAllDrinks() {
    val _sql: String = "DELETE FROM drinks"
    return performSuspending(__db, false, true) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        _stmt.step()
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun deleteDrink(id: String) {
    val _sql: String = "DELETE FROM drinks WHERE id = ?"
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
