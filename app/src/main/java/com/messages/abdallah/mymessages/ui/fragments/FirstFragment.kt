package com.messages.abdallah.mymessages.ui.fragments

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.viewModelScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.messages.abdallah.mymessages.R
import com.messages.abdallah.mymessages.ViewModel.MsgsTypesViewModel
import com.messages.abdallah.mymessages.ViewModel.MsgsViewModel
import com.messages.abdallah.mymessages.ViewModel.MyViewModelFactory
import com.messages.abdallah.mymessages.ViewModel.ViewModelFactory
import com.messages.abdallah.mymessages.adapter.MsgsTypes_Adapter
import com.messages.abdallah.mymessages.api.ApiService

import com.messages.abdallah.mymessages.databinding.FragmentFirstBinding
import com.messages.abdallah.mymessages.db.LocaleSource
import com.messages.abdallah.mymessages.repository.MsgsRepo
import com.messages.abdallah.mymessages.repository.MsgsTypesRepo
import com.messages.abdallah.mymessages.ui.MainActivity

import kotlinx.coroutines.launch
import java.util.*

class FirstFragment : Fragment() {
    private lateinit var _binding : FragmentFirstBinding
    private val binding get() = _binding!!

    var mprogressdaialog: Dialog? = null
    private val msgstypesAdapter by lazy {  MsgsTypes_Adapter() }

    private val retrofitService = ApiService.provideRetrofitInstance()
    private val mainRepository2 by lazy {  MsgsRepo(retrofitService, LocaleSource(requireContext())) }

    private val mainRepository by lazy {  MsgsTypesRepo(retrofitService, LocaleSource(requireContext())) }

    private val viewModel: MsgsTypesViewModel by viewModels{
        MyViewModelFactory(mainRepository,mainRepository2,requireActivity() as MainActivity)
    }


    private val viewModel2: MsgsViewModel by viewModels{
        ViewModelFactory(mainRepository2)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFirstBinding.inflate(inflater, container, false)
       Log.e("tessst","entred")
        (activity as MainActivity).fragment = 1

        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        setUpRv()
        adapterOnClick()
        menu_item()
    }

    private fun adapterOnClick(){
        //لاحظ الفانكشن انها بترمي الid
//        msgstypesAdapter.onItemClick = {id, MsgTypes ->
        msgstypesAdapter.onItemClick = {id ->
//            Toast.makeText(requireContext(), it.toString(), Toast.LENGTH_LONG).show()
//            val direction = FirstFragmentDirections.actionFirsFragmentToSecondFragment(id, MsgTypes)
            val direction = FirstFragmentDirections.actionFirsFragmentToSecondFragment(id)
            findNavController().navigate(direction)
        }
    }

    private fun setUpRv() = viewModel.viewModelScope.launch {



        viewModel.getPostsFromRoomWithCounts(requireContext() as MainActivity).observe(requireActivity()) { listTvShows ->
       //     Log.e("tessst",listTvShows.size.toString()+"  adapter")
//            msgstypesAdapter.stateRestorationPolicy= RecyclerView.Adapter.StateRestorationPolicy.ALLOW
//
//            if(binding.rcMsgTypes.adapter == null){
//                msgstypesAdapter.msgsTypesModel = listTvShows
//                binding.rcMsgTypes.layoutManager = LinearLayoutManager(requireContext())
//                binding.rcMsgTypes.adapter = msgstypesAdapter
//            }

            msgstypesAdapter.msgsTypesModel=listTvShows
            if(binding.rcMsgTypes.adapter == null) {
                binding.rcMsgTypes.layoutManager = LinearLayoutManager(requireContext())
                binding.rcMsgTypes.adapter = msgstypesAdapter
            }

        }

    }







    private fun menu_item() {
        // The usage of an interface lets you inject your own implementation
        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                // Add menu items here
                menuInflater.inflate(R.menu.first_frag_menu, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {

                when(menuItem.itemId){
                    R.id.action_refresh -> {
                        viewModel.refreshPosts(requireActivity() as MainActivity)
                    }

                }
                return true
            }

        },viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    fun showprogressdialog() {

        binding.progressBar.visibility = View.VISIBLE
        //  mprogressdaialog = Dialog(this)
        //  mprogressdaialog!!.setCancelable(false)
        //  mprogressdaialog!!.setContentView(R.layout.progress_dialog)

        //  mprogressdaialog!!.show()
    }

    fun hideprogressdialog() {
        Log.e("tesssst","entred")
        //  recreate()
        // mprogressdaialog!!.dismiss()
        binding.progressBar.visibility = View.GONE
//        recreate()

    }

    override fun onDestroy() {
        if (mprogressdaialog != null && mprogressdaialog!!.isShowing) mprogressdaialog!!.dismiss()
        super.onDestroy()
    }

    override fun onStop() {
        //  if (mprogressdaialog != null && mprogressdaialog!!.isShowing) mprogressdaialog!!.dismiss()
        super.onStop()
    }

}