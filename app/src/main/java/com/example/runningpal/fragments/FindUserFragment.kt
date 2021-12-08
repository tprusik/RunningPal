package com.example.runningpal.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.runningpal.R
import com.example.runningpal.ui.adapters.ContactsAdapter
import com.example.runningpal.ui.viewmodels.RunnersViewModel
import kotlinx.android.synthetic.main.fragment_find_user.*
import org.koin.android.ext.android.get


class FindUserFragment : Fragment() {

    private lateinit var viewModel : RunnersViewModel
    private  lateinit var userAdapter : ContactsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_find_user, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = get()
        setupRecyclerView()


        viewModel.allRunners.observe(viewLifecycleOwner, Observer {
            userAdapter.submitList(it)
        })

    }

    private fun setupRecyclerView() = rvFindContact.apply {
        userAdapter = ContactsAdapter()
        adapter = userAdapter
        layoutManager = LinearLayoutManager(context)
    }


}