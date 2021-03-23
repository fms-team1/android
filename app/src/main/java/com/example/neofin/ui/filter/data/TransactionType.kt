package com.example.neofin.ui.filter.data


class TransactionType(var id: String, var name: String) {

    override fun toString(): String {
        return name
    }

    override fun equals(other: Any?): Boolean {
        if (other is TransactionType) {
            if (other.name == name && other.id == id) return true
        }
        return false
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + name.hashCode()
        return result
    }
}