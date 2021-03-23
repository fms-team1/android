package com.example.neofin.ui.filter.data


class AgentIdName(var id: Int, var name: String) {

    override fun toString(): String {
        return name
    }

    override fun equals(other: Any?): Boolean {
        if (other is AgentIdName) {
            if (other.name == name && other.id == id) return true
        }
        return false
    }

    override fun hashCode(): Int {
        var result = id
        result = 31 * result + name.hashCode()
        return result
    }
}