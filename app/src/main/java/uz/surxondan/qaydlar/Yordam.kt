package uz.surxondan.qaydlar

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import uz.surxondan.qaydlar.databinding.ActivityYordamBinding

class Yordam : AppCompatActivity() {
private lateinit var  binding1: ActivityYordamBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding1= ActivityYordamBinding.inflate(layoutInflater)
        setContentView(binding1.root)
        onCliks()
    }

    private fun onCliks() {
        binding1.ivChiqish.setOnClickListener{
            finish()
        }
        binding1.cvTelegram.setOnClickListener{
            openUrl("https://t.me/J7471012")
        }
        binding1.cvMail.setOnClickListener{
            openUrl("https://mail.google.com/mail/u/0/?tab=rm&ogbl#inbox")
        }

    }

    private fun openUrl(link:String) {
       val uri=Uri.parse(link)
        val intent=Intent(Intent.ACTION_VIEW,uri)
        startActivity(intent)
    }
}