package com.example.map_pins.ui.profile

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.map_pins.R
import com.example.map_pins.data.db.AppDB
import com.example.map_pins.data.model.Pin
import com.example.map_pins.data.repository.PinRepository
import com.example.map_pins.databinding.EditDialogBinding
import com.example.map_pins.databinding.FragmentProfileBinding
import com.example.map_pins.viewmodel.ProfileViewModel
import com.example.map_pins.viewmodel.ProfileViewModelFactory
import kotlinx.android.synthetic.main.edit_dialog.view.*
import java.sql.Date
import java.text.ParseException
import java.time.LocalDate
import java.time.format.DateTimeFormatter


class ProfileFragment : Fragment(),
    RecyclerViewAdapter.OnItemClickListener,
    PinListener {

    lateinit var binding: FragmentProfileBinding
    lateinit var viewModel: ProfileViewModel
    lateinit var pinRepository: PinRepository

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(layoutInflater, R.layout.fragment_profile, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val appDB: AppDB = AppDB(requireContext())
        pinRepository = PinRepository(appDB)
        var recyclerViewAdapter =
            RecyclerViewAdapter(
                listOf(),
                pinRepository,
                this,
                this,
                requireContext()
            )

        binding.recyclerPins.layoutManager = LinearLayoutManager(this.activity);
        binding.recyclerPins.adapter = recyclerViewAdapter

        val factory = ProfileViewModelFactory(pinRepository)
        viewModel = ViewModelProvider(this, factory).get(ProfileViewModel::class.java)

        binding.viewmodel = viewModel

        viewModel.refresh()

        viewModel.pinList.observe(viewLifecycleOwner, Observer<List<Pin>> {
            it.let { recyclerViewAdapter.replaceData(it) }
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

    override fun onItemClick(position: Int) {

    }

    override fun onDeleteClick(view: View, pin: Pin) {
        confirmPinDelete(pin)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onEditClick(view: View, pin: Pin) {
        val dialogBuilder = AlertDialog.Builder(requireContext()).create()
        val inflater = this.layoutInflater
        var dialogView = inflater.inflate(R.layout.edit_dialog, null)

        var pin: Pin = pinRepository.getPinById(pin)
        var date = LocalDate.parse("2020-01-01", DateTimeFormatter.ofPattern("yyyy-MM-dd"))
        try {
            date = LocalDate.parse(pin.date)
        } catch (e: ParseException) {
            e.printStackTrace()
        }

        dialogView.title.setText(pin.name)
        dialogView.description.setText(pin.description)
        dialogView.datePicker1.updateDate(date.year, date.monthValue, date.dayOfMonth)

        dialogView.img_pin.setImageURI(Uri.parse(pinRepository.getImageByPinId(pin)[0].imgName))
        dialogView.img_augm.setImageURI(Uri.parse(pin.augmentedImage))

        dialogView.btn_save.setOnClickListener(View.OnClickListener {
            pin.name = dialogView.title.text.toString()
            pin.description = dialogView.description.text.toString()
            pin.date = currentDate(dialogView.datePicker1)
            viewModel.updatePin(pin)
            dialogBuilder.hide()
        })

        dialogView.btn_cancel.setOnClickListener(View.OnClickListener {
            dialogBuilder.hide()
        })

        dialogBuilder.setView(dialogView)
        dialogBuilder.show()
    }

    fun confirmPinDelete(pin: Pin) {

        val builder: AlertDialog.Builder = android.app.AlertDialog.Builder(context)
        builder.setTitle("Подтвердите действие!")
        builder.setMessage("Вы уверены, что хотите удалить?")
        builder.setCancelable(false)
        builder.setPositiveButton("Да",
            DialogInterface.OnClickListener { dialog, which ->
                viewModel.removePin(pin)
                Toast.makeText(
                    context,
                    "Запись успешно удалена",
                    Toast.LENGTH_SHORT
                ).show()
            })
        builder.setNegativeButton("Нет",
            DialogInterface.OnClickListener { dialog, which ->
                Toast.makeText(
                    context,
                    "Отменено",
                    Toast.LENGTH_SHORT
                ).show()
            })
        builder.show()
    }

    fun Context.toast(message: CharSequence) =
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()


    fun currentDate(view: DatePicker) : String {

        var datePicker = ""
        var monthPicker = ""
        var yearPicker = ""
        yearPicker = view.year.toString()
        monthPicker = if(view.month < 10){
            "0${view.month}";
        } else{
            view.month.toString();

        }
        datePicker = if(view.dayOfMonth < 10){
            "0${view.dayOfMonth}";
        } else {
            view.dayOfMonth.toString()
        }

        return "${yearPicker}-${monthPicker}-${datePicker}"
    }


}