import VueRouter from 'vue-router'
import Home from '../views/Home'
import Login from '../views/Login'
import Layout from "@/layout/Layout";
import request from "@/utils/request";
// import Role from "@/views/Role";
// import User from "@/views/User";


//创建并暴露一个路由器
const router = new VueRouter({
        routes: [
            {
                path: '/',
                name: 'Layout',
                redirect: "/login",
                component: Layout,
                children: [
                    {
                        path: '/main',
                        name: 'Home',
                        component: Home
                    },
                    // {   //用户管理
                    //     path: '/role',
                    //     name: 'Role',
                    //     component: Role
                    // },
                    // {   //用户管理
                    // 	path:'/user',
                    // 	name:'user',
                    // 	component:User
                    // }
                ]
            },
            {
                path: '/login',
                name: 'Login',
                component: Login
            }
        ]
    }
)


//activeRouter()
// function activeRouter() {
// 	const userStr = sessionStorage.getItem("user")
// 	if (userStr) {
// 		const user = JSON.parse(userStr)
// 		let root = {
// 			path: '/',
// 			name: 'Layout',
// 			component: Layout,
// 			redirect: '/main',
// 			children: [],
// 		}
// 		user.menus.forEach(
// 			menu => {
// 				let obj = {
// 					path: menu.path,
// 					name: menu.name,
// 					component: () => import("@/views/" + menu.name)
// 				};
// 				root.children.push(obj)
// 			}
// 		)
// 		if (router) {
// 			router.addRoute(root)
// 		}
// 	}
// }

router.beforeEach((to, from, next) => {
    // let hasRoute=localStorage.getItem("hasRoute")
    // if (hasRoute)
    request.get("/api/menu/nav", {
        headers: {
            Authorization: localStorage.getItem("token")
        }
    }).then(res => {
        //拿到menus
        localStorage.setItem("menus", res.data.nav)

        //拿到用户权限
        localStorage.setItem("perms", res.data.authoritys)

        //动态绑定路由
        let newRoutes = router.options.routes
        let nav = JSON.parse(res.data.nav)
        //nav=Array.from(nav);

        nav.forEach(menu => {
            if (menu.children) {
                menu.children.forEach(e => {
                    //转化路由
                    let route = menuToRoute(e)
                    //把路由添加到路由管理中
                    if (route) {
                        newRoutes[0].children.push(route)
                    }
                })
            }
        })

        newRoutes.forEach(newRoute => {
            router.addRoute(newRoute)
        })


    })
    next()
})


const menuToRoute = (menu) => {
    if (!menu.component) {
        return null
    }
    let route = {
        name: menu.component,
        path: menu.path,

    }
    route.component = () => import('@/views/' + menu.component)

    return route
}

export default router

