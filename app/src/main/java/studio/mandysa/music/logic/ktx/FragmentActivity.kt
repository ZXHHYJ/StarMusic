package studio.mandysa.music.logic.ktx

import android.view.LayoutInflater
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewbinding.ViewBinding
import androidx.viewpager2.adapter.FragmentStateAdapter

fun FragmentActivity.createFragmentStateAdapter(
    vararg fragments: Fragment
): FragmentStateAdapter {
    return object : androidx.viewpager2.adapter.FragmentStateAdapter(this) {
        override fun getItemCount(): Int {
            return fragments.size
        }

        override fun createFragment(position: Int): Fragment {
            return fragments[position]
        }

    }
}

inline fun <reified VB : ViewBinding> FragmentActivity.viewBinding() = lazy {
    inflateBinding<VB>(layoutInflater)
}

@Suppress("UNCHECKED_CAST")
inline fun <reified VB : ViewBinding> inflateBinding(layoutInflater: LayoutInflater) =
    VB::class.java.getMethod("inflate", LayoutInflater::class.java)
        .invoke(null, layoutInflater) as VB