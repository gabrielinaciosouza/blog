import React from "react";
import styles from "./postCard.module.css";
import Post from "@/models/post";
import Image from "next/image";

const PostCard = (post: Post) => {
    return (
        <div className={styles.container}>
            <Image
                className={styles.cardImage}
                src={post.coverImage}
                alt="Post Image"
                width={223}
                height={125}
                sizes="(max-width: 700px) 100vw, 223px"
                role="img"
            />
            <h2 className={styles.title}>{post.title}</h2>
        </div>
    );
};

export default PostCard;
