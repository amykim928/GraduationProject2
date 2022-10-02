package com.example.graduationproject

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment

class HomeFragment: Fragment(R.layout.fragment_home){
    /*private lateinit var binding: FragmentMainBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setOnClickListener()
    }

    private fun setOnClickListener() {
        val btnSequence = binding.container.children
        btnSequence.forEach { button ->
            button.setOnClickListener(this)
        }
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.button -> {
                Toast.makeText(this@HomeFragment, "click!", Toast.LENGTH_LONG).show()
            }

        }
    }

    companion object {
        private const val TAG = "MainFragment"
        fun instance() = HomeFragment()
    }*/
}
