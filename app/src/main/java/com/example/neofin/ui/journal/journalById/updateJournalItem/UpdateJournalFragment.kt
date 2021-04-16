package com.example.neofin.ui.journal.journalById.updateJournalItem

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import com.example.neofin.R
import com.example.neofin.utils.toast


class UpdateJournalFragment : Fragment(R.layout.fragment_update_journal) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val toolbar = (activity as AppCompatActivity).supportActionBar
        toolbar?.setDisplayHomeAsUpEnabled(false)
        toolbar?.hide()

        val id = arguments?.getInt("updateId")
        val idFiltered = arguments?.getInt("filteredJournal")

        if (id != 0) {
            toast(requireContext(), "journal: $id")
        } else {
            toast(requireContext(), "journalFiltered: $idFiltered")
        }
    }



}