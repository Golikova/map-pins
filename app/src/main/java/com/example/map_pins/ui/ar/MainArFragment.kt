package com.example.map_pins.ui.ar

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.example.map_pins.R
import com.example.map_pins.data.db.AppDB
import com.example.map_pins.data.model.Pin
import com.example.map_pins.data.repository.PinRepository
import com.google.ar.core.*
import com.google.ar.sceneform.AnchorNode
import com.google.ar.sceneform.FrameTime
import com.google.ar.sceneform.math.Quaternion
import com.google.ar.sceneform.math.Vector3
import com.google.ar.sceneform.rendering.Renderable
import com.google.ar.sceneform.rendering.ViewRenderable
import com.google.ar.sceneform.ux.ArFragment
import com.google.ar.sceneform.ux.TransformableNode
import kotlinx.android.synthetic.main.pin.view.*
import java.io.File


class MainArFragment : Fragment() {

    private lateinit var arFragment: MyArFragment
    private var shouldAddModelMap = HashMap<String, Boolean>()

    lateinit var pinRepository: PinRepository

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_ar_container, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        arFragment = (childFragmentManager.findFragmentById(R.id.sceneform_fragment) as MyArFragment)
        arFragment.planeDiscoveryController.hide();
        arFragment.arSceneView.scene.addOnUpdateListener(this::onUpdateFrame);

        val appDB : AppDB = AppDB(requireContext())
        pinRepository = PinRepository(appDB)

    }

    fun setupAugmentedImagesDb(config: Config, session: Session): Boolean {

        var images = pinRepository.getAllAugmentedPicks()
       Log.d("MyTag", images.toString())

        var augmentedImageDatabase: AugmentedImageDatabase = AugmentedImageDatabase(session)
        if (images != null) {
            for (image in images) {
                val bitmap: Bitmap = loadAugmentedImage(image) ?: return false
                augmentedImageDatabase.addImage(image, bitmap, 0.4F)
                shouldAddModelMap[image] = true
            }
        }
        config.augmentedImageDatabase = augmentedImageDatabase
        session.configure(config)
        return true
    }

    private fun loadAugmentedImage(imageName : String): Bitmap? {

        val imgFile = File(imageName)
        if (imgFile.exists()) {
            val myBitmap = BitmapFactory.decodeFile(imgFile.absolutePath)
            return myBitmap
        }
        return null
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun onUpdateFrame(frameTime: FrameTime) {
        val frame = arFragment.arSceneView.arFrame
        val augmentedImages =
            frame?.getUpdatedTrackables(
                AugmentedImage::class.java
            )
        for (augmentedImage in augmentedImages!!) {
            if (augmentedImage.trackingState == TrackingState.TRACKING && shouldAddModelMap[augmentedImage.name] == true) {
                    placeObject(
                        arFragment,
                        augmentedImage.createAnchor(augmentedImage.centerPose),
                        augmentedImage.name
                    )
                shouldAddModelMap[augmentedImage.name] = false
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private fun placeObject(
        arFragment: ArFragment,
        anchor: Anchor,
        imageName: String
    ) {
        ViewRenderable.builder()
            .setView(arFragment.context, R.layout.pin)
            .build()
            .thenAccept {
                it.isShadowCaster = false
                var pin : Pin = pinRepository.getPinByAugmentedImage(imageName)[0]
                it.view.ar_title.text = pin.name
                it.view.ar_image.setImageURI(Uri.parse(pinRepository.getImageByPinId(pin)[0].imgName))
                it.view.ar_description.text = pin.description
                it.view.ar_date.text = pin.date
                addControlsToScene(arFragment, anchor, it)
            }
            .exceptionally {
                val builder = AlertDialog.Builder(requireActivity().applicationContext)
                builder.setMessage(it.message).setTitle("Error")
                val dialog = builder.create()
                dialog.show()
                Log.d("MyTag", "Model error")
                return@exceptionally null
            }
    }

    private fun addControlsToScene(fragment: ArFragment, anchor: Anchor, renderable: Renderable) {
        val anchorNode = AnchorNode(anchor)
        val node = TransformableNode(fragment.transformationSystem)
        node.renderable = renderable
        node.setParent(anchorNode)
        node.localScale = Vector3(0.1f, 23f, 0.1f)
        node.localRotation = Quaternion.axisAngle(Vector3(1f, 0F, 0F), -90f)
        fragment.arSceneView.scene.addChild(anchorNode)
    }
}

