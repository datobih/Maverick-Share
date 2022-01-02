package com.example.maverickfilesender.model

import android.content.pm.ApplicationInfo
import android.graphics.drawable.Drawable

data class AppPackage(val applicationInfo: ApplicationInfo, val drawable: Drawable,var onSelect:Boolean,var data:ByteArray?)
