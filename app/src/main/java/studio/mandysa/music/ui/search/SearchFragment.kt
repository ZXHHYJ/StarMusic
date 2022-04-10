package studio.mandysa.music.ui.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import studio.mandysa.music.databinding.FragmentSearchBinding
import studio.mandysa.music.logic.ktx.viewBinding


class SearchFragment : Fragment() {

    private val mBinding: FragmentSearchBinding by viewBinding()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return mBinding.root
    }
}