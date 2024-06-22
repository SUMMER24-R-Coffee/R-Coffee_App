package datlowashere.project.rcoffee

import android.graphics.Bitmap
import android.graphics.BitmapShader
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.graphics.Shader
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.google.android.material.bottomnavigation.BottomNavigationView
import datlowashere.project.rcoffee.data.repository.AuthRepository
import datlowashere.project.rcoffee.databinding.ActivityMainBinding
import datlowashere.project.rcoffee.ui.viewmodel.AuthViewModel
import datlowashere.project.rcoffee.ui.viewmodel.AuthViewModelFactory
import datlowashere.project.rcoffee.utils.SharedPreferencesHelper

class MainActivity : AppCompatActivity()  {

    private lateinit var binding: ActivityMainBinding
    private lateinit var authViewModel: AuthViewModel
    private lateinit var navView: BottomNavigationView
    private var destinationChangedListener: NavController.OnDestinationChangedListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupViewModels()

        navView = binding.bottomNav
        val navController = findNavController(R.id.nav_host_fragment_activiy)

        val appBarConfiguration = AppBarConfiguration(
            setOf(R.id.nav_home, R.id.nav_history, R.id.nav_fav, R.id.nav_settting)
        )
        navView.setupWithNavController(navController)

        val emailUser = getEmailUser()

        if (emailUser.isNotEmpty()) {
            val defaultTextColor = ContextCompat.getColorStateList(this, android.R.color.black)
            binding.bottomNav.itemTextColor = defaultTextColor
            binding.bottomNav.setItemIconTintList(null)

        }
        //TODO: Set icontint for each nav when selected (case login)
        authViewModel.getUserData(emailUser)
        setUpImage()


        val switchToCancelled = intent.getBooleanExtra("SWITCH_TO_CANCELLED", false)
        val switchToCompleted = intent.getBooleanExtra("SWITCH_TO_COMPLETED", false)
        val bundle = Bundle().apply {
            putBoolean("SWITCH_TO_CANCELLED", switchToCancelled)
            putBoolean("SWITCH_TO_COMPLETED", switchToCompleted)
    }
        if (switchToCancelled || switchToCompleted) {
            navController.navigate(R.id.nav_history, bundle)
        }

        navView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    navController.navigate(R.id.nav_home)
                    true
                }
                R.id.nav_history -> {
                    navController.navigate(R.id.nav_history)
                    true
                }
                R.id.nav_fav -> {
                    navController.navigate(R.id.nav_fav)
                    true
                }
                R.id.nav_settting -> {
                    navController.navigate(R.id.nav_settting)
                    true
                }
                else -> false
            }
        }
        navController.addOnDestinationChangedListener { _, destination, _ ->
            destinationChangedListener?.let {
                navController.removeOnDestinationChangedListener(it)
                destinationChangedListener = null
            }
        }

    }
    private fun setupViewModels() {
        val authRepository = AuthRepository()
        val authViewModelFactory = AuthViewModelFactory(authRepository)
        authViewModel = ViewModelProvider(this, authViewModelFactory).get(AuthViewModel::class.java)
    }

    private fun setUpImage() {
        authViewModel.userResponse.observe(this, Observer { authResponse ->
            if (authResponse != null) {
                authResponse.users?.let { users ->
                    if (users.isNotEmpty()) {
                        val user = users[0]

                        user.user_img?.let { imageUrl ->
                            Glide.with(this)
                                .asBitmap()
                                .load(imageUrl)
                                .apply(
                                    RequestOptions
                                        .circleCropTransform()
                                        .override(dpToPx(48), dpToPx(48))
                                        .placeholder(R.drawable.user_circle)
                                )
                                .into(object : CustomTarget<Bitmap>() {
                                    override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                                        val borderedBitmap = addBorderToBitmap(resource, dpToPx(2), resources.getColor(R.color.black))
                                        binding.bottomNav.menu.findItem(R.id.nav_settting)?.icon = BitmapDrawable(resources, borderedBitmap)
                                    }

                                    override fun onLoadCleared(placeholder: Drawable?) {
                                    }

                                    override fun onLoadFailed(errorDrawable: Drawable?) {
                                    }
                                })
                        }
                    } else {
                    }
                }
            } else {
            }
        })
    }

    fun addBorderToBitmap(bitmap: Bitmap, borderWidth: Int, borderColor: Int): Bitmap {
        val borderSize = borderWidth * 1
        val newBitmap = Bitmap.createBitmap(bitmap.width + borderSize, bitmap.height + borderSize, bitmap.config)
        val canvas = Canvas(newBitmap)
        val paint = Paint()

        paint.isAntiAlias = true
        val bitmapShader = BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)
        paint.shader = bitmapShader
        val rect = RectF(borderWidth.toFloat(), borderWidth.toFloat(), (newBitmap.width - borderWidth).toFloat(), (newBitmap.height - borderWidth).toFloat())
        canvas.drawOval(rect, paint)

        paint.shader = null
        paint.style = Paint.Style.STROKE
        paint.color = borderColor
        paint.strokeWidth = borderWidth.toFloat()
        canvas.drawOval(rect, paint)

        return newBitmap
    }

    private fun dpToPx(dp: Int): Int {
        return (dp * resources.displayMetrics.density).toInt()
    }

    fun getEmailUser(): String {
        return SharedPreferencesHelper.getUserEmail(this) ?: ""
    }

    override fun onDestroy() {
        super.onDestroy()
        SharedPreferencesHelper.clear(this)
    }
}
