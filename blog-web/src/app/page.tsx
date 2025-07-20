import PostList from "@/components/postList/PostList";
import styles from "./homepage.module.css"
import Featured from "@/components/featured/Featured";

const Home = () => {
    return (
        <div className={styles.container} role="main">
            <Featured />
            <div className={styles.content}>
                <PostList />
            </div>
        </div>
    )
}

export default Home;