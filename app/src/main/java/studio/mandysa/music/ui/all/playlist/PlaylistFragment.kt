package studio.mandysa.music.ui.all.playlist

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import studio.mandysa.jiuwo.ktx.addFooter
import studio.mandysa.jiuwo.ktx.addHeader
import studio.mandysa.jiuwo.ktx.linear
import studio.mandysa.jiuwo.ktx.setup
import studio.mandysa.music.R
import studio.mandysa.music.databinding.FragmentPlaylistBinding
import studio.mandysa.music.databinding.ItemPlaylistHeaderBinding
import studio.mandysa.music.databinding.ItemSongBinding
import studio.mandysa.music.logic.ktx.load
import studio.mandysa.music.logic.ktx.viewBinding
import studio.mandysa.music.logic.model.MusicModel
import studio.mandysa.music.logic.model.PlaylistInfoModel
import studio.mandysa.music.service.playmanager.PlayManager
import studio.mandysa.music.service.playmanager.ktx.allArtist
import studio.mandysa.music.ui.event.EventViewModel

class PlaylistFragment : Fragment() {

    private val mBinding: FragmentPlaylistBinding by viewBinding()

    private val mViewModel: PlaylistViewModel by viewModels()

    private val mUserViewModel by activityViewModels<EventViewModel>()

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().onBackPressedDispatcher
            .addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    findNavController().navigateUp()
                }
            })
        val args = PlaylistFragmentArgs.fromBundle(requireArguments())
        mBinding.playlistState.showLoading {
            mUserViewModel.getCookieLiveData().observe(viewLifecycleOwner) { it ->
                mViewModel.bind(it, args.id).observe(viewLifecycleOwner) { it1 ->
                    when (it1) {
                        is PlaylistViewModel.Status.Header -> {
                            mBinding.playlistState.showContentState()
                            mBinding.playlistRecycle.addHeader(it1.value)
                        }
                        is PlaylistViewModel.Status.Next -> {
                            it1.value.forEach {
                                mBinding.playlistRecycle.addFooter(it)
                            }
                        }
                        is PlaylistViewModel.Status.Error -> {
                            mBinding.playlistState.showErrorState()
                        }
                    }
                }
            }
        }
        mBinding.playlistState.showLoadingState()
        mBinding.playlistRecycle.linear().setup {
            addType<PlaylistInfoModel>(R.layout.item_playlist_header) {
                ItemPlaylistHeaderBinding.bind(itemView).apply {
                    playlistCover.load(model.coverImgUrl)
                    playlistTitle.text = model.name
                    playlistDescription.text = model.description
                }
            }
            addType<MusicModel>(R.layout.item_song) {
                ItemSongBinding.bind(itemView).apply {
                    songIndex.text = (modelPosition + 1).toString()
                    songName.text = model.title
                    songSingerName.text = model.artist.allArtist()
                    itemView.setOnClickListener {
                        PlayManager.loadPlaylist(
                            models,
                            modelPosition
                        )
                    }
                    more.setOnClickListener {

                    }
                }
            }
        }
        /* mBinding.playlistRecycle.addBottom {
             mViewModel.nextPage()
         }*/
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return mBinding.root
    }
}