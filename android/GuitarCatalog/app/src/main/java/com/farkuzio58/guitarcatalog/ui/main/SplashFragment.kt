/*
 * Autor: Alejandro Valle Rodríguez
 * Funcionalidad: Fragment para mostrar una foto al inicio de la App.
 */

package com.farkuzio58.guitarcatalog.ui.main

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.farkuzio58.guitarcatalog.R

private const val WAIT_TIME: Long = 700

class SplashFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_splash, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navigateToNextFragmentAfterDelay()
    }

    private fun navigateToNextFragmentAfterDelay() {
        val handler = Handler(Looper.myLooper()!!)
        handler.postDelayed({
            findNavController().navigate(R.id.action_splashFragment_to_mainFragment)
        }, WAIT_TIME)
    }
}