import React from "react";
import styles from "./cardList.module.css"
import Pagination from "@/components/pagination/Pagination";

const CardList = () => {
    return (
        <div className={styles.container}>
            <h1 className={styles.title}>Recent Posts</h1>
            <div className={styles.posts}>
                <div className={styles.post}>
                    <div className={styles.imageContainer}>

                    </div>
                    <div className={styles.textContainer}></div>
                </div>
            </div>
            <Pagination/>
        </div>
    )
}

export default CardList;
