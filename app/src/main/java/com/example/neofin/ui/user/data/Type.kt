package com.example.neofin.ui.user.data

class Type (var backName: String, var frontName: String) {

    override fun toString(): String {
        return frontName
    }

    override fun equals(other: Any?): Boolean {
        if (other is Type) {
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