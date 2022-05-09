package com.arepade.oze.database

import androidx.lifecycle.LiveData
import androidx.paging.PagingSource
import androidx.room.*
import com.arepade.oze.dataModels.Bookmarked
import com.arepade.oze.dataModels.ItemsItem

@Dao
interface UsersDao {
    @Query("select * from itemsitem")
    fun getAllUsers(): PagingSource<Int, ItemsItem>


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(vararg users: ItemsItem)
}

@Dao
interface BookmarkedDao {
    @Query("select * from bookmarked")
    fun getBookmarked(): LiveData<List<Bookmarked>>


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(vararg bookmark: Bookmarked)

    @Delete
    fun delete(bookmark: Bookmarked)

    @Query("DELETE FROM bookmarked")
    fun clear()
}

@Database(entities = [ItemsItem::class, Bookmarked::class], version = 1, exportSchema = false)
abstract class UsersDatabase : RoomDatabase() {
    abstract val usersDao: UsersDao
    abstract val bookmarkedDao: BookmarkedDao
}
