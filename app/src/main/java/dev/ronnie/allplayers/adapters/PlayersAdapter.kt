package dev.ronnie.allplayers.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import dev.ronnie.allplayers.databinding.AdapterItemBinding
import dev.ronnie.allplayers.models.Data

class PlayersAdapter() :
    PagingDataAdapter<Data, PlayersAdapter.PlayersViewHolder>(
        OrdersDiffCallback()
    ) {

    override fun onBindViewHolder(holder: PlayersViewHolder, position: Int) {

        val data = getItem(position)

        holder.binding.let {
            it.name.text = data!!.first_name
            it.lName.text = data.last_name
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlayersViewHolder {

        return PlayersViewHolder(
            AdapterItemBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )

    }


    class PlayersViewHolder(
        val binding: AdapterItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {

    }

    private class OrdersDiffCallback : DiffUtil.ItemCallback<Data>() {
        override fun areItemsTheSame(oldItem: Data, newItem: Data): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Data, newItem: Data): Boolean {
            return oldItem == newItem
        }
    }

}