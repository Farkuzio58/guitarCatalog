/*
 * Autor: Alejandro Valle Rodríguez
 * Funcionalidad: Fragment principal del programa que ofrece un menú con 6 botones para ver guitarras de distintas formas.
 */

package com.farkuzio58.guitarcatalog.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.farkuzio58.guitarcatalog.R
import com.farkuzio58.guitarcatalog.databinding.FragmentMainBinding

class MainFragment : Fragment() {

    var _binding: FragmentMainBinding? = null
    val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMainBinding.inflate(inflater, container, false)

        binding.cvSt.setOnClickListener{
            val bundle = bundleOf("shape" to 0)
            findNavController().navigate(R.id.action_mainFragment_to_guitarListFragment, bundle)
        }
        binding.cvT.setOnClickListener{
            val bundle = bundleOf("shape" to 1)
            findNavController().navigate(R.id.action_mainFragment_to_guitarListFragment, bundle)
        }
        binding.cvDc.setOnClickListener{
            val bundle = bundleOf("shape" to 2)
            findNavController().navigate(R.id.action_mainFragment_to_guitarListFragment, bundle)
        }
        binding.cvSc.setOnClickListener{
            val bundle = bundleOf("shape" to 3)
            findNavController().navigate(R.id.action_mainFragment_to_guitarListFragment, bundle)
        }
        binding.cvOther.setOnClickListener{
            val bundle = bundleOf("shape" to 4)
            findNavController().navigate(R.id.action_mainFragment_to_guitarListFragment, bundle)
        }
        binding.cvAll.setOnClickListener{
            val bundle = bundleOf("shape" to 5)
            findNavController().navigate(R.id.action_mainFragment_to_guitarListFragment, bundle)
        }
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
        }
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(false)
    }

    override fun onResume() {
        super.onResume()
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(false)
    }

    override fun onPause() {
        super.onPause()
        (requireActivity() as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}