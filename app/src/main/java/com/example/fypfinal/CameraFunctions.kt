package com.example.fypfinal
//Obsolete
class CameraFunctions {//(private val activity: Activity, private val handler: Handler)

//    private var cameraId: String? = null
//    private var cameraDevice: CameraDevice? = null
//    private var previewSession: CameraCaptureSession? = null
//    private var previewCaptureRequestBuilder: CaptureRequest.Builder? = null
//
//    fun start(previewSurface: Surface) {
//        val cameraManager = activity.getSystemService(Context.CAMERA_SERVICE) as CameraManager
//        try {
//            cameraId = cameraManager.cameraIdList[0]
//        } catch (e: Exception) {
//            Log.e("camera", "No access to camera", e)
//            handler.sendMessage(Message.obtain(
//                handler,
//                MainActivity.MESSAGE_CAMERA_NOT_AVAILABLE,
//                "No access to camera...."))
//        }
//
//        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
//            Log.e("camera", "No permission to take photos")
//            handler.sendMessage(Message.obtain(
//                handler,
//                MainActivity.MESSAGE_CAMERA_NOT_AVAILABLE,
//                "No permission to take photos"))
//            return
//        }
//
//        if (cameraId == null) {
//            return
//        }
//
//        try {
//            cameraManager.openCamera(cameraId!!, object : CameraDevice.StateCallback() {
//
//                override fun onOpened(camera: CameraDevice) {
//                    cameraDevice = camera
//
//                    val stateCallback = object : CameraCaptureSession.StateCallback() {
//
//                        override fun onConfigured(session: CameraCaptureSession) {
//                            previewSession = session
//                            try {
//                                previewCaptureRequestBuilder = cameraDevice!!.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW)
//                                previewCaptureRequestBuilder!!.addTarget(previewSurface)
//                                previewCaptureRequestBuilder!!.set(CaptureRequest.FLASH_MODE, CaptureRequest.FLASH_MODE_TORCH)
//
//                                val thread = HandlerThread("CameraPreview")
//                                thread.start()
//
//                                previewSession!!.setRepeatingRequest(previewCaptureRequestBuilder!!.build(), null, null)
//                            } catch (e: CameraAccessException) {
//                                if (e.message != null) {
//                                    Log.e("camera", e.message!!)
//                                }
//                            }
//                        }
//
//                        override fun onConfigureFailed(session: CameraCaptureSession) {
//                            Log.e("camera", "Session configuration failed")
//                        }
//                    }
//
//                    try {
//                        camera.createCaptureSession(Collections.singletonList(previewSurface), stateCallback, null) //1
//                    } catch (e: CameraAccessException) {
//                        if (e.message != null) {
//                            Log.e("camera", e.message!!)
//                        }
//                    }
//                }
//
//                override fun onDisconnected(camera: CameraDevice) {}
//
//                override fun onError(camera: CameraDevice, error: Int) {}
//            }, null)
//        } catch (e: Exception) {
//            if (e.message != null) {
//                Log.e("camera", e.message!!)
//                handler.sendMessage(Message.obtain(
//                    handler,
//                    MainActivity.MESSAGE_CAMERA_NOT_AVAILABLE,
//                    e.message))
//            }
//        }
//
//        // Wait for 10 seconds and record the heart rate
//        Handler().postDelayed({
//            previewCaptureRequestBuilder?.set(CaptureRequest.FLASH_MODE, CaptureRequest.FLASH_MODE_OFF)
//            previewSession?.stopRepeating()
//            handler.sendMessage(Message.obtain(handler, MainActivity.MESSAGE_HEART_RATE, "Heart rate recorded"))
//        }, 10000)
//    }
//
//            fun stop() {
//                try {
//                    previewSession?.abortCaptures()
//                    previewSession?.close()
//                    cameraDevice?.close()
//                } catch (e: Exception) {
//                    Log.e("camera", "Error stopping camera", e)
//                }
//            }
//
//            companion object {
//            const val REQUEST_CAMERA_PERMISSION = 1
//        }
}
