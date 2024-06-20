package com.example.pokeinfo.presentation.home

import android.Manifest
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pokeinfo.R
import com.example.pokeinfo.databinding.FragmentHomeBinding
import com.example.pokeinfo.presentation.MainActivity
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlin.system.exitProcess

@RequiresApi(Build.VERSION_CODES.O)
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

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        askNotificationPermission()
    }

    private fun initData() {
        initRecyclerView()
        observer()
        viewModel.getPokemons()
    }


    private fun observer() {
        getPokemons()
        showHideLoading()
        checkForErrors()
    }

    private fun getPokemons() {
        lifecycleScope.launch {
            viewModel.pokemonList.collectLatest {
                if (!it.isNullOrEmpty()) {
                    pokemonAdapter.pokemons = it
                    if (it.size == 15) {
                        startBackgroundTask()
                    }
                    showNotification()
                }
            }
        }
    }

    private fun checkForErrors() {
        viewModel.errorIsActive.observe(viewLifecycleOwner) {
            if (it) {
                MaterialAlertDialogBuilder(requireContext())
                    .setTitle("Ups Something happen...")
                    .setCancelable(false)
                    .setMessage("Close and try to open the app again, we have fetching and storing the data.")
                    .setNegativeButton("Close") { dialog, _ ->
                        dialog.dismiss()
                        (requireActivity() as MainActivity).finish()
                        exitProcess(0)
                    }
                    .show()
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

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            initData()
        } else {
            showInContextUI()
        }
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun askNotificationPermission() {
        when {
            ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED -> {
                initData()
            }

            shouldShowRequestPermissionRationale(Manifest.permission.POST_NOTIFICATIONS) -> {
                showInContextUI()
            }

            else -> {
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }

    private fun showInContextUI() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Notification Permission")
            .setMessage("You need to be aware of what is happening in the background.")
            .setNegativeButton("Denied") { dialog, which ->
                dialog.dismiss()
            }
            .setPositiveButton("Grant") { _, _ ->
                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                val uri = Uri.fromParts("package", requireContext().packageName, null)
                intent.setData(uri)
                startActivity(intent)
            }
            .show()
    }

    private fun showNotification() {
        val intent =
            Intent(requireContext(), (requireActivity() as MainActivity)::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }

        val pendingIntent =
            PendingIntent.getActivity(requireContext(), 0, intent, PendingIntent.FLAG_IMMUTABLE)

        val notification = Notification.Builder(requireContext(), CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle("New data arrive")
            .setContentText("New data fetched from the poke api, check it out")
            .setPriority(Notification.PRIORITY_MAX)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channelName = "channel name"
            val channelDescription = "channel description"
            val channelImportance = NotificationManager.IMPORTANCE_HIGH

            val channel = NotificationChannel(CHANNEL_ID, channelName, channelImportance).apply {
                description = channelDescription
            }

            val notificationManager =
                requireContext().getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }

        with(NotificationManagerCompat.from(requireContext())) {
            if (ActivityCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                return
            }
            notify(NOTIFICATION_ID, notification.build())
        }
    }


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