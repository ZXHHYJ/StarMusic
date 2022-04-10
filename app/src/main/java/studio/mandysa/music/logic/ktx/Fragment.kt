package studio.mandysa.music.logic.ktx

import android.view.LayoutInflater
import androidx.fragment.app.Fragment
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.viewbinding.ViewBinding
import androidx.viewpager2.adapter.FragmentStateAdapter
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

fun Fragment.createFragmentStateAdapter(vararg fragments: Fragment): FragmentStateAdapter {
    return object : androidx.viewpager2.adapter.FragmentStateAdapter(this) {
        override fun getItemCount(): Int {
            return fragments.size
        }

        override fun createFragment(position: Int): Fragment {
            return fragments[position]
        }

    }
}

inline fun <reified VB : ViewBinding> viewBinding() =
    FragmentBindingDelegate(VB::class.java)

@Suppress("UNCHECKED_CAST")
class FragmentBindingDelegate<VB : ViewBinding>(
    private val clazz: Class<VB>
) : ReadOnlyProperty<Fragment, VB> {

    private var _binding: VB? = null

    override fun getValue(thisRef: Fragment, property: KProperty<*>): VB {
        if (_binding == null) {
            _binding = clazz.getMethod("inflate", LayoutInflater::class.java)
                .invoke(null, thisRef.layoutInflater) as VB
            thisRef.viewLifecycleOwner.lifecycle.addObserver(object : DefaultLifecycleObserver {
                override fun onDestroy(owner: LifecycleOwner) {
                    super.onDestroy(owner)
                    _binding = null
                }
            })
        }
        return _binding!!
    }
}