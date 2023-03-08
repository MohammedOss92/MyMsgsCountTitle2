package com.messages.abdallah.mymessages.ui.fragments

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.messages.abdallah.mymessages.R
import com.messages.abdallah.mymessages.databinding.FragmentSplashBinding


class SplashFragment : Fragment() {

    private lateinit var _binding : FragmentSplashBinding
    private val binding get() = _binding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentSplashBinding.inflate(inflater, container, false)

        Handler(Looper.myLooper()!!).postDelayed({
//            val direction = SplashFragmentDirections.actionSplashFragmentToFirsFragment()
//            findNavController().navigate(direction)
            findNavController()
                .navigate(R.id.action_splashFragment_to_firsFragment,
                    null,
                    NavOptions.Builder()
                        .setPopUpTo(R.id.splashFragment,
                            true).build()
                )

        },5000)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }


}