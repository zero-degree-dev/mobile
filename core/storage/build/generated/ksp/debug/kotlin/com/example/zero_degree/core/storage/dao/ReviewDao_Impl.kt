package com.example.zero_degree.core.storage.dao

import androidx.room.EntityInsertAdapter
import androidx.room.RoomDatabase
import androidx.room.util.getColumnIndexOrThrow
import androidx.room.util.performSuspending
import androidx.sqlite.SQLiteStatement
import com.example.zero_degree.core.storage.entity.ReviewEntity
import kotlin.Int
import kotlin.String
import kotlin.Suppress
import kotlin.Unit
import kotlin.collections.List
import kotlin.collections.MutableList
import kotlin.collections.mutableListOf
import kotlin.reflect.KClass

@Suppress(names = ["UNCHECKED_CAST", "DEPRECATION", "REDUNDANT_PROJECTION", "REMOVAL"])
public class ReviewDao_Impl(
  __db: RoomDatabase,
) : ReviewDao {
  private val __db: RoomDatabase

  private val __insertAdapterOfReviewEntity: EntityInsertAdapter<ReviewEntity>
  init {
    this.__db = __db
    this.__insertAdapterOfReviewEntity = object : EntityInsertAdapter<ReviewEntity>() {
      protected override fun createQuery(): String = "INSERT OR REPLACE INTO `reviews` (`id`,`targetId`,`targetType`,`userId`,`userName`,`rating`,`comment`,`date`) VALUES (?,?,?,?,?,?,?,?)"

      protected override fun bind(statement: SQLiteStatement, entity: ReviewEntity) {
        statement.bindText(1, entity.id)
        statement.bindText(2, entity.targetId)
        statement.bindText(3, entity.targetType)
        statement.bindText(4, entity.userId)
        val _tmpUserName: String? = entity.userName
        if (_tmpUserName == null) {
          statement.bindNull(5)
        } else {
          statement.bindText(5, _tmpUserName)
        }
        statement.bindLong(6, entity.rating.toLong())
        val _tmpComment: String? = entity.comment
        if (_tmpComment == null) {
          statement.bindNull(7)
        } else {
          statement.bindText(7, _tmpComment)
        }
        val _tmpDate: String? = entity.date
        if (_tmpDate == null) {
          statement.bindNull(8)
        } else {
          statement.bindText(8, _tmpDate)
        }
      }
    }
  }

  public override suspend fun insertReview(review: ReviewEntity): Unit = performSuspending(__db, false, true) { _connection ->
    __insertAdapterOfReviewEntity.insert(_connection, review)
  }

  public override suspend fun insertReviews(reviews: List<ReviewEntity>): Unit = performSuspending(__db, false, true) { _connection ->
    __insertAdapterOfReviewEntity.insert(_connection, reviews)
  }

  public override suspend fun getAllReviews(): List<ReviewEntity> {
    val _sql: String = "SELECT * FROM reviews"
    return performSuspending(__db, true, false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfTargetId: Int = getColumnIndexOrThrow(_stmt, "targetId")
        val _columnIndexOfTargetType: Int = getColumnIndexOrThrow(_stmt, "targetType")
        val _columnIndexOfUserId: Int = getColumnIndexOrThrow(_stmt, "userId")
        val _columnIndexOfUserName: Int = getColumnIndexOrThrow(_stmt, "userName")
        val _columnIndexOfRating: Int = getColumnIndexOrThrow(_stmt, "rating")
        val _columnIndexOfComment: Int = getColumnIndexOrThrow(_stmt, "comment")
        val _columnIndexOfDate: Int = getColumnIndexOrThrow(_stmt, "date")
        val _result: MutableList<ReviewEntity> = mutableListOf()
        while (_stmt.step()) {
          val _item: ReviewEntity
          val _tmpId: String
          _tmpId = _stmt.getText(_columnIndexOfId)
          val _tmpTargetId: String
          _tmpTargetId = _stmt.getText(_columnIndexOfTargetId)
          val _tmpTargetType: String
          _tmpTargetType = _stmt.getText(_columnIndexOfTargetType)
          val _tmpUserId: String
          _tmpUserId = _stmt.getText(_columnIndexOfUserId)
          val _tmpUserName: String?
          if (_stmt.isNull(_columnIndexOfUserName)) {
            _tmpUserName = null
          } else {
            _tmpUserName = _stmt.getText(_columnIndexOfUserName)
          }
          val _tmpRating: Int
          _tmpRating = _stmt.getLong(_columnIndexOfRating).toInt()
          val _tmpComment: String?
          if (_stmt.isNull(_columnIndexOfComment)) {
            _tmpComment = null
          } else {
            _tmpComment = _stmt.getText(_columnIndexOfComment)
          }
          val _tmpDate: String?
          if (_stmt.isNull(_columnIndexOfDate)) {
            _tmpDate = null
          } else {
            _tmpDate = _stmt.getText(_columnIndexOfDate)
          }
          _item = ReviewEntity(_tmpId,_tmpTargetId,_tmpTargetType,_tmpUserId,_tmpUserName,_tmpRating,_tmpComment,_tmpDate)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun getReviewById(id: String): ReviewEntity? {
    val _sql: String = "SELECT * FROM reviews WHERE id = ?"
    return performSuspending(__db, true, false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindText(_argIndex, id)
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfTargetId: Int = getColumnIndexOrThrow(_stmt, "targetId")
        val _columnIndexOfTargetType: Int = getColumnIndexOrThrow(_stmt, "targetType")
        val _columnIndexOfUserId: Int = getColumnIndexOrThrow(_stmt, "userId")
        val _columnIndexOfUserName: Int = getColumnIndexOrThrow(_stmt, "userName")
        val _columnIndexOfRating: Int = getColumnIndexOrThrow(_stmt, "rating")
        val _columnIndexOfComment: Int = getColumnIndexOrThrow(_stmt, "comment")
        val _columnIndexOfDate: Int = getColumnIndexOrThrow(_stmt, "date")
        val _result: ReviewEntity?
        if (_stmt.step()) {
          val _tmpId: String
          _tmpId = _stmt.getText(_columnIndexOfId)
          val _tmpTargetId: String
          _tmpTargetId = _stmt.getText(_columnIndexOfTargetId)
          val _tmpTargetType: String
          _tmpTargetType = _stmt.getText(_columnIndexOfTargetType)
          val _tmpUserId: String
          _tmpUserId = _stmt.getText(_columnIndexOfUserId)
          val _tmpUserName: String?
          if (_stmt.isNull(_columnIndexOfUserName)) {
            _tmpUserName = null
          } else {
            _tmpUserName = _stmt.getText(_columnIndexOfUserName)
          }
          val _tmpRating: Int
          _tmpRating = _stmt.getLong(_columnIndexOfRating).toInt()
          val _tmpComment: String?
          if (_stmt.isNull(_columnIndexOfComment)) {
            _tmpComment = null
          } else {
            _tmpComment = _stmt.getText(_columnIndexOfComment)
          }
          val _tmpDate: String?
          if (_stmt.isNull(_columnIndexOfDate)) {
            _tmpDate = null
          } else {
            _tmpDate = _stmt.getText(_columnIndexOfDate)
          }
          _result = ReviewEntity(_tmpId,_tmpTargetId,_tmpTargetType,_tmpUserId,_tmpUserName,_tmpRating,_tmpComment,_tmpDate)
        } else {
          _result = null
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun getReviewsByTarget(targetId: String, targetType: String): List<ReviewEntity> {
    val _sql: String = "SELECT * FROM reviews WHERE targetId = ? AND targetType = ?"
    return performSuspending(__db, true, false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindText(_argIndex, targetId)
        _argIndex = 2
        _stmt.bindText(_argIndex, targetType)
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfTargetId: Int = getColumnIndexOrThrow(_stmt, "targetId")
        val _columnIndexOfTargetType: Int = getColumnIndexOrThrow(_stmt, "targetType")
        val _columnIndexOfUserId: Int = getColumnIndexOrThrow(_stmt, "userId")
        val _columnIndexOfUserName: Int = getColumnIndexOrThrow(_stmt, "userName")
        val _columnIndexOfRating: Int = getColumnIndexOrThrow(_stmt, "rating")
        val _columnIndexOfComment: Int = getColumnIndexOrThrow(_stmt, "comment")
        val _columnIndexOfDate: Int = getColumnIndexOrThrow(_stmt, "date")
        val _result: MutableList<ReviewEntity> = mutableListOf()
        while (_stmt.step()) {
          val _item: ReviewEntity
          val _tmpId: String
          _tmpId = _stmt.getText(_columnIndexOfId)
          val _tmpTargetId: String
          _tmpTargetId = _stmt.getText(_columnIndexOfTargetId)
          val _tmpTargetType: String
          _tmpTargetType = _stmt.getText(_columnIndexOfTargetType)
          val _tmpUserId: String
          _tmpUserId = _stmt.getText(_columnIndexOfUserId)
          val _tmpUserName: String?
          if (_stmt.isNull(_columnIndexOfUserName)) {
            _tmpUserName = null
          } else {
            _tmpUserName = _stmt.getText(_columnIndexOfUserName)
          }
          val _tmpRating: Int
          _tmpRating = _stmt.getLong(_columnIndexOfRating).toInt()
          val _tmpComment: String?
          if (_stmt.isNull(_columnIndexOfComment)) {
            _tmpComment = null
          } else {
            _tmpComment = _stmt.getText(_columnIndexOfComment)
          }
          val _tmpDate: String?
          if (_stmt.isNull(_columnIndexOfDate)) {
            _tmpDate = null
          } else {
            _tmpDate = _stmt.getText(_columnIndexOfDate)
          }
          _item = ReviewEntity(_tmpId,_tmpTargetId,_tmpTargetType,_tmpUserId,_tmpUserName,_tmpRating,_tmpComment,_tmpDate)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun deleteAllReviews() {
    val _sql: String = "DELETE FROM reviews"
    return performSuspending(__db, false, true) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        _stmt.step()
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun deleteReview(id: String) {
    val _sql: String = "DELETE FROM reviews WHERE id = ?"
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
