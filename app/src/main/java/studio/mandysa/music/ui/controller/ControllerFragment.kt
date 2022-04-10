package studio.mandysa.music.ui.controller

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import studio.mandysa.music.R
import studio.mandysa.music.databinding.FragmentControllerBinding
import studio.mandysa.music.logic.ktx.load
import studio.mandysa.music.logic.ktx.playManager
import studio.mandysa.music.logic.ktx.viewBinding

class ControllerFragment : Fragment() {
    private val mBinding: FragmentControllerBinding by viewBinding()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        playManager {
            changeMusicLiveData().observe(viewLifecycleOwner) {
                mBinding.apply {
                    controllerTitle.text = it.title
                    controllerCover.load(it.coverUrl)
                }
            }
            pauseLiveData().observe(viewLifecycleOwner) {
                mBinding.controllerPlayOrPause.setImageResource(if (it) R.drawable.ic_play else R.drawable.ic_pause)
            }
            mBinding.controllerPlayOrPause.setOnClickListener {
                if (pauseLiveData().value == true)
                    play()
                else pause()
            }
            mBinding.controllerNextSong.setOnClickListener {
                skipToNext()
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return mBinding.root
    }
}