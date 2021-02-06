package dev.ronnie.allplayers.adapters

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import dev.ronnie.allplayers.R
import dev.ronnie.allplayers.databinding.AdapterItemBinding
import dev.ronnie.allplayers.models.Player

class PlayersAdapter(private val clicked: (String) -> Unit) :
    PagingDataAdapter<Player, PlayersAdapter.PlayersViewHolder>(
        PlayersDiffCallback()
    ) {

    private var duration: Long = 250
    private var onAttach = true

    override fun onBindViewHolder(holder: PlayersViewHolder, position: Int) {

        val data = getItem(position)

        holder.bind(data, position)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlayersViewHolder {

        return PlayersViewHolder(
            AdapterItemBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )

    }


    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                onAttach = false
                super.onScrollStateChanged(recyclerView, newState)
            }
        })
        super.onAttachedToRecyclerView(recyclerView)
    }

    private fun setAnimation(itemView: View, i: Int) {
        var i = i
        if (!onAttach) {
            i = -1
        }
        val isNotFirstItem = i == -1
        i++
        itemView.alpha = 0f
        val animatorSet = AnimatorSet()
        val animator = ObjectAnimator.ofFloat(itemView, "alpha", 0f, 0.5f, 1.0f)
        ObjectAnimator.ofFloat(itemView, "alpha", 0f).start()
        animator.startDelay = if (isNotFirstItem) duration / 2 else i * duration / 3
        animator.duration = 500
        animatorSet.play(animator)
        animator.start()
    }

    inner class PlayersViewHolder(
        private val binding: AdapterItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(data: Player?, position: Int) {

            binding.let {

                val name = it.root.context.getString(
                    R.string.name,
                    data?.first_name, data?.last_name
                )
                it.root.setOnClickListener {
                    clicked(name)
                }
                it.name.text = name
                it.position.text = it.root.context.getString(
                    R.string.adapter_item,
                    "Position", data?.position
                )
                it.team.text = it.root.context.getString(
                    R.string.adapter_item,
                    "Team", data?.team?.full_name
                )
                it.division.text = it.root.context.getString(
                    R.string.adapter_item,
                    "Division", data?.team?.division
                )

                setAnimation(it.root, position)
            }

        }
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