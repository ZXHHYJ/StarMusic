package studio.mandysa.music.logic.model

import com.drake.net.cache.CacheMode
import mandysax.anna2.annotation.*
import mandysax.anna2.model.ResponseBody
import studio.mandysa.music.logic.repository.UserRepository.cookie
import studio.mandysa.music.logic.repository.UserRepository.userId
import java.util.concurrent.TimeUnit

/**
 * @author 黄浩
 */
interface NeteaseCloudMusicApi {

    //发送验证码
    @Get("captcha/sent")
    suspend fun sendCaptcha(    @Query("phone") phone: String): CaptchaModel

    @Get("login/cellphone")
    suspend fun phoneLogin(
        @Query("phone") phone: String,
        @Query("captcha") captcha: String
    ): PhoneLoginModel

    @Get("artist/sublist")
    @Path("data")
    suspend fun artistSublist(@Query("cookie") cookie: String = cookie()): List<ArtistSubModel>

    //搜索音乐
    @Get("cloudsearch")
    @Path("result/songs")
    suspend fun searchMusic(
        @Query("cookie") cookie: String = cookie(),
        @Query("keywords") keywords: String,
        @Query("limit") limit: Int,
        @Query("offset") offset: Int
    ): List<SearchSongModel>

    //搜索歌手
    @Get("cloudsearch")
    @Path("result/artists")
    suspend fun searchSinger(
        @Query("cookie") cookie: String = cookie(),
        @Query("keywords") keywords: String,
        @Query("limit") limit: Int = 30,
        @Query("offset") offset: Int = 0,
        @Query("type") type: Int = 100
    ): List<SearchSingerModel>

    //获取音乐详细信息
    @Get("song/detail")
    @Path("songs")
    suspend fun getSongInfo(
        @Query("cookie") cookie: String = cookie(),
        @Query("ids") ids: List<Any>
    ): List<MusicModel>

    //获取推荐歌曲
    @Get("recommend/songs")
    @Path("data/dailySongs")
    suspend fun getRecommendSong(@Query("cookie") cookie: String = cookie()): List<RecommendSong>

    //获取推荐歌单
    @Get("recommend/resource")
    @Path("recommend")
    @FormUrlEncoded
    suspend fun getRecommendPlaylist(@Query("cookie") cookie: String = cookie()): List<PlaylistModel>

    //歌单广场
    @Get("personalized")
    @Path("result")
    suspend fun getPlaylistSquare(): ArrayList<PlaylistModel>

    //获取歌词
    @Get("lyric")
    @Path("lrc/lyric")
    suspend fun getLyric(@Query("id") id: String): String

    //获取歌单详情
    @Get("playlist/detail")
    @Path("playlist")
    suspend fun getSongListInfo(
        @Query("cookie") cookie: String = cookie(),
        @Query("id") id: String
    ): PlaylistInfoModel

    @Get("user/playlist")
    @Path("playlist")
    suspend fun getUserPlaylist(@Query("uid") uid: String = userId()): List<UserPlaylist>

    //获取账号信息
    @Post("user/account")
    @Path("profile")
    @FormUrlEncoded
    suspend fun getUserInfo(
        @Field("cookie") cookie: String = cookie(),
        @Query("timestamp") timestamp: Long = System.currentTimeMillis()
    ): UserModel

    //获取用户详情
    @Post("user/detail")
    suspend fun getUserDetail(
        @Query("uid") uid: String = userId()
    ): UserDetailModel

    //获取所有榜单
    @Get("toplist")
    @Path("list")
    suspend fun getToplist(): List<ListModel>

    @Get("like")
    suspend fun likeMusic(
        @Query("cookie") cookie: String = cookie(),
        @Query("id") id: String,
        @Query("like") like: Boolean
    ): ResponseBody

    @Get("subscribe")
    suspend fun subscribe(@Query("t") t: Int, @Query("id") playlistId: String): ResponseBody

    //主页轮播图
    @Get("banner?type=1")
    @Path("banners")
    suspend fun getBannerList(): ArrayList<BannerModel>

//    @Get("personal_fm")
//    @Path("data")
//    fun getPersonalFm(@Query("cookie") cookie: String): Observable<List<PersonalFmModel>>

    @Get("likelist")
    @Path("ids")
    suspend fun getLikeList(
        @Query("cookie") cookie: String = cookie(),
        @Query("uid") uid: String = userId()
    ): List<String>

