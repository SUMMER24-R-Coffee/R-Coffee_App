package datlowashere.project.rcoffee.ui.view.activity.product

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import datlowashere.project.rcoffee.R
import datlowashere.project.rcoffee.databinding.ActivityProductInformationBinding
import datlowashere.project.rcoffee.data.model.Product
import datlowashere.project.rcoffee.utils.FormatterHelper

class ProductInformationActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProductInformationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProductInformationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val product: Product? = intent.getParcelableExtra("product")

        product?.let {
            binding.tvProductNamInf.text = it.product_name
            binding.tvDescriptionPrdInf.text = it.description
            binding.tvProductPriceInf.text = FormatterHelper.formatCurrency(it.price)
            binding.tvAverageStarInf.text = it.average_rating.toFloat().toString()

            Glide.with(this)
                .load(it.img)
                .centerCrop()
                .placeholder(R.drawable.img_default)
                .into(binding.imgProductInf)
        }
    }
}
