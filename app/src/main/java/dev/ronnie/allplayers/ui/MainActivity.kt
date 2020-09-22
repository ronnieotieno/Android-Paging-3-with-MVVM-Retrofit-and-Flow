package dev.ronnie.allplayers.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import dev.ronnie.allplayers.R
import dev.ronnie.allplayers.adapters.PlayersAdapter
import dev.ronnie.allplayers.adapters.PlayersLoadingStateAdapter
import dev.ronnie.allplayers.databinding.ActivityMainBinding
import dev.ronnie.allplayers.utils.InjectorUtils
import dev.ronnie.allplayers.utils.RecyclerViewItemDecoration
import dev.ronnie.allplayers.viewmodels.MainViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private val viewModel: MainViewModel by viewModels {
        InjectorUtils.provideViewModelFactory()
    }
    lateinit var binding: ActivityMainBinding
    private val adapter =
        PlayersAdapter { name: String -> snackBarClickedPlayer(name) }

    private var searchJob: Job? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        setUpAdapter()
        startSearchJob()
        binding.swipeRefreshLayout.setOnRefreshListener {
            adapter.refresh()
        }

    }

    private fun startSearchJob() {

        searchJob?.cancel()
        searchJob = lifecycleScope.launch {
            viewModel.searchPlayers()
                .collectLatest {
                    adapter.submitData(it)
                }
        }
    }

    private fun snackBarClickedPlayer(name: String) {
        val parentLayout = findViewById<View>(android.R.id.content)
        Snackbar.make(parentLayout, name, Snackbar.LENGTH_LONG)
            .show()
    }

    private fun setUpAdapter() {

        binding.allProductRecyclerView.apply {
            this.layoutManager = LinearLayoutManager(this@MainActivity)
            this.setHasFixedSize(true)
            this.addItemDecoration(RecyclerViewItemDecoration())
        }
        binding.allProductRecyclerView.adapter = adapter.withLoadStateFooter(
            footer = PlayersLoadingStateAdapter()
        )

        adapter.addLoadStateListener { loadState ->

            if (loadState.refresh is LoadState.Loading && adapter.snapshot().isEmpty()) {
                binding.progressBarPopular.visibility = View.VISIBLE

            } else {
                binding.swipeRefreshLayout.isRefreshing = false
                binding.progressBarPopular.visibility = View.GONE

                val error = when {
                    loadState.prepend is LoadState.Error -> loadState.prepend as LoadState.Error
                    loadState.append is LoadState.Error -> loadState.append as LoadState.Error
                    loadState.refresh is LoadState.Error -> loadState.refresh as LoadState.Error

                    else -> null
                }
                error?.let {
                    Toast.makeText(this, it.error.localizedMessage, Toast.LENGTH_SHORT).show()

                }

            }
        }

    }
}
