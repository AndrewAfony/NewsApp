package com.myapp.newsapp.util

fun String.toNormTime(): String {
    return this.substringAfter("T").substringBefore("Z")
}