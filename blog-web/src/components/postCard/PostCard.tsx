import React from "react";
import styles from "./postCard.module.css";
import Post from "@/models/post";
import Image from "next/image";
import Link from "next/link";

const PostCard = (post: Post) => {
    return (
        <Link href={`/posts/${post.slug}`}>
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
        </Link>
    );
};

export default PostCard;
