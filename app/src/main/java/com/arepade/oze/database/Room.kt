package com.arepade.oze.database


import androidx.paging.PagingSource
import androidx.room.*
import com.arepade.oze.dataModels.Bookmarked
import com.arepade.oze.dataModels.ItemsItem
import io.reactivex.Completable
import io.reactivex.Observable

@Dao
interface UsersDao {
    @Query("select * from itemsitem")
    fun getAllUsers(): PagingSource<Int, ItemsItem>


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(vararg users: ItemsItem):Completable
}

@Dao
interface BookmarkedDao {
    @Query("select * from bookmarked")
    fun getBookmarked(): Observable<List<Bookmarked>>


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(vararg bookmark: Bookmarked): Completable

    @Delete
    fun delete(bookmark: Bookmarked): Completable

    @Query("DELETE FROM bookmarked")
    fun clear() : Completable
}

@Database(entities = [ItemsItem::class, Bookmarked::class], version = 1, exportSchema = false)
abstract class UsersDatabase : RoomDatabase() {
    abstract val usersDao: UsersDao
    abstract val bookmarkedDao: BookmarkedDao
}