    @Get("artist/detail")
    @Path("data")
    suspend fun getSingerDetails(
        @Query("cookie") cookie: String = cookie(),
        @Query("id") id: String
    ): SingerDetailedModel

    //获取歌单所有歌曲
    //https://neteasecloudmusicapi.vercel.app/#/?id=%e8%8e%b7%e5%8f%96%e6%ad%8c%e5%8d%95%e6%89%80%e6%9c%89%e6%ad%8c%e6%9b%b2
    @Get("playlist/track/all")
    @Path("songs")
    suspend fun getPlaylistSongs(
        @Query("cookie") cookie: String = cookie(),
        @Query("id") id: String
    ): ArrayList<PlaylistSong>

    @Get("artist/album")
    @Path("hotAlbums")
    suspend fun getSingerAlbum(
        @Query("cookie") cookie: String = cookie(),
        @Query("id") id: String,
        @Query("limit") limit: Int,
        @Query("offset") offset: Int
    ): List<SingerAlbumModel>

    //歌手热门 50 首歌曲
    //https://neteasecloudmusicapi.vercel.app/#/?id=%e6%ad%8c%e6%89%8b%e7%83%ad%e9%97%a8-50-%e9%a6%96%e6%ad%8c%e6%9b%b2
    @Get("artist/top/song")
    @Path("songs")
    suspend fun getSingerHotSong(@Query("id") id: String): List<MusicModel>

    @Get("album")
    suspend fun getAlbumContent(
        @Query("cookie") cookie: String = cookie(),
        @Query("id") id: String
    ): AlbumInfoModel

    //二维码key生成接口
    //https://neteasecloudmusicapi.vercel.app/#/?id=_1-%e4%ba%8c%e7%bb%b4%e7%a0%81-key-%e7%94%9f%e6%88%90%e6%8e%a5%e5%8f%a3
    @Get("login/qr/key")
    @Path("data/unikey")
    suspend fun getQRKey(@Query("timestamp") timestamp: Long = System.currentTimeMillis()): String

    //二维码生成接口
    //https://neteasecloudmusicapi.vercel.app/#/?id=_2-%e4%ba%8c%e7%bb%b4%e7%a0%81%e7%94%9f%e6%88%90%e6%8e%a5%e5%8f%a3
    @Get("login/qr/create")
    @Path("data/qrimg")
    suspend fun getQRImg(@Query("key") key: String, @Query("qrimg") qrimg: Boolean = true): String

    //二维码检测扫码状态接口
    //https://neteasecloudmusicapi.vercel.app/#/?id=_3-%e4%ba%8c%e7%bb%b4%e7%a0%81%e6%a3%80%e6%b5%8b%e6%89%ab%e7%a0%81%e7%8a%b6%e6%80%81%e6%8e%a5%e5%8f%a3
    @Get("login/qr/check")
    suspend fun check(
        @Query("key") key: String,
        @Query("timestamp") timestamp: Long = System.currentTimeMillis()
    ): CheckModel

    //最近播放-歌曲
    //https://neteasecloudmusicapi.vercel.app/#/?id=%e6%9c%80%e8%bf%91%e6%92%ad%e6%94%be-%e6%ad%8c%e6%9b%b2
    @Get("record/recent/song")
    @Path("data/list")
    suspend fun getRecentSongs(
        @Query("cookie") cookie: String = cookie(),
        @Query("limit") limit: Int
    ): List<RecentSongModel>

    //获取我的数字专辑
    //https://neteasecloudmusicapi.vercel.app/#/?id=%e6%9c%80%e8%bf%91%e6%92%ad%e6%94%be-%e6%ad%8c%e6%9b%b2
    @Get("digitalAlbum/purchased")
    @Path("paidAlbums")
    suspend fun getMyDigitalAlbum(@Query("cookie") cookie: String = cookie()): List<MyDigitalAlbum>

    @EnableCache(CacheMode.READ)
    @CacheValidTime(7, TimeUnit.DAYS)
    suspend fun longCache(): NeteaseCloudMusicApi

    @EnableCache(CacheMode.READ)
    suspend fun onlyCache(): NeteaseCloudMusicApi

    @EnableCache(CacheMode.WRITE)
    suspend fun network(): NeteaseCloudMusicApi
}