package dev.ronnie.allplayers.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import dev.ronnie.allplayers.R
import kotlinx.android.synthetic.main.network_state_item.view.*


class PlayersLoadingStateAdapter(private val retry: () -> Unit) :
    LoadStateAdapter<PlayersLoadingStateAdapter.LoadStateViewHolder>() {


    override fun onBindViewHolder(holder: LoadStateViewHolder, loadState: LoadState) {

        val progress = holder.itemView.progress_bar_item
        val retryBtn = holder.itemView.rety_btn
        val txtErrorMessage = holder.itemView.error_msg_item

        if (loadState is LoadState.Loading) {
            progress.visibility =
                View.VISIBLE;
            txtErrorMessage.visibility = View.GONE
            retryBtn.visibility = View.GONE

        } else {

            progress.visibility =
                View.GONE
        }

        if (loadState is LoadState.Error) {
            txtErrorMessage.visibility = View.VISIBLE
            retryBtn.visibility = View.VISIBLE
            txtErrorMessage.text = loadState.error.localizedMessage
        }

        if (loadState is LoadState.NotLoading && loadState.endOfPaginationReached) {
            txtErrorMessage.visibility = View.VISIBLE
            txtErrorMessage.text = "You have reached the end"
        }

        retryBtn.setOnClickListener {
            retry.invoke()
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState): LoadStateViewHolder {
        return LoadStateViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.network_state_item, parent, false)
        )
    }

    class LoadStateViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {

    }
}