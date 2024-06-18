/*
 * Autor: Alejandro Valle Rodríguez
 * Funcionalidad: Fragment para mostrar el listado de guitarras, poder filtrarlas y ordenarlas, buscar guitarras y ver las favoritas.
 */

package com.farkuzio58.guitarcatalog.ui.list

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.farkuzio58.guitarcatalog.R
import com.farkuzio58.guitarcatalog.data.Guitar
import com.farkuzio58.guitarcatalog.databinding.FragmentGuitarListBinding
import com.farkuzio58.guitarcatalog.ui.adapter.GuitarAdapter
import com.google.android.material.slider.RangeSlider

class GuitarListFragment : Fragment() {
    var _binding: FragmentGuitarListBinding? = null
    val binding get() = _binding!!
    val viewmodel: GuitarListViewModel by viewModels()
    private lateinit var guitarAdapter: GuitarAdapter
    private var loadFavourites = false
    private var forma: String? = null
    private var brand: String? = null
    private var tremolo: String? = null
    private var strings: String? = null
    private var price: String? = null
    private var isSearch = false
    private var canConfigure = 0
    private var sortConfig = 1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentGuitarListBinding.inflate(inflater, container, false)
        val ip = context?.getSharedPreferences("guitarCatalog", Context.MODE_PRIVATE)
            ?.getString("serverIp", "192.168.1.58")
        val shapes = listOf(
            "Stratocaster",
            "Telecaster",
            "Double Cut",
            "Single Cut",
            "Otras Formas",
            "Todas las Guitarras"
        )
        val sortModes = listOf(getString(R.string.sortBy), getString(R.string.brandAsc), getString(R.string.brandDesc), getString(R.string.priceAsc), getString(R.string.priceDesc))

