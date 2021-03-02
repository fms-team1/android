package com.example.fms.utils

import android.content.Context
import android.util.Log
import android.widget.Toast

fun toast(context: Context, text: String) {
    Toast.makeText(context, text, Toast.LENGTH_SHORT).show()
}

fun logs(text: String) {
    Log.v("CHOS", text)
}