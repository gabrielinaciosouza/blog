import styles from "./homepage.module.css"
import Featured from "@/components/featured/Featured";

export default function Home() {
    return (
        <div className={styles.container} role="main">
            <Featured/>
            <div className={styles.content}>
            </div>
        </div>
    )
}
