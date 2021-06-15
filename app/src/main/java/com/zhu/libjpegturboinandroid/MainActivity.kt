package com.zhu.libjpegturboinandroid

import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.zhu.libjpegturboinandroid.databinding.ActivityMainBinding
import org.libjpegturbo.turbojpeg.TJ
import org.libjpegturbo.turbojpeg.TJCompressor
import java.nio.ByteBuffer

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val byteFile = assets.open("cookie.jpeg").readBytes()
        val bmp = BitmapFactory.decodeByteArray(byteFile, 0, byteFile.size)
        val size: Int = bmp.rowBytes * bmp.height
        val bytes = ByteArray(size)
        val b: ByteBuffer = ByteBuffer.wrap(bytes)
        bmp.copyPixelsToBuffer(b)
        bmp.recycle()

        val compressor = TJCompressor()
        compressor.setSourceImage(bytes, 0, 0, bmp.width, 0, bmp.height, TJ.PF_RGBX)
        compressor.setSubsamp(TJ.SAMP_420)
        compressor.setJPEGQuality(100)
        val outputBytes: ByteArray = compressor.compress(0)
        val output = BitmapFactory.decodeByteArray(outputBytes, 0, compressor.compressedSize)
        compressor.close()

        binding.imgInfo.text = "compressor result ${(output.rowBytes) / 1024}kb"
        binding.imageView.setImageBitmap(output)
    }
}