package com.example.myfavdish.view.activities

import android.Manifest
import android.app.Activity
import android.app.Dialog
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.provider.Settings
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.example.myfavdish.R
import com.example.myfavdish.application.FavDishApplication
import com.example.myfavdish.databinding.ActivityAddUpdateDishBinding
import com.example.myfavdish.databinding.DialogCustomImageSelectionBinding
import com.example.myfavdish.databinding.DialogCustomListBinding
import com.example.myfavdish.model.entities.FavDish
import com.example.myfavdish.utils.Constants
import com.example.myfavdish.view.adapters.CustomListAdapter
import com.example.myfavdish.viewmodel.FavDishViewModel
import com.example.myfavdish.viewmodel.FavDishViewModelFactory
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.karumi.dexter.listener.single.PermissionListener
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream
import java.util.*


class AddUpdateDishActivity : AppCompatActivity(), View.OnClickListener {
    companion object{
        private const val CAMERA = 100
        private const val GALLERY = 101

        private const val IMAGE_DIRECTORY = "FavDishImages"

    }



    private lateinit var mBinding: ActivityAddUpdateDishBinding
    private  var mImagePath: String = ""
    private lateinit var mCustomListDialog: Dialog
    private val mFavDishViewModel : FavDishViewModel by viewModels {
        FavDishViewModelFactory((application as FavDishApplication).repository)
    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mBinding = ActivityAddUpdateDishBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        setupActionBar()

        mBinding.ivAddDishImage.setOnClickListener(this@AddUpdateDishActivity)

        mBinding.etType.setOnClickListener(this)
        mBinding.etCategory.setOnClickListener(this)
        mBinding.etCookingTime.setOnClickListener(this)

        mBinding.btnAddDish.setOnClickListener(this)
    }

    override fun onClick(v: View) {

        when (v.id) {

            R.id.iv_add_dish_image -> {

                customImageSelectionDialog()
                return
            }
            R.id.et_type -> {
                customItemsDialog(resources.getString(R.string.title_select_dish_type),
                    Constants.dishTypes(),
                    Constants.DISH_TYPE)
                return

            }
            R.id.et_category -> {
                customItemsDialog(resources.getString(R.string.title_select_dish_category),
                    Constants.dishCategories(),
                    Constants.DISH_CATEGORY)
                return

            }
            R.id.et_cooking_time -> {
                customItemsDialog(resources.getString(R.string.title_select_dish_cooking_time),
                    Constants.dishCookTime(),
                    Constants.DISH_COOKING_TIME)
                return

            }
            R.id.btn_add_dish -> {

                val title = mBinding.etTitle.text.toString().trim { it <= ' ' }
                val type = mBinding.etType.text.toString().trim { it <= ' ' }
                val category = mBinding.etCategory.text.toString().trim { it <= ' ' }
                val ingredients = mBinding.etIngredients.text.toString().trim { it <= ' ' }
                val cookingTimeInMinutes = mBinding.etCookingTime.text.toString().trim { it <= ' ' }
                val cookingDirection = mBinding.etDirectionToCook.text.toString().trim { it <= ' ' }

              when{
                  TextUtils.isEmpty(mImagePath) ->{
                      Toast.makeText(this@AddUpdateDishActivity,
                      resources.getString(R.string.err_msg_select_dish_image),
                          Toast.LENGTH_SHORT).show()
                  }
                  TextUtils.isEmpty(title) ->{
                      Toast.makeText(this@AddUpdateDishActivity,
                          resources.getString(R.string.err_msg_enter_dish_title),
                          Toast.LENGTH_SHORT).show()
                  }
                  TextUtils.isEmpty(category) ->{
                  Toast.makeText(this@AddUpdateDishActivity,
                      resources.getString(R.string.err_msg_select_dish_category),
                      Toast.LENGTH_SHORT).show()
              }
                  TextUtils.isEmpty(ingredients) ->{
                  Toast.makeText(this@AddUpdateDishActivity,
                      resources.getString(R.string.err_msg_enter_dish_ingredients),
                      Toast.LENGTH_SHORT).show()
              }
                  TextUtils.isEmpty(type) ->{
                  Toast.makeText(this@AddUpdateDishActivity,
                      resources.getString(R.string.err_msg_select_dish_type),
                      Toast.LENGTH_SHORT).show()
              }
                  TextUtils.isEmpty(cookingTimeInMinutes) ->{
                  Toast.makeText(this@AddUpdateDishActivity,
                      resources.getString(R.string.err_msg_select_dish_cooking_time),
                      Toast.LENGTH_SHORT).show()
              }
                  TextUtils.isEmpty(cookingDirection) ->{
                      Toast.makeText(this@AddUpdateDishActivity,
                          resources.getString(R.string.err_msg_enter_dish_cooking_instructions),
                          Toast.LENGTH_SHORT).show()
                  }
                  else ->{

                      val favDishDetails: FavDish = FavDish(
                          mImagePath,
                          Constants.DISH_IMAGE_SOURCE_LOCAL,
                          title,
                          type,
                          category,
                          ingredients,
                          cookingTimeInMinutes,
                          cookingDirection,
                          false
                      )
                      mFavDishViewModel.insert(favDishDetails)
                      Toast.makeText(
                          this@AddUpdateDishActivity,
                          "You successfully added your favorite dish details.",
                          Toast.LENGTH_SHORT
                      ).show()
                      Log.e("Insertion", "Success")
                      finish()

                  }




              }



            }

        }

    }

    /**
     * A function for ActionBar setup.
     */
    private fun setupActionBar() {
        setSupportActionBar(mBinding.toolbarAddDishActivity)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)


