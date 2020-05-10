package com.example.map_pins.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.map_pins.R
import com.google.ar.core.Config
import com.google.ar.core.Session
import com.google.ar.sceneform.ux.ArFragment

class MyArFragment : ArFragment() {

    override fun getSessionConfiguration(session: Session): Config? {
        Log.d("MyTag", "Session config running")
        planeDiscoveryController.setInstructionView(null)
//        val config = Config(session)
        val config : Config = super.getSessionConfiguration(session)
        config.updateMode = Config.UpdateMode.LATEST_CAMERA_IMAGE
       // config.cloudAnchorMode = Config.CloudAnchorMode.ENABLED
        session.configure(config)
        arSceneView.setupSession(session)
        Log.d("MyTag", "Session config continue")

         if ((parentFragment as MainArFragment).setupAugmentedImagesDb(config, session)) {
             Log.d("MyTag", "Success")
         } else {
             Log.e("MyTag", "Faliure setting up db")
         }
        return config
    }

}