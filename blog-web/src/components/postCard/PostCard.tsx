import React from "react";
import styles from "./postCard.module.css";
import Post from "@/models/post";
import Image from "next/image";

const PostCard = (post: Post) => {
    return (
        <div className={styles.container}>

            <Image className={styles.image} src="/logo2.png" alt="Post Image" width={0}
                height={0}
                sizes="100vw"
                role="img"
            />


            <div className={styles.contentWrapper}>
                <div className={styles.date}>{post.creationDate}</div>
                <h2 className={styles.title}>{post.title}</h2>
                <p className={styles.content} dangerouslySetInnerHTML={{ __html: post.content }}></p>
            </div>
        </div>
    );
};

export default PostCard;
