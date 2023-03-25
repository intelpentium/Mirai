package fathurrahman.projeku.mirai.viewmodel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.HiltAndroidApp
import dagger.hilt.android.lifecycle.HiltViewModel
import fathurrahman.projeku.mirai.common.ActionLiveData
import fathurrahman.projeku.mirai.db.WeatherDB
import fathurrahman.projeku.mirai.db.table.FavoriteTable
import fathurrahman.projeku.mirai.ext.ioThread
import fathurrahman.projeku.mirai.services.entity.ResponseWeather
import javax.inject.Inject

@HiltViewModel
class RepoViewModel @Inject constructor(
    val application: Application
): ViewModel(){

    val db = WeatherDB.getDatabase(this.application)

    val responseFavorite = ActionLiveData<FavoriteTable>()

    //======================= Local Database Favorite ===================
    fun setFavorite(name: String, status: String){
        ioThread {
            db.daoFavorite().insert(FavoriteTable(0, name, status))
        }
    }

    fun getFavorite(): LiveData<List<FavoriteTable>> {
        return db.daoFavorite().getAll("1")
    }

    fun getFavoriteName(name: String) : LiveData<FavoriteTable> {
        return db.daoFavorite().getByName(name)
    }

    fun updateFavorite(name: String, status: String){
        ioThread {
            db.daoFavorite().update(name, status)
        }
    }

    fun deleteFavorite(){
        ioThread {
            db.daoFavorite().deleteAll()
        }
    }
}