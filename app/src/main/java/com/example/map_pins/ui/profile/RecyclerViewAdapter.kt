package com.example.map_pins.ui.profile

import android.app.Application
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.map_pins.data.model.Pin
import com.example.map_pins.data.repository.PinRepository
import com.example.map_pins.databinding.ListItemBinding
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_add_pin.*
import kotlinx.android.synthetic.main.list_item.view.*
import java.io.File


class RecyclerViewAdapter(private var items: List<Pin>,
                          private var pinRepository: PinRepository,
                          private var listener: OnItemClickListener,
                          private var itemClickListener: PinListener,
                          private var context: Context
)
    : RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent?.context)
        val binding = ListItemBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(
            binding,
            pinRepository,
            context
        )
    }

    fun replaceData(items: List<Pin>) {
        this.items = items
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position], listener, itemClickListener)
    }

    override fun getItemCount(): Int = items.size

    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }

    class ViewHolder(
        private var binding: ListItemBinding,
        private var pinRepository: PinRepository,
        private var context: Context
    ) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(pin: Pin, listener: OnItemClickListener?, itemClickListener: PinListener) {
            binding.pin = pin

            var image = pinRepository.getImageByPinId(pin)

            var file = File(image[0].imgName)
            itemView.item_image.setImageURI(Uri.fromFile(file))

            if (listener != null) {
                binding.root.setOnClickListener({ _ -> listener.onItemClick(layoutPosition) })
                itemView.delete_btn.setOnClickListener { itemClickListener.onDeleteClick(it, pin)}
                itemView.edit_btn.setOnClickListener { itemClickListener.onEditClick(it, pin)}
                }
            binding.executePendingBindings()
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

}

}
