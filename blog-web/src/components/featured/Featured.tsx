import React from "react";
import styles from "./featured.module.css"
import Image from "next/image";

const Featured = () => {
    return (
        <div className={styles.container}>
            <h1 className={styles.title}>
                <b>Hey! Gabriel Dev here!</b> Discover my stories and creative ideas.
            </h1>
            <div className={styles.post}>
                <div className={styles.imgContainer}>
                    <Image src="/profile-picture.png" alt="" fill className={styles.image}></Image>
                </div>

                <div className={styles.textContainer}>
                    <h1 className={styles.postTitle}>Title goes here, After ready put some informative text about me.</h1>
                    <p className={styles.postDescription}>And here goes description, after ready put some cool description in English, I think this will be nice for new readers</p>
                    <button className={styles.button}>Read More</button>
                </div>
            </div>
        </div>
    )
}

export default Featured;
