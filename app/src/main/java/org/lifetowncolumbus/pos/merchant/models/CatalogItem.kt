package org.lifetowncolumbus.pos.merchant.models

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity(tableName = "catalog")
data class CatalogItem(@PrimaryKey(autoGenerate = true) var id: Long?,
                       @ColumnInfo(name = "name") var name: String,
                       @ColumnInfo(name = "value") var value: Double)