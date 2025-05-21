package com.example.myapplication

import java.util.UUID

data class Item(
    val id: String = UUID.randomUUID().toString(),
    val number: Int
)

data class ListState(
    val items: List<Item> = emptyList(),
)