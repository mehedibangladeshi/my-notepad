package com.greytechlab.mynotepad.ui.base

import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.greytechlab.mynotepad.data.sharedprefs.SharedPreferencesEnum
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

abstract class BaseFragment : Fragment(), CoroutineScope {

    fun showToast(text: String) = Toast.makeText(requireContext(), text, Toast.LENGTH_SHORT).show()

    private lateinit var job: Job

    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.Main

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        job = Job()

    }

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }

    fun getFilterMode(): Int {
        return SharedPreferencesEnum.getInstance(requireContext())!!
            .getInt(SharedPreferencesEnum.Key.FILTER_MODE)

    }

    fun setFilterMode(filterMode: Int) {
        return SharedPreferencesEnum.getInstance(requireContext())!!
            .put(SharedPreferencesEnum.Key.FILTER_MODE, filterMode)
    }
}