import router from "@/router"
import Layout from "@/layout/Layout";

//设置动态路由
export function activeRouter(meuns) {
    let root = {
        path: '/',
        name: "Layout",
        component: Layout,
        redirect: '/login',
        children: [
            {
                name: 'Home',
                path: '/main',
                component: () => import("@/views/Home")
            }
        ]
    }
    console.log(meuns)
    // meuns.forEach(
    //     menu=>{
    //         let obj={
    //             path: menu.path,
    //             name: menu.name,
    //             component: ()=>import("@/views/"+menu.name)
    //         };
    //         root.children.push(obj)
    //     }
    // )
    router.addRoute(root)
}