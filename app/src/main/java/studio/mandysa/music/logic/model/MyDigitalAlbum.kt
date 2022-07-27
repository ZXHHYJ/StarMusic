package studio.mandysa.music.logic.model

import mandysax.anna2.annotation.Path
import mandysax.anna2.annotation.Value

/**
 * @author 黄浩
 */
class MyDigitalAlbum {

//    {
//        "total":1,
//        "paidAlbums":[
//        {
//            "paidTime":1599066153299,
//            "albumId":93609698,
//            "cover":"https://p2.music.126.net/9Tm64FK0P01acRW54FH34g==/109951165211406094.jpg",
//            "albumName":"以家人之名 影视原声带",
//            "artist":{
//            "id":122455,
//            "name":"群星",
//            "transName":null,
//            "alias":"原声带;Various Artists;华语群星",
//            "aliaName":"华语群星",
//            "realName":null,
//            "areaId":7,
//            "initial":0,
//            "type":3,
//            "picId":3261151501061433,
//            "picture":null,
//            "hotAlbumIds":"",
//            "musicSize":49144,
//            "albumSize":10187,
//            "score":14036070,
//            "click":0,
//            "hotSongs":[
//
//            ],
//            "hotAlbums":[
//
//            ],
//            "albums":[
//
//            ],
//            "briefDesc":"",
//            "desc":"",
//            "json":null,
//            "valid":99,
//            "copyright":1
//        },
//            "artists":[
//            {
//                "id":122455,
//                "name":"群星",
//                "transName":null,
//                "alias":null,
//                "aliaName":null,
//                "realName":null,
//                "areaId":0,
//                "initial":0,
//                "type":0,
//                "picId":0,
//                "picture":null,
//                "hotAlbumIds":null,
//                "musicSize":0,
//                "albumSize":0,
//                "score":0,
//                "click":0,
//                "hotSongs":[
//
//                ],
//                "hotAlbums":[
//
//                ],
//                "albums":[
//
//                ],
//                "briefDesc":"",
//                "desc":"",
//                "json":null,
//                "valid":0,
//                "copyright":0
//            }
//            ],
//            "valid":99,
//            "transName":null,
//            "boughtCount":1,
//            "fromUser":null,
//            "sub":false,
//            "aliasName":"",
//            "channel":0
//        }
//        ],
//        "code":200
//    }

    @Value("paidTime")
    lateinit var paidTime: String

    @Value("albumId")
    lateinit var albumId: String

    @Value("albumName")
    lateinit var albumName: String

    @Value("cover")
    lateinit var cover: String

    @Path("artist")
    @Value("name")
    lateinit var artistName: String

    @Path("artist")
    @Value("id")
    lateinit var artistId: String
}
