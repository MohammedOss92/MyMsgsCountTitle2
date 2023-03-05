package com.messages.abdallah.mymessages.adapter

import android.annotation.SuppressLint
import android.content.ClipData
import android.content.Context
import android.os.Build
import android.text.ClipboardManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.messages.abdallah.mymessages.R
import com.messages.abdallah.mymessages.Utils
import com.messages.abdallah.mymessages.databinding.MsgsDesignBinding
import com.messages.abdallah.mymessages.models.MsgModelWithTitle

class Msgs_Adapter(val con:Context) : RecyclerView.Adapter<Msgs_Adapter.MyViewHolder>() {

    var onItemClick: ((item:MsgModelWithTitle,position:Int) -> Unit)? = null

    @SuppressLint("NotifyDataSetChanged")
    inner class MyViewHolder(val binding: MsgsDesignBinding) : RecyclerView.ViewHolder(binding.root) {


        init {
            Log.e("tessst",msgsModel[0].msgModel!!.is_fav.toString())
            binding.favBtn.setOnClickListener {

                onItemClick?.invoke(msgsModel[adapterPosition],adapterPosition)
            }

            binding.moreBtn.setOnClickListener{popupMenus(it)}
        }

        private fun popupMenus(view: View) {

            val current_msgsModel = msgsModel[position]
            val popupMenu = PopupMenu(con,view)
            popupMenu.inflate(R.menu.menu_msg)
            popupMenu.setOnMenuItemClickListener {
                when(it.itemId){
                    R.id.share ->{
                        val v = LayoutInflater.from(con).inflate(R.layout.msgs_design,null)
                        val tvMsg = v.findViewById<TextView>(R.id.tvMsg_m)

                        Utils.IntenteShare(con, "مسجاتي", "مسجاتي",binding.tvMsgM.text.toString() )
                        Toast.makeText(con, "share", Toast.LENGTH_SHORT).show()
                        true
                    }
                    R.id.copy ->{
                        val v = LayoutInflater.from(con).inflate(R.layout.msgs_design,null)
                        val tvMsg = v.findViewById<TextView>(R.id.tvMsg_m)

//                        val stringYouExtracted: String = tvMsg.getText().toString()
                        val stringYouExtracted: String = binding.tvMsgM.text.toString()

                        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
                            val clipboard =
                                con.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                            clipboard.text = stringYouExtracted
                        } else {
                            val clipboard =
                                con.getSystemService(Context.CLIPBOARD_SERVICE) as android.content.ClipboardManager
                            val clip = ClipData
                                .newPlainText(stringYouExtracted, stringYouExtracted)
                            clipboard.setPrimaryClip(clip)
                        }
                        Toast.makeText(con, "تم نسخ النص", Toast.LENGTH_SHORT).show()
                        Toast.makeText(con, "copy", Toast.LENGTH_SHORT).show()
                        true
                    }
                    R.id.edit ->{
                        Toast.makeText(con, "edit", Toast.LENGTH_SHORT).show()
                        true
                    }

                    else -> true
                }
            }
            popupMenu.show()
        }
    }

    private val diffCallback = object :DiffUtil.ItemCallback<MsgModelWithTitle>(){
        override fun areItemsTheSame(oldItem: MsgModelWithTitle, newItem: MsgModelWithTitle): Boolean {
            return oldItem.msgModel?.id == newItem.msgModel?.id
        }

        override fun areContentsTheSame(oldItem: MsgModelWithTitle, newItem: MsgModelWithTitle): Boolean {
            return newItem == oldItem
        }

    }

    private val differ = AsyncListDiffer(this, diffCallback)
    var msgsModel: List<MsgModelWithTitle>
        get() = differ.currentList
        set(value) {
            differ.submitList(value)
        }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(MsgsDesignBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        Log.e("tessst","notifyyyy")
        val current_msgsModel = msgsModel[position]
        holder.binding.apply {
            tvTitleM.text=current_msgsModel.typeTitle.toString()
            tvMsgM.text=current_msgsModel.msgModel?.MessageName
            newMsgM.setImageResource(R.drawable.new_msg)

            if (current_msgsModel.msgModel?.new_msgs == 0){

                newMsgM.setVisibility(View.INVISIBLE)
            }
            else {
                newMsgM.setVisibility(View.VISIBLE)
            }

            // check if the item is favorite or not
            if (current_msgsModel.msgModel!!.is_fav){
                favBtn.setImageResource(R.drawable.baseline_favorite_true)

            }else{
                favBtn.setImageResource(R.drawable.baseline_favorite_border_false)

            }
        }
    }

    override fun getItemCount(): Int {
        return msgsModel.size
    }
}