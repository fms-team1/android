package com.example.neofin.ui.filter.data


class Period(var period: String, var name: String) {

    override fun toString(): String {
        return name
    }

    override fun equals(other: Any?): Boolean {
        if (other is Period) {
            if (other.name == name && other.period == period) return true
        }
        return false
    }

    override fun hashCode(): Int {
        var result = period.hashCode()
        result = 31 * result + name.hashCode()
        return result
    }
}