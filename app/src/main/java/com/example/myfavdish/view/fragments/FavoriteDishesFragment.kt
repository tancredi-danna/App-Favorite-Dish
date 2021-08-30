package com.example.myfavdish.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.myfavdish.application.FavDishApplication
import com.example.myfavdish.databinding.FragmentFavoriteDishesBinding
import com.example.myfavdish.model.entities.FavDish
import com.example.myfavdish.view.activities.MainActivity
import com.example.myfavdish.view.adapters.FavDishAdapter
import com.example.myfavdish.viewmodel.FavDishViewModel
import com.example.myfavdish.viewmodel.FavDishViewModelFactory

class  FavoriteDishesFragment : Fragment() {


    private val mFavDishViewModel : FavDishViewModel by viewModels {
        FavDishViewModelFactory((requireActivity().application as FavDishApplication).repository)
    }
    private var _binding: FragmentFavoriteDishesBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        _binding = FragmentFavoriteDishesBinding.inflate(inflater, container, false)

        return _binding!!.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding?.rvFavoriteDish?.layoutManager = GridLayoutManager(requireActivity(), 2)
        val adapter = FavDishAdapter(this@FavoriteDishesFragment)

        _binding?.rvFavoriteDish?.adapter = adapter

        mFavDishViewModel.allFavoriteDishList.observe(viewLifecycleOwner){
            dishes ->
            dishes?.let {
                if(it.isNotEmpty()){
                    for(dish in it){

                        adapter.dishesList(it)

                        _binding?.rvFavoriteDish?.visibility = View.VISIBLE
                        _binding?.textDashboard?.visibility = View.GONE
                    }
                }else{
                    _binding?.rvFavoriteDish?.visibility = View.GONE
                    _binding?.textDashboard?.visibility = View.VISIBLE
                }
            }
        }
    }
    fun favoriteDishDetails(favDish: FavDish){
        findNavController().navigate(FavoriteDishesFragmentDirections.actionNavigationFavoriteDishesToNavigationDishDetails2(favDish))
        if (requireActivity() is MainActivity){
        (activity as MainActivity?)?.hideBottomNavigationView()
     }
    }

    override fun onResume() {
        super.onResume()
        if (requireActivity() is MainActivity){
            (activity as MainActivity?)?.showBottomNavigationView()
        }
    }


}