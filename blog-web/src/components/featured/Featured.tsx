import React from "react";
import styles from "./featured.module.css"
import Image from "next/image";

const Featured = () => {
    return (
        <div className={styles.container}>
            <h1 className={styles.title}>
                <b>Hey, I&apos;m Gabriel!</b> Unraveling Code, Stories & Big Ideas!🚀
            </h1>
            <div className={styles.post}>
                <div className={styles.imgContainer}>
                    <Image src="/profile-picture.png" alt="" fill className={styles.image}></Image>
                </div>

                <div className={styles.textContainer}>
                    <h1 className={styles.postTitle}>🎯 Tech, Logic & Creativity - Why I Love Engineering</h1>
                    <p className={styles.postDescription}>Every bug has a lesson, every project has a story. Join me as I navigate the ever-evolving world of software engineering!</p>
                    <div className={styles.buttonContainer}>
                        <button className={styles.button}>About Me</button>
                        <div className={styles.social}>
                            <Image src="/facebook.png" alt="facebook" width={24} height={24} />
                            <Image src="/instagram.png" alt="instagram" width={24} height={24} />
                            <Image src="/tiktok.png" alt="tiktok" width={24} height={24} />
                            <Image src="/youtube.png" alt="youtube" width={24} height={24} />
                        </div>
                    </div>
                </div>
            </div>
        </div>
    )
}

export default Featured;
