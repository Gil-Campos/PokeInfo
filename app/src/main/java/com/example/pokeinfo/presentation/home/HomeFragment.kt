package com.example.pokeinfo.presentation.home

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pokeinfo.databinding.FragmentHomeBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch


@AndroidEntryPoint
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val viewModel: HomeViewModel by viewModels()

    private lateinit var pokemonAdapter: PokemonListAdapter

    private val handler = Handler(Looper.getMainLooper())
    private lateinit var job: Job

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initRecyclerView()
        observer()
        viewModel.getPokemons()
    }

    private fun observer() {
        getPokemons()
        showHideLoading()
    }


    private fun getPokemons() {
        lifecycleScope.launch {
            viewModel.pokemonList.collectLatest {
                if (!it.isNullOrEmpty()) {
                    pokemonAdapter.pokemons = it
                    if (it.size == 15) {
                        startBackgroundTask()
                    }
                }
            }
        }
    }

    private fun showHideLoading() {
        viewModel.isLoading.observe(viewLifecycleOwner) {
            if (it) {
                binding.progressBar.visibility = View.VISIBLE
            } else {
                binding.progressBar.visibility = View.GONE
            }
        }
    }

    private fun initRecyclerView() {
        binding.rvPokemonList.apply {
            pokemonAdapter = PokemonListAdapter()
            adapter = pokemonAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    private fun startBackgroundTask() {
        job = CoroutineScope(Dispatchers.IO).launch {
            while (isActive) {
                delay(30000) // 10 seconds
                handler.post {
                    viewModel.getPokemons()
                }
            }
        }
    }

//    @RequiresApi(Build.VERSION_CODES.O)
//    private suspend fun doBackgroundTask() {
//        delay(10000)
//        viewModel.getPokemons()
//        showNotification()
//    }
//
//    private fun showNotification() {
//        val intent = Intent(requireContext(), MainActivity::class.java).apply {
//            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
//        }
//
//        val pendingIntent =
//            PendingIntent.getActivity(requireContext(), 0, intent, PendingIntent.FLAG_IMMUTABLE)
//
//        val notification = Notification.Builder(requireContext(), CHANNEL_ID)
//            .setSmallIcon(R.drawable.ic_launcher_foreground)
//            .setContentTitle("New data arrive")
//            .setContentText("New data fetched from the poke api, check it out")
//            .setPriority(Notification.PRIORITY_MAX)
//            .setAutoCancel(true)
//            .setContentIntent(pendingIntent)
//
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            val channelName = "channel name"
//            val channelDescription = "channel description"
//            val channelImportance = NotificationManager.IMPORTANCE_HIGH
//
//            val channel = NotificationChannel(CHANNEL_ID, channelName, channelImportance).apply {
//                description = channelDescription
//            }
//
//            val notificationManager =
//                requireContext().getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
//            notificationManager.createNotificationChannel(channel)
//        }
//
//        with(NotificationManagerCompat.from(requireContext())) {
//            notify(NOTIFICATION_ID, notification.build())
//        }
//    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        job.cancel()
    }

    companion object {
        private const val NOTIFICATION_ID = 1
        private const val CHANNEL_ID = "channel_id"

    }
}