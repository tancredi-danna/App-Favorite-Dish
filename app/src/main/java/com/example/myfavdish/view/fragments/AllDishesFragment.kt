package com.example.myfavdish.view.fragments

import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.myfavdish.R
import com.example.myfavdish.application.FavDishApplication
import com.example.myfavdish.databinding.FragmentAllDishesBinding
import com.example.myfavdish.model.entities.FavDish
import com.example.myfavdish.view.activities.AddUpdateDishActivity
import com.example.myfavdish.view.activities.MainActivity
import com.example.myfavdish.view.adapters.FavDishAdapter
import com.example.myfavdish.viewmodel.FavDishViewModel
import com.example.myfavdish.viewmodel.FavDishViewModelFactory

class AllDishesFragment : Fragment() {

    private lateinit var mBinding: FragmentAllDishesBinding

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val mFavDishViewModel: FavDishViewModel by viewModels {
        FavDishViewModelFactory((requireActivity().application as FavDishApplication).repository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
      mBinding = FragmentAllDishesBinding.inflate(inflater, container, false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mBinding.rvDishesList.layoutManager = GridLayoutManager(requireActivity(), 2)
        val favDishAdapter = FavDishAdapter(this@AllDishesFragment)

        mBinding.rvDishesList.adapter = favDishAdapter


        mFavDishViewModel.allDishesList.observe(viewLifecycleOwner) {
            dishes ->
            dishes.let {
                if(it.isNotEmpty()){
                    mBinding.rvDishesList.visibility = View.VISIBLE
                    mBinding.tvNoDishesAddedYet.visibility = View.GONE

                    favDishAdapter.dishesList(it)

                }else{
                    mBinding.rvDishesList.visibility = View.GONE
                    mBinding.tvNoDishesAddedYet.visibility = View.VISIBLE
                }
            }
        }
    }
    fun dishDetails(favDish: FavDish){
        findNavController().navigate(AllDishesFragmentDirections.actionNavigationAllDishesToNavigationDishDetails(
            favDish
        ))
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
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_all_dishes, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.action_add_dish ->{
                startActivity( Intent(requireContext(), AddUpdateDishActivity::class.java))
                return true
            }

        }

        return super.onOptionsItemSelected(item)
    }

}