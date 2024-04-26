package uz.surxondan.qaydlar

import android.os.Bundle
import android.widget.Toolbar
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import uz.surxondan.qaydlar.databinding.ActivityDasturHaqidaBinding

class DasturHaqida : AppCompatActivity() {
    private  lateinit var  binding2:ActivityDasturHaqidaBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
            binding2= ActivityDasturHaqidaBinding.inflate(layoutInflater)
        setContentView(binding2.root)
        binding2.toolbardastr.setNavigationOnClickListener {
            finish()
        }

    }
}