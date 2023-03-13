package com.messages.abdallah.mymessages.ui.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.viewModelScope
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.messages.abdallah.mymessages.R
import com.messages.abdallah.mymessages.ViewModel.MsgsTypesViewModel
import com.messages.abdallah.mymessages.ViewModel.MsgsViewModel
import com.messages.abdallah.mymessages.ViewModel.MyViewModelFactory
import com.messages.abdallah.mymessages.ViewModel.ViewModelFactory
import com.messages.abdallah.mymessages.adapter.Msgs_Adapter
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

    private lateinit var _binding: FragmentFavoriteBinding
    private val binding get() = _binding!!

    lateinit var msgfavadapter :Msgs_Fav_Adapter

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
        msgfavadapter = Msgs_Fav_Adapter(requireContext(),this /*,this*/ )

        menu_item()
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
                viewModel.update_fav(it.id,false) // update item state
                val result = mainRepository.deleteFav(it)   // delete favorite item from db
                Toast.makeText(requireContext(),"تم الحذف من المفضلة",Toast.LENGTH_SHORT).show()
                setUpRv()
            }

        }
    }

//    @SuppressLint("SuspiciousIndentation")
//    private fun setUpRv() = viewModel.viewModelScope.launch {
//
//        viewModel.getFav()
//            .observe(viewLifecycleOwner) { listTvShows ->
//                //     Log.e("tessst",listTvShows.size.toString()+"  adapter")
//                // msgfavadapter.stateRestorationPolicy= RecyclerView.Adapter.StateRestorationPolicy.ALLOW
////                msgfavadapter.msgs_fav_list = listTvShows
////                binding.rcMsgFav.adapter = msgfavadapter
//                msgfavadapter.stateRestorationPolicy= RecyclerView.Adapter.StateRestorationPolicy.ALLOW
////            msgsAdapter.msgsModel = listShows
////            binding.rcMsgs.adapter = msgsAdapter
//                msgfavadapter.notifyDataSetChanged()
//                if(binding.rcMsgFav.adapter == null){
//                    msgfavadapter.msgs_fav_list = listTvShows
//                    binding.rcMsgFav.layoutManager = LinearLayoutManager(requireContext())
//                    binding.rcMsgFav.adapter = msgfavadapter
//                    msgfavadapter.notifyDataSetChanged()
//                }
//            }
//
//    }


    @SuppressLint("SuspiciousIndentation")
    private  fun setUpRv() = viewModel.viewModelScope.launch {

//        binding.rcMsgTypes.apply {
//            adapter = msgstypesAdapter
//            setHasFixedSize(true)
//        }


        viewModel.getFav().observe(viewLifecycleOwner) { listShows ->
            //  msgsAdapter.stateRestorationPolicy=RecyclerView.Adapter.StateRestorationPolicy.ALLOW
            msgfavadapter.stateRestorationPolicy= RecyclerView.Adapter.StateRestorationPolicy.ALLOW
//            msgsAdapter.msgsModel = listShows
//            binding.rcMsgs.adapter = msgsAdapter
            msgfavadapter.msgs_fav_list = listShows
            if(binding.rcMsgFav.adapter == null){
                binding.rcMsgFav.layoutManager = LinearLayoutManager(requireContext())
                binding.rcMsgFav.adapter = msgfavadapter
            }else{
                msgfavadapter.notifyDataSetChanged()
            }
            Log.e("tessst","enter111")

        }
    }

    private fun menu_item() {
        // The usage of an interface lets you inject your own implementation

        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                // Add menu items here
                menuInflater.inflate(R.menu.favorite_frag_menu, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {

                when(menuItem.itemId){

                    R.id.action_zakrafah_fav ->{
                        val dir = FavoriteFragmentDirections.actionFavoriteFragmentToEditFragment2("")
                        NavHostFragment.findNavController(this@FavoriteFragment).navigate(dir)
                    }


                }
                return true
            }

        },viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

}