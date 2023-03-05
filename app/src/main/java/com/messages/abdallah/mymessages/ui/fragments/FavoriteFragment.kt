package com.messages.abdallah.mymessages.ui.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.messages.abdallah.mymessages.ViewModel.MsgsTypesViewModel
import com.messages.abdallah.mymessages.ViewModel.MsgsViewModel
import com.messages.abdallah.mymessages.ViewModel.MyViewModelFactory
import com.messages.abdallah.mymessages.ViewModel.ViewModelFactory
import com.messages.abdallah.mymessages.adapter.Msgs_Fav_Adapter
import com.messages.abdallah.mymessages.api.ApiService
import com.messages.abdallah.mymessages.databinding.FragmentFavoriteBinding
import com.messages.abdallah.mymessages.db.LocaleSource
import com.messages.abdallah.mymessages.models.FavoriteModel
import com.messages.abdallah.mymessages.repository.MsgsRepo
import com.messages.abdallah.mymessages.repository.MsgsTypesRepo
import com.messages.abdallah.mymessages.ui.MainActivity
import kotlinx.coroutines.launch

class FavoriteFragment : Fragment() {

    private var _binding: FragmentFavoriteBinding? = null
    private val binding get() = _binding!!

    private val msgfavadapter by lazy { Msgs_Fav_Adapter() }

    private val retrofitService = ApiService.provideRetrofitInstance()

    private val mainRepository by lazy { MsgsRepo(retrofitService, LocaleSource(requireContext())) }

    private val viewModel: MsgsViewModel by viewModels {
        ViewModelFactory(mainRepository)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentFavoriteBinding.inflate(inflater, container, false)
        (activity as MainActivity).fragment = 1
        setUpRv()
        adapterOnClick()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


    }

    private fun adapterOnClick() {

        msgfavadapter.onItemClick = {
            viewModel.viewModelScope.launch {
                viewModel.delete_fav(it)   // delete favorite item from db
                Toast.makeText(requireContext(),"item removed from favorites",Toast.LENGTH_SHORT).show()
                viewModel.update_fav(it.id,false) // update item state
                setUpRv()
            }

        }
    }

    @SuppressLint("SuspiciousIndentation")
    private fun setUpRv() = viewModel.viewModelScope.launch {

        viewModel.getFav()
            .observe(requireActivity()) { listTvShows ->
                //     Log.e("tessst",listTvShows.size.toString()+"  adapter")
                // msgfavadapter.stateRestorationPolicy= RecyclerView.Adapter.StateRestorationPolicy.ALLOW
                if (binding.rcMsgFav.adapter == null) {
                    msgfavadapter.msgs_fav_list = listTvShows
                    binding.rcMsgFav.layoutManager = LinearLayoutManager(requireActivity())
                    binding.rcMsgFav.adapter = msgfavadapter
                } else
                    // update adapter list
                    msgfavadapter.msgs_fav_list = listTvShows
                    msgfavadapter.notifyDataSetChanged()
            }

    }

}