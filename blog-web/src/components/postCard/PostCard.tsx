import React from "react";
import styles from "./postCard.module.css";
import Post from "@/models/post";

const PostCard = (post: Post) => {
    return (
       <div className={styles.container}>
            <img className={styles.image} src="/logo2.png"></img>
            <div className={styles.contentWrapper}>
                <div className={styles.date}>{post.creationDate}</div>
                <h2 className={styles.title}>{post.title}</h2>
                <p className={styles.content} dangerouslySetInnerHTML={{ __html: post.content }}></p>
            </div>
       </div>
    );
};

export default PostCard;