        val sortAdapter =
            ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, sortModes)
        sortAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerOrdenar.adapter = sortAdapter

        val marcasAdapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            listOf(getString(R.string.brand))
        )
        marcasAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerMarcas.adapter = marcasAdapter

        val fav = arguments?.getString("fav")
        val search = arguments?.getString("search")
        val shape = arguments?.getInt("shape")

        if (fav == "si") {
            activity?.title = getString(R.string.favGuitars)
            loadFavourites = true
            viewmodel.load(ip!!, loadFavourites, null, null, null, null, null, true, null)
        } else if (shape != null && shape != 5) {
            forma = shapes[shape]
            if (shape == 4)
                activity?.title = getString(R.string.other)
            else {
                val lanCode = context?.getSharedPreferences("guitarCatalog", Context.MODE_PRIVATE)
                    ?.getString("lan", "es")
                when(lanCode){
                    "es" -> activity?.title = getString(R.string.guitars) + " " + forma.toString()
                    "en" -> activity?.title = forma.toString() + " " + getString(R.string.guitars)
                    "de" -> activity?.title = forma.toString() + "-" + getString(R.string.guitars)
                }
            }

            viewmodel.load(ip!!, loadFavourites, forma, null, null, null, null, true, null)
        } else {
            activity?.title = getString(R.string.all)
            viewmodel.load(ip!!, loadFavourites, null, null, null, null, null, true, null)
        }

        if (search == "si") {
            isSearch = true
            binding.lottieLoading.visibility = View.GONE
            binding.lnlSearch.visibility = View.VISIBLE
            binding.lnlFilters.visibility = View.GONE
            requireActivity().title = getString(R.string.guitarSearch)
            val params = binding.recyclerView.layoutParams as ViewGroup.MarginLayoutParams
            params.topMargin = (75 * resources.displayMetrics.density + 0.5f).toInt()
            binding.recyclerView.layoutParams = params

            binding.tieSearch.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                }

                override fun afterTextChanged(s: Editable?) {
                    viewmodel.load(ip!!, false, null, null, null, null, null, false, s.toString())
                }
            })
        } else {

            viewmodel.getBrands(ip!!)

            viewmodel.brands.observe(viewLifecycleOwner) {
                it?.let {
                    val brandsList = it.toMutableList()
                    brandsList.add(0, getString(R.string.brand))
                    val marcasAdapter = ArrayAdapter(
                        requireContext(),
                        android.R.layout.simple_spinner_item,
                        brandsList
                    )
                    marcasAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    binding.spinnerMarcas.adapter = marcasAdapter
                }
            }

            val slider = binding.sliderRangoPrecio
            slider.valueFrom = 0f
            slider.valueTo = 1000f
            binding.sliderRangoPrecio.stepSize = 1f
            binding.sliderRangoPrecio.setValues(0f, 1000f)

            viewmodel.prices.observe(viewLifecycleOwner) {
                it?.let {
                    var newMin = (it[0])
                    var newMax = (it[1])
                    if (newMin == newMax) {
                        newMin--
                        newMax++
                    }

                    slider.valueFrom = newMin
                    slider.valueTo = newMax
                    binding.sliderRangoPrecio.stepSize = 1f
                    binding.sliderRangoPrecio.setValues(newMin, newMax)
                } ?: run {
                }
            }

            val tremolos = listOf(getString(R.string.tremolo), getString(R.string.yes), "No")
            val cuerdas = listOf(getString(R.string.strings), "6", "7", "8", "9")

            val tremolosAdapter =
                ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, tremolos)
            tremolosAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.spinnerTremolo.adapter = tremolosAdapter

            val cuerdasAdapter =
                ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, cuerdas)
            cuerdasAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.spinnerCuerdas.adapter = cuerdasAdapter

            binding.spinnerOrdenar.onItemSelectedListener =
                object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(
                        parent: AdapterView<*>?,
                        view: View?,
                        position: Int,
                        id: Long
                    ) {
                        if (binding.spinnerOrdenar.selectedItemPosition != 0) {
                            when (binding.spinnerOrdenar.selectedItemPosition) {
                                1 -> {
                                    guitarAdapter.sortByBrand(binding.recyclerView)
                                    sortConfig = 1
                                }
                                2 -> {
                                    guitarAdapter.sortByBrandDes(binding.recyclerView)
                                    sortConfig = 2
                                }
                                3 -> {
                                    guitarAdapter.sortByPrice(binding.recyclerView)
                                    sortConfig = 3
                                }
                                4 -> {
                                    guitarAdapter.sortByPriceDes(binding.recyclerView)
                                    sortConfig = 4
                                }
                            }
                        }
                    }
                    override fun onNothingSelected(parent: AdapterView<*>) {
                    }
                }

            binding.spinnerMarcas.onItemSelectedListener =
                object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(
                        parent: AdapterView<*>?,
                        view: View?,
                        position: Int,
                        id: Long
                    ) {
                        if (canConfigure > 3) {
                            brand = if (position != 0)
                                parent!!.getItemAtPosition(position).toString()
                            else
                                null
                            viewmodel.load(
                                ip!!,
                                loadFavourites,
                                forma,
                                brand,
                                tremolo,
                                strings,
                                price,
                                false,
                                null
                            )
                            //sort()
                        } else
                            canConfigure++
                    }

                    override fun onNothingSelected(parent: AdapterView<*>) {
                    }
                }

            binding.spinnerTremolo.onItemSelectedListener =
                object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(
                        parent: AdapterView<*>?,
                        view: View?,
                        position: Int,
                        id: Long
                    ) {
                        if (canConfigure > 3) {
                            tremolo = if (position != 0)
                                parent!!.getItemAtPosition(position).toString()
                            else
                                null
                            viewmodel.load(
                                ip!!,
                                loadFavourites,
                                forma,
                                brand,
                                tremolo,
                                strings,
                                price,
                                false, null
                            )
                            //sort()
                        } else
                            canConfigure++
                    }

                    override fun onNothingSelected(parent: AdapterView<*>) {
                    }
                }

            binding.spinnerCuerdas.onItemSelectedListener =
                object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(
                        parent: AdapterView<*>?,
                        view: View?,
                        position: Int,
                        id: Long
                    ) {
                        if (canConfigure > 3) {
                            strings = if (position != 0)
                                parent!!.getItemAtPosition(position).toString()
                            else
                                null
                            viewmodel.load(
                                ip!!,
                                loadFavourites,
                                forma,
                                brand,
                                tremolo,
                                strings,
                                price,
                                false, null
                            )
                            //sort()
                        } else
                            canConfigure++
                    }

                    override fun onNothingSelected(parent: AdapterView<*>) {
                    }
                }

            binding.sliderRangoPrecio.addOnSliderTouchListener(object :
                RangeSlider.OnSliderTouchListener {
                override fun onStartTrackingTouch(slider: RangeSlider) {
                }

                override fun onStopTrackingTouch(slider: RangeSlider) {
                    val values = slider.values
                    val minValue = values[0]
                    val maxValue = values[1]
                    price = "$minValue AND $maxValue"
                    viewmodel.load(
                        ip!!,
                        loadFavourites,
                        forma,
                        brand,
                        tremolo,
                        strings,
                        price,
                        false, null
                    )
                    //sort()
                }
            })
        }
        binding.recyclerView
        return binding!!.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpRecyclerView()

        viewmodel.allGuitar?.observe(viewLifecycleOwner, Observer{
            if(it.isEmpty()){
                showNoDataError()
            }
            else{
                guitarAdapter.submitList(it)
                success()
            }
        })
    }

    fun showNoDataError(){
        binding.lottieLoading.visibility = View.GONE
        if(viewmodel.reachable){
            binding.lottieNoConnection.visibility = View.INVISIBLE
            Log.e("-----------", loadFavourites.toString())
            if(loadFavourites)
                binding.tvInfo.text = getString(R.string.noFavsInfo)
            else
                binding.tvInfo.text = ""
        }
        else{
            binding.lottieEmpty.visibility = View.GONE
        }

        binding.recyclerView.visibility = View.GONE
        binding.cvNoData.visibility = View.VISIBLE
    }

    fun success(){
        binding.lottieLoading.visibility = View.GONE
        binding.recyclerView.visibility = View.VISIBLE
        binding.cvNoData.visibility = View.GONE
    }

    fun setUpRecyclerView(){
        guitarAdapter = GuitarAdapter(requireContext()){ guitar:Guitar ->
            val bundle = bundleOf("id" to guitar.id)
            findNavController().navigate(R.id.action_guitarListFragment_to_guitarDetailFragment, bundle)
        }
        with(binding.recyclerView){
            layoutManager = GridLayoutManager(requireContext(), 2)
            adapter = guitarAdapter
        }
    }

    fun sort(){
        when(sortConfig){
            1 -> guitarAdapter.sortByBrand(binding.recyclerView)
            2 -> guitarAdapter.sortByBrandDes(binding.recyclerView)
            3 -> guitarAdapter.sortByPrice(binding.recyclerView)
            4 -> guitarAdapter.sortByPriceDes(binding.recyclerView)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}