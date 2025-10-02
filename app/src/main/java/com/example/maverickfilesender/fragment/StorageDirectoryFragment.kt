package com.example.maverickfilesender.fragment

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.os.StatFs
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.maverickfilesender.R
import com.example.maverickfilesender.activities.MainActivity
import com.example.maverickfilesender.constants.Constants
import com.example.maverickfilesender.databinding.FragmentStorageDirectoryBinding
import java.io.File
import java.text.DecimalFormat

class StorageDirectoryFragment : Fragment() {

    private var _binding: FragmentStorageDirectoryBinding? = null
    private val binding get() = _binding!!

    private var mainActivity: MainActivity? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mainActivity = context as? MainActivity
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentStorageDirectoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.R) {
            if (!Environment.isExternalStorageManager()) {
                val intent = Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION)
                val uri = Uri.fromParts("package", requireContext().packageName, null)
                intent.data = uri
                startActivity(intent)
            }
        }

        val files = ContextCompat.getExternalFilesDirs(requireContext(), null)

        var sdPath = ""
        if (files.size > 1) {
            sdPath = runCatching { getSDirectory(files[1].path) }.getOrDefault("")
        }

        val internalStoragePath = "/storage/emulated/0"
        ensureMaverickDirectoryExists(internalStoragePath)

        updateInternalStorageUi(internalStoragePath)

        if (sdPath.isNotEmpty()) {
            binding.llSDStorage.visibility = View.VISIBLE
            updateSdStorageUi(sdPath)
        } else {
            binding.llSDStorage.visibility = View.GONE
        }

        binding.llInternalStorage.setOnClickListener {
            navigateToDirectory(internalStoragePath)
        }

        binding.llSDStorage.setOnClickListener {
            if (sdPath.isNotEmpty()) {
                navigateToDirectory(sdPath)
            }
        }
    }

    private fun ensureMaverickDirectoryExists(basePath: String) {
        val directory = File("$basePath/Download/Maverick")
        if (!directory.exists()) {
            val created = directory.mkdirs()
            Log.d("StorageDirectoryFragment", "Maverick directory created: $created")
        }
    }

    private fun updateInternalStorageUi(path: String) {
        val total = getTotalSpace(path)
        val available = getAvailableSpace(path)
        val used = total - available

        if (total >= 1000) {
            val totalTb = total / 1024
            binding.tvIntenalUsed.text =
                if (used < 1000) formatSize(used, "GB") else formatSize(used / 1024f, "TB")
            binding.tvInternalTotalSize.text = formatSize(totalTb, "TB")
        } else {
            binding.tvIntenalUsed.text = formatSize(used, "GB")
            binding.tvInternalTotalSize.text = formatSize(total, "GB")
        }

        binding.progressInternalStorage.progress = ((used / total) * 100).toInt()
    }

    private fun updateSdStorageUi(path: String) {
        val total = getTotalSpace(path)
        val available = getAvailableSpace(path)
        val used = total - available

        if (total >= 1000) {
            val totalTb = total / 1024
            binding.tvSdUsed.text =
                if (used < 1000) formatSize(used, "GB") else formatSize(used / 1024f, "TB")
            binding.tvSDTotalSize.text = formatSize(totalTb, "TB")
        } else {
            binding.tvSdUsed.text = formatSize(used, "GB")
            binding.tvSDTotalSize.text = formatSize(total, "GB")
        }

        binding.progressSD.progress = ((used / total) * 100).toInt()
    }

    private fun navigateToDirectory(path: String) {
        val bundle = Bundle().apply {
            putString(Constants.BUNDLE_STORAGE_DIRECTORY, path)
        }

        val directoryFragment = FilesDirectoryFragment().apply {
            arguments = bundle
        }

        mainActivity?.supportFragmentManager?.beginTransaction()?.apply {
            addToBackStack(null)
            replace(R.id.holder_files_fragment, directoryFragment)
            commit()
        }
    }

    private fun formatSize(value: Float, unit: String): String {
        return "${roundToTwoDecimals(value)}$unit"
    }

    fun roundToTwoDecimals(value: Float): Float {
        val df = DecimalFormat("#.##")
        return df.format(value).toFloat()
    }

    fun getSDirectory(path: String): String {
        var sdPath = ""
        var separators = 0
        for (char in path) {
            if (char == '/') {
                separators++
            }
            if (separators == 3) {
                break
            }
            sdPath += char
        }
        return sdPath
    }

    fun getTotalSpace(path: String): Float {
        val stat = StatFs(File(path).path)
        val totalSpace = stat.blockCountLong * stat.blockSizeLong
        val spaceKb = totalSpace.toFloat() / 1024f
        val spaceMb = spaceKb / 1024f
        return spaceMb / 1024f
    }

    fun getAvailableSpace(path: String): Float {
        val stat = StatFs(File(path).path)
        val availableSpace = stat.availableBlocksLong * stat.blockSizeLong
        val spaceKb = availableSpace.toFloat() / 1024f
        val spaceMb = spaceKb / 1024f
        return spaceMb / 1024f
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}