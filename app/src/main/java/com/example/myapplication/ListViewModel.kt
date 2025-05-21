package com.example.myapplication

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.random.Random

sealed class Intent {
    object AddItem : Intent()
    data class DeleteItem(val id: String) : Intent()
}

class ListViewModel : ViewModel() {

    private val _state = MutableStateFlow(ListState())
    val state: StateFlow<ListState> = _state

    init {
        startAutoAdd()
    }

    private fun startAutoAdd() {
        viewModelScope.launch {
            while (true) {
                delay(5000)
                addRandomItem()
            }
        }
    }

    private fun addRandomItem() {
        val currentItems = _state.value.items
        val nextNumber = findMinimalMissingNumber(currentItems.map { it.number })
        val newItem = Item(number = nextNumber)
        val currentList = currentItems.toMutableList()
        val position = Random.nextInt(currentList.size + 1)
        currentList.add(position, newItem)
        _state.update {
            it.copy(items = currentList)
        }
    }

    fun processIntent(intent: Intent) {
        when (intent) {
            is Intent.DeleteItem -> deleteItem(intent.id)
            else -> {}
        }
    }

    private fun deleteItem(id: String) {
        _state.update {
            it.copy(items = it.items.filter { item -> item.id != id })
        }
    }
    private fun findMinimalMissingNumber(numbers: List<Int>): Int {
        val set = numbers.toSet()
        var i = 1
        while (true) {
            if (!set.contains(i)) return i
            i++
        }
    }
}
