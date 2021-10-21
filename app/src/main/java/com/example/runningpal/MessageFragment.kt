package com.example.runningpal

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.fragment_message.*


class MessageFragment : Fragment() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        var messageList = mutableListOf(
            Message("Jacek","Hej to ja co wczoraj robiłeś"),
            Message("Kasia","Idziemy o 16 na trening ? "),
            Message("Wladek","O 20 rzeźna ? ")
        )

        val adapter = MessageAdapter(messageList)
        rvMessageFragment.adapter = adapter
        rvMessageFragment.layoutManager = LinearLayoutManager(context)





    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_message, container, false)
    }


}
