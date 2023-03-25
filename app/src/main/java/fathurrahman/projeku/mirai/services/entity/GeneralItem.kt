package fathurrahman.projeku.mirai.services.entity

import android.graphics.Bitmap
import com.google.gson.annotations.SerializedName

class GeneralItem (
    val listItem: List<ResponseGalleryModel>,
) : ListItem(TYPE_GENERAL)