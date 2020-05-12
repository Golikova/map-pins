package com.example.map_pins.ui

import android.Manifest
import android.app.Activity
import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.map_pins.R
import com.example.map_pins.data.db.AppDB
import com.example.map_pins.data.repository.PinRepository
import com.example.map_pins.databinding.FragmentAddPinBinding
import com.example.map_pins.viewmodel.AddPinViewModel
import com.example.map_pins.viewmodel.AddPinViewModelFactory
import kotlinx.android.synthetic.main.fragment_add_pin.*
import java.io.File


class AddPinFragment : Fragment() {

    lateinit var binding : FragmentAddPinBinding
    private val PICK_IMAGE = 100
    private val PICK_AUGM = 101
    private val REQUEST_EXTERNAL_STORAGE = 1
    private val PERMISSIONS_STORAGE = arrayOf(
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.WRITE_EXTERNAL_STORAGE
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(layoutInflater, R.layout.fragment_add_pin, container, false)

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val appDB : AppDB = AppDB(requireContext())
        val pinRepository : PinRepository = PinRepository(appDB)
        val factory = AddPinViewModelFactory(pinRepository)
        val viewModel = ViewModelProvider(this, factory).get(AddPinViewModel::class.java)
        binding.viewmodel = viewModel

        btn_choose_photo.setOnClickListener(View.OnClickListener {
            openGalleryForImage(it)
        })

        btn_choose_augmented.setOnClickListener(View.OnClickListener {
            openGalleryForAugmented(it)
        })

        viewModel.isValid.observe(viewLifecycleOwner, Observer { isValid ->
            isValid?.let {
                if (!it) {
                    requireContext().toast("Для начала заполните все поля!")
                    viewModel.isValid.value = null
                }
                if (it) {
                    requireContext().toast("Сохранено!")
                    viewModel.isValid.value = null
                }
            }
        })

    }

    private fun openGalleryForImage(view: View) {
            val gallery =
                Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
            startActivityForResult(gallery, PICK_IMAGE)

    }

    private fun openGalleryForAugmented(view: View) {
        val gallery =
            Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
        startActivityForResult(gallery, PICK_AUGM)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        activity
        if (resultCode == RESULT_OK && requestCode == PICK_IMAGE){
            if (data != null) {
                var file = File(data.data?.let { getRealPathFromURI(it) })
                binding.viewmodel?.imageUri = Uri.parse(file.absolutePath)
                img_pin.setImageURI(Uri.fromFile(file))
            }
        }

        if (resultCode == RESULT_OK && requestCode == PICK_AUGM){
            if (data != null) {
                var file = File(data.data?.let { getRealPathFromURI(it) })
                binding.viewmodel?.augmUri = Uri.parse(file.absolutePath)
                img_augm.setImageURI(Uri.fromFile(file))
            }
        }
    }

    private fun getRealPathFromURI(contentURI: Uri): String? {
        val result: String
        val cursor: Cursor? = context?.contentResolver?.query(contentURI, null, null, null, null)
        if (cursor == null) {
            result = contentURI.path!!
        } else {
            cursor.moveToFirst()
            val idx: Int = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA)
            result = cursor.getString(idx)
            cursor.close()
        }
        return result
    }

    fun Context.toast(message: CharSequence) =
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()

}