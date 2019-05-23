package com.camera

import android.Manifest
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.Toast
import com.idfy.rft.*
import kotlinx.android.synthetic.main.activity_main.*
import android.widget.ArrayAdapter
import com.camera.Constants.Doctype.Companion.AADHAAR
import com.camera.Constants.Doctype.Companion.AADHAAR_BACK
import com.camera.Constants.Doctype.Companion.AADHAAR_FRONT
import com.camera.Constants.Doctype.Companion.DOCUMENT
import com.camera.Constants.Doctype.Companion.DRIVING_LICENSE
import com.camera.Constants.Doctype.Companion.FACE_IMAGE
import com.camera.Constants.Doctype.Companion.PAN
import com.camera.Constants.Doctype.Companion.PASSPORT_BACK
import com.camera.Constants.Doctype.Companion.PASSPORT_FRONT
import com.camera.Constants.Doctype.Companion.VOTER
import com.camera.Constants.Doctype.Companion.VOTER_BACK
import com.camera.Constants.Doctype.Companion.VOTER_FRONT
import com.tbruyelle.rxpermissions2.RxPermissions




class MainActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener {

    private var type: DocType? = null




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val rxPermissions = RxPermissions(this)
        rxPermissions.request(Manifest.permission.CAMERA).subscribe({granted->

        },{

        })



        val rft = RftSdk.init(
            this, BuildConfig.ACCOUNTID,
            BuildConfig.TOKEN,
            initCallback
        )

        val spinnerArrayAdapter = ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, resources.getStringArray(R.array.type))
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spnType.setAdapter(spinnerArrayAdapter)
        spnType.setOnItemSelectedListener(this)
        type = DocType.AADHAAR_FRONT

        btnAccess.setOnClickListener {
            if (type == DocType.FACE_IMAGE) {
                rft.captureFace(this,imageListener)
            } else {
                rft.captureDoc(this, type!!, call)
            }

        }

    }


    override fun onNothingSelected(parent: AdapterView<*>?) {

    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        when (position) {
            AADHAAR_FRONT -> type = DocType.AADHAAR_FRONT
            AADHAAR_BACK -> type = DocType.AADHAAR_BACK
            AADHAAR -> type = DocType.AADHAAR
            PAN -> type = DocType.PAN
            DRIVING_LICENSE -> type = DocType.DRIVING_LICENSE
            FACE_IMAGE -> type = DocType.FACE_IMAGE
            PASSPORT_FRONT -> type = DocType.PASSPORT_FRONT
            PASSPORT_BACK -> type = DocType.PASSPORT_BACK
            VOTER -> type = DocType.VOTER
            VOTER_FRONT -> type = DocType.VOTER_FRONT
            VOTER_BACK -> type = DocType.VOTER_BACK
            DOCUMENT -> type = DocType.DOCUMENT
        }
    }

    private val imageListener = object : RftImageCaptureCallback {
        override fun onCorrectCapture(p0: Bitmap?) {
            ivImage.setImageBitmap(p0)
        }

        override fun onCaptureError(p0: RftException?) {

        }

        override fun onIncorrectCapture(p0: Bitmap?, p1: Reason?) {

        }

    }

    private fun showMessage(message:String){
        Toast.makeText(this@MainActivity, message, Toast.LENGTH_SHORT).show()
    }

    private val call = object : RftImageCaptureCallback {
        override fun onCorrectCapture(p0: Bitmap?) {
            ivImage.setImageBitmap(p0)
            showMessage("onCorrectCapture")

        }

        override fun onCaptureError(p0: RftException?) {
            showMessage("onCaptureError")

        }

        override fun onIncorrectCapture(p0: Bitmap?, p1: Reason?) {
            showMessage("onIncorrectCapture")
        }

    }

    private val initCallback = object : RftSdkInitCallback {
        override fun onSdkInitFailure(p0: RftException) {

        }

        override fun onSdkInitSuccess() {
        }

    }
}
