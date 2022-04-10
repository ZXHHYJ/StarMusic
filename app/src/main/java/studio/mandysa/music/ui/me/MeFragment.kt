package studio.mandysa.music.ui.me

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import studio.mandysa.music.databinding.FragmentMeBinding
import studio.mandysa.music.logic.ktx.viewBinding

class MeFragment : Fragment() {
    private val mBinding: FragmentMeBinding by viewBinding()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return mBinding.root
    }

}