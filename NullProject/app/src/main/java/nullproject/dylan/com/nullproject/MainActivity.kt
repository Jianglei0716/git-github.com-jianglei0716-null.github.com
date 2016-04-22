package nullproject.dylan.com.nullproject

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.dylan.ablum.ImageConstants
import com.dylan.ablum.MultiSelectorActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        clickMe.setOnClickListener(View.OnClickListener { choosePhoto() })
    }

    fun choosePhoto() {
        var intent = Intent(this, MultiSelectorActivity::class.java)
        startActivity(intent)
    }
}
