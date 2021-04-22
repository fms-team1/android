package com.example.neofin.ui.user.data

class StatusName (var backName: String?, var name: String) {

    override fun toString(): String {
        return name
    }

    override fun equals(other: Any?): Boolean {
        if (other is StatusName) {
            if (other.name == name && other.backName == backName) return true
        }
        return false
    }

    override fun hashCode(): Int {
        var result = backName.hashCode()
        result = 31 * result + name.hashCode()
        return result
    }

}