package studio.mandysa.music.ui.play

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.LinearInterpolator
import androidx.fragment.app.Fragment
import com.flaviofaria.kenburnsview.RandomTransitionGenerator
import studio.mandysa.music.databinding.FragmentPlayBinding
import studio.mandysa.music.logic.ktx.playManager
import studio.mandysa.music.logic.ktx.viewBinding
import studio.mandysa.music.service.playmanager.PlayManager


class PlayFragment : Fragment() {

    private val mBinding: FragmentPlayBinding by viewBinding()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        playManager {
            playStateLivedata().observe(viewLifecycleOwner) {
                if (it == PlayManager.STATE.PAUSE) {
                    mBinding.playBackground.pause()
                } else {
                    mBinding.playBackground.resume()
                }
            }
            changeMusicLiveData()
                .observe(viewLifecycleOwner) { model ->
                    mBinding.playBackground.load(model.coverUrl)
                }
        }
        //背景模糊动画速度
        mBinding.playBackground.setTransitionGenerator(
            RandomTransitionGenerator(
                2000,
                LinearInterpolator()
            )
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return mBinding.root
    }
}