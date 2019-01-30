package org.lifetowncolumbus.pos.merchant.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "catalog")
data class CatalogItem(@PrimaryKey(autoGenerate = true) var id: Long?,
                       @ColumnInfo(name = "name") var name: String,
                       @ColumnInfo(name = "value") var value: Double)