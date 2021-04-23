package com.example.neofin.ui.journal.journalById.data

import com.example.neofin.ui.addTransactions.data.CategoryIdName

class CategoryItem (var id: Int?, var name: String) {

    override fun toString(): String {
        return name
    }

    override fun equals(other: Any?): Boolean {
        if (other is CategoryItem) {
            if (other.name == name && other.id == id) return true
        }
        return false
    }

    override fun hashCode(): Int {
        var result = id ?: 0
        result = 31 * result + name.hashCode()
        return result
    }
}