package universerverside

class UrlMappings {

    static mappings = {
        "/$controller/$action?/$id?(.$format)?"{
            constraints {
                // apply constraints here
            }
        }

        "/regnew/$fnum/$snum/$tnum/$loginarg/$passwordarg"(controller: "core", action: "regNew")
        "/getparam/$fnum/$snum/$tnum/$fieldnum"(controller: "core", action: "getParam")
        "/chstate/$fnum/$snum/$tnum/$statenum/$loginarg/$passwordarg"(controller : "core", action:"changeState")
        "/genrep/$param/$fdate/$sdate/$loginarg/$passwordarg"(controller : "core", action : "generateReport")
        "/nuser/$loginarg/$passwordarg/$loginarg2/$passwordarg2"(controller : "core", action : "newUser")
        "/getgenrep/$loginarg/$passwordarg"(controller : "core", action : "getGeneralReport")
        "/ensys/$loginarg/$passwordarg"(controller : "core", action : "enterSystem")

        "/"(view:"/index")
        "500"(view:'/error')
        "404"(view:'/notFound')
    }
}
