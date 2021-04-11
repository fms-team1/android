package com.example.neofin.ui.addTransactions.data

class SectionName (var backName: Int, var frontName: String) {

    override fun toString(): String {
        return frontName
    }

    override fun equals(other: Any?): Boolean {
        if (other is SectionName) {
            if (other.frontName === frontName && other.backName == backName) return true
        }
        return false
    }

    override fun hashCode(): Int {
        var result = backName.hashCode()
        result = 31 * result + backName.hashCode()
        return result
    }
}