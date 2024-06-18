/*
 * Autor: Alejandro Valle Rodríguez
 * Funcionalidad: Fragment para mostrar los detalles de cada guitarra.
 */

package com.farkuzio58.guitarcatalog.ui.detail

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.farkuzio58.guitarcatalog.ImageLoader
import com.farkuzio58.guitarcatalog.R
import com.farkuzio58.guitarcatalog.data.Guitar
import com.farkuzio58.guitarcatalog.databinding.FragmentGuitarDetailBinding

class GuitarDetailFragment : Fragment() {
    var _binding: FragmentGuitarDetailBinding? = null
    val binding get() = _binding!!
    val viewmodel: GuitarDetailViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentGuitarDetailBinding.inflate(inflater)
        return binding!!.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val id = arguments?.getInt("id")
        val ip = context?.getSharedPreferences("guitarCatalog", Context.MODE_PRIVATE)?.getString("serverIp", "192.168.1.58")

        var guitar:Guitar? = viewmodel.getGuitar(ip!!, id!!)

        if(guitar == null){
            binding.cvNoData.visibility = View.VISIBLE
            binding.lnlData.visibility = View.GONE
            binding.imgFav.visibility = View.GONE
            binding.txMarcaDetail.visibility = View.GONE
            binding.txModeloDetail.visibility = View.GONE
            activity?.title = getString(R.string.detailError)
        } else{
            activity?.title = "${guitar!!.marca} ${guitar!!.modelo}"
            binding.txMarcaDetail.text = guitar!!.marca
            binding.txModeloDetail.text = guitar!!.modelo

            if(guitar!!.rotacion != 0f){
                val imageLoader = ImageLoader(requireContext(), binding.imageView, guitar.rotacion)
                imageLoader.load(guitar.urlImagen)
            } else{
                val imageLoader = ImageLoader(requireContext(), binding.imageView, guitar.rotacion)
                imageLoader.load(guitar.urlImagen)
            }

            binding.tvId.text = binding.tvId.text.toString() + " " + guitar!!.idExterno
            binding.tvForma.text = binding.tvForma.text.toString() + " " + guitar!!.forma
            binding.tvCuerpo.text = binding.tvCuerpo.text.toString() + " " + guitar!!.madera_cuerpo
            binding.tvMastil.text = binding.tvMastil.text.toString() + " " + guitar!!.madera_mastil
            binding.tvDiapason.text = binding.tvDiapason.text.toString() + " " + guitar!!.madera_diapason
            binding.tvConfiguracion.text = binding.tvConfiguracion.text.toString() + " " + guitar!!.configuracion
            binding.tvPastillas.text = binding.tvPastillas.text.toString() + " " + guitar!!.pastillas
            binding.tvTrastes.text = binding.tvTrastes.text.toString() + " " + guitar!!.trastes
            binding.tvCuerdas.text = binding.tvCuerdas.text.toString() + " " + guitar!!.nCuerdas
            binding.tvTremolo.text = binding.tvTremolo.text.toString() + if (guitar!!.tremolo) " Si" else " No"
            binding.tvPrecio.text = binding.tvPrecio.text.toString() + " " + guitar!!.precio + "€"
            binding.floatingActionButton.setOnClickListener{
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(guitar!!.url))
                startActivity(intent)
            }
            if (viewmodel.isFavourite(guitar!!.id))
                binding.imgFav.setImageResource(R.drawable.fav_filled)
            binding.imgFav.setOnClickListener{
                if (viewmodel.isFavourite(guitar!!.id)) {
                    viewmodel.deleteFavourite(guitar!!.id)
                    binding.imgFav.setImageResource(R.drawable.fav_border)
                }
                else{
                    viewmodel.addFavourite(guitar!!.id)
                    binding.imgFav.setImageResource(R.drawable.fav_filled)
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}