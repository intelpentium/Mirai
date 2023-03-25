package fathurrahman.projeku.mirai.ui

import android.Manifest
import android.R
import android.content.pm.PackageManager
import android.database.Cursor
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.core.app.ActivityCompat
import androidx.core.view.isVisible
import fathurrahman.projeku.mirai.databinding.ActivityMainBinding
import fathurrahman.projeku.mirai.modules.base.BaseActivity
import fathurrahman.projeku.mirai.services.entity.DateItem
import fathurrahman.projeku.mirai.services.entity.GeneralItem
import fathurrahman.projeku.mirai.services.entity.ListItem
import fathurrahman.projeku.mirai.services.entity.ResponseGalleryModel
import fathurrahman.projeku.mirai.ui.adapter.AdapterMain
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*


class MainActivity : BaseActivity() {
    lateinit var mBinding: ActivityMainBinding

    private val adapter2 by lazy(LazyThreadSafetyMode.NONE) {
        AdapterMain()
    }

    private val listData: ArrayList<ResponseGalleryModel> = ArrayList()
    private val LOCATION_PERMISSION_CODE = 100

    var pilihan = arrayOf("Day", "Month", "Year")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        requestPermissions()
        initView()
    }

    private fun initView() {

        mBinding.search.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                adapterView: AdapterView<*>?,
                view: View?,
                position: Int,
                l: Long
            ) {
//                Toast.makeText(applicationContext, pilihan[position], Toast.LENGTH_LONG).show()
                getData(pilihan[position].toString())
            }

            override fun onNothingSelected(adapterView: AdapterView<*>?) {}
        }

        val adapter: ArrayAdapter<*> =
            ArrayAdapter<Any?>(this, R.layout.simple_spinner_item, pilihan)
        adapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item)
        mBinding.search.adapter = adapter

        mBinding.rv.adapter = adapter2
    }

    fun convertLongToDate(time: Long): String =
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            DateTimeFormatter.ofPattern("dd MMMM yyyy").format(
                Instant.ofEpochMilli(time * 1000)
                    .atZone(ZoneId.systemDefault())
                    .toLocalDate()
            )
        } else {
            SimpleDateFormat("dd MMMM yyyy").format(
                Date(time * 1000)
            )
        }

    fun convertLongToYear(time: Long): String =
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            DateTimeFormatter.ofPattern("yyyy").format(
                Instant.ofEpochMilli(time * 1000)
                    .atZone(ZoneId.systemDefault())
                    .toLocalDate()
            )
        } else {
            SimpleDateFormat("yyyy").format(
                Date(time * 1000)
            )
        }

    fun convertLongToMonth(time: Long): String =
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            DateTimeFormatter.ofPattern("MMMM yyyy").format(
                Instant.ofEpochMilli(time * 1000)
                    .atZone(ZoneId.systemDefault())
                    .toLocalDate()
            )
        } else {
            SimpleDateFormat("MMMM yyyy").format(
                Date(time * 1000)
            )
        }

    fun convertLongToDay(time: Long): String =
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            DateTimeFormatter.ofPattern("dd").format(
                Instant.ofEpochMilli(time * 1000)
                    .atZone(ZoneId.systemDefault())
                    .toLocalDate()
            )
        } else {
            SimpleDateFormat("dd MMMM yyyy").format(
                Date(time * 1000)
            )
        }

    private fun getData(jenis: String) {

        mBinding.progress.isVisible = true
        listData.clear()

        val columns = arrayOf(
            MediaStore.Images.Media.DATA,
            MediaStore.Images.Media._ID,
            MediaStore.MediaColumns.DATE_MODIFIED
        )
        val orderBy = MediaStore.Images.Media.DATE_ADDED + " DESC"

        val imagecursor: Cursor = contentResolver
            .query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, columns, null, null, orderBy)!!

        val image_column_index: Int = imagecursor.getColumnIndex(MediaStore.Images.Media._ID)
        val count: Int = imagecursor.count
        var thumbnails: Bitmap? = null
        var arrPath: String

        for (i in 0 until count) {
            imagecursor.moveToPosition(i)
            val id: Int = imagecursor.getInt(image_column_index)
            val dataColumnIndex: Int = imagecursor.getColumnIndex(MediaStore.Images.Media.DATA)
            val createdTime =
                imagecursor.getLong(imagecursor.getColumnIndex(MediaStore.MediaColumns.DATE_MODIFIED))
            thumbnails = MediaStore.Images.Thumbnails.getThumbnail(
                contentResolver,
                id.toLong(),
                MediaStore.Images.Thumbnails.MICRO_KIND,
                null
            )
            arrPath = imagecursor.getString(dataColumnIndex)
            listData.add(
                ResponseGalleryModel(
                    arrPath,
                    thumbnails,
                    convertLongToDate(createdTime),
                    convertLongToYear(createdTime),
                    convertLongToMonth(createdTime),
                    convertLongToDay(createdTime)
                )
            )
        }

        imagecursor.close()

        val groupedMapMap: Map<String, List<ResponseGalleryModel>> =
            when (jenis) {
                "Year" -> {
                    listData.groupBy {it.year}
                }
                "Month" -> {
                    listData.groupBy {it.month}
                }
                else -> {
                    listData.groupBy {it.date}
                }
            }

        val consolidatedList = mutableListOf<ListItem>()

        for (date: String in groupedMapMap.keys) {
            consolidatedList.add(DateItem(date))

            val groupItems: List<ResponseGalleryModel> = groupedMapMap[date]!!
            consolidatedList.add(GeneralItem(groupItems))
        }

        Log.e("BISMILLAH", consolidatedList.toString())

        adapter2.items = consolidatedList
        adapter2.notifyDataSetChanged()

        if(listData.size >0){
            mBinding.progress.isVisible = false
        }
    }


    private fun requestPermissions() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
            LOCATION_PERMISSION_CODE
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == LOCATION_PERMISSION_CODE) {
            if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
//                assistant!!.start()
//                getData("Day")
            }
        }
    }
}