        mBinding.toolbarAddDishActivity.setNavigationOnClickListener { onBackPressed() }
    }


    /**
     * A function to launch the custom image selection dialog.
     */
    private fun customImageSelectionDialog() {
        val dialog = Dialog(this@AddUpdateDishActivity)

        val binding: DialogCustomImageSelectionBinding =
            DialogCustomImageSelectionBinding.inflate(layoutInflater)

        /*Set the screen content from a layout resource.
        The resource will be inflated, adding all top-level views to the screen.*/
        dialog.setContentView(binding.root)

        binding.tvCamera.setOnClickListener {

            Dexter.withContext(this).withPermissions(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                //Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA
            ).withListener(object : MultiplePermissionsListener {
                override fun onPermissionsChecked(report: MultiplePermissionsReport?) {
                    if(report != null){
                    if (report.areAllPermissionsGranted()) {
                        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                        startActivityForResult(intent, CAMERA)
                    }
            }
                }

                override fun onPermissionRationaleShouldBeShown(
                    permission: MutableList<PermissionRequest>?,
                    token: PermissionToken?
                ) {
                    showRationalDialogForPermissions()
                }

            }).onSameThread().check()

            dialog.dismiss()

        }
        binding.tvGallery.setOnClickListener {

            Dexter.withContext(this).withPermission(
                //Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ).withListener(object : PermissionListener {
                override fun onPermissionGranted(p0: PermissionGrantedResponse?) {

                    val intent = Intent(Intent.ACTION_PICK,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                    startActivityForResult(intent, GALLERY)

                }

                override fun onPermissionDenied(p0: PermissionDeniedResponse?) {
                    Toast.makeText(this@AddUpdateDishActivity, "You denied the Gallery permission now to select image", Toast.LENGTH_SHORT ).show()
                }

                override fun onPermissionRationaleShouldBeShown(
                    p0: PermissionRequest?,
                    p1: PermissionToken?
                ) {
                    showRationalDialogForPermissions()
                }

            }).onSameThread().check()

            dialog.dismiss()
        }
        dialog.show()

    }
    fun selectedListItem(item: String, selection: String){
        when(selection){
            Constants.DISH_TYPE ->{
                mCustomListDialog.dismiss()
                mBinding.etType.setText(item)
            }
            Constants.DISH_CATEGORY ->{
                mCustomListDialog.dismiss()
                mBinding.etCategory.setText(item)
            }
            else ->{
                mCustomListDialog.dismiss()
                mBinding.etCookingTime.setText(item)
            }

        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == Activity.RESULT_OK){
            if(requestCode == CAMERA){

                val thumbnail : Bitmap = data?.extras?.get("data") as Bitmap

                Glide.with(this)
                    .load(thumbnail)
                    .centerCrop()
                    .into(mBinding.ivDishImage)

                mImagePath = saveImageToInternalStorage(thumbnail)
                Log.i("ImagePath", mImagePath)

                mBinding.ivAddDishImage.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_baseline_edit_24))

            }
            if(requestCode == GALLERY){

                val  selectedPhotoUri = data?.data
                //mBinding.ivDishImage.setImageURI(selectedPhotoUri)

                Glide.with(this)
                    .load(selectedPhotoUri)
                    .centerCrop()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .listener(object : RequestListener<Drawable>{

                        override fun onLoadFailed(
                            e: GlideException?,
                            model: Any?,
                            target: Target<Drawable>?,
                            isFirstResource: Boolean
                        ): Boolean {
                           Log.e("TAG", "Error loading the image", e)
                            return false
                        }

                        override fun onResourceReady(
                            resource: Drawable?,
                            model: Any?,
                            target: Target<Drawable>?,
                            dataSource: DataSource?,
                            isFirstResource: Boolean
                        ): Boolean {
                            resource?.let {
                                val bitmap : Bitmap = resource.toBitmap()
                                mImagePath = saveImageToInternalStorage(bitmap)
                                Log.i("ImagePath", mImagePath)

                            }
                            return false
                        }


                    })
                    .into(mBinding.ivDishImage)

                mBinding.ivAddDishImage.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_baseline_edit_24))

            }
        }else if(resultCode == Activity.RESULT_CANCELED){
            Log.e("cancelled","cancelled")
        }
    }

    private fun showRationalDialogForPermissions(){
        AlertDialog.Builder(this).setMessage("It looks like you have turned off permission required for this feature, go to Setting")
            .setPositiveButton("GO TO SETTINGS"){_,_ ->
                try {
                    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                    val uri = Uri.fromParts("package", packageName, null)
                    intent.data = uri
                    startActivity(intent)
                }catch (e: ActivityNotFoundException){
                    e.printStackTrace()
                }

            }.setNegativeButton("Cancel"){dialog,_->
                dialog.dismiss()
            }.show()
    }
    private fun saveImageToInternalStorage(bitmap: Bitmap): String{
        val wrapper = ContextWrapper(applicationContext)

        var file = wrapper.getDir(IMAGE_DIRECTORY, Context.MODE_PRIVATE)
        file = File(file, "${UUID.randomUUID()}.jpg")

        try{
            val stream : OutputStream = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
            stream.flush()
            stream.close()
        }catch (e: IOException){
            e.printStackTrace()
        }
        return  file.absolutePath
    }
    private fun customItemsDialog(title: String, itemsList: List<String>, selection: String){
         mCustomListDialog = Dialog(this)
        val binding : DialogCustomListBinding = DialogCustomListBinding.inflate(layoutInflater)

        mCustomListDialog.setContentView(binding.root)
        binding.tvTitle.text = title
        binding.rvList.layoutManager = LinearLayoutManager(this)

        val adapter = CustomListAdapter(this, itemsList, selection)
        binding.rvList.adapter = adapter

        mCustomListDialog.show()

    }

}

