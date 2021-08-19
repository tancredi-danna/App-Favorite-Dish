package com.example.myfavdish.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.myfavdish.R
import com.example.myfavdish.application.FavDishApplication
import com.example.myfavdish.databinding.FragmentDishDetailsBinding
import com.example.myfavdish.viewmodel.FavDishViewModel
import com.example.myfavdish.viewmodel.FavDishViewModelFactory
import java.io.IOException
import java.util.*


class DishDetailsFragment : Fragment() {
    // TODO: Rename and change types of parameters

    private var mBinding: FragmentDishDetailsBinding? = null
    private val mFavDishViewModel: FavDishViewModel by viewModels {
        FavDishViewModelFactory(((requireActivity().application) as FavDishApplication).repository)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = FragmentDishDetailsBinding.inflate(inflater, container ,false)
        // Inflate the layout for this fragment
        return mBinding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val args: DishDetailsFragmentArgs by navArgs()
        args.let {
            try {
                Glide.with(requireActivity())
                    .load(it.dishDetails.image)
                    .centerCrop().into(mBinding!!.ivDishImage)
            }catch (e : IOException){
                e.printStackTrace()
            }
                    /*.listener(object :RequestListener<Drawable>{
                        override fun onLoadFailed(
                            e: GlideException?,
                            model: Any?,
                            target: Target<Drawable>?,
                            isFirstResource: Boolean
                        ): Boolean {
                            Log.e("TAG", "error uploading the image")

                            return false
                               }

                        override fun onResourceReady(
                            resource: Drawable?,
                            model: Any?,
                            target: Target<Drawable>?,
                            dataSource: DataSource?,
                            isFirstResource: Boolean
                        ): Boolean {
                            if (resource != null) {
                                Palette.from(resource.toBitmap()).generate(){
                                    palette ->
                                    val intColor = palette?.vibrantSwatch?.rgb ?: 0
                                    mBinding!!.rlDishDetailMain.setBackgroundColor(intColor)
                                }
                            }
                            return false
                        }

                    })

            }catch (e : IOException){
                e.printStackTrace()
            }*/

            mBinding!!.tvTitle.text = it.dishDetails.title
            mBinding!!.tvType.text = it.dishDetails.type.capitalize(Locale.ROOT)
            mBinding!!.tvCategory.text = it.dishDetails.category
            mBinding!!.tvIngredients.text = it.dishDetails.ingredients
            mBinding!!.tvCookingDirection.text = it.dishDetails.instructionsToCook
            mBinding!!.tvCookingTime.text =
                resources.getString(R.string.lbl_estimate_cooking_time , it.dishDetails.cookingTime)
            if (args.dishDetails.favoriteDish){
                mBinding!!.ivFavoriteDish.setImageDrawable(ContextCompat.getDrawable(requireActivity(),
                    R.drawable.ic_favorite_selected
                ))
            }else{
                mBinding!!.ivFavoriteDish.setImageDrawable(ContextCompat.getDrawable(requireActivity(),
                    R.drawable.ic_favorite_unselected

                ))
            }

        }

        mBinding!!.ivFavoriteDish.setOnClickListener{
            args.dishDetails.favoriteDish = !args.dishDetails.favoriteDish

            mFavDishViewModel.update(args.dishDetails)

            if(args.dishDetails.favoriteDish){
                mBinding!!.ivFavoriteDish.setImageDrawable(ContextCompat.getDrawable(requireActivity(),
                    R.drawable.ic_favorite_selected
                ))
                Toast.makeText(requireActivity(), resources.getString(R.string.mag_added_from_favorite),Toast.LENGTH_LONG).show()

            }else{
                mBinding!!.ivFavoriteDish.setImageDrawable(ContextCompat.getDrawable(requireActivity(),
                    R.drawable.ic_favorite_unselected

                ))
                Toast.makeText(requireActivity(), resources.getString(R.string.mag_removed_from_favorite),Toast.LENGTH_LONG).show()
            }

        }

    }

    override fun onDestroy() {
        super.onDestroy()
        mBinding = null
    }
}