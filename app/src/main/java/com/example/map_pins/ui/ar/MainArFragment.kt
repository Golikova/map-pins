package com.example.map_pins.ui

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.example.map_pins.R
import com.google.ar.core.*
import com.google.ar.sceneform.AnchorNode
import com.google.ar.sceneform.FrameTime
import com.google.ar.sceneform.math.Quaternion
import com.google.ar.sceneform.math.Vector3
import com.google.ar.sceneform.rendering.Renderable
import com.google.ar.sceneform.rendering.ViewRenderable
import com.google.ar.sceneform.ux.ArFragment
import com.google.ar.sceneform.ux.TransformableNode
import java.io.IOException


class MainArFragment : Fragment() {

    private lateinit var arFragment: MyArFragment
    private var shouldAddModel = true

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

    }

    fun setupAugmentedImagesDb(config: Config, session: Session): Boolean {
       Log.d("MyTag", "db Setup")


        var augmentedImageDatabase: AugmentedImageDatabase = AugmentedImageDatabase(session)
        val images: Array<out String>? = context!!.assets.list("augmented")
        if (images != null) {
            for (image in images) {
                val bitmap: Bitmap = loadAugmentedImage(image) ?: return false
                augmentedImageDatabase.addImage(image, bitmap, 0.4F)
            }
        }
        config.augmentedImageDatabase = augmentedImageDatabase
        Log.d("MyTag", augmentedImageDatabase.numImages.toString())
        session.configure(config)
        return true
    }

    private fun loadAugmentedImage(imageName : String): Bitmap? {

        try {
            context?.assets?.open("augmented/$imageName")
                .use { `is` ->
                    Log.d("MyTag", "img loaded")
                    return BitmapFactory.decodeStream(`is`) }
        } catch (e: IOException) {
            Log.e("MyTag", "IO Exception", e)
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
            Log.d("MyTag", augmentedImage.trackingMethod.name)

            if (augmentedImage.trackingState == TrackingState.TRACKING) {
                Log.d("MyTag", "img found!")
//                if (augmentedImage.name == "first" && shouldAddModel) {
                    placeObject(
                        arFragment,
                        augmentedImage.createAnchor(augmentedImage.centerPose)
                    )
                    shouldAddModel = false
//                }
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private fun placeObject(
        arFragment: ArFragment,
        anchor: Anchor
    ) {
        ViewRenderable.builder()
            .setView(arFragment.context, R.layout.pin)
            .build()
            .thenAccept {
                it.isShadowCaster = false
                it.isShadowReceiver = false

                it.view.findViewById<ImageButton>(R.id.info_button).setOnClickListener {
                    // TODO: do smth here
                }
                addControlsToScene(arFragment, anchor, it)
            }
            .exceptionally {
                val builder = AlertDialog.Builder(activity!!.applicationContext)
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
        node.localRotation = Quaternion.axisAngle(Vector3(1f, 0F, 0F), -90f)

        fragment.arSceneView.scene.addChild(anchorNode)
    }
}

