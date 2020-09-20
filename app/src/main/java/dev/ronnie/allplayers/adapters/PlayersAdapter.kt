package dev.ronnie.allplayers.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import dev.ronnie.allplayers.R
import dev.ronnie.allplayers.databinding.AdapterItemBinding
import dev.ronnie.allplayers.models.Player

class PlayersAdapter(val clicked: (String) -> Unit) :
    PagingDataAdapter<Player, PlayersAdapter.PlayersViewHolder>(
        PlayersDiffCallback()
    ) {

    override fun onBindViewHolder(holder: PlayersViewHolder, position: Int) {

        val data = getItem(position)!!

        holder.binding.let {

            val name = it.root.context.getString(
                R.string.name,
                data.first_name, data.last_name
            )
            it.root.setOnClickListener {
                clicked(name)
            }
            it.name.text = name
            it.position.text = it.root.context.getString(
                R.string.adapter_item,
                "Position", data.position
            )
            it.team.text = it.root.context.getString(
                R.string.adapter_item,
                "Team", data.team.full_name
            )
            it.division.text = it.root.context.getString(
                R.string.adapter_item,
                "Division", data.team.division
            )

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

    private class PlayersDiffCallback : DiffUtil.ItemCallback<Player>() {
        override fun areItemsTheSame(oldItem: Player, newItem: Player): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Player, newItem: Player): Boolean {
            return oldItem == newItem
        }
    }

}