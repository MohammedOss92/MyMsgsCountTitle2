package com.messages.abdallah.mymessages.ui.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.RecyclerView
import com.messages.abdallah.mymessages.ViewModel.MsgsViewModel
import com.messages.abdallah.mymessages.ViewModel.ViewModelFactory
import com.messages.abdallah.mymessages.adapter.Msgs_Adapter
import com.messages.abdallah.mymessages.api.ApiService
import com.messages.abdallah.mymessages.databinding.FragmentSecondBinding
import com.messages.abdallah.mymessages.db.LocaleSource
import com.messages.abdallah.mymessages.models.FavoriteModel
import com.messages.abdallah.mymessages.models.MsgModelWithTitle
import com.messages.abdallah.mymessages.repository.MsgsRepo
import com.messages.abdallah.mymessages.ui.MainActivity
import kotlinx.coroutines.launch


class SecondFragment : Fragment() {

    private var _binding : FragmentSecondBinding?=null
    private val binding get() = _binding!!
    private var argsId = -1
    private var MsgTypes_name = ""

    lateinit var  msgsAdapter :Msgs_Adapter

    private val retrofitService = ApiService.provideRetrofitInstance()

    private val mainRepository by lazy {  MsgsRepo(retrofitService, LocaleSource(requireContext())) }

    private val viewModel: MsgsViewModel by viewModels{
        ViewModelFactory(mainRepository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        argsId = SecondFragmentArgs.fromBundle(requireArguments()).id
//        MsgTypes_name = SecondFragmentArgs.fromBundle(requireArguments()).msgType
        (activity as MainActivity).fragment = 2
        msgsAdapter = Msgs_Adapter()
        // (activity as MainActivity).id = argsId
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSecondBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //Toast.makeText(requireContext(), argsId.toString(), Toast.LENGTH_LONG).show()
        //Toast.makeText(requireContext(), MsgTypes_name, Toast.LENGTH_LONG).show()

        setUpRv()
        adapterOnClick()

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun adapterOnClick(){

        msgsAdapter.onItemClick = { it: MsgModelWithTitle, i: Int ->
            val fav= FavoriteModel(it.msgModel!!.id,it.msgModel!!.MessageName,it.typeTitle,it.msgModel!!.new_msgs,it.msgModel!!.ID_Type_id)
            // check if item is favorite or not
            if (it.msgModel!!.is_fav){
                viewModel.update_fav(it.msgModel!!.id,false) // update favorite item state
                viewModel.delete_fav(fav) //delete item from db
                Toast.makeText(requireContext(),"item removed from favorites",Toast.LENGTH_SHORT).show()
                setUpRv()
                msgsAdapter.notifyDataSetChanged()
            }else{
                viewModel.update_fav(it.msgModel!!.id,true)
                viewModel.add_fav(fav) // add item to db
                Toast.makeText(requireContext(),"item added to favorites",Toast.LENGTH_SHORT).show()
                setUpRv()
                msgsAdapter.notifyDataSetChanged()
            }

        }
    }

    private  fun setUpRv() = viewModel.viewModelScope.launch {

//        binding.rcMsgTypes.apply {
//            adapter = msgstypesAdapter
//            setHasFixedSize(true)
//        }


        viewModel.getMsgsFromRoom_by_id(argsId,requireContext()).observe(viewLifecycleOwner) { listShows ->
            //  msgsAdapter.stateRestorationPolicy=RecyclerView.Adapter.StateRestorationPolicy.ALLOW
            msgsAdapter.msgsModel = listShows
            binding.rcMsgs.adapter = msgsAdapter
            Log.e("tessst","enter111")

        }
    }
}