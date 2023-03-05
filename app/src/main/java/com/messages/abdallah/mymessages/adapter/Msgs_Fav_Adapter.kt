package com.messages.abdallah.mymessages.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.messages.abdallah.mymessages.R
import com.messages.abdallah.mymessages.databinding.MsgsDesignBinding
import com.messages.abdallah.mymessages.databinding.MsgsFavDeBinding
import com.messages.abdallah.mymessages.models.FavoriteModel
import com.messages.abdallah.mymessages.models.MsgModelWithTitle

class Msgs_Fav_Adapter : RecyclerView.Adapter<Msgs_Fav_Adapter.MyViewHolder>() {
    var onItemClick: ((fav:FavoriteModel) -> Unit)? = null // pass favorite item on click

    inner class MyViewHolder(val binding: MsgsFavDeBinding) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.favBtn.setOnClickListener {

                onItemClick?.invoke(msgs_fav_list[adapterPosition])

            }
        }

    }
    private val diffCallback = object : DiffUtil.ItemCallback<FavoriteModel>(){
        override fun areItemsTheSame(oldItem: FavoriteModel, newItem: FavoriteModel): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: FavoriteModel, newItem: FavoriteModel): Boolean {
            return newItem == oldItem
        }

    }

    private val differ = AsyncListDiffer(this, diffCallback)
    var msgs_fav_list: List<FavoriteModel>
        get() = differ.currentList
        set(value) {
            differ.submitList(value)
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(MsgsFavDeBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: Msgs_Fav_Adapter.MyViewHolder, position: Int) {
        val current_msgsModel = msgs_fav_list[position]
        holder.binding.apply {
            tvTitleM.text=current_msgsModel.TypeTitle
            tvMsgM.text=current_msgsModel.MessageName

            newMsgM.setImageResource(R.drawable.new_msg)

            if (current_msgsModel.new_msgs == 0){

                newMsgM.setVisibility(View.INVISIBLE)
            }
            else {
                newMsgM.setVisibility(View.VISIBLE)
            }
        }
    }

    override fun getItemCount(): Int {
        return msgs_fav_list.size
    }
}